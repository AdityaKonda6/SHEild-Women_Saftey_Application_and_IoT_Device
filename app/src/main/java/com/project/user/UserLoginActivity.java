package com.project.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.util.AppController;
import com.project.util.Keys;
import com.project.util.Loggers;
import com.project.util.SharedPreference;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends AppCompatActivity {

    Button btnULogin;
    ProgressBar pBar;
    EditText edtUPhone, edtUPass;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            SharedPreference.removeKey("u_id");
            SharedPreference.removeKey("u_name");
            SharedPreference.removeKey("u_email");
            SharedPreference.removeKey("u_phone");
            SharedPreference.removeKey("u_password");

            Intent i = new Intent(UserLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        edtUPass = findViewById(R.id.editTextUPassword);
        edtUPhone = findViewById(R.id.editTextUPhone);
        btnULogin = findViewById(R.id.buttonULogin);
        pBar = findViewById(R.id.progressBar);

        Log.i("NIK123123", SharedPreference.get(Keys.FireKey.F_TOKEN));

        btnULogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_phone = edtUPhone.getText().toString().trim();
                String u_password = edtUPass.getText().toString().trim();

                if (u_phone.equals("") || u_password.equals(""))
                    Toast.makeText(UserLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                else if (u_phone.length() != 10)
                    edtUPhone.setError("Please enter 10 digit phone number");
                else if (u_password.length() < 6)
                    edtUPass.setError("Password must be at least 6 characters");
                else if (checkPermissonStatus())
                    userLogin(u_phone, u_password);
                else
                    Toast.makeText(UserLoginActivity.this, "Please accept required permission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermissonStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 789);
                    return false;
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 456);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        askNotificationPermission();
        Loggers.i(SharedPreference.get(Keys.FireKey.F_TOKEN));
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
            } else
                Toast.makeText(this, "Notification allowed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Notification permission accepted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Application needs notification permission to get important alerts", Toast.LENGTH_SHORT).show();
                break;
            case 456:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 789);
                } else
                    Toast.makeText(this, "Application needs SEND SMS permission", Toast.LENGTH_SHORT).show();
                break;
            case 789:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Call Phone permission accepted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Application needs Call Phone permission", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void userLogin(String uPhone, String uPassword) {
        pBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Loggers.i(response);
                pBar.setVisibility(View.GONE);
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.optString("success").equals("1")) {
                        JSONObject data = json.optJSONObject("data");
                        SharedPreference.save("u_id", data.optString("u_id"));
                        SharedPreference.save("u_name", data.optString("u_name"));
                        SharedPreference.save("u_email", data.optString("u_email"));
                        SharedPreference.save("u_phone", data.optString("u_phone"));
                        SharedPreference.save("u_password", uPassword);
                        SharedPreference.save("u_address", data.optString("u_address"));
                        SharedPreference.save("u_relative_one", data.optString("u_relative_one"));
                        SharedPreference.save("u_relative_two", data.optString("u_relative_two"));
                        SharedPreference.save("u_relative_three", data.optString("u_relative_three"));

                        Intent i = new Intent(UserLoginActivity.this, UserDashboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), json.optString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_phone", uPhone);
                params.put("u_password", uPassword);
                params.put("u_token", SharedPreference.get(Keys.FireKey.F_TOKEN));
                return params;
            }
        };

        AppController.getInstance().add(request);
    }
}
