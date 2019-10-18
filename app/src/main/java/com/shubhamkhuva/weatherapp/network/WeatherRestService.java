package com.shubhamkhuva.weatherapp.network;

import com.shubhamkhuva.weatherapp.modal.ForecastResponse;
import com.shubhamkhuva.weatherapp.modal.WeatherResponse;
import com.shubhamkhuva.weatherapp.utils.CommonKey;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRestService {
    @GET("data/2.5/weather?APPID=" + CommonKey.API_KEY)
    Observable<WeatherResponse> getWeatherInfo(@Query("q") String cityName);

    @GET("data/2.5/forecast?APPID=" + CommonKey.API_KEY)
    Observable<ForecastResponse> getForecastInfo(@Query("q") String cityName);
}
