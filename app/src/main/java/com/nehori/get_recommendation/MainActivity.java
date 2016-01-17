package com.nehori.get_recommendation;

import java.util.ArrayList;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.pm.PackageManager;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int EVENT_LIST_CURRENT_NOS = 1;
    private boolean isEnabled = false;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnabled = Util.isEnabled(this);
        Log.i(TAG, Here.at() + "isEnabled = " + isEnabled);
    }

    public void buttonOnClicked(View view) {
        switch (view.getId()) {
            case R.id.btnListNotify:
                Log.i(TAG, Here.at() + "List recommendation...");
                listCurrentNotification();
                break;
            default:
                break;
        }
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

    private void getCurrentNotificationContent() {
        ArrayList<String> appList = new ArrayList<>();

        StatusBarNotification[] currentNos = NotificationMonitor.getCurrentNotifications();
        Log.i(TAG, Here.at());
        if (currentNos != null) {
            for (int i = 0; i < currentNos.length; i++) {
                    CharSequence charsequence = setNotificationText(currentNos[i], i);
                    if (charsequence != null) {
                          appList.add(charsequence.toString());
                    }
             }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
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
