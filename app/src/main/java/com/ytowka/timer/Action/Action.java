package com.ytowka.timer.Action;

import com.ytowka.timer.Action.ActionType.ActionType;

import java.util.concurrent.TimeUnit;

public class Action {
    private int timeSeconds;
    private boolean timed;
    private ActionType actionType;


    public Action(int timeSeconds, String name, boolean timed,int color){
        this.timeSeconds = timeSeconds;
        this.timed = timed;
        actionType = new ActionType(name, color);
    }
    public Action(int timeSeconds, boolean timed, ActionType actionType) {
        this.timeSeconds = timeSeconds;
        this.timed = timed;
        this.actionType = actionType;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }
    public String getName() {
        return actionType.getName();
    }
    public boolean isTimed() {
        return timed;
    }
    public int getColor(){
        return actionType.getColor();
    }
    public String getTime(){
        int minutes =       (int) TimeUnit.SECONDS.toMinutes(timeSeconds);
        timeSeconds = timeSeconds - (int)TimeUnit.MINUTES.toSeconds(minutes);


        if(minutes == 0){
            return String.valueOf(timeSeconds);
        }else{
            String minutesS = String.valueOf(minutes);
            String secondsS = String.valueOf(timeSeconds);
            if(minutes<10) minutesS = "0"+minutesS;
            if(timeSeconds<10) secondsS = "0"+secondsS;
            return minutesS+":"+secondsS;
        }
    }

}
