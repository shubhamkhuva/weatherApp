package com.shubhamkhuva.weatherapp.architect;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import com.shubhamkhuva.weatherapp.R;
import com.shubhamkhuva.weatherapp.modal.ForecastResponse;
import com.shubhamkhuva.weatherapp.modal.WeatherResponse;
import com.shubhamkhuva.weatherapp.network.WeatherAPIClient;
import com.shubhamkhuva.weatherapp.network.WeatherRestService;
import com.shubhamkhuva.weatherapp.utils.CommonKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;

public class WeatherModelImpl implements WeatherContract.Model{
    private Context context;
    private WeatherRestService weatherRestService;

    public WeatherModelImpl(Context context) {
        this.context = context;
        weatherRestService = WeatherAPIClient.getClient().create(WeatherRestService.class);
    }

    @Override
    public Observable<WeatherResponse> initiateWeatherInfoCall(String cityName) {
        return weatherRestService.getWeatherInfo(cityName);
    }

    @Override
    public Observable<ForecastResponse> initiateForecastInfoCall(String cityName) {
        return weatherRestService.getForecastInfo(cityName);
    }

    @Override
    public String getDayfromDate(int inputday) {
        if(inputday==1){
            return "Mon";
        }else if(inputday==2){
            return "Tue";
        }else if(inputday==3){
            return "Wed";
        }else if(inputday==4){
            return "Thu";
        }else if(inputday==5){
            return "Fri";
        }else if(inputday==6){
            return "Sat";
        }else if(inputday==0){
            return "Sun";
        }else{
            return "";
        }
    }
}
