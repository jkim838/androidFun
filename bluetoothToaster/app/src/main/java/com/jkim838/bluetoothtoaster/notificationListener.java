package com.jkim838.bluetoothtoaster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.Nullable;

public class notificationListener extends NotificationListenerService // allows overriding method as a file?
{
    static NoticeListener thisListener;
    private Context context;
    @Nullable
    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i("NOTIF", "Notification posted");
        Log.i("NOTIF_ID", "ID: " + sbn.getId());
        Log.i("NOTIF_PACKAGE", "PACKAGE: " + sbn.getPackageName());
        Log.i("NOTIF_CONTENT", "CONTENT: " + sbn.getNotification());
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i("NOTIF", "Notification removed");
    }
    // MUST WAIT FOR THIS EVENT TO OCCUR FIRST
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }
    public void setListener(NoticeListener thisListener)
    {
        notificationListener.thisListener = thisListener;
    }
}
