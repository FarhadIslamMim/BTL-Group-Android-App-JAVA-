package com.oshnisoft.erp.btl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.oshnisoft.erp.btl.activity.DashboardActivity;
import androidx.core.app.NotificationCompat;

public class NotificationMosfet {
    private static final String KEY_CHANNEL_ID = "MY_CHANNEL";
    public static final int ID = 100;
    Context mContext;

    public NotificationMosfet(Context mContext) {
        this.mContext = mContext;
    }

    public Notification getMyActivityNotification(String content, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(
                    new NotificationChannel(KEY_CHANNEL_ID, "Timer Notification", NotificationManager.IMPORTANCE_HIGH));

        }
        PendingIntent contentIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(mContext, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        }else
        {
            contentIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(mContext, DashboardActivity.class), 0);
        }

        return new NotificationCompat.Builder(mContext, KEY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setOnlyAlertOnce(true) // so when data is updated don't make sound and alert in android 8.0+
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .build();
    }

    public void updateNotification(String content, String title) {

        Notification notification = getMyActivityNotification(content, title);
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID, notification);

    }
}
