package com.oshnisoft.erp.btl.ui.cashCollection;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.ICashCollection;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.model.Bank;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
import io.realm.Realm;


public class PaymentFragment extends Fragment {

    private static final String TAG = "OrderFragment";
    Calendar calendar = Calendar.getInstance();
    int currentMonth = calendar.get(Calendar.MONTH);
    FastItemAdapter<ICashCollection> fastItemAdapter;
    CompositeDisposable mCompositeDisposable;
    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;


    @BindView(R.id.btnAddNew)
    Button btnAddNew;
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
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        type = getArguments().getString("Pay_Type");
        getMonthlyOrder();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public void getMonthlyOrder() {
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        if (type.contains("-dealer")) {
            mCompositeDisposable.add(apiServices.getDealerPayments(token)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDataList<ICashCollection>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDataList<ICashCollection> value) {
                            if (value.isSuccess() && value.getDataList() != null) {
                                setAdapter(value.getDataList());
                            }
                        }
                    }));
        } else if (type.contains("-depot")) {
            mCompositeDisposable.add(apiServices.getDepotPayments(token)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDataList<ICashCollection>>() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDataList<ICashCollection> value) {
                            if (value.isSuccess() && value.getDataList() != null) {
                                setAdapter(value.getDataList());
                            }
                        }
                    }));
        }
    }

    public void setAdapter(List<ICashCollection> iDraftOrderList) {
        List<ICashCollection> iCashCollectionList = new ArrayList<>();
        for(ICashCollection iCashCollection:iDraftOrderList){
            Bank bank = r.where(Bank.class).equalTo("id", iCashCollection.getBank_id()).findFirst();
            if(bank != null){
                iCashCollection.setBankName(bank.getName());
                iCashCollection.setBankAccount(bank.getAccount_number());
                iCashCollectionList.add(iCashCollection);
            }


        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iCashCollectionList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);


        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<ICashCollection>() {
            @Override
            public boolean filter(ICashCollection item, CharSequence constraint) {
                return !item.getUser().getName().toLowerCase().contains(constraint.toString().toLowerCase());
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

        /*fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDraftOrder>() {
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
        });*/
    }


    @OnClick({R.id.btnAddNew})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddNew:
                Fragment fragment;
                if(type.contains("-dealer")){
                    fragment = new AddDealerPaymentFragment();
                } else if(type.contains("-depot")){
                    fragment = new AddDepotPaymentFragment();
                } else {
                    fragment = new AddDealerPaymentFragment();
                }
                AppContainer.KEY_DealerOrderType = type;
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("create_order").commit();
                break;
        }
    }
}