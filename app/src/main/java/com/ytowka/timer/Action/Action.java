package com.ytowka.timer.Action;

import com.google.gson.annotations.SerializedName;
import com.ytowka.timer.Action.ActionType.ActionType;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Action implements Serializable {

    @SerializedName("timeSeconds")
    private int timeSeconds;
    @SerializedName("reps")
    private boolean reps;
    @SerializedName("actionType")
    private ActionType actionType;

    private boolean expanded = false;
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public void collapse(){
        expanded = false;
    }

    public Action(int timeSeconds, String name, boolean timed, int color){
        this.timeSeconds = timeSeconds;
        this.reps = timed;
        actionType = new ActionType(name, color);
    }
    public Action(int timeSeconds, boolean timed, ActionType actionType) {
        this.timeSeconds = timeSeconds;
        this.reps = timed;
        this.actionType = new ActionType(actionType.getName(),actionType.getColor());
    }
    public void bind(Action a){
        timeSeconds = a.getTimeSeconds();
        reps = a.isReps();
        actionType.bind(a.getActionType());
    }
    public int getTimeSeconds() {
        return timeSeconds;
    }
    public String getName() {
        return actionType.getName();
    }
    public boolean isReps() {
        return reps;
    }

    public void setReps(boolean reps) {
        this.reps = reps;
    }
    public int getColor(){
        return actionType.getColor();
    }
    public String getTime(){
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
    public void getT(){
        int minutes;

    }
    public ActionType getActionType() {
        return actionType;
    }
    public void setTimeSeconds(int time){
        this.timeSeconds = time;
    }
}
