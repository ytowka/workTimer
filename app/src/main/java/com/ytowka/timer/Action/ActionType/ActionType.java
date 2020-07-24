package com.ytowka.timer.Action.ActionType;

import com.google.gson.annotations.SerializedName;
import com.ytowka.timer.Action.Action;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionType implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("color")
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
    public void bind(ActionType at){
        name = at.getName();
        color = at.getColor();
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setColor(int color) {
        this.color = color;
    }
}
