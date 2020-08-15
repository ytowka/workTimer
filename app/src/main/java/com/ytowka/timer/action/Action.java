package com.ytowka.timer.action;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ytowka.timer.R;
import com.ytowka.timer.action.ActionType.ActionType;
import com.ytowka.timer.set.MainActivity;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Action implements Serializable {


    @Expose
    @SerializedName("timeSeconds")
    private int timeSeconds;
    @Expose
    @SerializedName("reps")
    private boolean reps;
    @Expose
    @SerializedName("actionType")
    private ActionType actionType;

    @Expose(serialize = false, deserialize = false)
    private boolean expanded = false;
    @Expose(serialize = false, deserialize = false)
    public static final Action finishAction = new Action(0, MainActivity.res.getString(R.string.finish),true,MainActivity.res.getColor(R.color.finish));

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
