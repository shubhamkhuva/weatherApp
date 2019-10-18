package com.shubhamkhuva.weatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.shubhamkhuva.weatherapp.R;
import com.shubhamkhuva.weatherapp.utils.CommonFunc;
import com.shubhamkhuva.weatherapp.utils.CommonKey;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonFunc.setFullScreen(this);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences session_data = CommonFunc.getMembersSharedPrefString(getApplicationContext());
            String city = session_data.getString(CommonKey.SELECTED_CITY_KEY, "");
            if (city != null) {
                if(!city.equals("")) {
                    Intent intent = new Intent(SplashScreen.this, WeatherHome.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, WeatherCity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, CommonKey.SPLASH_TIME);


    }
}
