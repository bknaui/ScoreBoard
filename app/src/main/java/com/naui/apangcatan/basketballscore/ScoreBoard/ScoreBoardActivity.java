package com.naui.apangcatan.basketballscore.ScoreBoard;

import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.naui.apangcatan.basketballscore.R;

/**
 * Created by apangcatan on 23/03/2018.
 */

public class ScoreBoardActivity extends AppCompatActivity implements ScoreBoardContract.ScoreView, View.OnClickListener {
    ImageView guestAddPoints, guestMinusPoints;// guestTimeoutAdd, guestTimeoutMinus;
    ImageView homeAddPoints, homeMinusPoints;// homeTimeoutAdd, homeTimeoutMinus;
    ImageView pauseTime, playTime, pauseShotclock, playShotclock, restartShotclock, buzzer, help, arrow_left, arrow_right;

    RatingBar home_foul_rating, guest_foul_rating;

    TextView guestPoints, guestName;//  guestFoulValue, guestTimeoutValue;
    TextView homePoints, homeName;//, homeFoulValue, homeTimeoutValue;
    TextView time, shotclock, quarter;

    ScoreBoardPresenter homePresenter;
    public static MediaPlayer BUZZER_MP, BUZZER_THEME;
    Dialog dialog;

    int guestPointsValue = 0;
    int homePointsValue = 0;

    int guest_foul_counter = 0;
    int home_foul_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_layout);
        init();
    }


    public void init() {

        dialog = new Dialog(this);


        BUZZER_MP = MediaPlayer.create(this, R.raw.buzzer);
        BUZZER_THEME = MediaPlayer.create(this, R.raw.theme);

        guestAddPoints = findViewById(R.id.guest_points_add);
        guestMinusPoints = findViewById(R.id.guest_points_minus);
        guestPoints = findViewById(R.id.guest_points_value);
        guestName = findViewById(R.id.guest_team_name);
        guest_foul_rating = findViewById(R.id.guest_foul_rate);

        homeAddPoints = findViewById(R.id.home_points_add);
        homeMinusPoints = findViewById(R.id.home_points_minus);
        homePoints = findViewById(R.id.home_points_value);
        homeName = findViewById(R.id.home_team_name);
        home_foul_rating = findViewById(R.id.home_foul_rate);

        pauseTime = findViewById(R.id.time_pause);
        playTime = findViewById(R.id.time_play);
        pauseShotclock = findViewById(R.id.shotclock_pause);
        playShotclock = findViewById(R.id.shotclock_play);
        restartShotclock = findViewById(R.id.shotclock_restart);

        homeName.setOnClickListener(this);
        guestName.setOnClickListener(this);

        arrow_left = findViewById(R.id.arrow_left);
        arrow_right = findViewById(R.id.arrow_right);
        time = findViewById(R.id.time);
        shotclock = findViewById(R.id.shotclock);
        quarter = findViewById(R.id.quarter);
        buzzer = findViewById(R.id.buzzer);
        help = findViewById(R.id.help);

        restartShotclock.setOnClickListener(this);
        pauseShotclock.setOnClickListener(this);
        playShotclock.setOnClickListener(this);
        pauseTime.setOnClickListener(this);
        playTime.setOnClickListener(this);
        arrow_right.setOnClickListener(this);
        arrow_left.setOnClickListener(this);

        time.setOnClickListener(this);
        quarter.setOnClickListener(this);
        buzzer.setOnClickListener(this);

        guestAddPoints.setOnClickListener(this);
        guestMinusPoints.setOnClickListener(this);
        homeAddPoints.setOnClickListener(this);
        homeMinusPoints.setOnClickListener(this);

        homePresenter = new ScoreBoardPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_left:
                arrow_left.setImageResource(R.drawable.arrow_left);
                arrow_right.setImageResource(R.drawable.arrow_right_disable);
                break;
            case R.id.arrow_right:
                arrow_left.setImageResource(R.drawable.arrow_left_disable);
                arrow_right.setImageResource(R.drawable.arrow_right);
                break;
            case R.id.guest_team_name:
                dialog.setContentView(R.layout.dialog_name_settings);
                dialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                final EditText guest_name = dialog.findViewById(R.id.dialog_settings_name);
                Button dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
                dialog_settings_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = guest_name.getText().toString();
                        guestName.setText(name);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.home_team_name:
                dialog.setContentView(R.layout.dialog_name_settings);
                dialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                final EditText home_name = dialog.findViewById(R.id.dialog_settings_name);
                dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
                dialog_settings_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = home_name.getText().toString();
                        homeName.setText(name);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.quarter:
                dialog.setContentView(R.layout.dialog_quarter_settings);
                dialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                final Spinner dialog_settings_quarter = dialog.findViewById(R.id.dialog_settings_quarter);
                dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
                dialog_settings_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selected_quarter = dialog_settings_quarter.getSelectedItem().toString();
                        homePresenter.updateQuarter(selected_quarter);

                        //SWITCHING
                        if (selected_quarter.equalsIgnoreCase("3Q")) {
                            //NAME SWITCH
                            String temp = homeName.getText().toString();
                            homeName.setText(guestName.getText().toString());
                            guestName.setText(temp);

                            //SCORE SWITCH
                            int temp_val = homePointsValue;
                            homePointsValue = guestPointsValue;
                            guestPointsValue = temp_val;

                            homePoints.setText(homePointsValue + "");
                            guestPoints.setText(guestPointsValue + "");

                        }
                        home_foul_rating.setRating(0);
                        guest_foul_rating.setRating(0);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.time:
                dialog.setContentView(R.layout.dialog_time_settings);
                dialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                final EditText dialog_settings_minutes = dialog.findViewById(R.id.dialog_settings_minutes);
                final EditText dialog_settings_seconds = dialog.findViewById(R.id.dialog_settings_seconds);
                dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
                dialog_settings_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog_settings_minutes.getText().toString().isEmpty()) {
                            dialog_settings_minutes.requestFocus();
                            dialog_settings_minutes.setError("Required");
                        } else if (dialog_settings_seconds.getText().toString().isEmpty()) {
                            dialog_settings_seconds.requestFocus();
                            dialog_settings_seconds.setError("Required");
                        } else {
                            int minutes = Integer.parseInt(dialog_settings_minutes.getText().toString());
                            int seconds = Integer.parseInt(dialog_settings_seconds.getText().toString());
                            homePresenter.setTime(minutes, seconds);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.shotclock_restart:
                homePresenter.restartShotclock();
                break;
            case R.id.shotclock_play:
                homePresenter.playShotclock();
                break;
            case R.id.shotclock_pause:
                homePresenter.pauseShotclock();
                break;
            case R.id.time_pause:
                homePresenter.pauseTime();
                break;
            case R.id.time_play:
                homePresenter.playTime();
                break;
            case R.id.guest_points_add:
                guestPointsValue++;
                displayGuestPoints(guestPointsValue);

                /** Removed because it looks like an overkill **/
                //homePresenter.guestAddPoints();
                break;
            case R.id.guest_points_minus:
                if (guestPointsValue > 0) {
                    guestPointsValue--;
                    displayGuestPoints(guestPointsValue);
                }

                /** Removed because it looks like an overkill **/
                //homePresenter.guestMinusPoints();
                break;

            case R.id.home_points_add:
                homePointsValue++;
                displayHomePoints(homePointsValue);

                /** Removed because it looks like an overkill **/
                //homePresenter.homeAddPoints();
                break;
            case R.id.home_points_minus:
                if (homePointsValue > 0) {
                    homePointsValue--;
                }
                displayHomePoints(homePointsValue);

                /** Removed because it looks like an overkill **/
                //homePresenter.homeMinusPoints();
                break;
            case R.id.buzzer:
                BUZZER_MP.start();
                break;
            case R.id.help:
                //TODO: ADD Guide on how to use this app
                break;
        }
    }

    @Override
    public void setGuestName(String name) {
        //  guestName.setText(name);
    }

    @Override
    public void setHomeName(String name) {
        // homeName.setText(name);
    }

    @Override
    public void setQuarter(String quarter) {
        this.quarter.setText(quarter);
    }

    @Override
    public void displayShotclockPlay() {
        playShotclock.setVisibility(View.VISIBLE);
        pauseShotclock.setVisibility(View.GONE);
    }

    @Override
    public void displayShotclockPause() {
        playShotclock.setVisibility(View.GONE);
        pauseShotclock.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayTimePlay() {
        pauseTime.setVisibility(View.GONE);
        playTime.setVisibility(View.VISIBLE);

    }

    @Override
    public void displayTimePause() {
        pauseTime.setVisibility(View.VISIBLE);
        playTime.setVisibility(View.GONE);
    }

    @Override
    public void displayShotclock(int seconds) {
        shotclock.setText(String.format("%02d", seconds));
    }

    @Override
    public void displayTime(int mins, int seconds) {
        time.setText(String.format("%02d", mins) + ":" + String.format("%02d", seconds));
    }

    @Override
    public void displayGuestPoints(int points) {
        guestPoints.setText(points + "");
    }

    @Override
    public void displayGuestFoul(int foul) {
        //  guestFoulValue.setText(foul + "");
    }

    @Override
    public void displayGuestTimeout(int timeout) {
        //  guestTimeoutValue.setText(timeout + "");
    }

    @Override
    public void displayHomePoints(int points) {
        homePoints.setText(points + "");
    }

    @Override
    public void displayHomeFoul(int foul) {
        //  homeFoulValue.setText(foul + "");
    }

    @Override
    public void displayHomeTimeout(int timeout) {
        // homeTimeoutValue.setText(timeout + "");
    }

    @Override
    public void disableShotclockTime() {
        shotclock.setTextColor(Color.parseColor("#9E9E9E"));
    }

    @Override
    public void enableShotclockTime() {
        shotclock.setTextColor(Color.parseColor("#ff0000"));
    }

}
