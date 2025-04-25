package com.project.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.user.UserAlertActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, UserAlertActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required to start activity from a non-activity context
        context.startActivity(activityIntent);
    }
}
