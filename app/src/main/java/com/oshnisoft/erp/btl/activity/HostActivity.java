package com.oshnisoft.erp.btl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.ui.asset.AssetFragment;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceListFragment;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceFragment;
import com.oshnisoft.erp.btl.ui.bill.BillFragment;
import com.oshnisoft.erp.btl.ui.cashCollection.PaymentViewPager;
import com.oshnisoft.erp.btl.ui.leave.LeaveFragment;
import com.oshnisoft.erp.btl.ui.customer.CustomerFragment;
import com.oshnisoft.erp.btl.ui.customer.DealerFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.realm.Realm;

public class HostActivity extends AppCompatActivity {

    @Inject
    Realm r;

    String flag;
    Resources res;
    String title;
    Activity activity;

    int month, year;

    int dealer_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        ButterKnife.bind(this);
        App.getComponent().inject(this);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        res = getResources();
        activity = this;
        Intent intent = getIntent();
        getToday();
        flag = intent.getStringExtra("flag");
        if (intent.hasExtra("dealer_id")) {
            dealer_id = intent.getIntExtra("dealer_id", 0);
        }


        Log.e("HostActivity", flag);
        setFragmentAndTitle();

    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        /*if (id == R.id.action_list){
            TPListActivity.start(this, month, year);
        }

        if (id == R.id.action_pwds_list){
            PWDSListFragment fragment = new PWDSListFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("pwds_list").commit();
        }
        if (id == R.id.action_gwds_list){
            GWDSListFragment fragment = new GWDSListFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("gwds_list").commit();
        }


        if (id == R.id.action_dvr_list){
            DVRSummaryFragment fragment = new DVRSummaryFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dvr_list").commit();
        }

        if (id == R.id.action_dvr_doc){
            DCRDoctorListFragment fragment = new DCRDoctorListFragment();
            Bundle b = new Bundle();
            b.putInt("MONTH", month);
            b.putInt("YEAR", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dvr_list").commit();
        }*/

        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            /*if(WPUtils.IS_CHANGED){
                ToastUtils.displayConfirmationPopupForWorkPlan(activity);
            } else {
                getSupportFragmentManager().popBackStack();
            }*/
        } else {
            finish();
        }
    }



    public void getToday() {
        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
    }


    public void setFragmentAndTitle() {

        Fragment fragment = new AssetFragment();
        Bundle b = new Bundle();
        getToday();
        switch (flag) {
            case "customer":
                title = "Customer List";
                fragment = new CustomerFragment();
                b.putInt("id", dealer_id);
                fragment.setArguments(b);
                break;
            case "dealer_list":
                title = "Dealer List";
                fragment = new DealerFragment();
                break;
//            case "order":
//                title = "Order";
//                fragment = new OrderFragment();
//                //((PWDSProductsFragment) fragment).monthChangedListener = this;
//                break;
            case "leave":
                title = "Leave";
                fragment = new LeaveFragment();
                //((GWDSGiftFragment) fragment).monthChangedListener = this;
                break;
            case "Attendance":
                title = "Attendance";
                fragment = new AttendanceFragment();
                //fragment = new WPCalendarFragment();
                //((WPCalendarFragment) fragment).monthChangedListener = this;
                break;
            case "Attendance_List":
                title = "Attendance List";
                fragment = new AttendanceListFragment();
                //fragment = new WPCalendarFragment();
                //((WPCalendarFragment) fragment).monthChangedListener = this;
                break;
            case "asset":
                title = "Asset";
                fragment = new AssetFragment();
                break;
            case "bill":
                title = "Bill";
                fragment = new BillFragment();
                break;
//            case "order":
//                title = "Order";
//                fragment = new OrderViewPager();
//                break;
            case "payment":
                title = "Online";
                fragment = new PaymentViewPager();
                break;
            default:
                title = "D S S";
                //fragment = new DSSCalendarFragment();
                break;
        }

        setTitle(title);
        b.putInt("month", month);
        b.putInt("year", year);
        //b.putSerializable(StringConstants.DATE_MODEL, DCRUtils.getToday());
        fragment.setArguments(b);
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("host").commit();

    }
}