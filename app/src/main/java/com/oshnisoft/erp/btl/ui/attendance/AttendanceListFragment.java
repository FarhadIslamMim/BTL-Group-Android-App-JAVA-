package com.oshnisoft.erp.btl.ui.attendance;

import static com.oshnisoft.erp.btl.utils.DateTimeUtils.MONTH_NAME;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.IAttendance;
import com.oshnisoft.erp.btl.model.ResponseDataList;
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


public class AttendanceListFragment extends Fragment {

    public AttendanceListFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "AttendanceListFragment";
    Calendar calendar = Calendar.getInstance();
    int currentMonth = calendar.get(Calendar.MONTH);
    FastItemAdapter<IAttendance> fastItemAdapter;
    CompositeDisposable mCompositeDisposable;
    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;

    @BindView(R.id.ivPreviousMonth)
    ImageView ivPrev;
    @BindView(R.id.ivNextMonth)
    ImageView ivNext;
    @BindView(R.id.txtMonth)
    TextView txtMonth;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    Context context;

    String type;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendance_list, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        getMonthlyAttendance();
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public void getMonthlyAttendance() {
        if (calendar.get(Calendar.MONTH) == currentMonth) {
            ivNext.setVisibility(View.GONE);
        } else {
            ivNext.setVisibility(View.VISIBLE);
        }
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        txtMonth.setText(MONTH_NAME[month - 1] + ", " + year);
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.getAttendance(token, month, year)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<IAttendance>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDataList<IAttendance> value) {
                        if (value.isSuccess() && value.getDataList() != null) {
                            setAdapter(value.getDataList());
                        }
                    }
                }));

    }

    public void setAdapter(List<IAttendance> iAttendanceList) {

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iAttendanceList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);
    }

    @OnClick({R.id.ivPreviousMonth, R.id.ivNextMonth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPreviousMonth:
                calendar.add(Calendar.MONTH, -1);
                getMonthlyAttendance();
                break;
            case R.id.ivNextMonth:
                calendar.add(Calendar.MONTH, 1);
                getMonthlyAttendance();
                break;
        }
    }
}