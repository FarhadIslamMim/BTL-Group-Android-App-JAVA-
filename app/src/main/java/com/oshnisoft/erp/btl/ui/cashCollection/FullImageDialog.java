package com.oshnisoft.erp.btl.ui.cashCollection;

import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.oshnisoft.erp.btl.R;

import butterknife.BindView;
import butterknife.ButterKnife;



@SuppressLint("ValidFragment")
public class FullImageDialog extends DialogFragment {
    private static final String TAG = "FullImageDialog";

    android.app.AlertDialog alertDialog;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.ivSample)
    ImageView ivSample;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;
    Context context;
    String url;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public FullImageDialog(){ }
    public FullImageDialog(String url){
        this.url = url;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_full_image, null);
        ButterKnife.bind(this, v);
        initializeView();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

         alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });


        return alertDialog;
    }

    public void initializeView(){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        Glide.with(context).load(url).placeholder(circularProgressDrawable).into(ivSample);
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
