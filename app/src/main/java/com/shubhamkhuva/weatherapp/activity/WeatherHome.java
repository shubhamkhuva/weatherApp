package com.shubhamkhuva.weatherapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.shubhamkhuva.weatherapp.R;
import com.shubhamkhuva.weatherapp.adapter.ForecastAdapter;
import com.shubhamkhuva.weatherapp.architect.WeatherContract;
import com.shubhamkhuva.weatherapp.architect.WeatherModelImpl;
import com.shubhamkhuva.weatherapp.architect.WeatherPresenterImpl;
import com.shubhamkhuva.weatherapp.modal.ForecastModel;
import com.shubhamkhuva.weatherapp.sqllite.DBManager;
import com.shubhamkhuva.weatherapp.utils.CommonKey;
import com.shubhamkhuva.weatherapp.utils.CommonFunc;
import org.apache.commons.text.WordUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherHome extends Activity implements WeatherContract.View {
    private WeatherContract.Model weatherModel;
    private WeatherContract.Presenter weatherPresenter;
    private DBManager dbManager;
    private WeatherHome activity;
    SharedPreferences session_data;

    @BindView(R.id.mainContent) ConstraintLayout mainContent;
    @BindView(R.id.weather_loading) ProgressBar weatherLoading;
    @BindView(R.id.pullToRefresh) SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.city_name) TextView cityName;
    @BindView(R.id.weather_current_desc) TextView tempDesc;
    @BindView(R.id.weather_today_min) TextView tempMin;
    @BindView(R.id.weather_today_max) TextView tempMax;
    @BindView(R.id.weather_current_value) TextView temperature;
    @BindView(R.id.weather_current_icon) ImageView temperatureIcon;
    @BindView(R.id.weather_forecast) RecyclerView forecastRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_weather_home);
        ButterKnife.bind(activity);
        weatherModel = new WeatherModelImpl(activity);
        weatherPresenter = new WeatherPresenterImpl(activity, weatherModel, Schedulers.io(), AndroidSchedulers.mainThread());
        weatherPresenter.initView();

    }
    @OnClick(R.id.city_enter) void cityEnter() {
        Intent intent = new Intent(this, WeatherCity.class);
        startActivity(intent);
    }

    @Override
    public void onInitView() {
        dbManager = new DBManager(this);
        dbManager.open();
        session_data = CommonFunc.getMembersSharedPrefString(getApplicationContext());
        callWeatherReport();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                callWeatherReport();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void callWeatherReport() {

        showLoading();
        String city = session_data.getString(CommonKey.SELECTED_CITY_KEY, "");
        if(CommonFunc.isNetworkConnected(getApplicationContext())) {
            weatherPresenter.getWeatherData(city);
            weatherPresenter.getForecastData(city);
            callTimer();
        }else{
            if(dbManager.checkIfMyTitleExists()) {
                HashMap<String, String> temp = dbManager.fetchWeather();
                if(temp.get("city").equals(city)) {
                    hideLoading();
                    temperature.setText(temp.get("temp"));
                    tempMin.setText(temp.get("tempMin"));
                    tempMax.setText(temp.get("tempMax"));
                    tempDesc.setText(temp.get("tempText"));

                    byte[] imageByteArray = Base64.decode(temp.get("tempIcon"), Base64.DEFAULT);
                    Glide.with(getApplicationContext())
                            .load(imageByteArray)
                            .into(temperatureIcon);

                    List<ForecastModel> forecastData = new ArrayList<>();
                    forecastData.add(new ForecastModel(temp.get("day1Day"), "", temp.get("day1Value")));
                    forecastData.add(new ForecastModel(temp.get("day2Day"), "", temp.get("day2Value")));
                    forecastData.add(new ForecastModel(temp.get("day3Day"), "", temp.get("day3Value")));
                    forecastData.add(new ForecastModel(temp.get("day4Day"), "", temp.get("day4Value")));
                    forecastData.add(new ForecastModel(temp.get("day5Day"), "", temp.get("day5Value")));
                    showForeCastData(forecastData);
                    callTimer();
                }else{
                    handleErrorView("Oops! You are not connected with internet.");
                }
            }else{
                handleErrorView("Oops! You are not connected with internet.");
            }
        }
    }

    private void callTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                callWeatherReport();
                handler.postDelayed(this, 300000); //now is every 2 minutes
            }
        }, 300000);
    }


    @Override
    public void setTemperature(String city, double temperatureValue,double minTemp, double maxTemp, String desc, String icon) {
        hideLoading();

        int tempValue = (int)temperatureValue;
        temperature.setText(tempValue +CommonKey.DEGREE);
        int minValue = (int)minTemp;
        tempMin.setText(minValue +CommonKey.DEGREE);
        int maxValue = (int)maxTemp;
        tempMax.setText(maxValue +CommonKey.DEGREE);

        tempDesc.setText(WordUtils.capitalize(desc));
        cityName.setText(city);

        weatherPresenter.storeValueinDB(city,temperature.getText().toString(),tempMin.getText().toString(),tempMax.getText().toString(),tempDesc.getText().toString(),icon,temperatureIcon,this);

    }

    @Override
    public void showLoading() {
        mainContent.setVisibility(View.GONE);
        weatherLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mainContent.setVisibility(View.VISIBLE);
        weatherLoading.setVisibility(View.GONE);
    }

    @Override
    public void showForeCastData(List<ForecastModel> list) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Collections.reverse(list);
        forecastRecycle.setLayoutManager(mLayoutManager);
        forecastRecycle.setAdapter(new ForecastAdapter(list,getApplicationContext()));
        forecastRecycle.getAdapter().notifyDataSetChanged();
        forecastRecycle.scheduleLayoutAnimation();
    }

    @Override
    public void storeForecast(int i,String day, String icon, String minmax) {
        if(i==1)
            dbManager.updateForecastDay1(day,minmax);
        if(i==2)
            dbManager.updateForecastDay2(day,minmax);
        if(i==3)
            dbManager.updateForecastDay3(day,minmax);
        if(i==4)
            dbManager.updateForecastDay4(day,minmax);
        if(i==5)
            dbManager.updateForecastDay5(day,minmax);
    }

    @Override
    public void handleErrorView(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        cityEnter();
                    }
                })
                .show();
    }

}
