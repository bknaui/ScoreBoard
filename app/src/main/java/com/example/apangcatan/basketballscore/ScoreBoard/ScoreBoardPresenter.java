package com.example.apangcatan.basketballscore.ScoreBoard;

import android.os.Handler;

/**
 * Created by apangcatan on 23/03/2018.
 */

/**
 * UTILITY METHODS AT THE BOTTOM
 *
 */


public class ScoreBoardPresenter implements ScoreBoardContract.ScorePresenter {

    private ScoreBoardContract.ScoreView homeView;
    int shotclock_seconds = 24;
    int time_minutes = 0;
    int time_seconds = 30;

    /**  DISABLED FEATURE **/
    // int homeFoul = 0, homePoints = 0, homeTimeout = 0;
    // int guestFoul = 0, guestPoints = 0, guestTimeout = 0;


    Handler shotclockHandler = new Handler();
    Runnable shotclockRunnable = null;

    Handler timeHandler = new Handler();
    Runnable timeRunnable = null;

    boolean isTimeRunning = false;

    public ScoreBoardPresenter(ScoreBoardContract.ScoreView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void playShotclock() {
        if (!isTimeRunning) {
            return;
        }
        /** Only ticks when there is enough time and changes shot clock play icon to pause **/
        if (!disableShotclock(shotclock_seconds)) {
            homeView.displayShotclockPause();
            getShotclockRunnable();
            shotclockHandler.postDelayed(shotclockRunnable, 1000);
        }
    }

    @Override
    public void playTime() {

        isTimeRunning = true;

        /** Changes the play icon to pause **/
        homeView.displayTimePause();
        playShotclock();

        getTimeRunnable();
        timeHandler.postDelayed(timeRunnable, 1000);
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
        time_minutes = minute;
        time_seconds = second;
        homeView.displayTime(time_minutes, time_seconds);
    }

    @Override
    public void pauseShotclock() {
        homeView.displayShotclockPlay();
        shotclockHandler.removeCallbacks(shotclockRunnable);
    }

    @Override
    public void restartShotclock() {
        if (!disableShotclock(24)) {
            shotclock_seconds = 24;
            homeView.enableShotclockTime();
            homeView.displayShotclock(shotclock_seconds);
            return;
        }

        shotclock_seconds = 24;
        homeView.displayShotclock(0);
        homeView.disableShotclockTime();

    }

    @Override
    public void pauseTime() {
        pauseShotclock();
        isTimeRunning = false;
        homeView.displayTimePlay();
        if (shotclock_seconds == 0) {
            shotclock_seconds = 24;
            homeView.displayShotclock(shotclock_seconds);
        }
        timeHandler.removeCallbacks(timeRunnable);

    }


    /**
     *   DISABLED FEATURE SECTIONS
     *
     @Override public void guestAddPoints() {
     guestPoints++;
     homeView.displayGuestPoints(guestPoints);
     //guestRef.setValue(guestPoints);
     }

     @Override public void guestAddFoul() {
     guestFoul++;
     homeView.displayGuestFoul(guestFoul);
     }

     @Override public void guestMinusPoints() {
     if (guestPoints > 0) {
     guestPoints--;
     homeView.displayGuestPoints(guestPoints);
     }
     }

     @Override public void guestMinusFoul() {
     if (guestFoul > 0) {
     guestFoul--;
     homeView.displayGuestFoul(guestFoul);
     }
     }

     @Override public void guestTimeoutAdd() {
     guestTimeout++;
     homeView.displayGuestTimeout(guestTimeout);
     }

     @Override public void guestTimeoutMinus() {
     if (guestTimeout > 0) {
     guestTimeout--;
     homeView.displayGuestTimeout(guestTimeout);
     }
     }

     @Override public void homeAddPoints() {
     homePoints++;
     homeView.displayHomePoints(homePoints);
     }

     @Override public void homeAddFoul() {
     homeFoul++;
     homeView.displayHomeFoul(homeFoul);
     }

     @Override public void homeMinusPoints() {
     if (homePoints > 0) {
     homePoints--;
     homeView.displayHomePoints(homePoints);
     }
     }

     @Override public void homeMinusFoul() {
     if (homeFoul > 0) {
     homeFoul--;
     homeView.displayHomeFoul(homeFoul);
     }
     }

     @Override public void homeTimeoutAdd() {
     homeTimeout++;
     homeView.displayHomeTimeout(homeTimeout);
     }

     @Override public void homeTimeoutMinus() {
     if (homeTimeout > 0) {
     homeTimeout--;
     homeView.displayHomeTimeout(homeTimeout);
     }
     }
      ** /



     /****   UTILITY SECTION    ****/

    /**
     * Perform logical operation for shot clock ticks
     **/
    private void getShotclockRunnable() {
        shotclockRunnable = new Runnable() {
            @Override
            public void run() {
                if (shotclock_seconds > 1) {
                    shotclock_seconds--;
                    homeView.displayShotclock(shotclock_seconds);
                    shotclockHandler.postDelayed(this, 1000);
                    return;
                }
                time_seconds--;
                if (time_seconds < 0) {
                    time_minutes--;
                    time_seconds = 59;
                }
                homeView.displayShotclock(shotclock_seconds);
                homeView.displayTime(time_minutes, time_seconds);
                pauseTime();
                homeView.displayShotclockPlay();
                shotclockHandler.removeCallbacks(shotclockRunnable);
            }

        };
    }

    /**
     * Perform logical operation for game time ticks
     **/
    private void getTimeRunnable() {

        timeRunnable = new Runnable() {
            @Override
            public void run() {
                if (time_seconds > 1) {
                    time_seconds--;
                    homeView.displayTime(time_minutes, time_seconds);
                    if (disableShotclock(shotclock_seconds)) {
                        shotclockHandler.removeCallbacks(shotclockRunnable);
                        homeView.displayShotclock(0);
                        homeView.disableShotclockTime();
                    }
                    timeHandler.postDelayed(this, 1000);
                    return;
                }

                if (time_minutes > 0) {
                    time_minutes--;
                    time_seconds = 59;
                    homeView.displayTime(time_minutes, time_seconds);
                    timeHandler.postDelayed(this, 1000);
                    return;
                }
                time_seconds = 0;
                homeView.displayTime(time_minutes, time_seconds);
                pauseTime();
                pauseShotclock();
                homeView.displayTimePlay();

            }
        };
    }


    /**
     * Checks if there is enough time for shot clock
     **/
    public boolean disableShotclock(int temp_shotclock) {
        if (time_seconds - temp_shotclock <= 0 && time_minutes <= 0) {
            return true;
        }
        return false;
    }
}
