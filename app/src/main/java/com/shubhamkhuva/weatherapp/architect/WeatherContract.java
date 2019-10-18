package com.shubhamkhuva.weatherapp.architect;

import android.app.Activity;
import android.widget.ImageView;

import com.shubhamkhuva.weatherapp.modal.ForecastModel;
import com.shubhamkhuva.weatherapp.modal.ForecastResponse;
import com.shubhamkhuva.weatherapp.modal.WeatherResponse;

import java.util.List;

import io.reactivex.Observable;

public interface WeatherContract {
    interface View {
        void onInitView();

        void setTemperature(String city, double temperatureValue,double minTemp, double maxTemp, String desc, String icon);

        void showLoading();

        void hideLoading();

        void showForeCastData(List<ForecastModel> list);

        void storeForecast(int i,String day, String icon, String minmax);

        void handleErrorView(String msg);
    }
    interface Presenter {
        void initView();

        void getWeatherData(String cityName);

        void getForecastData(String cityName);

        void handleWeatherResponse(WeatherResponse weatherResponse);

        void handleForecastResponse(ForecastResponse forecastResponse);

        void storeValueinDB(String city,String temp, String tempMin, String tempMax, String tempText, String icon, ImageView imageview, Activity activity);
    }
    interface Model {
        Observable<WeatherResponse> initiateWeatherInfoCall(String cityName);

        Observable<ForecastResponse> initiateForecastInfoCall(String cityName);

        String getDayfromDate(int inputday);
    }
}
