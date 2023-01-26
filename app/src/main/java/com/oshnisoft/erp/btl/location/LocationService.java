package com.oshnisoft.erp.btl.location;


import static com.oshnisoft.erp.btl.NotificationMosfet.ID;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.oshnisoft.erp.btl.NotificationMosfet;
import com.oshnisoft.erp.btl.dash.MyWorker;

import java.util.concurrent.TimeUnit;


public class LocationService extends Service implements LocationListener {

    private final static String TAG = "LocationUpdateService";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 120000;//
    public static double updateLat, updateLon;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    static String CHANNEL_ID = "CHANNEL_ID";
    private static int DISPLACEMENT = 0;

    Location lastKnownLocationGPS = null;
    private double currentLat, currentLng;
    private LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private boolean isGPSEnabled, isNetworkEnabled, isPassiveEnable;
    NotificationMosfet notificationMosfet;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationMosfet = new NotificationMosfet(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        Notification notification = notificationMosfet.getMyActivityNotification("Default", "BTL Group");
        startForeground(ID, notification);
        if (!isEnabledLocation()) {
            notificationMosfet.updateNotification("GPS OFF", "BTL Group");
        }


        if (isGooglePlayServicesAvailable()) {
            initLocationManager();
            Log.e(TAG, "mGoogleApiClient CALLED!!!!!!!!!!!!!");
            startLocationUpdates();
        }

        startWorker();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent intent) {
        return super.stopService(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service is Destroying...");
        super.onDestroy();
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged ");
        updateLocation(location);
        if (location != null) {
            Log.i(TAG, "onLocationChanged:: " + location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


//    protected void createLocationRequest() {
//        mLocationRequest = LocationRequest.create()
//                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//                .setSmallestDisplacement(DISPLACEMENT);
//    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            Log.e(TAG, " ***** Update google play service ");
            return false;
        }
    }

    private boolean isEnabledLocation() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            // permission has been granted, continue as usual

            if (mLocationManager != null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                criteria.setAltitudeRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                criteria.setBearingRequired(false);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
                mLocationManager.getBestProvider(criteria, true);
                if (isGPSEnabled) {
                    lastKnownLocationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (isNetworkEnabled) {
                    lastKnownLocationGPS = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, DISPLACEMENT, this);
            }

            if (lastKnownLocationGPS != null && LocationUtils.isBetterLocation(lastKnownLocationGPS, mCurrentLocation, UPDATE_INTERVAL_IN_MILLISECONDS)) {
                updateLocation(lastKnownLocationGPS);
            }
        }
    }

    private void initLocationManager() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isPassiveEnable = mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        Log.d("LocationUpdateInte", "initLocationManager: " + isPassiveEnable);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager = null;
        }
    }

    private void updateLocation(Location location) {
        if (location == null) {
            Log.e(TAG, "Location found as null");
            return;
        }
//        Toast.makeText(this, "LAT=" + location.getLatitude() + " LON=" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        mCurrentLocation = location;
        updateLat = location.getLatitude();
        updateLon = location.getLongitude();
        Intent intent = new Intent("locationReceiver");
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lon", location.getLongitude());
        sendBroadcast(intent);
        notificationMosfet.updateNotification(String.valueOf(location.getLatitude()) + "\n" + String.valueOf(location.getLongitude()), "BTL GROUP");
    }

    public void startWorker() {
        Data.Builder myData = new Data.Builder();
        if (mCurrentLocation != null) {
            myData.putDouble("lat", mCurrentLocation.getLatitude());
            myData.putDouble("lon", mCurrentLocation.getLongitude());
        }
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest attendWork = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(myConstraints)
                .setInputData(myData.build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(MyWorker.class.getName(),
                ExistingPeriodicWorkPolicy.REPLACE, attendWork);
    }


}