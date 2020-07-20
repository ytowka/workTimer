package com.ytowka.timer.Action.ActionType;

import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionTypeAdapter {
    public static  ArrayList<ActionType> readyActions;
    static{
        readyActions = new ArrayList<>();
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.work),MainActivity.res.getColor(R.color.workColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
    }
}
