package com.oshnisoft.erp.btl.ui.leave;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.LeavePostModel;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;


public class ApplyLeaveFragment extends Fragment implements LeaveDatePickerFragment.DateDialogListener{

    @BindView(R.id.spLeaveType)
    Spinner spLeaveType;

    @BindView(R.id.txtFromDate)
    TextView txtFromDate;

    @BindView(R.id.txtToDate)
    TextView txtToDate;

    @BindView(R.id.tieReason)
    TextInputEditText tieReason;

    @BindView(R.id.llFromDate)
    LinearLayout llFromDate;

    @BindView(R.id.llEndDate)
    LinearLayout llEndDate;

    String startDate, endDate;

    @BindView(R.id.btnSubmitForApproval)
    Button btnSubmitForApproval;

    @Inject
    APIServices apiServices;

    LoadingDialog loadingDialog;
    @Inject
    Realm r;
    CompositeDisposable mCompositeDisposable;

    @Inject
    User user;

    @Inject
    UserData userData;


    public ApplyLeaveFragment() {
        // Required empty public constructor
    }
    Context context;
    Activity activity;
    @Inject
    List<LeaveTypeModel> leaveTypeModelList;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = ((Activity) context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View root = inflater.inflate(R.layout.fragment_apply_leave, container, false);
        ButterKnife.bind(this, root);
        ((AppCompatActivity) context).setTitle("Apply Leave");

        ArrayAdapter sp1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, leaveTypeModelList);
        sp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLeaveType.setAdapter(sp1);

        return root;
    }
    @OnClick({R.id.btnSubmitForApproval, R.id.llFromDate, R.id.llEndDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitForApproval:
                submitLeave();
                break;

            case R.id.llFromDate:
                LeaveDatePickerFragment dialog1 = LeaveDatePickerFragment.newInstance();
                dialog1.setDateDialogListener(this, true);
                dialog1.show(((AppCompatActivity) context).getSupportFragmentManager(), "DIALOG_START_DATE");
                break;
            case R.id.llEndDate:
                LeaveDatePickerFragment dialog2 = LeaveDatePickerFragment.newInstance();
                dialog2.setDateDialogListener(this, false);
                dialog2.show(((AppCompatActivity) context).getSupportFragmentManager(), "DIALOG_END_DATE");
                break;
        }
    }

    @Override
    public void setStartDate(String date) {
        startDate = date;
        txtFromDate.setText(DateTimeUtils.changeDateTimeFormat(date, DateTimeUtils.FORMAT5, DateTimeUtils.FORMAT3));
    }

    @Override
    public void setEndDate(String date) {
        endDate = date;
        txtToDate.setText(DateTimeUtils.changeDateTimeFormat(date, DateTimeUtils.FORMAT5, DateTimeUtils.FORMAT3));
    }

    public void submitLeave(){
        if(startDate == null){
            ToastUtils.longToast("Please Set From Date.");
            return;
        }
        if(endDate == null){
            ToastUtils.longToast("Please Set To Date.");
            return;
        }
        if(TextUtils.isEmpty(tieReason.getText().toString())){
            ToastUtils.longToast("Please put down reason of leave.");
            return;
        }
        String reason = tieReason.getText().toString();

        LeaveTypeModel leaveTypeModel = (LeaveTypeModel) spLeaveType.getSelectedItem();
        if(leaveTypeModel == null){
            ToastUtils.longToast("Please select leave type.");
            return;
        }
        LeavePostModel leavePostModel = new LeavePostModel();
        leavePostModel.setApplyDate(DateTimeUtils.getTodayDate());
        leavePostModel.setLeaveTypeID(leaveTypeModel.getId());
        leavePostModel.setEmpID(user.getEmployee_id());
        leavePostModel.setFromDate(startDate);
        leavePostModel.setToDate(endDate);
        leavePostModel.setPurpose(reason);
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.postLeaveApplication(token, leavePostModel)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("Server Error: "+e.getMessage());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponsePost value) {
                        if (value.isSuccess()) {
                            ToastUtils.longToast(value.getMessage());
                            requireActivity().onBackPressed();
                        }
                    }
                }));
    }
}