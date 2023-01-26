package com.oshnisoft.erp.btl.ui.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetQuantityListener;
import com.oshnisoft.erp.btl.listener.UploadOrderListener;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.DealerLimit;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.model.IDraftOrderProduct;
import com.oshnisoft.erp.btl.model.IProduct;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.net.APIClients;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.ConnectionUtils;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.io.Serializable;
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

public class OrderPreviewFragment extends Fragment implements DatePickerFragmentDate.DateDialogListener, SetQuantityListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @Inject
    APIServices apiServices;

    @Inject
    User user;

    @Inject
    UserData userData;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    LoadingDialog loadingDialog;


    List<IProduct> iProductList;
    List<IProduct> mainList = new ArrayList<>();
    FastItemAdapter<IProduct> fastItemAdapter;
    AlertDialog alertDialog;
    Context context;

    UploadOrderListener listener;
    Customer customer;
    String operation;
    static String date = "";

    @Inject
    Realm r;

    Unbinder unbinder;
    @BindView(R.id.txtCustomerName)
    TextView txtCustomerName;

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    boolean isEdit;
    IDraftOrder item;


    @BindView(R.id.txtDealerDue)
    TextView txtDealerDue;


    @BindView(R.id.txtDateDe)
    TextView txtDateDe;
    @BindView(R.id.llDate)
    LinearLayout llDate;


    @BindView(R.id.edtDelNote)
    TextView edtDelNote;
    DealerLimit list;

    static String note = "";
    public static double BALANCE_AMOUNT;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }


    public OrderPreviewFragment() {

    }

    public OrderPreviewFragment(UploadOrderListener listener) {
        this.listener = listener;
    }

    public static OrderPreviewFragment newInstance(String date1, String note1, UploadOrderListener listener) {
        OrderPreviewFragment fragment = new OrderPreviewFragment(listener);
        note = note1;
        date = date1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_order_preview, null);
        ButterKnife.bind(this, v);
        App.getComponent().inject(this);
        if (getArguments() != null) {
            iProductList = (List<IProduct>) getArguments().getSerializable("list");
            mainList = (List<IProduct>) getArguments().getSerializable("mainProductList");
            Log.d("TAG", "onCreateView: " + mainList.size());
            customer = (Customer) getArguments().getSerializable("customer");
            operation = getArguments().getString("operation");
            item = (IDraftOrder) getArguments().getSerializable("order");
            isEdit = getArguments().getBoolean("isEdit");
            list = (DealerLimit) getArguments().getSerializable("responseDataList");

        }

        for (int i = 0; i < iProductList.size(); i++) {
            iProductList.get(i).setDelete(true);
        }
        calOrder();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProduct();
        txtDateDe.setText(date);
        edtDelNote.setText(note);
        txtCustomerName.setText(customer.getName() + "/" + customer.getContact_person());
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
                calOrder();
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
                    calOrder();
                }
            }
        });

        fastItemAdapter.withItemEvent(new ClickEventHook<IProduct>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IProduct.ViewHolder) {
                    return ((IProduct.ViewHolder) viewHolder).delete;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IProduct> fastAdapter1, final IProduct item) {
                iProductList.remove(item);
                //fastAdapter1.notifyDataSetChanged();
                setProduct();

            }
        });

        fastItemAdapter.withItemEvent(new ClickEventHook<IProduct>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IProduct.ViewHolder) {
                    return ((IProduct.ViewHolder) viewHolder).txtQuantity;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IProduct> fastAdapter1, final IProduct item) {

                openSetQuantityDialog(item, position);
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


    @OnClick({R.id.btnCancel, R.id.btnEdit, R.id.btnSubmit, R.id.llDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                note = edtDelNote.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) iProductList);
                bundle.putSerializable("mainList", (Serializable) mainList);
                Fragment fragment = new AddItemFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case R.id.btnEdit:
                break;
            case R.id.btnSubmit:
                if (TextUtils.isEmpty(edtDelNote.getText()) && TextUtils.isEmpty(date)) {
                    ToastUtils.longToast("Please select delivery date and Write note");
                } else {
                    note = edtDelNote.getText().toString();
                    setAlertDialog();
                }
                break;
            case R.id.llDate:
                DatePickerFragmentDate dialog = DatePickerFragmentDate.newInstance();
                dialog.setDateDialogListener(this);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "DIALOG_DATE");
                break;
        }
    }

    double totalAmount = 0;

    public void calOrder() {
        totalAmount = 0;
        for (IProduct iProduct : iProductList) {

            if (iProduct.getQuantity() > 0) {
                totalAmount += iProduct.getPrice() * iProduct.getQuantity();
            }
        }
        if (list != null && list.getDealerLimit() > 0) {
            double i = Math.max(((list.getDealerLimit() < (list.getDelearDue() + totalAmount)) ? ((list.getDelearDue() + totalAmount) - list.getDealerLimit()) : 0), 0);
            if (i > 0) {
                txtDealerDue.setTextColor(Color.RED);
            } else {
                txtDealerDue.setTextColor(Color.BLACK);
            }

            txtDealerDue.setText(String.format("Limit: %s, Due: %s, C.Order Amt: %s, Exceed: %s", list.getDealerLimit(), list.getDelearDue(), totalAmount, i));
        } else {
            txtDealerDue.setTextColor(Color.BLACK);
            txtDealerDue.setText(String.format("Order Amount: %s", totalAmount));
        }
    }

    public void submitOrder() { //
        double totalAmount = 0;
        IDraftOrder orderUpload = new IDraftOrder();
        orderUpload.setDelivery_date(date);
        orderUpload.setNote(note);
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
            orderUpload.setType("Customer");

            if (ConnectionUtils.isNetworkConnected(context)) {
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
                                ((AppCompatActivity) context).onBackPressed();
                                listener.onSuccessUploadOrder();
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
                                ((AppCompatActivity) context).onBackPressed();
                                listener.onSuccessUploadOrder();
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
                /*orderUpload.setLocalId(id);
                orderUpload.setId(id);
                r.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        if (operation.equals(AppContainer.KEY_DRAFT_ORDER)) {
                            realm.where(IDraftOrder.class).equalTo("id", id).findFirst().deleteFromRealm();
                            RealmResults<IDraftOrderProduct> draftOrderProducts = realm.where(IDraftOrderProduct.class).equalTo("localOrderId", id).findAll();
                            draftOrderProducts.deleteAllFromRealm();
                        }
                        realm.insertOrUpdate(orderUpload);
                        realm.insertOrUpdate(orderProductList);
                        ToastUtils.longToast("Order Saved Successfully.");
                        ((AppCompatActivity) context).onBackPressed();
                        listener.onSuccessUploadOrder();
                    }
                });*/
            } else {

            }

        }
    }

    public void setAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        builder1.setCancelable(true);
        if(totalAmount >= BALANCE_AMOUNT){
            builder1.setMessage("Order amount is greater than balance amount. Please make online then re-order again.");
            builder1.setNegativeButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        } else {
            builder1.setMessage("Confirm Order?");
            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            submitOrder();
                        }
                    });
        }



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void openSetQuantityDialog(IProduct item, int position) {
        DialogFragment dialogFragment = new AddQuantityDialog(this, item, position);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_quantity");
    }

    @Override
    public void setDate(String date) {
        txtDateDe.setText(date);
        this.date = date;
    }

    @Override
    public void onSetQuantity(int quantity, int position) {
        iProductList.get(position).setQuantity(quantity);
        fastItemAdapter.notifyAdapterDataSetChanged();
        calOrder();
    }
}