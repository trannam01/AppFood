package com.example.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood.R;
import com.example.appfood.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            // Start home activity
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            // close splash activity
            finish();
        },3000);
    }
}