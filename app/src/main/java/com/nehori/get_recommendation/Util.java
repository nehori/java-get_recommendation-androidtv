package com.nehori.get_recommendation;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

public class Util {

    private static final String TAG = "Util";

    private Util() {
    }

    public static void writeNotificationListenerSetting(Context context) {
	Log.d(TAG, "writeNotificationListenerSetting");
        String s = android.provider.Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        String s1 = (new ComponentName(context, ".NotificationMonitor")).flattenToShortString();
        String s2;
        if (s == null || s.length() == 0) {
            s2 = s1;
        } else {
            s2 = (new StringBuilder()).append(s).append(":").append(s1).toString();
        }
	Log.i(TAG, Here.at() + ":" + s2);
        android.provider.Settings.Secure.putString(context.getContentResolver(), "enabled_notification_listeners", s2);
    }

    public static boolean isEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isRecommendation(StatusBarNotification statusbarnotification) {
        return TextUtils.equals(statusbarnotification.getNotification().category, "recommendation");
    }

    public boolean shouldBeIgnored(StatusBarNotification statusbarnotification) {
        Notification notification = statusbarnotification.getNotification();
        if (!isRecommendation(statusbarnotification) || notification.contentIntent == null) {
            return true;
	}
        return false;
    }

    public static boolean isInPartnerRow(StatusBarNotification statusbarnotification) {
            return "partner_row_entry".equals(statusbarnotification.getNotification().getGroup());
    }
}
