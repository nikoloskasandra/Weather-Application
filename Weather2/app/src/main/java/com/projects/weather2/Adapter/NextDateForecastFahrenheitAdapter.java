package com.projects.weather2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.weather2.Model.WeatherForecast;
import com.projects.weather2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NextDateForecastFahrenheitAdapter extends RecyclerView.Adapter<NextDateForecastFahrenheitAdapter.ViewHolder> {

    public Context                      context;
    public ArrayList<WeatherForecast>   weatherForecastArrayList;


    public NextDateForecastFahrenheitAdapter(Context context, ArrayList<WeatherForecast> weatherForecastArrayList) {
        this.context                    = context;
        this.weatherForecastArrayList   = weatherForecastArrayList;
    }


    @NonNull
    @Override
    public NextDateForecastFahrenheitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.next_day_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NextDateForecastFahrenheitAdapter.ViewHolder holder, int position) {

        WeatherForecast weatherForecast = weatherForecastArrayList.get(position);

        //dataBind (Temp: Celsius)
        holder.dayTv.setText(weatherForecast.getDay());
        holder.temperatureTv.setText(weatherForecast.getGetTemperatureInFahrenheit());
        holder.conditionTv.setText(weatherForecast.getCondition());
        Picasso.get().load("https:".concat(weatherForecast.getWeatherIcon())).into(holder.weatherIv);

    }

    @Override
    public int getItemCount() {
        return weatherForecastArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView    dayTv;
        private TextView    temperatureTv;
        private TextView    conditionTv;
        private ImageView   weatherIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTv           = itemView.findViewById(R.id.day);
            temperatureTv   = itemView.findViewById(R.id.perDayTemperature);
            conditionTv     = itemView.findViewById(R.id.ConditionPerDay);
            weatherIv       = itemView.findViewById(R.id.weatherIconPerDay);


        }
    }
}
