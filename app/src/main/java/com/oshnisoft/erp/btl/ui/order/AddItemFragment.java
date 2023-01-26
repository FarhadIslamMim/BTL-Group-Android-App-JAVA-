package com.oshnisoft.erp.btl.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.oshnisoft.erp.btl.listener.SetPriceListener;
import com.oshnisoft.erp.btl.listener.SetQuantityListener;
import com.oshnisoft.erp.btl.model.IProduct;
import com.oshnisoft.erp.btl.utils.AppContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddItemFragment extends Fragment implements SetQuantityListener, SetPriceListener {
    private static final String TAG = "CreateOrderFragment";

    @BindView(R.id.rvList)
    RecyclerView rvList;

    @BindView(R.id.addedNow)
    Button addedNow;

    Context context;

    List<IProduct> iProductList = new ArrayList<>();
    FastItemAdapter<IProduct> fastItemAdapter;
    String operation = AppContainer.KEY_NEW_ORDER;
    List<IProduct> item = new ArrayList<>();
    List<IProduct> mainList = new ArrayList<>();
    String dealerOrderType = "customer";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_item, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        if (getArguments() != null) {
            item = (List<IProduct>) getArguments().getSerializable("data");
            iProductList = (List<IProduct>) getArguments().getSerializable("mainList");
            setProduct();
        }
        for (int i = 0; i < item.size(); i++) {
            item.get(i).setDelete(false);
        }
        ((AppCompatActivity) context).setTitle("Add Item");
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

    @OnClick({R.id.addedNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addedNow:
                for (IProduct iProduct : iProductList) {
                    if (iProduct.getQuantity() > 0) {
                        item.add(iProduct);
                    }
                }
                ((AppCompatActivity) context).onBackPressed();
                break;
        }
    }

    boolean breakPoint = false;
    int breakPointCount = 0;

    boolean observeOrderedData(long id) {
        if (breakPoint) return false;
        if (item != null && item.size() > 0) {
            int size = item.size();
            IProduct it;
            for (int i = 0; i < size; i++) {
                it = item.get(i);
                if (id == it.getId()) {
                    breakPointCount++;
                    Log.d(TAG, "observeOrderedData: " + breakPointCount + " S " + size + " p " + it.getId());
                    if (breakPointCount == size) breakPoint = true;
                    return true;
                }
            }
        }
        return false;
    }

    public void setProduct() {
        if (iProductList != null && iProductList.size() > 0) {
            iProductList.removeIf(product -> observeOrderedData(product.getId()));
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iProductList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);

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
                if (dealerOrderType.equalsIgnoreCase("customer"))
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
                }


            }
        });
    }



    public void openSetQuantityDialog(IProduct item, int position) {
        DialogFragment dialogFragment = new AddQuantityDialog(this, item, position);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_quantity");
    }

    @Override
    public void onSetQuantity(int quantity, int position) {
        iProductList.get(position).setQuantity(quantity);
        fastItemAdapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void onSetPrice(double quantity, int position) {
        iProductList.get(position).setPrice(quantity);
        fastItemAdapter.notifyAdapterDataSetChanged();
    }

    public void openSetPriceDialog(IProduct item, int position) {
        DialogFragment dialogFragment = new AddPriceDialog(this, item, position);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "set_price");
    }
}