package com.oshnisoft.erp.btl.ui.bill;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.IBill;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class BillFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String month;
    private String year;

    @BindView(R.id.rvBill)
    RecyclerView rvBill;
    @BindView(R.id.Add)
    FloatingActionButton floatingActionButton;
    CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    FastItemAdapter<IBill> iBillFastItemAdapter;

    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            month = String.valueOf(b.getInt("month"));
            year = String.valueOf(b.getInt("year"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bill, container, false);
        ButterKnife.bind(this, v.getRootView());
        App.getComponent().inject(this);
        return v.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getBills(month, year);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateBillFragment();
                Bundle b = new Bundle();
                b.putInt("day", 1);
                b.putInt("month", 1);
                b.putInt("year", 2022);
                b.putBoolean("isEdit", false);
                fragment.setArguments(b);
                ((AppCompatActivity) requireContext()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("add_Bill").commit();
            }
        });
    }

    public void getBills(String month, String year) {
        loadingDialog = LoadingDialog.newInstance(requireContext(), "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getBill(token, month, year) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<IBill>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("Server Error: " + e.getMessage());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDataList<IBill> value) {
                        loadingDialog.dismiss();
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            generateData(value.getDataList());
                        } else {
                            ToastUtils.longToast("No Data available ");
                        }
                    }
                }));
    }

    void generateData(List<IBill> value) {
        iBillFastItemAdapter = new FastItemAdapter<>();
        iBillFastItemAdapter.add(value);
        iBillFastItemAdapter.setHasStableIds(true);
        iBillFastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rvBill.setLayoutManager(layoutManager);
        rvBill.setAdapter(iBillFastItemAdapter);

    }
}