package com.oshnisoft.erp.btl.net;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;


import com.oshnisoft.erp.btl.listener.SyncListener;
import com.oshnisoft.erp.btl.model.Customer;
import com.oshnisoft.erp.btl.model.District;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.model.ResponsePost;
import com.oshnisoft.erp.btl.model.Thana;
import com.oshnisoft.erp.btl.model.Unit;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.model.UserData;
import com.oshnisoft.erp.btl.model.UserDistrict;
import com.oshnisoft.erp.btl.model.UserThana;
import com.oshnisoft.erp.btl.model.Bank;
import com.oshnisoft.erp.btl.model.Product;
import com.oshnisoft.erp.btl.model.Dealer;
import com.oshnisoft.erp.btl.model.Depot;
import com.oshnisoft.erp.btl.model.TradeOffer;
import com.oshnisoft.erp.btl.model.FieldForce;
import com.oshnisoft.erp.btl.ui.leave.LeaveTypeModel;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;


public class RequestServices {

    String TAG = RequestServices.class.getSimpleName();
    CompositeDisposable mCompositeDisposable;

    public LoadingDialog loadingDialog;
    StringBuilder errorMsg, statusMsg;
    Context context;
    Realm r;
    APIServices apiServices;
    String token;
    UserData userData;
    User user;

    SyncListener syncListener;

    public void setErrorMsg(String msg){
        if(TextUtils.isEmpty(errorMsg)){
            errorMsg = new StringBuilder();
            errorMsg.append(msg);
        } else {
            errorMsg.append(", "+msg);
        }
    }

    public void setStatusMsg(String msg){
        if(TextUtils.isEmpty(statusMsg)){
            statusMsg = new StringBuilder();
            statusMsg.append(msg);
        } else {
            statusMsg.append(", "+msg);
        }
    }

    public void syncAll(Context context, APIServices apiServices, UserData userData, Realm r, User user, SyncListener syncListener){
        this.apiServices = apiServices;
        this.r = r;
        errorMsg = new StringBuilder();
        statusMsg = new StringBuilder();
        this.context = context;
        mCompositeDisposable = new CompositeDisposable();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        token = SharedPrefsUtils.getStringPreference(context, StringConstants.PREF_AUTH_TOKEN);
        this.userData = userData;
        this.user = user;
        this.syncListener = syncListener;
        postFCMToken();
    }

   // 1 Post FCM Token
    public void postFCMToken(){
        String fcmToken = SharedPrefsUtils.getStringPreference(context, SharedPrefsUtils.FCM_TOKEN);
        mCompositeDisposable.add(apiServices.updateFcmToken(token, fcmToken) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponsePost>() {
                    @Override
                    public void onComplete() {
                        getLeaveType();//2
                    }
                    @Override
                    public void onError(Throwable e) {

                        getLeaveType();//2
                    }
                    @Override
                    public void onNext(ResponsePost value) {
                        if (value.isSuccess()) {
                            Log.d(TAG, "FCM update onNext: "+value.getMessage());
                        }
                    }
                }));
    }

    //2 Get Leave Type
    public void getLeaveType(){
        mCompositeDisposable.add(apiServices.getLeaveTypes(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<LeaveTypeModel>>() {
                    @Override
                    public void onComplete() {
                        getDistricts();

                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Leave Types");
                        getDistricts();

                    }
                    @Override
                    public void onNext(ResponseDataList<LeaveTypeModel> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(LeaveTypeModel.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Leave Types");
                        }
                    }
                }));
    }

    //3 Get District
    public void getDistricts(){
        mCompositeDisposable.add(apiServices.getDistrict(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<District>>() {
                    @Override
                    public void onComplete() {
                        getThanas();//2
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("District");
                        getThanas();
                    }
                    @Override
                    public void onNext(ResponseDataList<District> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(District.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("District");
                        }
                    }
                }));
    }
    //4 Get Thana
    public void getThanas(){
        mCompositeDisposable.add(apiServices.getThana(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Thana>>() {
                    @Override
                    public void onComplete() {
                        getUserDistricts();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Thana");
                        getUserDistricts();
                    }
                    @Override
                    public void onNext(ResponseDataList<Thana> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Thana.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Thana");
                        }
                    }
                }));
    }
    //5 Get User Districts
    public void getUserDistricts(){
        mCompositeDisposable.add(apiServices.getUserDistricts(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<UserDistrict>>() {
                    @Override
                    public void onComplete() {
                        getUserThanas();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("User Districts");
                        getUserThanas();
                    }
                    @Override
                    public void onNext(ResponseDataList<UserDistrict> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UserDistrict.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("User Districts");
                        }
                    }
                }));
    }
    //6 Get User Thana
    public void getUserThanas(){
        mCompositeDisposable.add(apiServices.getUserThanas(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<UserThana>>() {
                    @Override
                    public void onComplete() {
                        getUnits();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("User Thana");
                        getUnits();
                    }
                    @Override
                    public void onNext(ResponseDataList<UserThana> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UserThana.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("User Thana");
                        }
                    }
                }));
    }


    //8 Get units
    public void getUnits(){
        mCompositeDisposable.add(apiServices.getUnit(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Unit>>() {
                    @Override
                    public void onComplete() {
                        getBanks();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Units");
                        getBanks();
                    }
                    @Override
                    public void onNext(ResponseDataList<Unit> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Unit.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Units");
                        }
                    }
                }));
    }

    //9 Get banks
    public void getBanks(){
        mCompositeDisposable.add(apiServices.getBanks(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Bank>>() {
                    @Override
                    public void onComplete() {
                        getProduct();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Banks");
                        getProduct();
                    }
                    @Override
                    public void onNext(ResponseDataList<Bank> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Bank.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Banks");
                        }
                    }
                }));
    }
    // 10 Get Product
    public void getProduct(){
        mCompositeDisposable.add(apiServices.getProduct(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Product>>() {

                    @Override
                    public void onComplete() {
                        if(user.getRole().equalsIgnoreCase("Dealer")){
                            getCustomer();
                        } else if(user.getRole().equalsIgnoreCase("Customer")){
                            //getProduct();
                        } else {
                            getDealers();
                        }


                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Product");
                        if(user.getRole().equalsIgnoreCase("Dealer")){
                            getCustomer();
                        } else if(user.getRole().equalsIgnoreCase("Customer")){
                            //getProduct();
                        } else {
                            getDealers();
                        }
                    }
                    @Override
                    public void onNext(ResponseDataList<Product> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Product.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Product");
                        }
                    }
                }));
    }
    // 11 Dealers
    public void getDealers(){
        mCompositeDisposable.add(apiServices.getDealers(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Dealer>>() {

                    @Override
                    public void onComplete() {
                        getDepots();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Dealer");
                        getDepots();
                    }
                    @Override
                    public void onNext(ResponseDataList<Dealer> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Dealer.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Dealer");
                        }
                    }
                }));
    }

    // 12 Depots
    public void getDepots(){
        mCompositeDisposable.add(apiServices.getDepots(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Depot>>() {

                    @Override
                    public void onComplete() {
                        getTradeOffer();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Depot");
                        getTradeOffer();
                    }
                    @Override
                    public void onNext(ResponseDataList<Depot> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Depot.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Depot");
                        }
                    }
                }));
    }

    // 12 Depots
    public void getTradeOffer(){
        mCompositeDisposable.add(apiServices.getTradeOffers(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<TradeOffer>>() {

                    @Override
                    public void onComplete() {
                        getFieldForces();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("TradeOffer");
                        getFieldForces();
                    }
                    @Override
                    public void onNext(ResponseDataList<TradeOffer> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(TradeOffer.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("TradeOffer");
                        }
                    }
                }));
    }

    // 13 FFs
    public void getFieldForces(){
        mCompositeDisposable.add(apiServices.getFFs(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<FieldForce>>() {

                    @Override
                    public void onComplete() {
                        getCustomer();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("FieldForce");
                        getCustomer();
                    }
                    @Override
                    public void onNext(ResponseDataList<FieldForce> value) {
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(FieldForce.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("FieldForce");
                        }
                    }
                }));
    }
    // 13 Customers
    public void getCustomer(){
        mCompositeDisposable.add(apiServices.getCustomers(token)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<Customer>>() {
                    @Override
                    public void onComplete() {
                        displayAlert();

                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("Customer");
                        displayAlert();
                    }
                    @Override
                    public void onNext(ResponseDataList<Customer> value) {
                        if (value.isSuccess() && value.getDataList() != null) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Customer.class);
                                    realm.insertOrUpdate(value.getDataList());
                                }
                            });
                        } else {
                            setStatusMsg("Customer");
                        }
                    }
                }));
    }




    private void displayAlert() {
        loadingDialog.dismiss();
        String title = "";
        String msg = "";
        if (TextUtils.isEmpty(errorMsg)) {

            title = "Success";
            if (TextUtils.isEmpty(statusMsg)) {
                msg = "Sync successfully done";
            } else {
                msg = statusMsg.toString() + " not found.";
            }
        } else {
            title = "Failed";
            if (TextUtils.isEmpty(statusMsg)) {
                msg = "Sync failed - " + errorMsg.toString();
            } else {
                msg = "Sync failed - " + errorMsg.toString() + ", Not Found - " + statusMsg.toString();
            }
        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                if(TextUtils.isEmpty(errorMsg)){
                    syncListener.onSyncComplete();
                } else {
                    syncListener.onSyncError();
                }
            }
        });
        alert.show();
    }



}
