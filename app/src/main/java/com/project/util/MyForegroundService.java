package com.project.util;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.project.user.UserAlertActivity;
import com.project.womensafety.R;

public class MyForegroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Show a notification for the foreground service (required)
        showNotification();

        // Launch the activity from here
        Intent activityIntent = new Intent(this, UserAlertActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);

        // Stop the service once the task is done
        stopSelf();

        return START_NOT_STICKY;
    }

    private void showNotification() {
        // Create and show a notification for the foreground service
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle("Foreground Service")
                .setContentText("Running foreground service")
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .build();

        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

