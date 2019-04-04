package com.aktivo.durationheartModel;

/**
 * Created by techiestown on 1/1/18.
 */

public class ProgressItem {
    public int color;
    public float progressItemPercentage;
    public String text;
    public float heart_value;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getProgressItemPercentage() {
        return progressItemPercentage;
    }

    public void setProgressItemPercentage(float progressItemPercentage) {
        this.progressItemPercentage = progressItemPercentage;
    }

    public float getHeart_value() {
        return heart_value;
    }

    public void setHeart_value(float heart_value) {
        this.heart_value = heart_value;
    }
}
