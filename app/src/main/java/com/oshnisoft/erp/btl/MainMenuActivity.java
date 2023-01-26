package com.oshnisoft.erp.btl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.activity.HostActivity;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.utils.MainMenuConstants;
import com.oshnisoft.erp.btl.utils.RedirectUtils;
import com.oshnisoft.erp.btl.utils.TempData;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity {


    LinearLayout root;


    //dependency injections
    @Inject
    App context;

    @Inject
    User user;
    @Inject
    List<MainMenuModel> mainMenuModels;

    //view injections
    @BindView(R.id.mainMenu)
    RecyclerView mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        App.getComponent().inject(this);

        root                                            = (LinearLayout) findViewById(R.id.root);
        mainMenu =(RecyclerView) findViewById(R.id.mainMenu);
        root.post(new Runnable() {
            public void run() {
                MainMenuConstants.getInstance().setActivityWH(MainMenuActivity.this);   //get activity width height
                setupMenuGrids();
            }
        });

    }
    private void setupMenuGrids() {
        //init colors

        String[] colors                                 = getResources().getStringArray(R.array.menuColors);
        TempData.MAIN_MENU_BG_COLORS                    = new int[colors.length];
        for(int i = 0; i < colors.length; i++){
            TempData.MAIN_MENU_BG_COLORS[i]             = Color.parseColor(colors[i]);
        }

        //init fast adapter
        final FastItemAdapter<MainMenuModel> fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(mainMenuModels);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<MainMenuModel>() {
            @Override
            public boolean onClick(View v, IAdapter<MainMenuModel> adapter, MainMenuModel item, int position) {
                //Toast.makeText(MainMenuActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
                if(position == 0){
                    ToastUtils.shortToast("Salary");
                    //Intent intent=new Intent(getApplicationContext(), CustomerCreate.class);
                    //startActivity(intent);
                    //RedirectUtils.go(MainMenuActivity.this, HostActivity.class, false, "user"); // location
                } else if(position == 1) {
                    //RedirectUtils.go(MainMenuActivity.this, HostActivity.class, false, "supplier"); // Tour Plan
                    ToastUtils.shortToast("Attendance");
                    RedirectUtils.go(context, HostActivity.class, false, "Attendance_List"); // attendance list
                } else if(position == 2){
                    //RedirectUtils.go(context, DVRActivity.class, false, "dvr"); // D V R
                    ToastUtils.shortToast("Leave");
                    RedirectUtils.go(context, HostActivity.class, false, "leave"); // location
                    //Intent intent=new Intent(getApplicationContext(),CustomerOrder.class);
                    //startActivity(intent);
                } else if(position == 3){
                    RedirectUtils.go(context, HostActivity.class, false, "asset"); // P W D S
                    ToastUtils.shortToast("Asset Management");
                } else if(position == 4){
                    RedirectUtils.go(context, HostActivity.class, false, "bill"); // G W D S
                    ToastUtils.shortToast("Bill");
                } else if(position == 5){
                    if (user.getRole().equalsIgnoreCase("customer")) {
                        ToastUtils.longToast("You are not permitted");
                    } else if (user.getRole().equalsIgnoreCase("dealer")) {
                        RedirectUtils.goWithDealerId(context, HostActivity.class, false, "customer", (int)user.getId());
                    } else {
                        RedirectUtils.go(context, HostActivity.class, false, "dealer_list");
                    }
                } else if(position == 6){
                    if(user.getRole().equalsIgnoreCase("Field Force") || user.getRole().equalsIgnoreCase("Dealer") || user.getRole().equalsIgnoreCase("Depot")) {
                       // RedirectUtils.go(context, HostActivity.class, false, "order");
                    } else {
                        ToastUtils.longToast("Unauthorize Access!!");
                    }
                }
                else if(position == 7){
                    RedirectUtils.go(context, HostActivity.class, false, "payment");
                }
                return true;
            }
        });

        //set layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        mainMenu.setLayoutManager(layoutManager);
        mainMenu.setAdapter(fastItemAdapter);
    }
}