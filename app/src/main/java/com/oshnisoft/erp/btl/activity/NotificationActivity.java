package com.oshnisoft.erp.btl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.INotification;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView rvList;
    FastItemAdapter<INotification> notificationFastItemAdapter;
    private CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);App.getComponent().inject(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notice");
        rvList = findViewById(R.id.rvList);
        notificationFastItemAdapter = new FastItemAdapter<>();
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.getNotifications(token).subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<INotification>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDataList<INotification> value) {
                        if (value.isSuccess() && value.getDataList() != null) {
                            setAdapter(value.getDataList());
                        }
                    }
                }));
    }

    public void setAdapter(List<INotification> notificationList){
        notificationFastItemAdapter = new FastItemAdapter<>();
        notificationFastItemAdapter.add(notificationList);
        notificationFastItemAdapter.withSelectable(true);
        notificationFastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(notificationFastItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }
}