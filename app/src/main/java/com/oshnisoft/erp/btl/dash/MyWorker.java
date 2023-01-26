package com.oshnisoft.erp.btl.dash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.location.LocationService;
import com.oshnisoft.erp.btl.location.LocationUtils;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceModel;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyWorker extends Worker {
    private static final String TAG = "MyWorker";
    Context context;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        App.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {


        String authToken = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        LocationModel model = new LocationModel();
        model.setLongitude(LocationService.updateLon);
        model.setLatitude(LocationService.updateLat);
        model.setAddress(LocationUtils.getAddress(context, LocationService.updateLat, LocationService.updateLon));
        model.setEmployeeId(SharedPrefsUtils.getLongPreference(context, StringConstants.PREF_EMPLOYEE_ID, 0));

        compositeDisposable.add(apiServices.postLocation(authToken, model)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<AttendanceModel>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError location: " + e.toString());
                    }

                    @Override
                    public void onNext(AttendanceModel value) {
                        Log.e(TAG, "onNext location: " + value.getMessage());
                    }
                }));
        return Result.success();
    }
}
