package com.project.sheild;


import com.project.womensafety.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class SukoonWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukoon_webview);

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("safety://backtoladdybuddy")) {
                    // Navigate back to MainActivity
                    Intent intent = new Intent(SukoonWebViewActivity.this, MainActivity1.class);
                    startActivity(intent);
                    return true; // Indicates that we've handled this URL
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl("file:///android_asset/therapy/index.html");
    }
}
