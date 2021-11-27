package com.example.controltime.Actividades;

import android.content.Intent;
import android.os.Bundle;

import com.example.controltime.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent main = new Intent().setClass(Splash.this, Activity_Login.class);
                startActivity(main);

                finish();
            }
        };
        timer.schedule(task,3000);
    }
}