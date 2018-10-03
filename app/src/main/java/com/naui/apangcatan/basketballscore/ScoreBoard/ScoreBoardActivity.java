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
import android.widget.Spinner;
import android.widget.TextView;

import com.naui.apangcatan.basketballscore.R;

/**
 * Created by apangcatan on 23/03/2018.
 */

public class ScoreBoardActivity extends AppCompatActivity implements ScoreBoardContract.ScoreView, View.OnClickListener {
    ImageView guestAddPoints, guestMinusPoints;//, guestFoulAdd, guestFoulMinus, guestTimeoutAdd, guestTimeoutMinus;
    ImageView homeAddPoints, homeMinusPoints;// homeFoulAdd, homeFoulMinus, homeTimeoutAdd, homeTimeoutMinus;

    TextView guestPoints;//, guestName,  guestFoulValue, guestTimeoutValue;
    TextView homePoints;//, homeName, homeFoulValue, homeTimeoutValue;

    TextView time, shotclock, quarter;

    ImageView pauseTime, playTime, pauseShotclock, playShotclock, restartShotclock, buzzer, help;

    ScoreBoardPresenter homePresenter;
    public static MediaPlayer BUZZER_MP, BUZZER_THEME;
    Dialog dialog;

    int guestPointsValue = 0;
    int homePointsValue = 0;

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

        homeAddPoints = findViewById(R.id.home_points_add);
        homeMinusPoints = findViewById(R.id.home_points_minus);
        homePoints = findViewById(R.id.home_points_value);

        pauseTime = findViewById(R.id.time_pause);
        playTime = findViewById(R.id.time_play);
        pauseShotclock = findViewById(R.id.shotclock_pause);
        playShotclock = findViewById(R.id.shotclock_play);
        restartShotclock = findViewById(R.id.shotclock_restart);

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
            case R.id.quarter:
                dialog.setContentView(R.layout.dialog_quarter_settings);
                dialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                final Spinner dialog_settings_quarter = dialog.findViewById(R.id.dialog_settings_quarter);
                Button dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
                dialog_settings_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selected_quarter = dialog_settings_quarter.getSelectedItem().toString();
                        homePresenter.updateQuarter(selected_quarter);
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


    /**   DISABLED FEATURES
     guestName = findViewById(R.id.guest_name);
     guestFoulAdd = findViewById(R.id.guest_foul_add);
     guestFoulMinus = findViewById(R.id.guest_foul_minus);
     guestFoulValue = findViewById(R.id.guest_foul_value);

     guestTimeoutAdd = findViewById(R.id.guest_timeouts_add);
     guestTimeoutMinus = findViewById(R.id.guest_timeouts_minus);
     guestTimeoutValue = findViewById(R.id.guest_timeouts_value);

     homeFoulAdd = findViewById(R.id.home_foul_add);
     homeFoulMinus = findViewById(R.id.home_foul_minus);
     homeFoulValue = findViewById(R.id.home_foul_value);

     homeTimeoutAdd = findViewById(R.id.home_timeouts_add);
     homeTimeoutMinus = findViewById(R.id.home_timeouts_minus);
     homeTimeoutValue = findViewById(R.id.home_timeouts_value);
     homeName = findViewById(R.id.home_name);

     guestName.setOnClickListener(this);

     guestFoulAdd.setOnClickListener(this);
     guestFoulMinus.setOnClickListener(this);
     guestTimeoutAdd.setOnClickListener(this);
     guestTimeoutMinus.setOnClickListener(this);
     homeName.setOnClickListener(this);

     homeFoulAdd.setOnClickListener(this);
     homeFoulMinus.setOnClickListener(this);
     homeTimeoutAdd.setOnClickListener(this);
     homeTimeoutMinus.setOnClickListener(this);

     **/

    /**
     *
     * DISABLED FEATURE
     *
     *
     case R.id.guest_timeouts_add:
     homePresenter.guestTimeoutAdd();
     break;
     case R.id.guest_timeouts_minus:
     homePresenter.guestTimeoutMinus();
     break;
     case R.id.home_foul_add:
     homePresenter.homeAddFoul();
     break;
     case R.id.home_foul_minus:
     homePresenter.homeMinusFoul();
     break;


     case R.id.guest_name:
     dialog.setContentView(R.layout.dialog_name_settings);
     final EditText dialog_settings_name_guest = dialog.findViewById(R.id.dialog_settings_name);
     Button dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
     dialog_settings_button.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    String name = dialog_settings_name_guest.getText().toString();
    if (name.isEmpty()) {
    dialog_settings_name_guest.requestFocus();
    dialog_settings_name_guest.setError("Required");
    } else {
    homePresenter.updateGuestName(name);
    dialog.dismiss();
    }
    }
    });
     dialog.show();
     break;
     case R.id.home_name:
     dialog.setContentView(R.layout.dialog_name_settings);
     final EditText dialog_settings_name_home = dialog.findViewById(R.id.dialog_settings_name);
     dialog_settings_button = dialog.findViewById(R.id.dialog_settings_button);
     dialog_settings_button.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    String name = dialog_settings_name_home.getText().toString();
    if (name.isEmpty()) {
    dialog_settings_name_home.requestFocus();
    dialog_settings_name_home.setError("Required");
    } else {
    homePresenter.updateHomeName(name);
    dialog.dismiss();
    }
    }
    });
     dialog.show();
     break;


     case R.id.guest_foul_add:
     homePresenter.guestAddFoul();
     break;
     case R.id.guest_foul_minus:
     homePresenter.guestMinusFoul();
     break;

     case R.id.home_timeouts_add:
     homePresenter.homeTimeoutAdd();
     break;
     case R.id.home_timeouts_minus:
     homePresenter.homeTimeoutMinus();
     break;

     **/

}
