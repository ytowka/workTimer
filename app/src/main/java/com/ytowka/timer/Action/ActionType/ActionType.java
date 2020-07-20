package com.ytowka.timer.Action.ActionType;

import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;
import java.util.ArrayList;

public class ActionType {

    private String name;
    private int color;
    public ActionType(String name, int color) {
        this.name = name;
        this.color = color;
    }
    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
