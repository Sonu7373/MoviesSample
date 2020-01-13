package com.movies.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.movies.moviesapp.Home.MovieListingActivity;

public class SplashScreenActivity extends AppCompatActivity {

    Context context;
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFields();
    }

    private void initializeFields() {
        context = SplashScreenActivity.this;

        tvVersion = findViewById(R.id.tvVersion);
        try {
            String versionName = BuildConfig.VERSION_NAME;
            int versionCode = BuildConfig.VERSION_CODE;
            tvVersion.setText("Version : " + versionName); // To set the app version
        } catch (Exception e) {

        }

        // Setting a delay to start the app
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, 2000);
    }

    // To start the app
    private void startApp() {
        Intent intent = new Intent(context, MovieListingActivity.class);
        startActivity(intent);
        finish();
    }
}
