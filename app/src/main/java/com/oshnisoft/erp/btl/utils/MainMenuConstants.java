package com.oshnisoft.erp.btl.utils;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

import com.oshnisoft.erp.btl.R;


/**
 * Created by Tariqul.Islam on 5/18/17.
 */

public class MainMenuConstants {


    private final String[] menuTexts = new String[]{
            "Salary",
            "Attendance",
            "Leave",
            "Asset",
            "Bill",
            "Customer",
            "Order",
            "Online"

    };

    private final String[] reportMenuTexts = new String[]{
            "Home",
            "Sample Statement",
            "Doctor Coverage",
            "Doctor DCR Report",
            "No DCR Yet",
            "DCR Summary",
            "Accompany Info",
            "Physical Stock Check"
    };

    private final int[] menuIcons = new int[]{
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
            R.drawable.btl,
    };
    private final int[] reportMenuIcons = new int[]{
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu,
            R.drawable.ic_menu
    };
    private Point wh = null;


    private static MainMenuConstants mainMenuConstants = null;
    private MainMenuConstants(){}

    public static MainMenuConstants getInstance(){
        if(mainMenuConstants == null){
            mainMenuConstants = new MainMenuConstants();
        }
        return mainMenuConstants;
    }

    public int[] getMenuIcons(){
        return menuIcons;
    }

    public String[] getMenuTexts(){
        return menuTexts;
    }


    public int[] getReportMenuIcons(){
        return reportMenuIcons;
    }

    public String[] getReportMenuTexts(){
        return reportMenuTexts;
    }

//    public Point getActivityWH(){
//        return wh;
//    }

    public void setActivityWH(Activity activity){
        if(!TempData.IS_WH_CALCULATED){
            Rect rect = new Rect();
            Window win = activity.getWindow();  // Get the Window
            win.getDecorView().getWindowVisibleDisplayFrame(rect);
            // Get the height of Status Bar
            int statusBarHeight = rect.top;
            // Get the height occupied by the decoration contents
            int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            // Calculate titleBarHeight by deducting statusBarHeight from contentViewTop
            int titleBarHeight = contentViewTop - statusBarHeight;
            // By now we got the height of titleBar & statusBar
            // Now lets get the screen size
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            int screenWidth = metrics.widthPixels;

//                // Now calculate the height that our layout can be set
//                // If you know that your application doesn't have statusBar added, then don't add here also. Same applies to application bar also
            int layoutHeight = screenHeight - (titleBarHeight + statusBarHeight);
            int calenderHeight = layoutHeight - (int) Math.ceil(2f * (titleBarHeight));
            int menuHeight = layoutHeight - (int) Math.ceil(3f * titleBarHeight);

            TempData.ACTIVITY_WH  = new Point(screenWidth, layoutHeight);
            TempData.CALENDER_WH = new Point((int) Math.ceil(screenWidth / 7f), layoutHeight);
            TempData.DOT_WH = new Point((int) Math.ceil(screenWidth / 31f), layoutHeight);
            TempData.MAIN_MENU_WH = new Point((int) Math.ceil(screenWidth / 3f), (int) Math.ceil(menuHeight / 3f));
            TempData.REPORT_MAIN_MENU_WH = new Point((int) Math.ceil(screenWidth / 3f), (int) Math.ceil(menuHeight / 3f));
            TempData.IS_WH_CALCULATED = true;
        }
    }

}
