package com.ytowka.timer.timer;

public interface TimerCallback {
    public void onFinishTimer();
    public void onUpdateTimer(int timeMillsPassed,int timeMillsLeft);
    public void onUpdateSecondTimer(int secondsLeft);
}
