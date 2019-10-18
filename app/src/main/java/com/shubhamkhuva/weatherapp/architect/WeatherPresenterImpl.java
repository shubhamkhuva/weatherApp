package com.shubhamkhuva.weatherapp.architect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.shubhamkhuva.weatherapp.modal.ForecastList;
import com.shubhamkhuva.weatherapp.modal.ForecastModel;
import com.shubhamkhuva.weatherapp.modal.ForecastResponse;
import com.shubhamkhuva.weatherapp.modal.WeatherInfo;
import com.shubhamkhuva.weatherapp.modal.WeatherResponse;
import com.shubhamkhuva.weatherapp.sqllite.DBManager;
import com.shubhamkhuva.weatherapp.utils.CommonKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public class WeatherPresenterImpl implements WeatherContract.Presenter{
    private WeatherContract.View weatherView;
    private WeatherContract.Model weatherModel;
    private Scheduler processThread;
    private Scheduler mainThread;
    private CompositeDisposable compositeDisposable;
    private DBManager dbManager;

    public WeatherPresenterImpl(WeatherContract.View weatherView, WeatherContract.Model weatherModel
            , Scheduler processThread, Scheduler mainThread) {
        this.weatherView = weatherView;
        this.weatherModel = weatherModel;
        this.processThread = processThread;
        this.mainThread = mainThread;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void initView() {
        weatherView.onInitView();
    }

    @Override
    public void getWeatherData(String cityName) {
        compositeDisposable.add(weatherModel.
            initiateWeatherInfoCall(cityName)
            .subscribeOn(processThread)
            .observeOn(mainThread)
            .subscribeWith(new DisposableObserver<WeatherResponse>() {
                @Override
                public void onNext(WeatherResponse weatherResponse) {
                    handleWeatherResponse(weatherResponse);
                }

                @Override
                public void onError(Throwable e) {
                    weatherView.handleErrorView("Oops! Look like city is incorrect.");
                }

                @Override
                public void onComplete() {

                }
            }));
    }

    @Override
    public void getForecastData(String cityName) {
        compositeDisposable.add(weatherModel.
                initiateForecastInfoCall(cityName)
                .subscribeOn(processThread)
                .observeOn(mainThread)
                .subscribeWith(new DisposableObserver<ForecastResponse>() {
                    @Override
                    public void onNext(ForecastResponse forecastResponse) {
                        handleForecastResponse(forecastResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void handleWeatherResponse(WeatherResponse weatherResponse) {
        try {
            double tempC = weatherResponse.getMain().getTemp() - CommonKey.CEL_DEG;
            double minTemp = weatherResponse.getMain().getTemp_min() - CommonKey.CEL_DEG;
            double maxTemp = weatherResponse.getMain().getTemp_max() - CommonKey.CEL_DEG;
            List<WeatherInfo> weatherInfos = weatherResponse.getWeatherInfo();
            String description = weatherInfos.get(0).getDescription();
            String icon = weatherInfos.get(0).getIcon();

            weatherView.setTemperature(weatherResponse.getName(), tempC,minTemp,maxTemp,description,icon);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handleForecastResponse(ForecastResponse forecastResponse) {
        List<ForecastList> list = forecastResponse.list;
        List<ForecastModel> forecastData = new ArrayList<>();
        HashMap<Integer, Integer> included = new HashMap<>();

        for (ForecastList forecastday : list) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(CommonKey.INPUT_DATE_FORMAT);
                Date date = sdf.parse(forecastday.getDt());

                Calendar now = Calendar.getInstance();
                if(now.get(Calendar.DATE)==date.getDate()){
                    continue;
                }
                if(included.size()>5){
                    break;
                }
                String day = weatherModel.getDayfromDate(date.getDay());
                if(included.containsValue(date.getDay())) {
                    continue;
                }
                included.put(included.size(), date.getDay());
                double minTemp = forecastday.getMain().getTemp_min() - CommonKey.CEL_DEG;
                double maxTemp = forecastday.getMain().getTemp_max() - CommonKey.CEL_DEG;
                int minValue = (int)minTemp;
                int maxValue = (int)maxTemp;
                weatherView.storeForecast(included.size(),day,forecastday.getWeather().get(0).getIcon(),maxValue +CommonKey.DEGREE+"/"+minValue +CommonKey.DEGREE);
                forecastData.add(new ForecastModel(day,forecastday.getWeather().get(0).getIcon(),maxValue +CommonKey.DEGREE+"/"+minValue +CommonKey.DEGREE));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        if (forecastData.size() > 0) {
            weatherView.showForeCastData(forecastData);
        }
    }

    @Override
    public void storeValueinDB(String city,String temp, String tempMin, String tempMax, String tempText, String icon, ImageView imageview, Activity activity) {
        dbManager = new DBManager(activity);
        dbManager.open();
        String iconUri = "http://openweathermap.org/img/w/"+icon+".png";
        Glide.with(activity).load(iconUri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInByte = baos.toByteArray();

                    if(!dbManager.checkIfMyTitleExists()) {
                        dbManager.insertWeather(city,temp,tempMin,tempMax,tempText,imageInByte);
                    }else{
                        dbManager.updateWeather(city,temp,tempMin,tempMax,tempText,imageInByte);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

        }).into(imageview);

    }
}
