package com.oshnisoft.erp.btl.ui.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetQuantityListener;
import com.oshnisoft.erp.btl.model.IProduct;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;


/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class AddQuantityDialog extends DialogFragment {
    private static final String TAG = "AddQuantityDialog";



    AlertDialog alertDialog;
    Context context;

    SetQuantityListener listener;

    @Inject
    Realm r;

    Unbinder unbinder;
    @BindView(R.id.txtProduct)
    TextView txtProduct;
    @BindView(R.id.tieQuantity)
    TextInputEditText tieQuantity;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnSet)
    Button btnSet;
    int position = 0;
    IProduct item;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public AddQuantityDialog(){ }
    public AddQuantityDialog(SetQuantityListener listener, IProduct item, int position){
        this.listener = listener;
        this.item = item;
        this.position = position;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_add_quantity, null);
        ButterKnife.bind(this, v);
        txtProduct.setText(item.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

         alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tieQuantity.getText().toString())){
                    ToastUtils.longToast("Enter valid number");
                } else {
                    int qty = Integer.valueOf(tieQuantity.getText().toString());
                    listener.onSetQuantity(qty, position);
                    alertDialog.dismiss();
                }

            }
        });


        return alertDialog;
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
