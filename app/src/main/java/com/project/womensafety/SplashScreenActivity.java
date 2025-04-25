package com.project.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.project.admin.AddUserActivity;
import com.project.user.UserDashboardActivity;
import com.project.util.AppController;
import com.project.util.SharedPreference;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = null;

                if(SharedPreference.contains("a_id"))
                    i=new Intent(SplashScreenActivity.this, AddUserActivity.class);
                else if(SharedPreference.contains("u_id"))
                    i=new Intent(SplashScreenActivity.this, UserDashboardActivity.class);
                else
                    i=new Intent(SplashScreenActivity.this, MainActivity.class);

                //Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },300);
    }
}