package com.project.sheild;

import com.project.admin.AdminLoginActivity;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class Flashing extends AppCompatActivity {
    ImageView imageviewflashing;
    TextView textSirenAlert; // Declare TextView
    MediaPlayer mediaPlayer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            Intent i = new Intent(Flashing.this, MainActivity1.class);
            startActivity(i);
            mediaPlayer.stop();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashing);

        imageviewflashing = findViewById(R.id.imageactivity);
        textSirenAlert = findViewById(R.id.text_siren_alert); // Initialize TextView

        startSiren();
        startLights();
    }

    public void startSiren() {
        mediaPlayer = MediaPlayer.create(this, R.raw.police_siren);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @SuppressLint("WrongConstant")
    public void startLights() {
        ObjectAnimator anim = ObjectAnimator.ofInt(imageviewflashing, "backgroundColor",
                Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN);
        anim.setDuration(150);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        // Make the TextView visible and start blinking
        textSirenAlert.setVisibility(View.VISIBLE);
        textSirenAlert.setText("Siren Alert");
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }
}
