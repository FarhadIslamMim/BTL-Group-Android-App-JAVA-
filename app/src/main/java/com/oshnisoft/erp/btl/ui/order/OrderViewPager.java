package com.oshnisoft.erp.btl.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class OrderViewPager extends Fragment {

    public static final String TAG = "OrderViewPager";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject
    Realm r;
    @Inject
    User user;

    @BindView(R.id.pager)
    ViewPager2 vpOder;
    @BindView(R.id.tab_layout)
    TabLayout tabs;



    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    public OrderViewPager() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(this);
        vpOder.setAdapter(mSectionsPagerAdapter);
        new TabLayoutMediator(tabs, vpOder, (tab, position) -> {
            if(position == 0){
                tab.setText("Customer Order");
            } else if(position == 1) {
                tab.setText("Dealer Order");
            } else if(position == 2) {
                tab.setText("Depot Order");
            }
        }).attach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_order_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }




    private class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return a NEW fragment instance in createFragment(int)
            Fragment fragment = new OrderFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            if(user.getRole().equalsIgnoreCase("Dealer")){
                if (position == 0) {
                    args.putString("Order_Type", "dealer-to-customer");
                } else if (position == 1) {
                    args.putString("Order_Type", "dealer-to-dealer");
                }

            } else if(user.getRole().equalsIgnoreCase("Depot")){
                if (position == 0) {
                    args.putString("Order_Type", "depot-to-customer");
                } else if (position == 1) {
                    args.putString("Order_Type", "depot-to-depot");
                }

            } else {
                if (position == 0) {
                    args.putString("Order_Type", "ff-to-customer");
                } else if (position == 1) {
                    args.putString("Order_Type", "ff-to-dealer");
                } else if (position == 2) {
                    args.putString("Order_Type", "ff-to-depot");
                }
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            if(user.getRole().equalsIgnoreCase("Dealer") || user.getRole().equalsIgnoreCase("Depot")) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}
