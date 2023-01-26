package com.oshnisoft.erp.btl.dependency;


import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.MainMenuActivity;
import com.oshnisoft.erp.btl.MainMenuModel;
import com.oshnisoft.erp.btl.activity.ChangePasswordActivity;
import com.oshnisoft.erp.btl.activity.DashboardActivity;
import com.oshnisoft.erp.btl.activity.HostActivity;
import com.oshnisoft.erp.btl.activity.LoginActivity;
import com.oshnisoft.erp.btl.activity.NotificationActivity;
import com.oshnisoft.erp.btl.dash.MyWorker;
import com.oshnisoft.erp.btl.net.RequestServices;
import com.oshnisoft.erp.btl.ui.asset.AssetFragment;
import com.oshnisoft.erp.btl.ui.asset.CreateAssetFragment;
import com.oshnisoft.erp.btl.ui.asset.DatePickerFragment;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceFragment;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceListFragment;
import com.oshnisoft.erp.btl.ui.bill.BillDatePickerFragment;
import com.oshnisoft.erp.btl.ui.bill.BillFragment;
import com.oshnisoft.erp.btl.ui.bill.CreateBillFragment;
import com.oshnisoft.erp.btl.ui.cashCollection.AddDealerPaymentFragment;
import com.oshnisoft.erp.btl.ui.cashCollection.AddDepotPaymentFragment;
import com.oshnisoft.erp.btl.ui.cashCollection.PaymentFragment;
import com.oshnisoft.erp.btl.ui.cashCollection.PaymentViewPager;
import com.oshnisoft.erp.btl.ui.cashCollection.SetBankDialog;
import com.oshnisoft.erp.btl.ui.cashCollection.SetCustomerDialog;
import com.oshnisoft.erp.btl.ui.customer.CustomerCreateFragment;
import com.oshnisoft.erp.btl.ui.customer.CustomerFragment;
import com.oshnisoft.erp.btl.ui.customer.DealerFragment;
import com.oshnisoft.erp.btl.ui.leave.ApplyLeaveFragment;
import com.oshnisoft.erp.btl.ui.leave.LeaveDatePickerFragment;
import com.oshnisoft.erp.btl.ui.leave.LeaveFragment;
import com.oshnisoft.erp.btl.ui.order.AddItemFragment;
import com.oshnisoft.erp.btl.ui.order.AddPriceDialog;
import com.oshnisoft.erp.btl.ui.order.AddQuantityDialog;
import com.oshnisoft.erp.btl.ui.order.CreateCustomerOrderFragment;
import com.oshnisoft.erp.btl.ui.order.CreateDealerOrderFragment;
import com.oshnisoft.erp.btl.ui.order.CreateDepotOrderFragment;
import com.oshnisoft.erp.btl.ui.order.DatePickerFragmentDate;
import com.oshnisoft.erp.btl.ui.order.DraftOrderFragment;
import com.oshnisoft.erp.btl.ui.order.OrderFragment;
import com.oshnisoft.erp.btl.ui.order.OrderPreviewDialog;
import com.oshnisoft.erp.btl.ui.order.OrderPreviewFragment;
import com.oshnisoft.erp.btl.ui.order.OrderViewPager;

import dagger.Component;


@Component(modules = AppModule.class)

public interface AppComponent {

    void inject(LoginActivity mainActivity);
    void inject(DashboardActivity dashboardActivity);
    void inject(MainMenuActivity mainMenuActivity);
    void inject(MainMenuModel mainMenuModel);
    void inject(HostActivity hostActivity);
    void inject(DatePickerFragment datePickerFragment);

    void inject(AssetFragment assetFragment);
    void inject(CreateAssetFragment createAssetFragment);

    void inject(AttendanceFragment attendanceFragment);
    void inject(AttendanceListFragment attendanceListFragment);
    void inject(ApplyLeaveFragment applyLeaveFragment);
    void inject(LeaveDatePickerFragment leaveDatePickerFragment);

    void inject(BillFragment billFragment);
    void inject(CreateBillFragment createBillFragment);
    void inject(BillDatePickerFragment billDatePickerFragment);

    void inject(CustomerFragment customerFragment);
    void inject(CustomerCreateFragment customerCreateFragment);
    void inject(DealerFragment dealerFragment);

    void inject(AddDealerPaymentFragment addDealerPaymentFragment);
    void inject(AddDepotPaymentFragment addDepotPaymentFragment);
    void inject(PaymentFragment paymentFragment);
    void inject(PaymentViewPager paymentViewPager);
    void inject(SetBankDialog setBankDialog);
    void inject(SetCustomerDialog setCustomerDialog);
    void inject(com.oshnisoft.erp.btl.ui.cashCollection.DatePickerFragment datePickerFragment);

    void inject(LeaveFragment leaveFragment);

    void inject(AddItemFragment addItemFragment);
    void inject(AddPriceDialog addPriceDialog);
    void inject(AddQuantityDialog addQuantityDialog);
    void inject(CreateDealerOrderFragment createDealerOrderFragment);
    void inject(CreateDepotOrderFragment createDepotOrderFragment);
    void inject(CreateCustomerOrderFragment createCustomerOrderFragment);
    void inject(DatePickerFragmentDate datePickerFragmentDate);
    void inject(DraftOrderFragment draftOrderFragment);
    void inject(OrderFragment orderFragment);
    void inject(OrderPreviewDialog orderPreviewDialog);
    void inject(OrderPreviewFragment orderPreviewFragment);
    void inject(OrderViewPager orderViewPager);
    void inject(com.oshnisoft.erp.btl.ui.order.SetCustomerDialog setCustomerDialog);

    void inject(NotificationActivity notificationActivity);
    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(MyWorker myWorker);


    final class Initializer {
        private Initializer() {
        }

        public static AppComponent init(App app, RequestServices requestServices) {
            return DaggerAppComponent.builder().appModule(new AppModule(app, requestServices)).build();
        }
    }

}
