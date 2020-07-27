package com.ytowka.timer.Action.ActionType;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
