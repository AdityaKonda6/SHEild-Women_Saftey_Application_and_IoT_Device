package com.project.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.user.UserAlertActivity;
import com.project.womensafety.R;

import java.util.Map;
import java.util.Random;

public class SafetyNotification extends FirebaseMessagingService{

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Loggers.i("Firebase Token ==> " + token);
        SharedPreference.save(Keys.FireKey.F_TOKEN, token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        /*Intent broadcastIntent = new Intent("your.custom.NOTIFICATION_ACTION");
        sendBroadcast(broadcastIntent);*/

        /*Intent serviceIntent = new Intent(this, MyForegroundService.class);
        //serviceIntent.putExtra("data", remoteMessage.getData().toString()); // Pass any needed data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }*/

        Loggers.i("NIK123123 ==> Message Received ==> " + message.getNotification());
        showNotification(message.getNotification().getTitle(), message.getNotification().getBody());
    }


    private void showNotification(String title, String body) {
        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());

        final int randomCode = new Random().nextInt(50) + 1;

        Intent i = new Intent(this, UserAlertActivity.class);


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomCode, i, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE );

        String channelId = getString(R.string.default_notification_channel_id);
        //Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent);


        //notificationBuilder.setSound(alarmSound);


        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Women Safety Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationBuilder
                .setContentTitle(title)
                .setContentText(body);

        notificationManager.notify(randomCode, notificationBuilder.build());

        Intent intent = new Intent(this, UserAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getApplicationContext().startActivity(intent);
    }
}
