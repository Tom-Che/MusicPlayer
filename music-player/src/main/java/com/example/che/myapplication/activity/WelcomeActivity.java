package com.example.che.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.che.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Che on 2016/04/15.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TimerTask task = new TimerTask() {
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();

        timer.schedule(task, 3000);
    }
}
