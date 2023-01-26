package com.oshnisoft.erp.btl.ui.bill;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;


import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CreateBillFragment extends Fragment {
    boolean isEdit;

    @BindView(R.id.dateID)
    TextView dateID;


    @BindView(R.id.daBill)
    TextInputEditText daBill;

    @BindView(R.id.taBill)
    TextInputEditText taBill;


    @BindView(R.id.remarks)
    TextInputEditText remarks;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.holiday)
    CheckBox holiday;
    CompositeDisposable mCompositeDisposable;
    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;

    public CreateBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEdit = getArguments().getBoolean("is_Edit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_bill, container, false);
        ButterKnife.bind(this, v.getRootView());
        App.getComponent().inject(this);
        return v.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cal = Calendar.getInstance();
        dateID.setText("" + cal.get(Calendar.YEAR) + "-" + monthConvert(cal.get(Calendar.MONTH) + 1) + "-" + monthConvert(cal.get(Calendar.DATE)));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBill();
            }
        });

        dateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillDatePickerFragment dialog1 = BillDatePickerFragment.newInstance();
                dialog1.setDateDialogListener(new BillDatePickerFragment.BillDateDialogListener() {
                    @Override
                    public void setDate(String date) {
                        dateID.setText(date);
                    }
                }, true);
                dialog1.show(((AppCompatActivity) requireContext()).getSupportFragmentManager(), "DIALOG_DATE");
            }
        });
    }

    public void postBill() {
        String summary = Objects.requireNonNull(remarks.getText()).toString();
        String date = Objects.requireNonNull(dateID.getText()).toString();
        String daAmount = Objects.requireNonNull(daBill.getText()).toString();
        String taAmount = Objects.requireNonNull(taBill.getText()).toString();
        String isHoliday = (holiday.isChecked()) ? "YES" : "NO";

        if (TextUtils.isEmpty(date) && TextUtils.isEmpty(summary) && TextUtils.isEmpty(daAmount) && TextUtils.isEmpty(taAmount)) {
            ToastUtils.longToast("Please Input All necessary field");
            return;
        }
        PostBill postBill = new PostBill();
        postBill.setDate(date);
        postBill.setDaAmount(Integer.parseInt(daAmount));
        postBill.setIsHoliday(isHoliday);
        postBill.setTaAmount(Integer.parseInt(taAmount));
        postBill.setDailySummary(summary);
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(requireContext(), "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.postBill(token, postBill).subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("Server Error: " + e.getMessage());
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

    public String monthConvert(int month) {
        String monthCal = String.valueOf(month);
        if (monthCal.length() == 1) {
            return "0" + monthCal;
        }
        return monthCal;
    }
}