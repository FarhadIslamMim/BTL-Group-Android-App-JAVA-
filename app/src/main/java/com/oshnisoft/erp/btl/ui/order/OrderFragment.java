package com.oshnisoft.erp.btl.ui.order;



import static com.oshnisoft.erp.btl.utils.DateTimeUtils.MONTH_NAME;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
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


public class OrderFragment extends Fragment {
    //01958252616
    private static final String TAG = "OrderFragment";
    Calendar calendar = Calendar.getInstance();
    int currentMonth = calendar.get(Calendar.MONTH);
    FastItemAdapter<IDraftOrder> fastItemAdapter;
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
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.btnNewOrder)
    Button btnNewOrder;
    @BindView(R.id.btnDraftOrder)
    Button btnDraftOrder;
    @BindView(R.id.etFilter)
    EditText etFilter;
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
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        type = getArguments().getString("Order_Type");
        getMonthlyOrder();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public void getMonthlyOrder() {
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
        if (type.equalsIgnoreCase("ff-to-dealer") || type.equalsIgnoreCase("dealer-to-dealer")) {
            mCompositeDisposable.add(apiServices.getDealerOrder(token, month, year)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDataList<IDraftOrder>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDataList<IDraftOrder> value) {
                            if (value.isSuccess() && value.getDataList() != null) {
                                setAdapter(value.getDataList());
                            }
                        }
                    }));
        } else if (type.equalsIgnoreCase("ff-to-depot") || type.equalsIgnoreCase("depot-to-depot")) {
            mCompositeDisposable.add(apiServices.getDepotOrder(token, month, year)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDataList<IDraftOrder>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDataList<IDraftOrder> value) {
                            if (value.isSuccess() && value.getDataList() != null) {
                                setAdapter(value.getDataList());
                            }
                        }
                    }));
        } else if (type.equalsIgnoreCase("ff-to-customer") || type.equalsIgnoreCase("depot-to-customer") || type.equalsIgnoreCase("dealer-to-customer")) {
            mCompositeDisposable.add(apiServices.getCustomerOrder(token, month, year)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDataList<IDraftOrder>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDataList<IDraftOrder> value) {
                            if (value.isSuccess() && value.getDataList() != null) {
                                setAdapter(value.getDataList());
                            }
                        }
                    }));
        }
    }

    public void setAdapter(List<IDraftOrder> iDraftOrderList) {
        double total = 0;
        for(IDraftOrder iDraftOrder:iDraftOrderList){
            total += iDraftOrder.getTotal_amount();
        }
        txtTotal.setText(String.format("Total Order Amount: BDT. %.0f", total));
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iDraftOrderList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);


        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDraftOrder>() {
            @Override
            public boolean filter(IDraftOrder item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastItemAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDraftOrder>() {
            @Override
            public boolean onClick(View v, IAdapter<IDraftOrder> adapter, IDraftOrder item, int position) {
                if(item.getStatus().equalsIgnoreCase("Ordered") || item.getStatus().equalsIgnoreCase("Updated")) {
                    AppContainer.KEY_DealerOrderType = type;
                    Bundle bundle = new Bundle();
                    bundle.putString("operation", AppContainer.KEY_EDIT_ORDER);
                    bundle.putSerializable("data", item);
                    Fragment fragment;
                    if(type.contains("-dealer")){
                        fragment = new CreateDealerOrderFragment();
                    } else if(type.contains("-depot")){
                        fragment = new CreateDepotOrderFragment();
                    } else {
                        fragment = new CreateCustomerOrderFragment();
                    }
                    fragment.setArguments(bundle);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("create_order").commit();
                }
                return false;
            }
        });
    }


    @OnClick({R.id.btnNewOrder, R.id.ivPreviousMonth, R.id.ivNextMonth, R.id.btnDraftOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewOrder:
                Fragment fragment;
                if(type.contains("-dealer")){
                    fragment = new CreateDealerOrderFragment();
                } else if(type.contains("-depot")){
                    fragment = new CreateDepotOrderFragment();
                } else {
                    fragment = new CreateCustomerOrderFragment();
                }
                AppContainer.KEY_DealerOrderType = type;
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("create_order").commit();
                break;
            case R.id.btnDraftOrder:
                Fragment fragment1 = new DraftOrderFragment();
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).addToBackStack("draft_order").commit();
                break;
            case R.id.ivPreviousMonth:
                calendar.add(Calendar.MONTH, -1);
                getMonthlyOrder();
                break;
            case R.id.ivNextMonth:
                calendar.add(Calendar.MONTH, 1);
                getMonthlyOrder();
                break;
        }
    }
}