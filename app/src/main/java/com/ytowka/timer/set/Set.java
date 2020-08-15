package com.ytowka.timer.set;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ytowka.timer.action.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Set implements Serializable {
    @Expose
    @SerializedName("actions")
    private ArrayList<Action> actions;
    @Expose
    @SerializedName("name")
    private String name;

    public Set(String name){
        actions = new ArrayList<>();
        this.name = name;
    }
    public Set(){
        actions = new ArrayList<>();
        this.name = "";
    }
    public void setName(String name) {
        this.name = name;
    }
    public void bind(ArrayList<Action> actions,String name){
        this.actions = actions;
        this.name = name;
    }
    public Set(ArrayList<Action> actions, String name) {
        this.actions = actions;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Action> getActions() {
        return actions;
    }
    public String getTime(){
        int seconds = 0;
        for(Action i: actions) seconds += i.getTimeSeconds();
        int minutes =       (int)TimeUnit.SECONDS.toMinutes(seconds);
        seconds = seconds - (int)TimeUnit.MINUTES.toSeconds(minutes);

        if(minutes == 0){
            return String.valueOf(seconds);
        }else{
            String minutesS = String.valueOf(minutes);
            String secondsS = String.valueOf(seconds);
            if(minutes<10) minutesS = "0"+minutesS;
            if(seconds<10) secondsS = "0"+secondsS;
            return minutesS+":"+secondsS;
        }

    }
    public int getTotalTimeSeconds(){
        int time = 0;
        for(Action i : actions){
            time += i.getTimeSeconds();
        }
        return time;
    }
    public int getTotalTimeSeconds(int underIndex){
        int time = 0;
        for(int i = 0; i < underIndex; i++){
            time += actions.get(i).getTimeSeconds();
        }
        //Log.i("debug","under index: "+underIndex+", ");
        return time;
    }
}
