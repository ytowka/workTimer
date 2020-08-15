package com.ytowka.timer.action.ActionType;

import android.graphics.Color;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ytowka.timer.R;
import com.ytowka.timer.set.MainActivity;

import java.io.Serializable;

public class ActionType implements Serializable {
    public static final int lightThreshold = 190;

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
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

    public boolean isBgLight(){
        Color color = Color.valueOf(this.color);
        float R = color.red()*255;
        float G = color.green()*255;
        float B = color.blue()*255;
        float avarage = (R + G + B)/3;
        return avarage > lightThreshold;
    }
    public int textColor() {
        return isBgLight() ? MainActivity.res.getColor(R.color.dark_text) : MainActivity.res.getColor(R.color.light_text);
    }
}
