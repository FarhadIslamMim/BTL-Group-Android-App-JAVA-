package com.oshnisoft.erp.btl.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.listener.SetCustomerListener;
import com.oshnisoft.erp.btl.listener.SetPriceListener;
import com.oshnisoft.erp.btl.listener.SetQuantityListener;
import com.oshnisoft.erp.btl.listener.UploadOrderListener;
import com.oshnisoft.erp.btl.model.Balance;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.DealerLimit;
import com.oshnisoft.erp.btl.model.Depot;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.model.IDraftOrderProduct;
import com.oshnisoft.erp.btl.model.IProduct;
import com.oshnisoft.erp.btl.model.Product;
import com.oshnisoft.erp.btl.model.ResponseData;
import com.oshnisoft.erp.btl.model.TradeOffer;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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


public class CreateDepotOrderFragment extends Fragment implements SetCustomerListener, UploadOrderListener, SetQuantityListener, SetPriceListener {
    private static final String TAG = "CreateDepotOrderFragment";

    @BindView(R.id.txtCustomerName)
    TextView txtCustomerName;
    @BindView(R.id.txtOrderAmount)
    TextView txtOrderAmount;
    @BindView(R.id.btnSetCustomer)
    Button btnSetCustomer;
    @BindView(R.id.etFilter)
    EditText etFilter;
    @BindView(R.id.btnPreview)
    Button btnPreview;
    @BindView(R.id.rvList)
    RecyclerView rvList;

    Customer selectedCustomer;
    Context context;

    List<IProduct> iProductList = new ArrayList<>();
    FastItemAdapter<IProduct> fastItemAdapter;
    String operation = AppContainer.KEY_NEW_ORDER;
    IDraftOrder item = new IDraftOrder();
    boolean isOwnOrder = false;

    boolean isEdit = false;

    DealerLimit list;

    @Inject
    Realm r;

    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    @Inject
    User user;

    double creditLimit = 0, balance = 0;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customer_order_create, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        ((AppCompatActivity) context).setTitle("Depot Order");
        btnSetCustomer.setText("Select Depot");
        txtCustomerName.setText("Please Select Depot");
        OrderPreviewFragment.BALANCE_AMOUNT = 999999999.00;
        if(user.getRole().equalsIgnoreCase("depot")){
            ((AppCompatActivity) context).setTitle("Depot Own Order");
            isOwnOrder = true;
            btnSetCustomer.setEnabled(false);
            btnSetCustomer.setText("Select Depot");
            txtCustomerName.setText(user.getName());
            selectedCustomer = new Customer();
            selectedCustomer.setId(user.getId());
            selectedCustomer.setName(user.getName());
            selectedCustomer.setContact_person("");
        }

        if (getArguments() != null) {
            operation = getArguments().getString("operation");
            item = (IDraftOrder) getArguments().getSerializable("data");
            isEdit = true;
            getDealerBalance(item.getUser_id());
            if(isOwnOrder) {
                ((AppCompatActivity) context).setTitle("Edit Own Order");
            } else {
                ((AppCompatActivity) context).setTitle("Edit Depot Order");
                Depot dealer = r.where(Depot.class).equalTo("id", item.getUser_id()).findFirst();

                btnSetCustomer.setText("Change Depot");
                txtCustomerName.setText(dealer.getName());
                selectedCustomer = new Customer();
                selectedCustomer.setId(dealer.getId());
                selectedCustomer.setName(dealer.getName());
                selectedCustomer.setContact_person(dealer.getContact_person());
            }
        }

        setProduct();
        return root;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    @OnClick({R.id.btnSetCustomer, R.id.btnPreview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetCustomer:
                DialogFragment dialogFragment = new SetCustomerDialog(this, "depot");
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_customer");
                break;
            case R.id.btnPreview:
                openPreviewDialog();
                break;
        }
    }

    boolean breakPoint = false;
    int breakPointCount = 0;

    int observeOrderedData(long id) {
        if (breakPoint) return 0;
        if (item != null && item.getItems() != null && item.getItems().size() > 0) {
            int size = item.getItems().size();
            IDraftOrderProduct it;
            for (int i = 0; i < size; i++) {
                it = item.getItems().get(i);
                if (id == it.getProduct_id()) {
                    breakPointCount++;
                    Log.d(TAG, "observeOrderedData: " + breakPointCount + " S " + size + " p " + it.getProduct_id());
                    if (breakPointCount == size) breakPoint = true;
                    return it.getQuantity();
                }
            }
        }
        return 0;
    }

    public void setProduct() {
        List<Product> products = r.where(Product.class).findAll();
        if (products != null && products.size() > 0) {
            for (Product product : products) {
                IProduct iProduct = new IProduct();
                iProduct.setId(product.getId());
                iProduct.setName(product.getName());
                iProduct.setCategory(product.getCategory());
                iProduct.setUnit(product.getUnit());
                iProduct.setQuantity(Math.max(observeOrderedData(product.getId()), 0));
                iProduct.setPrice(product.getDepot_price());
                iProduct.setTradeOffer(getTradeOffer(product));
                iProductList.add(iProduct);
            }
        }
        if (operation.equals(AppContainer.KEY_EDIT_ORDER)) {
            iProductList.sort(new Comparator<IProduct>() {
                @Override
                public int compare(IProduct iProduct, IProduct t1) {
                    return t1.getQuantity() - iProduct.getQuantity();
                }
            });
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iProductList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        sort();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IProduct>() {
            @Override
            public boolean filter(IProduct item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

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
                sort();
            }
        });

        fastItemAdapter.withItemEvent(new ClickEventHook<IProduct>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IProduct.ViewHolder) {
                    return ((IProduct.ViewHolder) viewHolder).txtMarketPrice;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IProduct> fastAdapter1, final IProduct item) {
                openSetPriceDialog(item, position);
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
                    sort();
                }


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



    public String getTradeOffer(Product product) {
        TradeOffer tradeOffer = r.where(TradeOffer.class).equalTo("product_id", product.getId()).findFirst();
        if (tradeOffer != null) {
            String tradeOfferString;
            if(tradeOffer.getBy_percentage() > 0){
                tradeOfferString = String.format("%.2f percent discount offer for this product.", tradeOffer.getBy_percentage());
            } else {
                tradeOfferString = String.format("Get 1 free if you buy %d", tradeOffer.getBy_quantity());
            }
            return tradeOfferString;
        }
        return null;
    }

    public void openSetQuantityDialog(IProduct item, int position) {
        DialogFragment dialogFragment = new AddQuantityDialog(this, item, position);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_quantity");
    }

    public void openPreviewDialog() {
        List<IProduct> iProductListForPreview = new ArrayList<>();
        for (IProduct iProduct : iProductList) {
            if (iProduct.getQuantity() > 0) {
                iProductListForPreview.add(iProduct);
            }
        }
        if (iProductListForPreview.size() > 0 && selectedCustomer != null) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) iProductListForPreview);
            bundle.putSerializable("mainProductList", (Serializable) iProductList);
            bundle.putSerializable("customer", (Serializable) selectedCustomer);
            bundle.putSerializable("order", (Serializable) item);
            bundle.putString("operation", operation);
            bundle.putBoolean("isEdit", isEdit);
            bundle.putSerializable("responseDataList", (Serializable) list);
            Fragment fragment;
            if(isEdit){
                fragment = OrderPreviewFragment.newInstance(item.getDelivery_date(), item.getNote(), this);
            } else {
                fragment = new OrderPreviewFragment(this);
            }
            fragment.setArguments(bundle);
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("order").commit();


        } else {
            ToastUtils.longToast("Please select customer and input at least one product quantity!!");
        }
    }

    @Override
    public void onSetCustomer(Customer customer) {
        if (customer != null) {
            getDealerBalance(customer.getId());
            this.selectedCustomer = customer;
            txtCustomerName.setText(customer.getName() + "/" + customer.getContact_person());
            btnSetCustomer.setText("Change Depot");
        }
    }


    @Override
    public void onNoCustomerSet() {
        //ToastUtils.longToast("No Dealer Selected");
        if (selectedCustomer == null) {
            txtCustomerName.setText("No Depot Selected");
            btnSetCustomer.setText("Set Depot");
        }
    }

    @Override
    public void onSuccessUploadOrder() {
        ((AppCompatActivity) context).onBackPressed();
    }

    @Override
    public void onEdit() {

    }

    @Override
    public void onCancel() {
        ((AppCompatActivity) context).onBackPressed();
    }

    @Override
    public void onSetQuantity(int quantity, int position) {
        iProductList.get(position).setQuantity(quantity);
        fastItemAdapter.notifyAdapterDataSetChanged();
        sort();
    }

    @Override
    public void onSetPrice(double quantity, int position) {
        iProductList.get(position).setPrice(quantity);
        fastItemAdapter.notifyAdapterDataSetChanged();
        sort();
    }

    public void openSetPriceDialog(IProduct item, int position) {
        DialogFragment dialogFragment = new AddPriceDialog(this, item, position);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_price");
    }

    public void sort(){
        List<IProduct> iProductListTemp = fastItemAdapter.getAdapterItems();
        iProductListTemp.sort(new Comparator<IProduct>() {
            @Override
            public int compare(IProduct iProduct, IProduct t1) {
                return t1.getQuantity() - iProduct.getQuantity();
            }
        });
        fastItemAdapter.set(iProductListTemp);
        fastItemAdapter.notifyAdapterDataSetChanged();
        setOrderAmount();
    }

    public double orderAmount(){
        double orderAmount = 0;
        List<IProduct> iProductListTemp = fastItemAdapter.getAdapterItems();
        for(IProduct iProduct:iProductListTemp){
            if(iProduct.getQuantity() > 0){
                orderAmount += (iProduct.getPrice() * iProduct.getQuantity());
            }
        }
        return orderAmount;
    }
    public void setOrderAmount(){
        double orderAmount = orderAmount();
        if(orderAmount > balance + creditLimit){
            txtOrderAmount.setTextColor(getContext().getResources().getColor(com.mikepenz.materialdrawer.R.color.md_red_900));
        }
        txtOrderAmount.setText(String.format("Balance: %.2f, Credit Limit: %.2f Order Amount: %.2f", balance, creditLimit, orderAmount()));
    }

    public void getDealerBalance(long dealerId){
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(getContext(), "Please wait...");
        loadingDialog.show();
        String auth_token = SharedPrefsUtils.getStringPreference(getContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.getDealerBalance(auth_token, dealerId) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseData<Balance>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseData<Balance> value) {
                        if (value.isSuccess()) {
                            creditLimit = value.getData().getCredit_limit();
                            balance = value.getData().getCurrent_balance();
                        }
                    }
                }));
    }
}