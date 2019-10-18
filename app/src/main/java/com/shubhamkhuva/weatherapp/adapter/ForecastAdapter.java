package com.shubhamkhuva.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shubhamkhuva.weatherapp.R;
import com.shubhamkhuva.weatherapp.modal.ForecastModel;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {
    private List<ForecastModel> forecastData;
    private Context mContext;
    public ForecastAdapter(List<ForecastModel> forecastData, Context mContext) {
        this.forecastData = forecastData;
        this.mContext = mContext;
    }
    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View setMenuView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_forecast,
                parent, false);
        return new ForecastHolder(setMenuView);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        holder.setForecastDataModel(forecastData.get(position),mContext);
    }

    @Override
    public int getItemCount() {
        return forecastData.size();
    }

    static class ForecastHolder extends RecyclerView.ViewHolder {

        private TextView dayNameTV;
        private TextView conditionTextTV;
        private TextView minMaxTempTV;
        private ImageView conditionIconIV;

        private ForecastHolder(View itemView) {
            super(itemView);
            dayNameTV = itemView.findViewById(R.id.day);
            minMaxTempTV = itemView.findViewById(R.id.minmax);
            conditionIconIV = itemView.findViewById(R.id.foricon);
        }

        private void setForecastDataModel(ForecastModel forecastDataModel,Context mContext) {
            dayNameTV.setText(forecastDataModel.getDay());
            minMaxTempTV.setText(forecastDataModel.getMinMaxTemp());
            Glide.with(mContext).load("http://openweathermap.org/img/w/"+forecastDataModel.getIcon()+".png").into(conditionIconIV);
        }
    }
}
