package com.nehori.get_recommendation;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
public class NotificationMonitor extends NotificationListenerService {

    private static final String TAG = "NotificationMonitor";
    private static final int EVENT_UPDATE_CURRENT_NOS = 0;
    public static List<StatusBarNotification[]> mCurrentNotifications = new ArrayList<>();
    public static int mCurrentNotificationsCounts = 0;

    private Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_UPDATE_CURRENT_NOS:
                    updateCurrentNotifications();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, Here.at());
        mMonitorHandler.sendMessage(mMonitorHandler.obtainMessage(EVENT_UPDATE_CURRENT_NOS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, Here.at());
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        updateCurrentNotifications();
        Log.d(TAG, Here.at());
        Log.i(TAG, "have " + mCurrentNotificationsCounts + " active notifications");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        updateCurrentNotifications();
        Log.d(TAG, Here.at());
        Log.i(TAG, "have " + mCurrentNotificationsCounts + " active notifications");
    }

    private void updateCurrentNotifications() {
        try {
            Log.d(TAG, Here.at());
            StatusBarNotification[] activeNos = getActiveNotifications();
            if (mCurrentNotifications.size() == 0) {
                mCurrentNotifications.add(null);
            }
            mCurrentNotifications.set(0, activeNos);
            mCurrentNotificationsCounts = activeNos.length;
        } catch (Exception e) {
            Log.e(TAG, "Should not be here!!");
            e.printStackTrace();
        }
    }

    public static StatusBarNotification[] getCurrentNotifications() {
        if (mCurrentNotifications.size() == 0) {
            Log.i(TAG, "mCurrentNotifications size is 0");
            return null;
        }
        return mCurrentNotifications.get(0);
    }
}