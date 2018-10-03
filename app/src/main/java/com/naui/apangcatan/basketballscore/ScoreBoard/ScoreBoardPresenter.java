package com.naui.apangcatan.basketballscore.ScoreBoard;

import android.os.Handler;

/**
 * Created by apangcatan on 23/03/2018.
 */

/**
 * UTILITY METHODS AT THE BOTTOM
 */


public class ScoreBoardPresenter implements ScoreBoardContract.ScorePresenter {

    private ScoreBoardContract.ScoreView homeView;
    int DEFAULT_SHOT_CLOCK_SECONDS = 24;

    int RUNNABLE_DELAY = 1000;
    int shotclockSeconds;
    int timeMinutes;
    int timeSeconds;

    /**
     * DISABLED FEATURE
     * int homeFoul = 0, homePoints = 0, homeTimeout = 0;
     * int guestFoul = 0, guestPoints = 0, guestTimeout = 0;
     */


    Handler shotclockHandler = new Handler();
    Runnable shotclockRunnable;

    Handler timeHanlder = new Handler();
    Runnable timeRunnable;

    boolean isTimeRunning = false;

    public ScoreBoardPresenter(ScoreBoardContract.ScoreView homeView) {
        this.homeView = homeView;
        this.shotclockSeconds = DEFAULT_SHOT_CLOCK_SECONDS;
        this.timeMinutes = 15;
        this.timeSeconds = 0;
    }


    @Override
    public void playShotclock() {
        if (!isTimeRunning) {
            return;
        }
        /** Only ticks when there is enough time and changes shot clock play icon to pause **/
        if (!hasEnoughTime(shotclockSeconds)) {
            getShotClockRunnable();
            homeView.displayShotclockPause();
            shotclockHandler.postDelayed(shotclockRunnable, RUNNABLE_DELAY);
        }
    }

    @Override
    public void playTime() {

        isTimeRunning = true;

        /** Changes the play icon to pause **/
        homeView.displayTimePause();
        playShotclock();

        getTimeRunnable();
        timeHanlder.postDelayed(timeRunnable, RUNNABLE_DELAY);
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
        timeMinutes = minute;
        timeSeconds = second;
        homeView.displayTime(timeMinutes, timeSeconds);
    }

    @Override
    public void pauseShotclock() {
        homeView.displayShotclockPlay();
        shotclockHandler.removeCallbacks(shotclockRunnable);
    }

    @Override
    public void restartShotclock() {
        shotclockSeconds = DEFAULT_SHOT_CLOCK_SECONDS;
        if (!hasEnoughTime(shotclockSeconds)) {

            homeView.enableShotclockTime();
            homeView.displayShotclock(shotclockSeconds);
            return;
        }

        homeView.displayShotclock(0);
        homeView.disableShotclockTime();

    }

    @Override
    public void pauseTime() {
        pauseShotclock();
        homeView.displayTimePlay();
        if (shotclockSeconds == 0) {
            shotclockSeconds = DEFAULT_SHOT_CLOCK_SECONDS;
            homeView.displayShotclock(shotclockSeconds);
        }
        timeHanlder.removeCallbacks(timeRunnable);

    }

    /**
     * Perform logical operation for game time ticks
     **/
    private void getTimeRunnable() {

        timeRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeSeconds > 1) {
                    timeSeconds--;
                    homeView.displayTime(timeMinutes, timeSeconds);
                    if (hasEnoughTime(shotclockSeconds)) {
                        homeView.displayShotclock(0);
                        homeView.disableShotclockTime();
                        shotclockHandler.removeCallbacks(shotclockRunnable);
                    }
                    timeHanlder.postDelayed(this, RUNNABLE_DELAY);
                    return;
                }

                if (timeMinutes > 0) {
                    timeMinutes--;
                    timeSeconds = 59;
                    homeView.displayTime(timeMinutes, timeSeconds);
                    timeHanlder.postDelayed(this, 1000);
                    return;
                }
                ScoreBoardActivity.BUZZER_MP.start();
                timeSeconds = 0;
                homeView.displayTime(timeMinutes, timeSeconds);
                pauseTime();
                pauseShotclock();
                homeView.displayTimePlay();
            }
        };
    }

    /****   UTILITY SECTION    ****/

    /**
     * Perform logical operation for shot clock ticks
     **/
    private void getShotClockRunnable() {
        shotclockRunnable = new Runnable() {
            @Override
            public void run() {
                if (shotclockSeconds > 1) {
                    shotclockSeconds--;
                    homeView.displayShotclock(shotclockSeconds);
                    shotclockHandler.postDelayed(this, RUNNABLE_DELAY);
                    return;
                }
                timeSeconds--;
                if (timeSeconds < 0) {
                    timeMinutes--;
                    timeSeconds = 59;
                }
                ScoreBoardActivity.BUZZER_MP.start();
                shotclockSeconds = DEFAULT_SHOT_CLOCK_SECONDS;
                homeView.displayShotclock(shotclockSeconds);
                homeView.displayTime(timeMinutes, timeSeconds);
                pauseTime();
                homeView.displayShotclockPlay();
                shotclockHandler.removeCallbacks(shotclockRunnable);
            }

        };
    }


    /**
     * Checks if there is enough time for shot clock
     **/
    public boolean hasEnoughTime(int shotclock) {
        if (timeSeconds - shotclock <= 0 && timeMinutes <= 0) {
            return true;
        }
        return false;
    }
}
/**
 * DISABLED FEATURE SECTIONS
 *
 * @Override public void guestAddPoints() {
 * guestPoints++;
 * homeView.displayGuestPoints(guestPoints);
 * //guestRef.setValue(guestPoints);
 * }
 * @Override public void guestAddFoul() {
 * guestFoul++;
 * homeView.displayGuestFoul(guestFoul);
 * }
 * @Override public void guestMinusPoints() {
 * if (guestPoints > 0) {
 * guestPoints--;
 * homeView.displayGuestPoints(guestPoints);
 * }
 * }
 * @Override public void guestMinusFoul() {
 * if (guestFoul > 0) {
 * guestFoul--;
 * homeView.displayGuestFoul(guestFoul);
 * }
 * }
 * @Override public void guestTimeoutAdd() {
 * guestTimeout++;
 * homeView.displayGuestTimeout(guestTimeout);
 * }
 * @Override public void guestTimeoutMinus() {
 * if (guestTimeout > 0) {
 * guestTimeout--;
 * homeView.displayGuestTimeout(guestTimeout);
 * }
 * }
 * @Override public void homeAddPoints() {
 * homePoints++;
 * homeView.displayHomePoints(homePoints);
 * }
 * @Override public void homeAddFoul() {
 * homeFoul++;
 * homeView.displayHomeFoul(homeFoul);
 * }
 * @Override public void homeMinusPoints() {
 * if (homePoints > 0) {
 * homePoints--;
 * homeView.displayHomePoints(homePoints);
 * }
 * }
 * @Override public void homeMinusFoul() {
 * if (homeFoul > 0) {
 * homeFoul--;
 * homeView.displayHomeFoul(homeFoul);
 * }
 * }
 * @Override public void homeTimeoutAdd() {
 * homeTimeout++;
 * homeView.displayHomeTimeout(homeTimeout);
 * }
 * @Override public void homeTimeoutMinus() {
 * if (homeTimeout > 0) {
 * homeTimeout--;
 * homeView.displayHomeTimeout(homeTimeout);
 * }
 * }
 * * /
