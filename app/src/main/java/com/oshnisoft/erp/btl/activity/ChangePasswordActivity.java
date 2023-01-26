package com.oshnisoft.erp.btl.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.ChangePassword;
import com.oshnisoft.erp.btl.net.APIServices;

import com.oshnisoft.erp.btl.utils.ConnectionUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String TAG = ChangePasswordActivity.class.getSimpleName();

    private ChangePasswordActivity activity = this;

    @BindView(R.id.connectionInfoTV)
    TextView connectionInfoTV;

    @BindView(R.id.editTextOldPassword)
    EditText editTextOldPassword;

    @BindView(R.id.editTextNewPassword)
    EditText editTextNewPassword;

    @BindView(R.id.editTextConfirmPassword)
    EditText editTextConfirmPassword;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    private CompositeDisposable mCompositeDisposable;

    @Inject
    User user;
    @Inject
    UserData userData;

    ChangePassword changePassword = new ChangePassword();

    public static void start(Context context){
        Intent intent = new Intent(context,ChangePasswordActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.buttonChangePassword)
    void onClickChangePassword(){
        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)){
            editTextOldPassword.setError("Old password is required!");
            return;
        }

        if (TextUtils.isEmpty(newPassword)){
            editTextNewPassword.setError("New password is required!");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            editTextConfirmPassword.setError("Confirm password is required!");
            return;
        }

        if (!newPassword.equals(confirmPassword)){
            editTextConfirmPassword.setError("Confirm password is not matched!");
            return;
        }


        if (ConnectionUtils.isNetworkConnected(activity)){
            connectionInfoTV.setVisibility(View.GONE);
            changePassword.setPassword(newPassword);
            changePassword.setOld_password(oldPassword);
            changePassword.setPassword_confirmation(confirmPassword);
            postNewPassword();

        }else{
            connectionInfoTV.setVisibility(View.VISIBLE);
        }

    }

    public void postNewPassword(){

        String deviceToken = SharedPrefsUtils.getStringPreference(activity, StringConstants.PREF_AUTH_TOKEN);
        displayProgress();
        mCompositeDisposable.add(apiServices.changePassword(deviceToken,
                        changePassword)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete change password");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError change password: "+e.toString());
                        hideProgress();
                        ToastUtils.shortToast("Server Error!!");

                    }

                    @Override
                    public void onNext(ResponsePost value) {
                        hideProgress();
                        if (value.isSuccess()) {
                            SharedPrefsUtils.setStringPreference(activity, StringConstants.REMEMBER_PASSWORD, changePassword.getPassword());
                            displayAlert("Success", value.getMessage());

                        }else{
                            ToastUtils.shortToast(value.getMessage());
                        }
                    }
                }));

    }


    public void displayAlert(final String title,final String msg){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                finish();

            }
        });
        alert.show();
    }


    private ProgressDialog progressDialog;
    private void displayProgress(){
        if (progressDialog==null)
            progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Password is changing.");
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgress(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ConnectionUtils.isNetworkConnected(activity)){
            connectionInfoTV.setVisibility(View.GONE);
        }else{
            connectionInfoTV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideProgress();

        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

}
