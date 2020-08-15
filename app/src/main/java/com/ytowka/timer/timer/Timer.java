package com.ytowka.timer.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Timer {
    public static final int INTERVAL = 5;

    private TextView textView;
    private CountDownTimer timer;
    private TimerCallback callback;

    private long millsToSecondStep;
    private int timeSecondsLeft;

    private int timeSecondsTotal;
    private long timeMillsLeft;

    private boolean active;

    public long timeMillsTotal(){return  timeSecondsTotal*1000;}
    public long timeMillsPassed(){return timeMillsTotal()-timeMillsLeft;}

    public Timer(TextView textView, TimerCallback callback,int timeSeconds) {
        this.textView = textView;
        textView.setText(getTime(timeSeconds));
        this.callback = callback;
        this.timeSecondsTotal = timeSeconds;
        this.timeMillsLeft = timeSeconds*1000;
        millsToSecondStep = timeSeconds*1000;
    }
    public void setTextColor(int color){
        textView.setTextColor(color);
    }
    public void restart(){
        pause();
        timeMillsLeft = timeMillsTotal();
        millsToSecondStep = timeMillsTotal();
        textView.setText(getTime(timeSecondsTotal));

    }
    public void start(){
        timer = new CountDownTimer(timeMillsLeft,INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                onUpdate(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                Timer.this.onFinish();
            }

        };
        timer.start();
        active = true;
    }
    public void pause(){
        if(active){
            active = false;
            timer.cancel();
        }
    }
    public boolean isActive(){
        return  active;
    }
    public int getTimeSecondsLeft() {
        return timeSecondsLeft;
    }
    private void onUpdate(long millisUntilFinished){
        timeMillsLeft = millisUntilFinished;
        if(timeMillsLeft<millsToSecondStep){
            timeSecondsLeft = (int)millsToSecondStep/1000;
            textView.setText(getTime(timeSecondsLeft));
            callback.onUpdateSecondTimer(timeSecondsLeft);
            millsToSecondStep-=1000;
        }
        callback.onUpdateTimer((int)timeMillsPassed(),(int)timeMillsLeft);
    }
    private void onFinish(){
        callback.onFinishTimer();
    }
    public int getProgress(int max){
        float multiply = (float)max/(float)timeMillsTotal();
        return (int)(timeMillsPassed()*multiply);
    }
    public static String getTime(int timeSeconds){
        int minutes =       (int) TimeUnit.SECONDS.toMinutes(timeSeconds);
        int Seconds = timeSeconds - (int)TimeUnit.MINUTES.toSeconds(minutes);

        if(minutes == 0){
            return String.valueOf(timeSeconds);
        }else{
            String minutesS = String.valueOf(minutes);
            String secondsS = String.valueOf(Seconds);
            if(minutes<10) minutesS = "0"+minutesS;
            if(Seconds<10) secondsS = "0"+secondsS;
            return minutesS+":"+secondsS;
        }
    }
}
