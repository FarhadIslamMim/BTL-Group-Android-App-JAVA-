package com.oshnisoft.erp.btl.ui.leave;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.ILeaveDetail;
import com.oshnisoft.erp.btl.model.ILeaveSummary;
import com.oshnisoft.erp.btl.model.LeaveReportModel;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class LeaveFragment extends Fragment {

    Context context;
    Activity activity;
    @BindView(R.id.ivPreviousYear)
    ImageView ivPreviousYear;
    @BindView(R.id.ivNextYear)
    ImageView ivNextYear;
    @BindView(R.id.txtYear)
    TextView txtYear;
    @BindView(R.id.rvSummary)
    RecyclerView rvSummary;
    @BindView(R.id.rvDetailList)
    RecyclerView rvDetailList;
    @BindView(R.id.btnNewLeave)
    Button btnNewLeave;


    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);

    FastItemAdapter<ILeaveSummary> iLeaveSummaryFastItemAdapter;
    FastItemAdapter<ILeaveDetail> iLeaveDetailFastItemAdapter;
    CompositeDisposable compositeDisposable;
    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;

    public LeaveFragment() {
        // Required empty public constructor
    }

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
        View root = inflater.inflate(R.layout.fragment_leave, container, false);
        ButterKnife.bind(this, root);
        ((AppCompatActivity) context).setTitle("Leave");

        getLeave();

        return root;
    }

    public void getLeave() {
        if (calendar.get(Calendar.YEAR) == currentYear) {
            ivNextYear.setVisibility(View.GONE);
        } else {
            ivNextYear.setVisibility(View.VISIBLE);
        }
        int year = calendar.get(Calendar.YEAR);
        txtYear.setText("" + year);
        compositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        compositeDisposable.add(apiServices.getLeaveData(token, year)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LeaveReportModel>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(LeaveReportModel value) {
                        if (value.isSuccess()) {
                            if(value.getData() != null){
                                setSummaryAdapter(value.getData());
                            }
                            if(value.getLeaveList() != null){
                                setDetailAdapter(value.getLeaveList());
                            }

                        }
                    }
                }));
    }

    public void setSummaryAdapter(List<ILeaveSummary> iLeaveSummaryList) {

        iLeaveSummaryFastItemAdapter = new FastItemAdapter<>();
        iLeaveSummaryFastItemAdapter.add(iLeaveSummaryList);
        iLeaveSummaryFastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvSummary.setLayoutManager(layoutManager);
        rvSummary.setAdapter(iLeaveSummaryFastItemAdapter);
    }

    public void setDetailAdapter(List<ILeaveDetail> iLeaveDetailList) {

        iLeaveDetailFastItemAdapter = new FastItemAdapter<>();
        iLeaveDetailFastItemAdapter.add(iLeaveDetailList);
        iLeaveDetailFastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvDetailList.setLayoutManager(layoutManager);
        rvDetailList.setAdapter(iLeaveDetailFastItemAdapter);
    }


    @OnClick({R.id.btnNewLeave, R.id.ivPreviousYear, R.id.ivNextYear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewLeave:
                Fragment fragment = new ApplyLeaveFragment();
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                break;

            case R.id.ivPreviousYear:
                calendar.add(Calendar.YEAR, -1);
                getLeave();
                break;
            case R.id.ivNextYear:
                calendar.add(Calendar.YEAR, 1);
                getLeave();
                break;
        }
    }
}