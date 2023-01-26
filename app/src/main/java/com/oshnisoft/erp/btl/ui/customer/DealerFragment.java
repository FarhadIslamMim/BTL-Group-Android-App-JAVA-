package com.oshnisoft.erp.btl.ui.customer;


import static io.realm.Realm.getApplicationContext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.RedirectUtils;

import com.oshnisoft.erp.btl.model.Dealer;
import com.oshnisoft.erp.btl.model.District;
import com.oshnisoft.erp.btl.model.Thana;

import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

public class DealerFragment extends Fragment {

    public static final String TAG = "DealerFragment";
    private static final int MY_PERMISSIONS_REQUEST_CALL = 500;
    RecyclerView rvList;
    EditText etFilter;
    Button btnAddNew;
    FastItemAdapter<CustomerList> customerAdapterFastItemAdapter;

    private CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    LoadingDialog loadingDialog;

    String phoneNo;
    Context context;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View root = inflater.inflate(R.layout.fragment_customer_list, container, false);
        rvList = root.findViewById(R.id.rvList);
        etFilter = root.findViewById(R.id.etFilter);
        etFilter.setHint("Search dealer");
        btnAddNew = root.findViewById(R.id.btnAddNew);
        btnAddNew.setVisibility(View.GONE);
        mCompositeDisposable = new CompositeDisposable();
        customerAdapterFastItemAdapter = new FastItemAdapter<>();
        setDealerList();

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerAdapterFastItemAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }


    public void setDealerList() {
        List<CustomerList> customerList = new ArrayList<>();
        List<Dealer> dealerList = r.where(Dealer.class)
                .findAll();
        if (dealerList != null && dealerList.size() > 0) {
            for (Dealer dealer : dealerList) {
                CustomerList iCustomer = new CustomerList();
                iCustomer.setId(dealer.getId());
                iCustomer.setName(dealer.getName());
                iCustomer.setPrimary_mobile_number(dealer.getMobile());
                iCustomer.setContact_person(dealer.getContact_person());
                iCustomer.setSite_district(r.where(District.class).equalTo("id", dealer.getShop_district_id()).findFirst().getName());
                iCustomer.setSite_thana(r.where(Thana.class).equalTo("id", dealer.getShop_upazila_id()).findFirst().getName());
                iCustomer.setSite_address_line(dealer.getShop_address_line());
                customerList.add(iCustomer);
            }
        }
        customerAdapterFastItemAdapter = new FastItemAdapter<>();
        customerAdapterFastItemAdapter.add(customerList);
        customerAdapterFastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<CustomerList>() {
            @Override
            public boolean filter(CustomerList item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        customerAdapterFastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<CustomerList>() {
            @Override
            public boolean onClick(View v, IAdapter<CustomerList> adapter, CustomerList item, int position) {
                openFragment(item.getId());
                return false;
            }
        });
        customerAdapterFastItemAdapter.withItemEvent(new ClickEventHook<CustomerList>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof CustomerList.ViewHolder) {
                    return ((CustomerList.ViewHolder) viewHolder).ivCall;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<CustomerList> fastAdapter, CustomerList item) {
                //ToastUtils.longToast(item.getTxtPhoneNo());
                call(item.getPrimary_mobile_number());

            }
        });
        customerAdapterFastItemAdapter.setHasStableIds(true);
        customerAdapterFastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(customerAdapterFastItemAdapter);


    }


    public void openFragment(long id) {

        //RedirectUtils.goWithDealerId((AppCompatActivity) context, HostActivity.class, false, "customer", (int)id);

    }

    public void call(String phoneNo) {
        this.phoneNo = phoneNo;
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            //ToastUtils.longToast("text sms");
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNo));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNo));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Call failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}