package com.project.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.admin.AddUserActivity;
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

public class UserDashboardActivity extends AppCompatActivity {

    ProgressBar pBar;
    Button btnOpenLocation;
    LinearLayout llayout;
    TextView tvData;

    String locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());

        pBar = findViewById(R.id.progressBar);
        btnOpenLocation = findViewById(R.id.buttonOpenLocation);
        llayout = findViewById(R.id.linearLayout);
        tvData = findViewById(R.id.textViewUData);

        getUserLocation();

        btnOpenLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + locationData;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(geoUri));
                startActivity(i);
            }
        });
    }

    private void getUserLocation() {
        pBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.GET_USER_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.GONE);
                Loggers.i(response);

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.optString("success").equals("1")){
                        JSONObject data = json.optJSONObject("data");
                        String userData = "Location :- " + data.optString("l_lat") + "," + data.optString("l_long");
                        userData += "\nTime :- " + data.optString("l_time");
                        locationData = data.optString("l_lat") + "," + data.optString("l_long");
                        tvData.setText(userData);
                        llayout.setVisibility(View.VISIBLE);
                        btnOpenLocation.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(UserDashboardActivity.this, json.optString("message"), Toast.LENGTH_SHORT).show();
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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", SharedPreference.get("u_id"));
                return params;
            }
        };

        AppController.getInstance().add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            userLogout();
        }else if(item.getItemId() == R.id.menuRefresh){
            getUserLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void userLogout() {
        pBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.USER_LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Loggers.i(response);
                pBar.setVisibility(View.GONE);
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.optString("success").equals("1")){
                        userLogoutProceed();
                    }
                    Toast.makeText(UserDashboardActivity.this, json.optString("message"), Toast.LENGTH_SHORT).show();
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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", SharedPreference.get("u_id"));
                return params;
            }
        };

        AppController.getInstance().add(request);
    }


    private void userLogoutProceed() {
        SharedPreference.removeKey("u_id");
        SharedPreference.removeKey("u_name");
        SharedPreference.removeKey("u_email");
        SharedPreference.removeKey("u_phone");
        SharedPreference.removeKey("u_password");
        SharedPreference.removeKey("u_address");
        SharedPreference.removeKey("u_relative_one");
        SharedPreference.removeKey("u_relative_two");
        SharedPreference.removeKey("u_relative_three");

        Intent i = new Intent(UserDashboardActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}