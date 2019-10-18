package com.shubhamkhuva.weatherapp.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastList {
    @SerializedName("dt_txt")
    @Expose
    private String dt_txt;

    @SerializedName("main")
    @Expose
    private WeatherMain main;

    @SerializedName("weather")
    @Expose
    private List<WeatherInfo> weather;

    public String getDt() { return dt_txt; }
    public void setDt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }
}
