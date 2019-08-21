package com.example.waldo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.waldo.activities.LoginActivity;
import com.example.waldo.activities.MainDashborad;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                    finish();

            }
        }, 1000);
    }
}
