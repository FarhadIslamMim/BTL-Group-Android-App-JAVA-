package com.oshnisoft.erp.btl.ui.cashCollection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetCustomerListener;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.Dealer;
import com.oshnisoft.erp.btl.model.Depot;
import com.oshnisoft.erp.btl.model.District;
import com.oshnisoft.erp.btl.model.Thana;
import com.oshnisoft.erp.btl.ui.order.ICustomer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;


/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class SetCustomerDialog extends DialogFragment {
    private static final String TAG = "SetCustomer2Dialog";


    List<ICustomer> iCustomerList = new ArrayList<>();
    FastItemAdapter<ICustomer> fastItemAdapter;
    AlertDialog alertDialog;
    Context context;

    SetCustomerListener listener;

    @Inject
    Realm r;

    Unbinder unbinder;
    @BindView(R.id.etFilter)
    EditText etFilter;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.ivClose)
    ImageView ivClose;

    String type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public SetCustomerDialog() {
    }

    public SetCustomerDialog(SetCustomerListener listener, String type) {
        this.listener = listener;
        this.type = type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_order_set_customer, null);
        ButterKnife.bind(this, v);
        if(type.equalsIgnoreCase("dealer")){
            setDealer();
            title.setText("Set Dealer");
            etFilter.setHint("Search Dealer");
        } else {
            setDepot();
            title.setText("Set Depot");
            etFilter.setHint("Search Depot");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNoCustomerSet();
                alertDialog.dismiss();

            }
        });


        return alertDialog;
    }

//    AppContainer.KEY_ORDER


    public void setDealer() {
        List<Dealer> customers = r.where(Dealer.class).findAll();
        if (customers != null && customers.size() > 0) {
            for (Dealer customer : customers) {
                ICustomer iCustomer = new ICustomer();
                iCustomer.setId(customer.getId());
                iCustomer.setName(customer.getName());
                iCustomer.setPrimary_mobile_number(customer.getMobile());
                iCustomer.setContact_person(customer.getContact_person());
                iCustomer.setSite_district(r.where(District.class).equalTo("id", customer.getShop_district_id()).findFirst().getName());
                iCustomer.setSite_thana(r.where(Thana.class).equalTo("id", customer.getShop_upazila_id()).findFirst().getName());
                iCustomer.setSite_address_line(customer.getShop_address_line());
                iCustomer.setZone_id(0);
                iCustomerList.add(iCustomer);
            }
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iCustomerList);
        fastItemAdapter.withSelectable(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ICustomer>() {
            @Override
            public boolean onClick(View v, IAdapter<ICustomer> adapter, ICustomer item, int position) {
                Dealer dealer = r.where(Dealer.class).equalTo("id", item.getId()).findFirst();
                Customer customer = new Customer();
                customer.setId(dealer.getId());
                customer.setName(dealer.getName());
                customer.setContact_person(dealer.getContact_person());
                listener.onSetCustomer(customer);
                alertDialog.dismiss();
                return false;
            }
        });

        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<ICustomer>() {
            @Override
            public boolean filter(ICustomer item, CharSequence constraint) {
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


    }

    public void setDepot() {
        List<Depot> customers = r.where(Depot.class).findAll();
        if (customers != null && customers.size() > 0) {
            for (Depot customer : customers) {
                ICustomer iCustomer = new ICustomer();
                iCustomer.setId(customer.getId());
                iCustomer.setName(customer.getName());
                iCustomer.setPrimary_mobile_number(customer.getMobile());
                iCustomer.setContact_person(customer.getContact_person());
                iCustomer.setSite_district(r.where(District.class).equalTo("id", customer.getShop_district_id()).findFirst().getName());
                iCustomer.setSite_thana(r.where(Thana.class).equalTo("id", customer.getShop_upazila_id()).findFirst().getName());
                iCustomer.setSite_address_line(customer.getShop_address_line());
                iCustomer.setZone_id(0);
                iCustomerList.add(iCustomer);
            }
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iCustomerList);
        fastItemAdapter.withSelectable(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ICustomer>() {
            @Override
            public boolean onClick(View v, IAdapter<ICustomer> adapter, ICustomer item, int position) {
                Depot dealer = r.where(Depot.class).equalTo("id", item.getId()).findFirst();
                Customer customer = new Customer();
                customer.setId(dealer.getId());
                customer.setName(dealer.getName());
                customer.setContact_person(dealer.getContact_person());
                listener.onSetCustomer(customer);
                alertDialog.dismiss();
                return false;
            }
        });

        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<ICustomer>() {
            @Override
            public boolean filter(ICustomer item, CharSequence constraint) {
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


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
