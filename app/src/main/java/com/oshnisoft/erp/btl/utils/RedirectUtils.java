package com.oshnisoft.erp.btl.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Tariqul.Islam on 5/25/17.
 */


public class RedirectUtils {

    public static void go(Context c, Class to, boolean isClearTop, String flag){
        Intent i = new Intent(c, to);
        i.putExtra("flag", flag);
        if(isClearTop) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        c.startActivity(i);
    }

    public static void goWithDealerId(Context c, Class to, boolean isClearTop, String flag, int dealerId){
        Intent i = new Intent(c, to);
        i.putExtra("flag", flag);
        i.putExtra("dealer_id", dealerId);
        if(isClearTop) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        c.startActivity(i);
    }


    public static void goNew(Context c, Class to){
        Intent i = new Intent(c, to);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    public static void goAndBack(Context c, Class to){
        Intent i = new Intent(c, to);
        i.putExtra(StringConstants.PARENT, c.getClass());
        c.startActivity(i);
    }

}
