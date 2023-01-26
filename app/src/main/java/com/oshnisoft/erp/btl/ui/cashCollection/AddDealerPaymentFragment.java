package com.oshnisoft.erp.btl.ui.cashCollection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetBankListener;
import com.oshnisoft.erp.btl.listener.SetCustomerListener;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.IBank;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import square.it.image.picker.MediaItem;
import square.it.image.picker.MediaOptions;
import square.it.image.picker.activities.MediaPickerActivity;

public class AddDealerPaymentFragment extends Fragment implements DatePickerFragment.DateDialogListener, SetBankListener, SetCustomerListener {

    private static final int REQUEST_MEDIA_FOR_PHOTO = 1001;



    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtDealer)
    TextView txtDealer;
    @BindView(R.id.txtBank)
    TextView txtBank;
    @BindView(R.id.tieAmount)
    TextInputEditText tieAmount;
    @BindView(R.id.tieNote)
    TextInputEditText tieNote;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.btnSetDealer)
    Button btnSetDealer;
    @BindView(R.id.btnSetBank)
    Button btnSetBank;
    @BindView(R.id.btnSetDate)
    Button btnSetDate;

    String date;
    long user_id;
    long bank_id;


    Uri photoUri;
    Context context;
    AppCompatActivity activity;

    @Inject
    User user;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View root = inflater.inflate(R.layout.fragment_add_dealer_payment, container, false);
        ButterKnife.bind(this,root);
        if(user.getRole().equalsIgnoreCase("Dealer")){
            btnSetDealer.setEnabled(false);
            txtDealer.setText(user.getName());
            user_id = user.getId();
        }
        return root;
    }


    @OnClick({R.id.ivPhoto, R.id.btnSetDate, R.id.btnSetBank, R.id.btnSetDealer, R.id.btnUpload})
    public  void onClickButterknife(View view){
        switch (view.getId()){
            case R.id.ivPhoto:
                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = null;
                File file = new File(
                        getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        new Random().nextLong() + ".jpg");
                options = builder.setIsCropped(true).setFixAspectRatio(true)
                        .setCroppedFile(file)
                        .setIsLineDraw(false)
                        .canSelectMultiPhoto(false).build();
                photoUri = null;
                MediaPickerActivity.open(this, REQUEST_MEDIA_FOR_PHOTO, options);
                break;
            case R.id.btnSetDate:
                DatePickerFragment dialog = DatePickerFragment.newInstance();
                dialog.setDateDialogListener(this);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"DIALOG_DATE");
                break;

            case R.id.btnSetBank:
                DialogFragment dialogBank = new SetBankDialog(this);
                dialogBank.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_bank");
                break;

            case R.id.btnSetDealer:
                DialogFragment dialogFragment = new SetCustomerDialog(this, "dealer");
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_dealer");
                break;
            case R.id.btnUpload:
                upload();
                break;
        }
    }




    @Override
    public void setDate(String date) {
        this.date = date;
        txtDate.setText(date);
        btnSetDate.setText("Change Date");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<MediaItem> mMediaSelectedList;
        if (requestCode == REQUEST_MEDIA_FOR_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                mMediaSelectedList = MediaPickerActivity.getMediaItemSelected(data);
                if (mMediaSelectedList != null) {
                    addImages(mMediaSelectedList.get(0), true);
                }
            }
        }
    }

    private void addImages(MediaItem mediaItem, boolean isPhoto) {

        if (isPhoto) {
            if (mediaItem.getUriCropped() == null) {
                photoUri = mediaItem.getUriOrigin();
                Glide.with(getActivity()).load(photoUri).into(ivPhoto);
            } else {
                photoUri = mediaItem.getUriCropped();
                Glide.with(getActivity()).load(photoUri).into(ivPhoto);
                ivPhoto.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getFileDataFromUri(Uri uri) {
        String encoded;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
        options.inJustDecodeBounds = false;
        Bitmap smallBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        smallBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return encoded;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onSetBank(IBank iBank) {

        this.bank_id = iBank.getId();
        txtBank.setText(iBank.getName() + "[" + iBank.getAccount_number() + "]");
        btnSetBank.setText("Change Bank");
    }

    @Override
    public void onSetCustomer(Customer customer) {
        this.user_id = customer.getId();
        txtDealer.setText(customer.getName());
        btnSetDealer.setText("Change Dealer");
    }

    @Override
    public void onNoCustomerSet() {

    }

    public void upload(){
        SubmitPayment submitPayment = new SubmitPayment();
        double amount = 0;
        String note = "";
        if(user_id <= 0){
            ToastUtils.longToast("Please Set Dealer!!");
            return;
        }
        if(bank_id <= 0){
            ToastUtils.longToast("Please Set Bank!!");
            return;
        }
        if(TextUtils.isEmpty(date)){
            ToastUtils.longToast("Please Set Date!!");
            return;
        }
        if(TextUtils.isEmpty(tieAmount.getText())){
            ToastUtils.longToast("Please Input Amount!!");
            return;
        } else {
            amount = Double.valueOf(tieAmount.getText().toString());
        }

        if(!TextUtils.isEmpty(tieNote.getText())){
            note = tieNote.getText().toString();
        }
        if(photoUri == null){
            ToastUtils.longToast("Please Set Bank Receipt!!");
            return;
        }
        submitPayment.setNote(note);
        submitPayment.setUserId(user_id);
        submitPayment.setBankId(bank_id);
        submitPayment.setDate(date);
        submitPayment.setAmount(amount);
        submitPayment.setImage(getFileDataFromUri(photoUri));
        loadingDialog = LoadingDialog.newInstance(getContext(), "Please wait...");
        loadingDialog.show();
        String auth_token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        compositeDisposable.add(apiServices.postPayment(auth_token, submitPayment)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        Log.e("POST PAYMENT", "onError login: " + e.toString());
                        ToastUtils.longToast("Submit attendance error: " + e.toString());
                    }

                    @Override
                    public void onNext(ResponsePost value) {
                        if (value.isSuccess()) {
                            ToastUtils.shortToast(value.getMessage());
                            requireActivity().onBackPressed();
                        } else {
                            ToastUtils.shortToast("Login Failed " + value.getMessage());
                        }
                    }
                }));



    }


}
