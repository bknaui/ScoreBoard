package com.example.apangcatan.basketballscore.ScoreBoard;

/**
 * Created by apangcatan on 23/03/2018.
 */

public class ScoreBoardContract {
    interface ScoreView {
        void setGuestName(String name);

        void setHomeName(String name);

        void setQuarter(String quarter);

        void displayShotclockPlay();

        void displayShotclockPause();

        void displayTimePlay();

        void displayTimePause();

        void displayShotclock(int seconds);

        void displayTime(String mins, String seconds);

        void displayGuestPoints(int points);

        void displayGuestFoul(int foul);

        void displayGuestTimeout(int timeout);

        void displayHomePoints(int points);

        void displayHomeFoul(int foul);

        void displayHomeTimeout(int timeout);

        void disableShotclockTime();

        void enableShotclockTime();

    }

    interface ScorePresenter {

        void updateQuarter(String quarter);

        void updateGuestName(String name);

        void updateHomeName(String name);

        void setTime(int minute, int second);

        void playShotclock();

        void pauseShotclock();

        void restartShotclock();

        void playTime();

        void pauseTime();

        void guestAddPoints();

        void guestAddFoul();

        void guestMinusPoints();

        void guestMinusFoul();

        void guestTimeoutAdd();

        void guestTimeoutMinus();

        void homeAddPoints();

        void homeAddFoul();

        void homeMinusPoints();

        void homeMinusFoul();

        void homeTimeoutAdd();

        void homeTimeoutMinus();

        void playBuzzer();

        void playTheme();

    }

    interface ScoreRepository {

    }
}
