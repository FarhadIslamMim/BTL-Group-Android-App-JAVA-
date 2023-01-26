package com.oshnisoft.erp.btl.ui.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.UploadOrderListener;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.model.IDraftOrderProduct;
import com.oshnisoft.erp.btl.model.IProduct;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.net.APIClients;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.ConnectionUtils;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class OrderPreviewDialog extends DialogFragment {
    private static final String TAG = "OrderPreviewDialog";

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    LoadingDialog loadingDialog;


    List<IProduct> iProductList = new ArrayList<>();
    FastItemAdapter<IProduct> fastItemAdapter;
    AlertDialog alertDialog;
    Context context;

    UploadOrderListener listener;
    Customer customer;
    String operation;

    @Inject
    Realm r;

    Unbinder unbinder;
    @BindView(R.id.txtCustomerName)
    TextView txtCustomerName;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    boolean isEdit;
    IDraftOrder item;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public OrderPreviewDialog() {
    }

    public OrderPreviewDialog(UploadOrderListener listener, List<IProduct> iProductList, Customer customer, boolean isEdit, IDraftOrder item, String operation) {
        this.listener = listener;
        this.iProductList = new ArrayList<>();
        this.iProductList.addAll(iProductList);
        this.customer = customer;
        this.isEdit = isEdit;
        this.item = item;
        this.operation = operation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_order_create_preview, null);
        ButterKnife.bind(this, v);
        setProduct();

        txtCustomerName.setText(customer.getName() + "/" + customer.getContact_person());
        txtDate.setText(DateTimeUtils.getToday(DateTimeUtils.FORMAT7));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);


        return alertDialog;
    }

    public void setProduct() {
        if (!ConnectionUtils.isNetworkConnected(context)) {
            btnSubmit.setText("Draft");
        }

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iProductList);
        fastItemAdapter.withSelectable(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.withItemEvent(new ClickEventHook<IProduct>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IProduct.ViewHolder) {
                    return ((IProduct.ViewHolder) viewHolder).ivPlus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IProduct> fastAdapter1, final IProduct item) {

                item.setQuantity(item.getQuantity() + 1);
                fastAdapter1.notifyDataSetChanged();
            }
        });

        fastItemAdapter.withItemEvent(new ClickEventHook<IProduct>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IProduct.ViewHolder) {
                    return ((IProduct.ViewHolder) viewHolder).ivMinus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IProduct> fastAdapter1, final IProduct item) {

                if (item.getQuantity() > 0) {
                    item.setQuantity(item.getQuantity() - 1);
                    fastAdapter1.notifyDataSetChanged();
                }
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


    @OnClick({R.id.btnCancel, R.id.btnEdit, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                alertDialog.dismiss();
                listener.onCancel();
                break;
            case R.id.btnEdit:
                alertDialog.dismiss();
                break;
            case R.id.btnSubmit:
                submitOrder();
                break;
        }
    }

    public void submitOrder() {

        double totalAmount = 0;
        IDraftOrder orderUpload = new IDraftOrder();
        List<IDraftOrderProduct> orderProductList = new ArrayList<>();
        long id;
        if (operation.equals(AppContainer.KEY_DRAFT_ORDER))
            id = item.getLocalId();
        else
            id = DateTimeUtils.getSystemTimeInMillis();
        for (IProduct iProduct : iProductList) {
            if (iProduct.getQuantity() > 0) {
                IDraftOrderProduct orderProduct = new IDraftOrderProduct();
                orderProduct.setProduct_id(iProduct.getId());
                orderProduct.setTrade_offer_id(0);
                orderProduct.setQuantity(iProduct.getQuantity());
                orderProduct.setUnit_price(iProduct.getPrice());
                orderProduct.setLocalOrderId(id);
                orderProduct.setName(iProduct.getName());
                orderProductList.add(orderProduct);
                totalAmount += iProduct.getPrice() * iProduct.getQuantity();
            }
        }
        if (orderProductList.size() > 0) {
            orderUpload.setItems(orderProductList);
            orderUpload.setOrdered_at(DateTimeUtils.getToday(DateTimeUtils.FORMAT6));
            orderUpload.setUser_id(customer.getId());
            orderUpload.setName(customer.getName() + "/" + customer.getContact_person());
            orderUpload.setTotal_amount(totalAmount);
            orderUpload.setType(((AppContainer.KEY_DealerOrderType.equals("customer")) ? "Customer" : "Dealer"));
            {
                String token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
                mCompositeDisposable = new CompositeDisposable();
                loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
                loadingDialog.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIClients.BaseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIServices api = retrofit.create(APIServices.class);

                if (isEdit) {

                    Call<ResponsePost> call = api.updateOrder(token, item.getId(), orderUpload);
                    call.enqueue(new Callback<ResponsePost>() {
                        @Override
                        public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                            loadingDialog.dismiss();
                            if (response.body() != null && response.body().isSuccess()) {
                                ToastUtils.longToast("Order Sent Successfully.");
                                listener.onSuccessUploadOrder();
                                dismiss();
                            } else if (response.body() != null) {
                                ToastUtils.longToast("Order Sent Failed!! Try again");
                            } else {
                                ToastUtils.longToast("Order Sent Failed!! Try again");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePost> call, Throwable t) {
                            loadingDialog.dismiss();
                            ToastUtils.longToast("Server Error!! Try again");
                        }
                    });
                } else {

                    Call<ResponsePost> call = api.postOrder(token, orderUpload);
                    call.enqueue(new Callback<ResponsePost>() {
                        @Override
                        public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                            loadingDialog.dismiss();
                            if (response.body() != null && response.body().isSuccess()) {
                                ToastUtils.longToast("Order Sent Successfully.");
                                listener.onSuccessUploadOrder();
                                dismiss();
                            } else if (response.body() != null) {
                                ToastUtils.longToast("Order Sent Failed!! Try again");
                            } else {
                                ToastUtils.longToast("Order Sent Failed!! Try again");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePost> call, Throwable t) {
                            loadingDialog.dismiss();
                            ToastUtils.longToast("Server Error!! Try again");
                        }
                    });
                }
            }

        }
    }
}
