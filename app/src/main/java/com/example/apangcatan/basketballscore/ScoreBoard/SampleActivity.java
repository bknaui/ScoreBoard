package com.example.apangcatan.basketballscore.ScoreBoard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.apangcatan.basketballscore.R;

/**
 * Created by apangcatan on 03/04/2018.
 */

public class SampleActivity extends AppCompatActivity {
    TextView sample;
    int counter = 0;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        sample = findViewById(R.id.sample);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                counter++;
                sample.setText(counter+"");
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void updateTextView(String message) {
        sample.setText(message);
    }
}
