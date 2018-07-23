package com.example.apangcatan.basketballscore.ScoreBoard;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by apangcatan on 23/03/2018.
 */

public class ScoreBoardPresenter implements ScoreBoardContract.ScorePresenter {
    private ScoreBoardContract.ScoreView homeView;
    Thread t;
    int shotclock = 24;
    int mins = 20;
    int sec = 0;

    int homeFoul = 0, homePoints = 0, homeTimeout = 0;
    int guestFoul = 0, guestPoints = 0, guestTimeout = 0;
    DatabaseReference homeRef, guestRef;
    FirebaseDatabase database;


    Handler shotclockHandler = new Handler();
    Runnable shotclockRunnable = null;

    Handler timeHandler = new Handler();
    Runnable timeRunnable = null;

    MediaPlayer buzzer, theme;

    boolean isTimeRunning = false;
    boolean isShotclockRunning = false;

    public ScoreBoardPresenter(ScoreBoardContract.ScoreView homeView, MediaPlayer buzzer, MediaPlayer theme) {
        this.homeView = homeView;
        this.buzzer = buzzer;
        this.theme = theme;
        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("Users/Home");
        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("HOME CHANGE", dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FCM CANCEL", "Failed to read value.", databaseError.toException());
            }
        });
        guestRef = database.getReference("Users/Guest");
        guestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("GUEST CHANGE", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Guest CANCEL", "Failed to read value.", databaseError.toException());
            }
        });
    }


    @Override
    public void updateQuarter(String quarter) {
        homeView.setQuarter(quarter);
    }

    @Override
    public void updateGuestName(String name) {
        homeView.setGuestName(name);
    }

    @Override
    public void updateHomeName(String name) {
        homeView.setHomeName(name);
    }

    @Override
    public void setTime(int minute, int second) {
        mins = minute;
        sec = second;
        homeView.displayTime(String.format("%02d", mins) + "", String.format("%02d", sec) + "");
    }

    @Override
    public void playShotclock() {
        if (isTimeRunning) {
            if (!disableShotclock(shotclock)) {
                homeView.displayShotclockPause();
                shotclockHandler = new Handler();
                final int delay = 1000; //milliseconds
                shotclockRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (shotclock > 1) {
                            shotclock--;
                            homeView.displayShotclock(shotclock);
                            shotclockHandler.postDelayed(this, delay);
                        } else {
                            shotclock = 0;
                            sec--;
                            if (sec < 0) {
                                mins--;
                                sec = 59;
                            }
                            homeView.displayShotclock(shotclock);
                            homeView.displayTime(String.format("%02d", mins) + "", String.format("%02d", sec) + "");
                            pauseTime();
                            playBuzzer();
                            shotclockHandler.removeCallbacks(shotclockRunnable);
                            homeView.displayShotclockPlay();
                        }
                    }
                };
                shotclockHandler.postDelayed(shotclockRunnable, delay);
            } else {

            }
        }
    }

    @Override
    public void pauseShotclock() {
        homeView.displayShotclockPlay();
        shotclockHandler.removeCallbacks(shotclockRunnable);
    }

    @Override
    public void restartShotclock() {
        int temp_shotclock = 24;
        if (!disableShotclock(temp_shotclock)) {
            shotclock = 24;
            homeView.enableShotclockTime();
            homeView.displayShotclock(shotclock);
        } else {
            shotclock = 24;
            homeView.displayShotclock(0);
            homeView.disableShotclockTime();
        }
    }

    @Override
    public void playTime() {
        isTimeRunning = true;
        homeView.displayTimePause();
        if (shotclock == 0) {
            shotclock = 0;
            disableShotclock(0);
        } else {
            playShotclock();
        }

        timeHandler = new Handler();
        final int delay = 1000; //milliseconds
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                if (sec > 1) {
                    sec--;
                    homeView.displayTime(String.format("%02d", mins) + "", String.format("%02d", sec) + "");
                    if (disableShotclock(shotclock)) {
                        homeView.displayShotclock(0);
                        homeView.disableShotclockTime();
                    }
                    timeHandler.postDelayed(this, delay);
                } else {
                    if (mins > 0) {
                        mins--;
                        sec = 59;
                        homeView.displayTime(String.format("%02d", mins) + "", String.format("%02d", sec) + "");
                        timeHandler.postDelayed(this, delay);
                    } else {
                        sec = 0;
                        homeView.displayTime(String.format("%02d", mins) + "", String.format("%02d", sec) + "");
                        pauseTime();
                        pauseShotclock();
                        playBuzzer();
                        homeView.displayTimePlay();
                    }
                }
            }
        };
        timeHandler.postDelayed(timeRunnable, delay);
    }

    @Override
    public void pauseTime() {
        isTimeRunning = false;
        pauseShotclock();
        homeView.displayTimePlay();
        timeHandler.removeCallbacks(timeRunnable);
    }

    @Override
    public void guestAddPoints() {
        guestPoints++;
        homeView.displayGuestPoints(guestPoints);
        guestRef.setValue(guestPoints);
    }

    @Override
    public void guestAddFoul() {
        guestFoul++;
        homeView.displayGuestFoul(guestFoul);
    }

    @Override
    public void guestMinusPoints() {
        if (guestPoints > 0) {
            guestPoints--;
            homeView.displayGuestPoints(guestPoints);
            guestRef.setValue(guestPoints);
        }
    }

    @Override
    public void guestMinusFoul() {
        if (guestFoul > 0) {
            guestFoul--;
            homeView.displayGuestFoul(guestFoul);
        }
    }

    @Override
    public void guestTimeoutAdd() {
        guestTimeout++;
        homeView.displayGuestTimeout(guestTimeout);
    }

    @Override
    public void guestTimeoutMinus() {
        if (guestTimeout > 0) {
            guestTimeout--;
            homeView.displayGuestTimeout(guestTimeout);
        }
    }

    @Override
    public void homeAddPoints() {
        homePoints++;
        homeView.displayHomePoints(homePoints);
        homeRef.setValue(homePoints);
    }

    @Override
    public void homeAddFoul() {
        homeFoul++;
        homeView.displayHomeFoul(homeFoul);
    }

    @Override
    public void homeMinusPoints() {
        if (homePoints > 0) {
            homePoints--;
            homeView.displayHomePoints(homePoints);
            homeRef.setValue(homePoints);
        }
    }

    @Override
    public void homeMinusFoul() {
        if (homeFoul > 0) {
            homeFoul--;
            homeView.displayHomeFoul(homeFoul);
        }
    }

    @Override
    public void homeTimeoutAdd() {
        homeTimeout++;
        homeView.displayHomeTimeout(homeTimeout);
    }

    @Override
    public void homeTimeoutMinus() {
        if (homeTimeout > 0) {
            homeTimeout--;
            homeView.displayHomeTimeout(homeTimeout);
        }
    }

    @Override
    public void playBuzzer() {
        buzzer.start();
    }

    @Override
    public void playTheme() {
        theme.start();
    }

    public boolean disableShotclock(int temp_shotclock) {
        boolean ok = false;
        Log.e("DisableShot", sec + " " + temp_shotclock + " " + mins);
        if (sec - temp_shotclock <= 0 && mins <= 0) {
            ok = true;
        }
        return ok;
    }
}
