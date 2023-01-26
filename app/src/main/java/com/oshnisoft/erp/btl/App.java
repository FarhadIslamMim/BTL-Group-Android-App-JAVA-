package com.oshnisoft.erp.btl;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import com.oshnisoft.erp.btl.dependency.DaggerAppComponent;
import com.oshnisoft.erp.btl.dependency.AppComponent;
import com.oshnisoft.erp.btl.net.RequestServices;

import io.realm.Realm;


/**
 * Created by sobuj on 30/06/2021.
 */

public class App extends Application {

    private static AppComponent appComponent;

    private static App mInstance;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    private RequestServices requestServices = new RequestServices();


    public static synchronized App getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mInstance = this;
        Realm.init(this);
        createNotificationChannel();
        //appComponent = DaggerAppComponent.Initializer.init(this, requestServices);
        appComponent = DaggerAppComponent.Initializer.init(this, requestServices);

    }

    public static AppComponent getComponent() {
        return appComponent;
    }


    public App getActivity() {
        return this;
    }

    //    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
