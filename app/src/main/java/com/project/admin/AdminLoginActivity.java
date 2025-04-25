package com.project.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.user.UserLoginActivity;
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

public class AdminLoginActivity extends AppCompatActivity {

    EditText edtAEmail, edtAPass;
    Button btnALogin;
    ProgressBar pBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            Intent i = new Intent(AdminLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        btnALogin = findViewById(R.id.buttonALogin);
        edtAEmail = findViewById(R.id.editTextAEmail);
        edtAPass = findViewById(R.id.editTextAPassword);
        pBar = findViewById(R.id.progressBar);

        btnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a_email = edtAEmail.getText().toString().trim();
                String a_pass = edtAPass.getText().toString().trim();

                if (a_email.equals("") && a_pass.equals("")){
                    Toast.makeText(AdminLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (a_pass.length()<6){
                    edtAPass.setError("Password must be at least 6 characters");
                }else {
                    adminLogin(a_email, a_pass);
                }
            }
        });
    }

    private void adminLogin(String aEmail, String aPass) {
        pBar.setVisibility(View.VISIBLE);
        Loggers.i(Keys.URL.ADMIN_LOGIN);
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.ADMIN_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.GONE);
                Loggers.i(response);

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.optString("success").equals("1")){
                        JSONObject data = json.optJSONObject("data");
                        SharedPreference.save("a_id", data.optString("a_id"));
                        SharedPreference.save("a_name", data.optString("a_name"));
                        SharedPreference.save("a_email", data.optString("a_email"));
                        SharedPreference.save("a_phone", data.optString("a_phone"));
                        SharedPreference.save("a_password", aPass);

                        Intent i = new Intent(AdminLoginActivity.this, AddUserActivity.class);
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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("a_email", aEmail);
                params.put("a_password", aPass);
                return params;
            }
        };

        AppController.getInstance().add(request);
    }
}