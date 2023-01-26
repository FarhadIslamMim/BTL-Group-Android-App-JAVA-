package com.oshnisoft.erp.btl.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by altaf.sil on 3/13/18.
 */

public class ConnectionUtils {
    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } catch (Exception e){
           e.printStackTrace();
        }
        return false;
    }

}
