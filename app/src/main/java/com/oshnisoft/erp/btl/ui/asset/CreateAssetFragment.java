package com.oshnisoft.erp.btl.ui.asset;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;
import com.oshnisoft.erp.btl.model.IAsset;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CreateAssetFragment extends Fragment implements DatePickerFragment.DateDialogListener {


    @BindView(R.id.expectedDate)
    TextView expectedDate;
    String date;

    @BindView(R.id.tieItem)
    TextInputEditText tieItem;

    @BindView(R.id.tieQuantity)
    TextInputEditText tieQuantity;

    @BindView(R.id.tieNote)
    TextInputEditText tieNote;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    CompositeDisposable mCompositeDisposable;
    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;
    Context context;
    @Inject
    User user;

    public CreateAssetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_asset, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick({R.id.expectedDate, R.id.btnSubmit})
    public  void onClickButterknife(View view){
        switch (view.getId()){
            case R.id.expectedDate:
                setDate();
                break;

            case R.id.btnSubmit:
                postAsset();
                break;
        }
    }

    public void postAsset() {
        String date = Objects.requireNonNull(expectedDate.getText()).toString();
        String item = Objects.requireNonNull(tieItem.getText()).toString();
        String quantity = Objects.requireNonNull(tieQuantity.getText()).toString();
        String note = Objects.requireNonNull(tieNote.getText()).toString();

        if (TextUtils.isEmpty(date) && TextUtils.isEmpty(item) && TextUtils.isEmpty(quantity) && TextUtils.isEmpty(note)) {
            ToastUtils.longToast("Please Input All necessary field");
            return;
        }
        IAsset iAsset = new IAsset();
        iAsset.setExpected_date(date);
        iAsset.setItem(item);
        iAsset.setQuantity(Integer.parseInt(quantity));
        iAsset.setNote(note);
        iAsset.setEmployee_id(user.getEmployee_id());

        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(requireContext(), "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.postAsset(token,iAsset).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("Server Error: " + e.getMessage());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponsePost value) {
                        if (value.isSuccess()) {
                            ToastUtils.longToast(value.getMessage());
                            requireActivity().onBackPressed();
                        }
                    }
                }));

    }

    public void setDate() {
        DatePickerFragment dialog = DatePickerFragment.newInstance();
        dialog.setDateDialogListener(this);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "DIALOG_DATE");
    }

    @Override
    public void setDate(String cal) {
        expectedDate.setText(cal);
    }




//    public String monthConvert(int month) {
//        String monthCal = String.valueOf(month);
//        if (monthCal.length() == 1) {
//            return "0" + monthCal;
//        }
//        return monthCal;
//    }

}