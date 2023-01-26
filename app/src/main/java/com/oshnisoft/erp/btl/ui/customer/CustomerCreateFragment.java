package com.oshnisoft.erp.btl.ui.customer;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.District;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.Thana;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.model.UserDistrict;
import com.oshnisoft.erp.btl.model.UserThana;
import com.oshnisoft.erp.btl.net.APIClients;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerCreateFragment extends Fragment {

    public static final String TAG = "CustomerCreateFragment";


    @BindView(R.id.tieName)
    TextInputEditText tieName;
    @BindView(R.id.tieContactPerson)
    TextInputEditText tieContactPerson;
    @BindView(R.id.tiePrimaryNumber)
    TextInputEditText tiePrimaryNumber;
    @BindView(R.id.tieSecondaryOne)
    TextInputEditText tieSecondaryOne;
    @BindView(R.id.spDistrictOnsiteAdd)
    Spinner spDistrictOnsiteAdd;
    @BindView(R.id.spThanaOnsiteAdd)
    Spinner spThanaOnsiteAdd;
    @BindView(R.id.tieOnSiteAddressLine)
    TextInputEditText tieOnSiteAddressLine;
    @BindView(R.id.btnUpload)
    Button btnUpload;

    @Inject
    List<District> districtList;

    @Inject
    UserData userData;
    List<Thana> thanaSiteList = new ArrayList<>();
    List<UserThana> thanaList;


    private CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    LoadingDialog loadingDialog;

    Context context;

    AppCompatActivity activity;
    Uri photoUri, shopUri;

    List<UserDistrict> userDistrictList;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = ((AppCompatActivity) context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View root = inflater.inflate(R.layout.fragment_customer_add, container, false);
        ButterKnife.bind(this, root);
        activity.setTitle("Create New Customer");
        setDistrictSpinner();
        return root;
    }

    public void setDistrictSpinner() {

        userDistrictList = r.where(UserDistrict.class).findAll();
        ArrayAdapter<UserDistrict> districtAdapter = new ArrayAdapter<UserDistrict>(context, android.R.layout.simple_spinner_dropdown_item, userDistrictList);
        ArrayAdapter<District> marketAdapter = new ArrayAdapter<District>(context, android.R.layout.simple_spinner_dropdown_item, districtList);
        spDistrictOnsiteAdd.setAdapter(districtAdapter);
        spDistrictOnsiteAdd.setSelection(0);


        spDistrictOnsiteAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setShopThana((UserDistrict) spDistrictOnsiteAdd.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void setShopThana(UserDistrict district) {

        thanaList = r.where(UserThana.class).equalTo("district_id", district.getId()).findAll();
        ArrayAdapter<UserThana> marketAdapter = new ArrayAdapter<UserThana>(context, android.R.layout.simple_spinner_dropdown_item, thanaList);
        spThanaOnsiteAdd.setAdapter(marketAdapter);
        spThanaOnsiteAdd.setSelection(0);
    }


    @OnClick({R.id.btnUpload})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnUpload:
                if (isValidate()) {
                    postCustomer();
                }
                break;
        }
    }

    CustomerUpload customerUpload;

    public boolean isValidate() {
        customerUpload = new CustomerUpload();
        if (TextUtils.isEmpty(tieName.getText())) {
            ToastUtils.longToast("Please input business name.");
            tieName.requestFocus();
            return false;
        } else {
            customerUpload.setName(tieName.getText().toString());
        }

        if (TextUtils.isEmpty(tieContactPerson.getText())) {
            ToastUtils.longToast("Please input contact person name.");
            tieContactPerson.requestFocus();
            return false;
        } else {
            customerUpload.setContact_person(tieContactPerson.getText().toString());
        }


        if (TextUtils.isEmpty(tiePrimaryNumber.getText())) {
            ToastUtils.longToast("Please input customer mobile number.");
            tiePrimaryNumber.requestFocus();
            return false;
        } else {
            customerUpload.setMobile(tiePrimaryNumber.getText().toString());
        }

        if (!TextUtils.isEmpty(tieSecondaryOne.getText())) {
            customerUpload.setSecondary_mobile_number(tieSecondaryOne.getText().toString());
        } else {
            customerUpload.setSecondary_mobile_number("");
        }


        customerUpload.setShop_district_id(((UserDistrict) spDistrictOnsiteAdd.getSelectedItem()).getId());

        customerUpload.setShop_upazila_id(((UserThana) spThanaOnsiteAdd.getSelectedItem()).getId());
        if (TextUtils.isEmpty(tieOnSiteAddressLine.getText())) {
            ToastUtils.longToast("Please input customer site address.");
            tieOnSiteAddressLine.requestFocus();
            return false;
        } else {
            customerUpload.setShop_address_line(tieOnSiteAddressLine.getText().toString());
        }

        customerUpload.setPassword("12345678");
        customerUpload.setPassword_confirmation("12345678");
        customerUpload.setStatus("Active");

        return true;
    }


    public void postCustomer() {

        String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIClients.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<ResponsePost> call = api.postCustomer(token, customerUpload);


        call.enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

                if (response.body() != null && response.body().isSuccess()) {
                    getCustomer(token, loadingDialog);
                } else if (response.body() != null) {
                    loadingDialog.dismiss();
                    ToastUtils.longToast("Customer Upload Failed!! Try again");
                } else {
                    loadingDialog.dismiss();
                    ToastUtils.longToast("Customer Upload Failed!! Try again");
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                loadingDialog.dismiss();
                ToastUtils.longToast("Server Error!! Try again");
            }
        });

    }

    public void getCustomer(String token, LoadingDialog loadingDialog) {
        mCompositeDisposable.add(apiServices.getCustomers(token)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Customer>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        ((AppCompatActivity) context).onBackPressed();

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ((AppCompatActivity) context).onBackPressed();
                    }

                    @Override
                    public void onNext(ResponseDataList<Customer> value) {
                        if (value.isSuccess() && value.getDataList() != null) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Customer.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        }
                    }
                }));
    }
}