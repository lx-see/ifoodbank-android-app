package com.example.ifoodbank;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        WebView webView = findViewById(R.id.web);
        webView.loadUrl("https://calendar.google.com/calendar/u/0/r");
        webView.requestFocus();
    }
}