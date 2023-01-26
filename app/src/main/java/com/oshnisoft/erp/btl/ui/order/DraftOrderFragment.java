package com.oshnisoft.erp.btl.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.model.IDraftOrderProduct;
import com.oshnisoft.erp.btl.utils.AppContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class DraftOrderFragment extends Fragment {

    Calendar calendar = Calendar.getInstance();
    int currentMonth = calendar.get(Calendar.MONTH);
    FastItemAdapter<IDraftOrder> fastItemAdapter;

    @BindView(R.id.etFilter)
    EditText etFilter;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    Context context;
    @Inject
    Realm r;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_draft_order, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        ((AppCompatActivity) context).setTitle("Customer Darft Order");
        getDraftOrder();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public void getDraftOrder() {
        List<IDraftOrder> iDraftOrderList = new ArrayList<>();
        List<IDraftOrder> draftOrderList = r.where(IDraftOrder.class).findAll();
        if (draftOrderList != null && draftOrderList.size() > 0) {
            for (IDraftOrder iDraftOrder : draftOrderList) {
                iDraftOrder.setItems(r.where(IDraftOrderProduct.class).equalTo("localOrderId", iDraftOrder.getLocalId()).findAll());
                iDraftOrderList.add(iDraftOrder);
            }
        }

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iDraftOrderList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(fastItemAdapter);

        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDraftOrder>() {
            @Override
            public boolean filter(IDraftOrder item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
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


        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDraftOrder>() {
            @Override
            public boolean onClick(View v, IAdapter<IDraftOrder> adapter, IDraftOrder item, int position) {

                    Bundle bundle = new Bundle();
                    bundle.putString("operation", AppContainer.KEY_DRAFT_ORDER);
                    bundle.putSerializable("data", item);
                    Fragment fragment = new CreateDealerOrderFragment();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("create_order").commit();
                return false;
            }
        });


    }

}