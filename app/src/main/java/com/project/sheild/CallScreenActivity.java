package com.project.sheild;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.project.womensafety.R;

public class CallScreenActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView callerNameTextView;
    private TextView callerNumberTextView;
    private ImageView callerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen); // Make sure this matches your layout file

        callerNameTextView = findViewById(R.id.caller_name);
        callerNumberTextView = findViewById(R.id.caller_number);
        callerImageView = findViewById(R.id.caller_image_view);

        Button answerButton = findViewById(R.id.answer_button);
        Button declineButton = findViewById(R.id.decline_button);

        answerButton.setOnClickListener(v -> {
            // Handle answer action (you can implement additional logic here)
            endCall(); // For now, just end the call
        });

        declineButton.setOnClickListener(v -> {
            endCall(); // End the call when declined
        });


        // Retrieve caller details from the intent
        String callerName = getIntent().getStringExtra("CALLER_NAME");
        String callerNumber = getIntent().getStringExtra("CALLER_NUMBER");
        String callerImageUri = getIntent().getStringExtra("CALLER_IMAGE");

        // Set caller details in UI
        callerNameTextView.setText(callerName);
        callerNumberTextView.setText(callerNumber);
        if (callerImageUri != null) {
            callerImageView.setImageURI(Uri.parse(callerImageUri)); // Set the selected image
        } else {
            // Set a default image if no image is provided
            callerImageView.setImageResource(R.drawable.mom); // Change this to your default image
        }

        // Play ringtone
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.start();

        // End call after 10 seconds
        new Handler().postDelayed(() -> endCall(), 10000);
    }

    private void endCall() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish(); // Close the activity after the call ends
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer when done to free up resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
