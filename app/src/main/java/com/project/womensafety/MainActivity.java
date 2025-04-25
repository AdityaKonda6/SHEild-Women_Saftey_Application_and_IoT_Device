package com.project.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.admin.AdminLoginActivity;
import com.project.sheild.MainActivity1;
import com.project.user.UserLoginActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAdmin, btnUser, buttonSHEild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdmin = findViewById(R.id.buttonAdmin);
        btnUser = findViewById(R.id.buttonUser);
        buttonSHEild = findViewById(R.id.buttonSHEild);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        buttonSHEild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity1.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void goBackToMainActivity(View view) {

            onBackPressed();
    }
}