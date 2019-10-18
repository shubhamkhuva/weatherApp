package com.shubhamkhuva.weatherapp.modal;

import android.graphics.drawable.Drawable;

public class ForecastModel {
    private String day;
    private String conditionIcon;
    private String minMaxTemp;

    public ForecastModel(String day, String conditionIcon, String minMaxTemp) {
        this.day = day;
        this.conditionIcon = conditionIcon;
        this.minMaxTemp = minMaxTemp;
    }

    public String getDay() {
        return day;
    }

    public String getIcon() {
        return conditionIcon;
    }

    public String getMinMaxTemp() {
        return minMaxTemp;
    }
}
