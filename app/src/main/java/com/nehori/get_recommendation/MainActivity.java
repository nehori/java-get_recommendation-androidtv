package com.nehori.get_recommendation;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.pm.PackageManager;

import android.graphics.drawable.BitmapDrawable;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int EVENT_LIST_CURRENT_NOS = 1;
    private boolean isEnabled = false;

    private static final float INITIAL_ITEMS_COUNT = 3.5F;
    private LinearLayout mCarouselContainer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_LIST_CURRENT_NOS:
                    listCurrentNotification();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.writeNotificationListenerSetting(this);
        // Get reference to carousel container
        mCarouselContainer = (LinearLayout) findViewById(R.id.carousel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnabled = Util.isEnabled(this);
        Log.i(TAG, Here.at() + "isEnabled = " + isEnabled);
        listCurrentNotification();
    }

    private CharSequence setNotificationText(StatusBarNotification sbn, int i) {
        Notification notification = sbn.getNotification();
        CharSequence title    = (CharSequence)notification.extras.get("android.title");
        CharSequence text     = (CharSequence)notification.extras.get("android.text");
        CharSequence infoText = (CharSequence)notification.extras.get("android.infoText");
        if(TextUtils.isEmpty(infoText)) {
            CharSequence charsequence = "";
            try {
                charsequence = getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(sbn.getPackageName(), 0));
            }
            catch (PackageManager.NameNotFoundException e) {
                  Log.e(TAG, Here.at() + "PackageManager.NameNotFoundException");
                  e.printStackTrace();
            }
            infoText = charsequence;
        }
        boolean partner = Util.isInPartnerRow(sbn);
        boolean recommend = Util.isRecommendation(sbn);
        Log.d(TAG, " (" + i + ") =================");
        Log.i(TAG, "      title:" + title);
        Log.i(TAG, "      text:" + text);
        Log.i(TAG, "      infoText:" + infoText);
        Log.d(TAG, "      category:" + notification.category);
        Log.d(TAG, "      partner row:" + partner);
        Log.d(TAG, "      recommend area:" + recommend);
        Log.d(TAG, "      name:" + sbn.getPackageName());
        Log.d(TAG, "      time:" + sbn.getPostTime());
        if (partner) {
              title = title + " (partner row)";
        }
        return infoText + " : " + title;
    }

    protected void setNotificationImage(StatusBarNotification sbn) {
        Resources resources;
        try {
            resources = this.getPackageManager().getResourcesForApplication(sbn.getPackageName());
        } catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
            return;
        } catch (android.content.res.Resources.NotFoundException notfoundexception) {
            return;
        }
        Notification notification = sbn.getNotification();
        if (notification.largeIcon == null) {
            Log.e(TAG, Here.at() + "notification.largeIcon == null");
            return;
        }
        BitmapDrawable bitmapdrawable = new BitmapDrawable(resources, notification.largeIcon);
        int i = bitmapdrawable.getIntrinsicWidth();
        int j = bitmapdrawable.getIntrinsicHeight();
        if(sbn.getNotification().extras != null) {
            i = sbn.getNotification().extras.getInt("notif_img_width", -1);
            j = sbn.getNotification().extras.getInt("notif_img_height", -1);
            Log.d(TAG, Here.at() + "width = " + i + " height = " + j);
        }
        ImageView imageItem = new ImageView(this);
        imageItem.setBackgroundResource(R.drawable.shadow);
        imageItem.setImageBitmap(Bitmap.createScaledBitmap(bitmapdrawable.getBitmap(), 250, 250, false));
        imageItem.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        /// Add image view to the carousel container
        mCarouselContainer.addView(imageItem);
        return;
    }

    private void getCurrentNotificationContent() {
        ArrayList<String> appList = new ArrayList<>();

        StatusBarNotification[] currentNos = NotificationMonitor.getCurrentNotifications();
        Log.i(TAG, Here.at());
        if (currentNos != null) {
            for (int i = 0; i < currentNos.length; i++) {
                    CharSequence charsequence = setNotificationText(currentNos[i], i);
                    if (charsequence != null) {
                            setNotificationImage(currentNos[i]);
                    }
             }
        }
    }

    private void listCurrentNotification() {
        if (isEnabled) {
            if (NotificationMonitor.getCurrentNotifications() == null) {
                Log.i(TAG, Here.at() + "getCurrentNotifications() is null");
                return;
            }
            getCurrentNotificationContent();
        } else {
              Log.e(TAG, Here.at() + "Please Enable Notification Access");
        }
    }
}
