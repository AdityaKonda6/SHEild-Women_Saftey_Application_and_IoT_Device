package com.project.speech;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class DefaultLoggerDelegate extends AppCompatActivity implements Logger.LoggerDelegate {

    private static final String TAG = Speech.class.getSimpleName();

    @Override
    public void error(String tag, String message) {
        Log.e(TAG, tag + " - " + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        Log.e(TAG, tag + " - " + message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        Log.d(TAG, tag + " - " + message);
    }

    @Override
    public void info(String tag, String message) {
        Log.i(TAG, tag + " - " + message);
    }
}
