package com.oshnisoft.erp.btl.activity;

import static com.oshnisoft.erp.btl.location.LocationStatus.REQUEST_LOCATION;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.MainMenuActivity;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SyncListener;
import com.oshnisoft.erp.btl.location.LocationService;
import com.oshnisoft.erp.btl.location.LocationStatus;
import com.oshnisoft.erp.btl.model.AttendanceStatus;
import com.oshnisoft.erp.btl.model.INotification;
import com.oshnisoft.erp.btl.model.LogoutResponse;
import com.oshnisoft.erp.btl.model.ResponseData;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.model.Target;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.net.RequestServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.RedirectUtils;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity implements SyncListener {
    private static final String TAG = "DashboardActivity";
    LinearLayout llContent;
    LocationService service;
    Context context;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.role)
    TextView role;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.cardAttendance)
    CardView llAttendance;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.attendStatus)
    TextView attendStatus;
    @BindView(R.id.btnPunch)
    Button btnPunch;
    @BindView(R.id.cardTarget)
    CardView cardTarget;
    @BindView(R.id.target)
    TextView target;
    @BindView(R.id.collection)
    TextView collection;
    @BindView(R.id.shortage)
    TextView shortage;
    @BindView(R.id.lpbAcheive)
    LinearProgressIndicator lpbAcheive;
    @BindView(R.id.llBill)
    LinearLayout llBill;
    @BindView(R.id.llCustomers)
    LinearLayout llCustomers;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.llOnline)
    LinearLayout llOnline;
    @BindView(R.id.cardSpecialNotice)
    CardView cardSpecialNotice;
    @BindView(R.id.txtNoticeDate)
    TextView txtNoticeDate;
    @BindView(R.id.txtNoticeContent)
    TextView txtNoticeContent;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    @Inject
    User user;

    @Inject
    UserData userData;

    @Inject
    RequestServices requestServices;

    int attendanceStatus = 0;



    CompositeDisposable mCompositeDisposable;
    LoadingDialog loadingDialog;
    static boolean syncNeeded = false;

    public static void start(Activity context, boolean syncNeed) {
        Intent intent = new Intent(context, DashboardActivity.class);
        context.startActivity(intent);
        syncNeeded = syncNeed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        llContent = findViewById(R.id.llContent);
        llContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int height;
                if (getSupportActionBar() != null) {
                    height = llContent.getHeight() - getSupportActionBar().getHeight();
                } else {
                    height = llContent.getHeight();
                }
                setMenuWH(height);
            }
        });
        name = findViewById(R.id.name);
        role = findViewById(R.id.role);
        name.setText(user.getName());
        role.setText(userData.getDesignation());
        date.setText(DateTimeUtils.getTodayDate());
        context = this;


        if (!isServiceRunning(this, LocationService.class)) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            //ToastUtils.shortToast("Service Start Again");
        } else {
            //ToastUtils.shortToast("Service Ongoing");
        }


        //Location On.
        new LocationStatus(this).isLocationEnable();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu) {
            // do something here
            //Toast.makeText(this, "menu clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);

        }

        if (id == R.id.action_logout) {
            //Toast.makeText(this, "Log out Clicked", Toast.LENGTH_SHORT).show();
            displayLogoutConfirmationPopup();
        }
        if (id == R.id.action_sync) {
            //Toast.makeText(this, "Sync Clicked", Toast.LENGTH_SHORT).show();
            syncAll();
        }


        return true;
    }

    public void setMenuWH(int h) {
        SharedPrefsUtils.setIntegerPreference(this, StringConstants.PREF_MENU_H, h / 3 - 20);

        if (getSupportActionBar() != null) {
            h = h - getSupportActionBar().getHeight() - 90;
        }
        SharedPrefsUtils.setIntegerPreference(this, StringConstants.PREF_SUMMARY_GRID_ITEM_H, h / 15);
    }


    public static boolean isServiceRunning(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void stopTrackerService() {

        if (isServiceRunning(this, LocationService.class)) {
            //stopService(LocationService)
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(this, "Gps enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void displayLogoutConfirmationPopup() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // alert.setTitle(getString(R.string.logout));
        alert.setMessage(StringConstants.LOGOUT_CONFIRM_MSG);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                logout();

            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public void logout() {
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
        loadingDialog.show();
        String auth_token = SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.userLogout(auth_token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LogoutResponse>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //listener.onError(e.getMessage(), 3);
                        ToastUtils.longToast("Logout Error!!");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(LogoutResponse value) {
                        if (value.isSuccess()) {
                            r.executeTransaction(realm -> {
                                realm.deleteAll();
                                finish();
                            });
                        } else {
                            ToastUtils.longToast("Logout Failed!!");
                        }
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (syncNeeded) {
            syncAll();
        } else {
            setDashboardUI();
        }
        getNotice();

    }

    public void syncAll() {
        requestServices.syncAll(this, apiServices, userData, r, user, this);
    }

    public void attendStatus() {
        if(user.getIs_employee().equalsIgnoreCase("Yes")) {
            llAttendance.setVisibility(View.VISIBLE);
            mCompositeDisposable = new CompositeDisposable();
            loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
            loadingDialog.show();
            String auth_token = SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_AUTH_TOKEN);
            mCompositeDisposable.add(apiServices.getAttendanceStatus(auth_token) //test jsonblob
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseData<AttendanceStatus>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();
                            if(user.getIs_hr().equalsIgnoreCase("No")){
                                getTarget();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                            if(user.getIs_hr().equalsIgnoreCase("No")){
                                getTarget();
                            }
                        }

                        @Override
                        public void onNext(ResponseData<AttendanceStatus> value) {
                            if (value.isSuccess()) {
                                date.setText(DateTimeUtils.changeDateTimeFormat(value.getData().getDate(), DateTimeUtils.FORMAT5, DateTimeUtils.FORMAT3));
                                AppContainer.KEY_ATTENDANCE_STATUS = value.getData().getStatus();
                                switch (value.getData().getStatus()) {
                                    case 0:
                                        attendStatus.setText("Absent");
                                        btnPunch.setText("Punch-In");
                                        btnPunch.setEnabled(true);
                                        break;
                                    case 1:
                                        attendStatus.setText("Present - (IN)");
                                        btnPunch.setText("Punch-Out");
                                        btnPunch.setEnabled(true);
                                        break;
                                    case 2:
                                        attendStatus.setText("Present - (IN-OUT)");
                                        btnPunch.setEnabled(false);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                attendStatus.setText("Absent");
                                btnPunch.setText("Punch-In");
                                btnPunch.setEnabled(true);
                            }
                        }
                    }));
        } else {
            if(user.getIs_hr().equalsIgnoreCase("No")) {
                getTarget();
            }
        }
    }

    public void getTarget() {
        if(user.getIs_hr().equalsIgnoreCase("No")) {
            mCompositeDisposable = new CompositeDisposable();
            loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
            loadingDialog.show();
            String auth_token = SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_AUTH_TOKEN);
            mCompositeDisposable.add(apiServices.getTarget(auth_token) //test jsonblob
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseData<Target>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseData<Target> value) {
                            if (value.isSuccess()) {
                                setTargetUI(value.getData());
                            } else {

                            }
                        }
                    }));
        } else {
            cardTarget.setVisibility(View.GONE);
        }
    }

    public void setTargetUI(Target target){
            this.target.setText(String.format("Target: %.2f", target.getTarget()));
            this.collection.setText(String.format("Online: %.2f", target.getCash_collection()));
            this.shortage.setText(String.format("Shortage: %.2f", target.getShortage()));
            lpbAcheive.setProgress((int)target.getAcheive_percent(), true);
    }

    public void setDashboardUI(){

        name.setText(String.format("Name : %s", user.getName()));
        if(user.getIs_employee().equalsIgnoreCase("Yes")){
            llAttendance.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.VISIBLE);
            cardSpecialNotice.setVisibility(View.VISIBLE);
            code.setText(String.format("Empl ID : %s", SharedPrefsUtils.getStringPreference(this, StringConstants.REMEMBER_PHONE)));
            if(user.getIs_hr().equalsIgnoreCase("No")){
                role.setText(String.format("Desg. : %s", userData.getDesignation()));
            } else {
                role.setText(String.format("Role. : %s", user.getRole()));
            }

        } else {
            code.setText(String.format("Code : %s", SharedPrefsUtils.getStringPreference(this, StringConstants.REMEMBER_PHONE)));
            role.setText(String.format("Role. : %s", user.getRole()));
            llAttendance.setVisibility(View.GONE);
            llBill.setVisibility(View.GONE);
            cardSpecialNotice.setVisibility(View.GONE);
        }

        if(user.getIs_hr().equalsIgnoreCase("No")){
            cardTarget.setVisibility(View.VISIBLE);
            llCustomers.setVisibility(View.VISIBLE);
            llOrder.setVisibility(View.VISIBLE);
            llOnline.setVisibility(View.VISIBLE);
        } else {
            cardTarget.setVisibility(View.GONE);
            llCustomers.setVisibility(View.GONE);
            llOrder.setVisibility(View.GONE);
            llOnline.setVisibility(View.GONE);
        }

        attendStatus();

    }

    public void getNotice() {
        mCompositeDisposable = new CompositeDisposable();
        //loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
        //loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.getNotifications(token).subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<INotification>>() {
                    @Override
                    public void onComplete() {
                        //loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //loadingDialog.dismiss();
                        r.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(User.class);
                                realm.delete(UserData.class);
                                ToastUtils.longToast("Please Login Again!!");
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onNext(ResponseDataList<INotification> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            INotification iNotification = value.getDataList().get(0);
                            txtNoticeContent.setText(iNotification.getContent());
                            txtNoticeDate.setText(DateTimeUtils.changeDateTimeFormat(iNotification.getCreated_at(), DateTimeUtils.FORMAT_FOR_ZONE, DateTimeUtils.FORMAT_APP_DEFAULT));
                        } else {
                            setEmptyNoticeText();
                        }
                    }
                }));
    }

    public void setEmptyNoticeText() {
        txtNoticeContent.setText("No Notice");
        txtNoticeDate.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnPunch, R.id.llBill, R.id.llCustomers, R.id.llOrder, R.id.llOnline, R.id.changePasswordTV})
    void onClick(View view){
        int id = view.getId();
        if(id == R.id.btnPunch){
            RedirectUtils.go(this, HostActivity.class, false, "Attendance");
        }

        if(id == R.id.llBill){
            RedirectUtils.go(this, HostActivity.class, false, "bill");
        }


        if(id == R.id.llCustomers){
            if (user.getRole().equalsIgnoreCase("customer")) {
                ToastUtils.longToast("You are not permitted");
            } else if (user.getRole().equalsIgnoreCase("dealer")) {
                RedirectUtils.goWithDealerId(context, HostActivity.class, false, "customer", (int)user.getId());
            } else {
                RedirectUtils.go(context, HostActivity.class, false, "dealer_list");
            }
        }


        if(id == R.id.llOrder){
            RedirectUtils.go(this, HostActivity.class, false, "order");
        }


        if(id == R.id.llOnline){
            RedirectUtils.go(this, HostActivity.class, false, "payment");
        }

        if(id == R.id.changePasswordTV){
            ChangePasswordActivity.start(context);
        }
    }


    @Override
    public void onSyncComplete() {
        setDashboardUI();
        syncNeeded = false;
    }

    @Override
    public void onSyncError() {
        setDashboardUI();
        syncNeeded = true;
    }


}