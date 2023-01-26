package com.oshnisoft.erp.btl.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.model.LoginResponse;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.permissionManager.PermissionManager;
import com.oshnisoft.erp.btl.permissionManager.RequestPermissionListener;
import com.oshnisoft.erp.btl.utils.ConnectionUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.cbRemember)
    AppCompatCheckBox cbRemember;


    @BindView(R.id.tiePhone)
    TextInputEditText tiePhone;

    @BindView(R.id.tiePassword)
    TextInputEditText tiePassword;

    PermissionManager mana;
    String[] permission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA
    };
    /*String[] permissionBackground = new String[]{
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };*/

    @Inject
    App app;
    @Inject
    Realm realm;
    private LoginActivity activity = this;

    public LoadingDialog loadingDialog;
    private CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getComponent().inject(this);

        init();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            DashboardActivity.start(this, true);
            finish();
        }

        String rememberPhone = SharedPrefsUtils.getStringPreference(activity, StringConstants.REMEMBER_PHONE);
        String rememberPassword = SharedPrefsUtils.getStringPreference(activity, StringConstants.REMEMBER_PASSWORD);

        if (rememberPhone != null && rememberPassword != null) {
            cbRemember.setChecked(true);
            tiePhone.setText(rememberPhone);
            tiePassword.setText(rememberPassword);
        } else {
            cbRemember.setChecked(false);
            tiePhone.setText("");
            tiePassword.setText("");
        }
    }
    public void init() {
        loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
        mCompositeDisposable = new CompositeDisposable();
    }

    @OnClick({R.id.btnLogin})
    public void onClick(View view) {
        withoutBackgroundPermission();
    }

    public void withoutBackgroundPermission() {
        mana = new PermissionManager();
        mana.requestPermission(this, permission, 123, new RequestPermissionListener() {
            @Override
            public void onSuccess() {
                batteryOptPermission();
            }

            @Override
            public void onFailed() {

            }
        });
    }

    public void login(final String phone, final String password) {
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.login(phone, password).subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete login");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError login: " + e.toString());
                        ToastUtils.shortToast("Login Failed: " + e.toString());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        if (value.isSuccess()) {
                            ToastUtils.shortToast(value.getMessage());
                            goToDashboard(phone, password, value);
                        } else {
                            ToastUtils.shortToast("Login Failed " + value.getMessage());
                        }
                    }
                }));
    }

    public void goToDashboard(final String phone, final String password, final LoginResponse value) {
        SharedPrefsUtils.setStringPreference(this, StringConstants.PREF_AUTH_TOKEN, "Bearer " + value.getToken());
        SharedPrefsUtils.setLongPreference(this, StringConstants.PREF_EMPLOYEE_ID, value.getData().getEmployee_id());
        if (cbRemember.isChecked()) {
            SharedPrefsUtils.setStringPreference(activity, StringConstants.REMEMBER_PHONE, phone);
            SharedPrefsUtils.setStringPreference(activity, StringConstants.REMEMBER_PASSWORD, password);
        }
        SharedPrefsUtils.setStringPreference(activity, StringConstants.REMEMBER_PHONE, phone);
        realm.executeTransaction(realm -> {
            //delete first
            realm.delete(User.class);
            realm.delete(UserData.class);
            //then insert
            realm.insertOrUpdate(value.getData());
            if (value.getData().getData() != null) {
                realm.insertOrUpdate(value.getData().getData());
            } else {
                UserData userData = new UserData();
                userData.setType("Admin");
                userData.setDa_amount(0);
                userData.setDesignation("GM");
                userData.setZone_id(0);
                userData.setSecondary_mobile_number("017");
                realm.insertOrUpdate(userData);
            }
        });

        //Go to Main activity
        DashboardActivity.start(this, true);
        //startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

        finish();
    }

    public void batteryOptOff() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            //batteryOptimization.launch(intent);
        }
    }

    public void batteryOptPermission() {
        loginAtt();
        /*PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
            batteryOptOff();
        } else {
            //startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

        }*/
    }


    public void loginAtt() {
        //tiePhone.setText("00001");
        //tiePassword.setText("12345678");
        String phone = tiePhone.getText().toString();
        String password = tiePassword.getText().toString();
        if (ConnectionUtils.isNetworkConnected(activity) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            login(phone, password);
        } else {
            ToastUtils.longToast("Check your internet connections or Input Parameter");
        }
    }

    /*ActivityResultLauncher<Intent> batteryOptimization = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                batteryOptPermission();
            } else {
                ToastUtils.longToast("Please give all permission");
            }
        }
    });*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mana.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean checkACCESS_BACKGROUND_LOCATIONPermission() {
        String permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void backGroundPermission() {
        new AlertDialog.Builder(this).setTitle("Background Permission").setCancelable(false).setMessage("Need background permission and IGNORE BATTERY OPTIMIZATIONS to track employee location for office attendance or vice versa.").setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                backGroundPermissionBackDialog();
                dialog.dismiss();
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ToastUtils.longToast("Background Permission Denied");
                dialogInterface.dismiss();
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void backGroundPermissionBackDialog() {
        /*mana.requestPermission(LoginActivity.this, permissionBackground, 123, new RequestPermissionListener() {
            @Override
            public void onSuccess() {
                batteryOptPermission();
            }

            @Override
            public void onFailed() {
                ToastUtils.longToast("Failed to background permission");
            }
        });*/

    }

}