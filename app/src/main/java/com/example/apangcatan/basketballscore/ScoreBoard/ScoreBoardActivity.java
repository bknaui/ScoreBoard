package com.example.apangcatan.basketballscore.ScoreBoard;

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

import com.example.apangcatan.basketballscore.R;

/**
 * Created by apangcatan on 23/03/2018.
 */

public class ScoreBoardActivity extends AppCompatActivity implements ScoreBoardContract.ScoreView, View.OnClickListener {
    ImageView guestPointAdd, guestPointMinus;//, guestFoulAdd, guestFoulMinus, guestTimeoutAdd, guestTimeoutMinus;
    ImageView homePointAdd, homePointMinus;// homeFoulAdd, homeFoulMinus, homeTimeoutAdd, homeTimeoutMinus;

    TextView guestPointValue;//, guestName,  guestFoulValue, guestTimeoutValue;
    TextView homePointValue;//, homeName, homeFoulValue, homeTimeoutValue;

    TextView time, shotclock, quarter;

    ImageView timePause, timePlay, shotclockPause, shotclockPlay, shotclockRestart, buzzer, help;

    ScoreBoardPresenter homePresenter;
    MediaPlayer buzzer_mp, buzzer_theme;
    Dialog dialog;

    int guestPoints = 0;
    int homePoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_layout);
        init();
    }


    public void init() {

        dialog = new Dialog(this);


        buzzer_mp = MediaPlayer.create(this, R.raw.buzzer);
        buzzer_theme = MediaPlayer.create(this, R.raw.theme);

        guestPointAdd = findViewById(R.id.guest_points_add);
        guestPointMinus = findViewById(R.id.guest_points_minus);
        guestPointValue = findViewById(R.id.guest_points_value);

        homePointAdd = findViewById(R.id.home_points_add);
        homePointMinus = findViewById(R.id.home_points_minus);
        homePointValue = findViewById(R.id.home_points_value);

        timePause = findViewById(R.id.time_pause);
        timePlay = findViewById(R.id.time_play);
        shotclockPause = findViewById(R.id.shotclock_pause);
        shotclockPlay = findViewById(R.id.shotclock_play);
        shotclockRestart = findViewById(R.id.shotclock_restart);
        time = findViewById(R.id.time);
        shotclock = findViewById(R.id.shotclock);
        quarter = findViewById(R.id.quarter);
        buzzer = findViewById(R.id.buzzer);
        help = findViewById(R.id.help);

        shotclockRestart.setOnClickListener(this);
        shotclockPause.setOnClickListener(this);
        shotclockPlay.setOnClickListener(this);
        timePause.setOnClickListener(this);
        timePlay.setOnClickListener(this);
        time.setOnClickListener(this);
        quarter.setOnClickListener(this);
        buzzer.setOnClickListener(this);
        guestPointAdd.setOnClickListener(this);
        guestPointMinus.setOnClickListener(this);
        homePointAdd.setOnClickListener(this);
        homePointMinus.setOnClickListener(this);

        homePresenter = new ScoreBoardPresenter(this, buzzer_mp, buzzer_theme);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quarter:
                //SHOWS DIALOG
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
                guestPoints++;
                displayGuestPoints(guestPoints);

                //Removed because it looks like an overkill
                //homePresenter.guestAddPoints();
                break;
            case R.id.guest_points_minus:
                guestPoints--;
                displayGuestPoints(guestPoints);

                //Removed because it looks like an overkill
                //homePresenter.guestMinusPoints();
                break;

            case R.id.home_points_add:
                homePoints++;
                displayHomePoints(homePoints);

                //Removed because it looks like an overkill
                //homePresenter.homeAddPoints();
                break;
            case R.id.home_points_minus:
                homePoints--;
                displayHomePoints(homePoints);
                //Removed because it looks like an overkill
                //homePresenter.homeMinusPoints();
                break;
            case R.id.buzzer:
                homePresenter.playBuzzer();
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
        shotclockPlay.setVisibility(View.VISIBLE);
        shotclockPause.setVisibility(View.GONE);
    }

    @Override
    public void displayShotclockPause() {
        shotclockPlay.setVisibility(View.GONE);
        shotclockPause.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayTimePlay() {
        timePause.setVisibility(View.GONE);
        timePlay.setVisibility(View.VISIBLE);

    }

    @Override
    public void displayTimePause() {
        timePause.setVisibility(View.VISIBLE);
        timePlay.setVisibility(View.GONE);
    }

    @Override
    public void displayShotclock(int seconds) {

        shotclock.setText(String.format("%02d", seconds));
    }

    @Override
    public void displayTime(String mins, String seconds) {
        time.setText(mins + ":" + seconds);
    }

    @Override
    public void displayGuestPoints(int points) {
        guestPointValue.setText(points + "");
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
        homePointValue.setText(points + "");
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


    /**DISABLED FEATURES
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
    @Override
    public void onClick(View view) {
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
    @Override
    public void onClick(View view) {
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
