package com.ytowka.timer.Set;

import com.ytowka.timer.Action.Action;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Set {
    private ArrayList<Action> actions;
    private int launches;
    private String name;

    public Set(String name){
        actions = new ArrayList<>();
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
    public int getLaunches() {
        return launches;
    }
    public void launch(){
        launches++;
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
}
