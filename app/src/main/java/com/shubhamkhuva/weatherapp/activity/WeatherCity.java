package com.shubhamkhuva.weatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.shubhamkhuva.weatherapp.R;
import com.shubhamkhuva.weatherapp.utils.CommonFunc;
import com.shubhamkhuva.weatherapp.utils.CommonKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherCity extends Activity {
    @BindView(R.id.city_name)
    EditText cityName;
    @BindView(R.id.back_button)
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);

        SharedPreferences session_data = CommonFunc.getMembersSharedPrefString(getApplicationContext());
        String city = session_data.getString(CommonKey.SELECTED_CITY_KEY, "");
        if (city != null) {
            if (city.equals("")) {
                backButton.setVisibility(View.INVISIBLE);
            }
        }
    }
    @OnClick(R.id.city_enter) void cityEnter() {
        CommonFunc.setSharedPrefString(getApplicationContext(), CommonKey.SELECTED_CITY_KEY,cityName.getText().toString());
        Intent intent = new Intent(this, WeatherHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @OnClick(R.id.back_button) void backButton() {
        onBackPressed();
    }
}
