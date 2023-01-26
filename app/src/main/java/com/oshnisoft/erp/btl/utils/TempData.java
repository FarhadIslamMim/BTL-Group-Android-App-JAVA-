package com.oshnisoft.erp.btl.utils;

import android.graphics.Point;


public class TempData {


    public static boolean IS_WH_CALCULATED = false;
    public static int[] MAIN_MENU_BG_COLORS;
    public static int[] REPORT_MENU_BG_COLORS;



    public static Point MAIN_MENU_WH;
    public static Point REPORT_MAIN_MENU_WH;
    public static Point ACTIVITY_WH;
    public static Point CALENDER_WH;
    public static Point DOT_WH;

    public static int menuW;

    public static int getCalendarHeight(){
        if(IS_WH_CALCULATED){
            return ACTIVITY_WH.y * 83/100;
        }
        return 0;
    }

    public static int[] DAY_STATUS_TP = new int[35];




}
