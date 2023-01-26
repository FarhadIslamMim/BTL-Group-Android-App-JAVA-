package com.oshnisoft.erp.btl.net;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by monir.sobuj on 5/25/17.
 */

public class APIClients {


    //web server
    //http://jsonblob.com/977510277885214720
    public final static String BaseURL = "http://103.181.42.235:8001/sspl/public/"; // main for arabica
    //public final static String BaseURL = "http://demo-sso.oshnisoft.com/"; // main for demo sso
    //public final static String BaseURL = "http://192.168.0.2:8001/sso/public/"; // main for sristy enterprise
    private final static String JSONBlobURL = "http://jsonblob.com/"; // main for sristy enterprise
    //private final static String BaseURL = "http://smartsell.oshnisoft.com/"; // demo live
    private final static String fcmURL = "https://fcm.googleapis.com/"; // fcm

    private static Retrofit retrofit = null, fcmRetrofit = null, jsonBlobRetrofit = null;

    public static Retrofit getInstance(){
        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL).client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getFCMInstance() {
        if (fcmRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            fcmRetrofit = new Retrofit.Builder()
                    .baseUrl(fcmURL).client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return fcmRetrofit;
    }

    public static Retrofit getJSONBlobInstance() {
        if (jsonBlobRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            jsonBlobRetrofit = new Retrofit.Builder()
                    .baseUrl(JSONBlobURL).client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return jsonBlobRetrofit;
    }

    private APIClients(){}
}
