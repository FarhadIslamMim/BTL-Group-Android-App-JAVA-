package com.oshnisoft.erp.btl.net;


import com.oshnisoft.erp.btl.dash.LocationModel;
import com.oshnisoft.erp.btl.model.AttendanceStatus;
import com.oshnisoft.erp.btl.model.Bank;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.Dealer;
import com.oshnisoft.erp.btl.model.DealerQuestion;
import com.oshnisoft.erp.btl.model.Depot;
import com.oshnisoft.erp.btl.model.District;
import com.oshnisoft.erp.btl.model.IAsset;
import com.oshnisoft.erp.btl.model.IAttendance;
import com.oshnisoft.erp.btl.model.IBill;
import com.oshnisoft.erp.btl.model.ICashCollection;
import com.oshnisoft.erp.btl.model.LeavePostModel;
import com.oshnisoft.erp.btl.model.LeaveReportModel;
import com.oshnisoft.erp.btl.model.LoginResponse;
import com.oshnisoft.erp.btl.model.LogoutResponse;
import com.oshnisoft.erp.btl.model.Problem;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.Target;
import com.oshnisoft.erp.btl.model.Thana;
import com.oshnisoft.erp.btl.model.TradeOffer;
import com.oshnisoft.erp.btl.model.Unit;
import com.oshnisoft.erp.btl.model.UserDistrict;
import com.oshnisoft.erp.btl.model.UserThana;
import com.oshnisoft.erp.btl.model.Zone;
import com.oshnisoft.erp.btl.model.FieldForce;
import com.oshnisoft.erp.btl.model.Product;
import com.oshnisoft.erp.btl.model.ResponseData;
import com.oshnisoft.erp.btl.model.Balance;
import com.oshnisoft.erp.btl.model.INotification;
import com.oshnisoft.erp.btl.model.ChangePassword;
import com.oshnisoft.erp.btl.model.IDraftOrder;
import com.oshnisoft.erp.btl.ui.attendance.AttendanceModel;
import com.oshnisoft.erp.btl.ui.attendance.SubmitAttendanceModel;
import com.oshnisoft.erp.btl.ui.bill.PostBill;
import com.oshnisoft.erp.btl.ui.cashCollection.SubmitPayment;
import com.oshnisoft.erp.btl.ui.customer.CustomerUpload;
import com.oshnisoft.erp.btl.ui.leave.LeaveTypeModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface APIServices {

    String TAG = APIServices.class.getSimpleName();

    @FormUrlEncoded
    @POST("api/login")
    Observable<LoginResponse> login(@Field("employee_no") String phone, @Field("pass_code") String password);

    @POST("api/hr/attendance")
    Observable<AttendanceModel> setAttendance(@Header("Authorization") String token, @Body SubmitAttendanceModel submitAttendanceModel);

    @GET("api/hr/today-attendance-status")
    Observable<ResponseData<AttendanceStatus>> getAttendanceStatus(@Header("Authorization") String token);

    @GET("api/hr/attendance")
    Observable<ResponseDataList<IAttendance>> getAttendance(@Header("Authorization") String token, @Query("month") int month, @Query("year") int year);

    @GET("api/achievement-by-user")
    Observable<ResponseData<Target>> getTarget(@Header("Authorization") String token);

    @GET("api/user-credit-balance/{id}")
    Observable<ResponseData<Balance>> getDealerBalance(@Header("Authorization") String token, @Path("id") long id);

    @POST("api/hr/location-track-store")
    Observable<AttendanceModel> postLocation(@Header("Authorization") String token, @Body LocationModel model);

    @POST("api/logout")
    Observable<LogoutResponse> userLogout(@Header("Authorization") String token);

    //leave-types
    @GET("api/hr/leave-types")
    Observable<ResponseDataList<LeaveTypeModel>> getLeaveTypes(@Header("Authorization") String token);

    @GET("api/hr/leave-report")
    Observable<LeaveReportModel> getLeaveData(@Header("Authorization") String token, @Query("year") int year);

    @POST("api/hr/leave-application-store")
    Observable<ResponsePost> postLeaveApplication(@Header("Authorization") String token, @Body LeavePostModel leavePostModel);

    @FormUrlEncoded
    @POST("api/user-fcm")
    Observable<ResponsePost> updateFcmToken(@Header("Authorization") String token, @Field("fcm_token") String fcmToken);

    @GET("api/districts")
    Observable<ResponseDataList<District>> getDistrict(@Header("Authorization") String token);

    @GET("api/upazilas")
    Observable<ResponseDataList<Thana>> getThana(@Header("Authorization") String token);

    @GET("api/district-by-user")
    Observable<ResponseDataList<UserDistrict>> getUserDistricts(@Header("Authorization") String token);

    @GET("api/upazila-by-user")
    Observable<ResponseDataList<UserThana>> getUserThanas(@Header("Authorization") String token);

    @GET("api/zones")
    Observable<ResponseDataList<Zone>> getZone(@Header("Authorization") String token);

    @GET("api/problems")
    Observable<ResponseDataList<Problem>> getProblems(@Header("Authorization") String token);

    @GET("api/question")
    Observable<ResponseDataList<DealerQuestion>> getDealerQuestions(@Header("Authorization") String token);

    @GET("api/unit")
    Observable<ResponseDataList<Unit>> getUnit(@Header("Authorization") String token);

    @GET("api/expense-bill-by-user")
    Observable<ResponseDataList<IBill>> getBill(@Header("Authorization") String token, @Query("month") String month, @Query("year") String year);

    @POST("api/expense-bill-store")
    Observable<ResponsePost> postBill(@Header("Authorization") String token, @Body PostBill postBill);

    @GET("api/products")
    Observable<ResponseDataList<Product>> getProduct(@Header("Authorization") String token);

    @GET("api/trade-offers")
    Observable<ResponseDataList<TradeOffer>> getTradeOffers(@Header("Authorization") String token);

    @GET("api/banks")
    Observable<ResponseDataList<Bank>> getBanks(@Header("Authorization") String token);

    @GET("api/dealers-by-user")
    Observable<ResponseDataList<Dealer>> getDealers(@Header("Authorization") String token);

    @GET("api/depots-by-user")
    Observable<ResponseDataList<Depot>> getDepots(@Header("Authorization") String token);

    @GET("api/field-forces-by-user")
    Observable<ResponseDataList<FieldForce>> getFFs(@Header("Authorization") String token);


    @GET("api/customers-by-user")
    Observable<ResponseDataList<Customer>> getCustomers(@Header("Authorization") String token);

    @GET("api/customers-by-user")
    Observable<ResponseDataList<Customer>> getCustomersByDealer(@Header("Authorization") String token, @Query("user_id") int id);


    @POST("api/customer-store")
    Call<ResponsePost> postCustomer(@Header("Authorization") String token, @Body CustomerUpload customerUpload);

    @POST("api/order-store")
    Call<ResponsePost> postOrder(@Header("Authorization") String token, @Body IDraftOrder orderUpload);


    @POST("api/order-update/{id}")
    Call<ResponsePost> updateOrder(@Header("Authorization") String token, @Path("id") long id, @Body IDraftOrder orderUpload);

    @GET("api/customer-orders")
    Observable<ResponseDataList<IDraftOrder>> getCustomerOrder(@Header("Authorization") String token, @Query("month") int month, @Query("year") int year);

    @GET("api/dealer-orders")
    Observable<ResponseDataList<IDraftOrder>> getDealerOrder(@Header("Authorization") String token, @Query("month") int month, @Query("year") int year);

    @GET("api/depots-orders")
    Observable<ResponseDataList<IDraftOrder>> getDepotOrder(@Header("Authorization") String token, @Query("month") int month,
                                                             @Query("year") int year);

    @POST("api/change-password")
    Observable<ResponsePost> changePassword(@Header("Authorization") String token, @Body ChangePassword changePassword);

    @POST("api/cash-collection/store")
    Observable<ResponsePost> postPayment(@Header("Authorization") String token, @Body SubmitPayment submitPayment);

    @GET("api/cash-collection/dealers")
    Observable<ResponseDataList<ICashCollection>> getDealerPayments(@Header("Authorization") String token);

    @GET("api/cash-collection/depots")
    Observable<ResponseDataList<ICashCollection>> getDepotPayments(@Header("Authorization") String token);

    @POST("api/hr/asset-requisition-store")
    Observable<ResponsePost> postAsset(@Header("Authorization") String token, @Body IAsset postAsset);

    @GET("api/hr/asset-requisitions")
    Observable<ResponseDataList<IAsset>> getAsset(@Header("Authorization") String token);

    @GET("api/notification")
    Observable<ResponseDataList<INotification>> getNotifications(@Header("Authorization") String token);





//    @GET("api/cash-collection")
//    Observable<ResponseDataList<IDraftOrder>> getDepotOrder(@Header("Authorization") String token, @Query("month") int month,
//                                                            @Query("year") int year);

    /*@GET("api/new-contacts-by-user")
    Call<ResponseDataList<NewContactListAdapter>> getNewContactStore(@Header("Authorization") String token);

    @POST("api/new_contacts-store")
    Call<ResponsePost> postNewContactStore(@Header("Authorization") String token, @Body NewContactsModel newContactsModel);

    @POST("api/new_contacts-update/{id}")
    Call<ResponsePost> updateNewContact(@Header("Authorization") String token, @Path("id") long id, @Body NewContactsModel newContactsModel);

    @POST("api/new_contacts_follow_up-store")
    Call<ResponsePost> postNewContactsFollowUpStore(@Header("Authorization") String token, @Body NewContactsModel newContactsModel);


    @POST("api/expense-bill-store")
    Call<ResponsePost> postBill(@Header("Authorization") String token, @Body BillModelAdapter billModel);

    @POST("api/expense-bill-by-user")
    Call<ResponseDataList<BillModelAdapter>> postGetBill(@Header("Authorization") String token, @Body BillModelAdapter billModel);

    @GET("api/expense-bill-by-user")
    Call<ResponseDataList<BillModelAdapter>> getBill(@Header("Authorization") String token, @Query("month") int month, @Query("year") int year);


    @GET("api/new-contact-answers/{id}")
    Call<ResponseDataList<QuestionAnswer>> getNewContactQuestionAnswer(@Header("Authorization") String token, @Path("id") long id);

    @GET("api/notification")
    Observable<ResponseDataList<INotification>> getNotifications(@Header("Authorization") String token);


    @POST("api/change-password")
    Observable<ResponsePost> changePassword(@Header("Authorization") String token, @Body ChangePassword changePassword);*/

}
