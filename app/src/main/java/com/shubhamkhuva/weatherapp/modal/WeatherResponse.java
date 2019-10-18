package com.shubhamkhuva.weatherapp.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    @SerializedName("main")
    @Expose
    private WeatherMain main;

    @SerializedName("weather")
    @Expose
    private List<WeatherInfo> weather;

    @SerializedName("name")
    @Expose
    private String name;

    public WeatherMain getMain() {
        return main;
    }
    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public List<WeatherInfo> getWeatherInfo() {
        return weather;
    }
    public void setWeatherinfo(List<WeatherInfo> weatherInfo) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
