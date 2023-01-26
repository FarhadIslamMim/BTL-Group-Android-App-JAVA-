package com.oshnisoft.erp.btl.ui.cashCollection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetBankListener;
import com.oshnisoft.erp.btl.model.Bank;
import com.oshnisoft.erp.btl.model.IBank;


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
public class SetBankDialog extends DialogFragment {
    private static final String TAG = "SetCustomer2Dialog";


    List<IBank> iBanks = new ArrayList<>();
    FastItemAdapter<IBank> fastItemAdapter;
    AlertDialog alertDialog;
    Context context;

    SetBankListener listener;

    @Inject
    Realm r;

    Unbinder unbinder;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.ivClose)
    ImageView ivClose;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public SetBankDialog() {
    }

    public SetBankDialog(SetBankListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_set_bank, null);
        ButterKnife.bind(this, v);
        setBank();


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });


        return alertDialog;
    }

//    AppContainer.KEY_ORDER


    public void setBank() {
        List<Bank> banks = r.where(Bank.class).findAll();
        if (banks != null && banks.size() > 0) {
            for (Bank bank : banks) {
                IBank iBank = new IBank();
                iBank.setId(bank.getId());
                iBank.setName(bank.getName());
                iBank.setBranch(bank.getBranch());
                iBank.setAccount_number(bank.getAccount_number());
                iBanks.add(iBank);
            }
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iBanks);
        fastItemAdapter.withSelectable(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IBank>() {
            @Override
            public boolean onClick(View v, IAdapter<IBank> adapter, IBank item, int position) {


                listener.onSetBank(item);
                alertDialog.dismiss();
                return false;
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
