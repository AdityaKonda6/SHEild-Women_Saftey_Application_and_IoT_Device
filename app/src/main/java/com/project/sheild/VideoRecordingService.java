package com.project.sheild;




import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.project.womensafety.R;

import java.io.IOException;

public class VideoRecordingService extends Service {

    private MediaRecorder mediaRecorder;
    private String videoFilePath;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getNotification());
        startVideoRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVideoRecording();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startVideoRecording() {
        try {
            videoFilePath = getExternalFilesDir(null).getAbsolutePath() + "/video_" + System.currentTimeMillis() + ".mp4";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(videoFilePath);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setVideoSize(1920, 1080);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setOrientationHint(90);

            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Background video recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    private void stopVideoRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(this, "Recording saved to: " + videoFilePath, Toast.LENGTH_LONG).show();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, "VideoRecordingChannel")
                .setContentTitle("SHEild Recording")
                .setContentText("Recording video in the background")
                .setSmallIcon(R.drawable.ic_notification)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "VideoRecordingChannel",
                    "Video Recording",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
