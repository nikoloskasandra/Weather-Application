package com.projects.weather2.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.weather2.Model.Weather;
import com.projects.weather2.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurrentDateForecastAdapter extends RecyclerView.Adapter<CurrentDateForecastAdapter.ViewHolder> {


    private Context             context;
    private ArrayList<Weather>  todaysForecast;

    public CurrentDateForecastAdapter(Context context, ArrayList<Weather> todaysForecast) {
        this.context = context;
        this.todaysForecast = todaysForecast;
    }

    @NonNull
    @Override
    public com.projects.weather2.Adapter.CurrentDateForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.today_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.projects.weather2.Adapter.CurrentDateForecastAdapter.ViewHolder holder, int position) {


        //get the weather on the appropriate position
        Weather weather = todaysForecast.get(position);

        //set the data to a holder:
        //time
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh");

        try{
            Date d = input.parse(weather.getTime());
            holder.timeTv.setText(output.format(d));

        }catch (ParseException e){
            e.printStackTrace();
        }



        //icon
        Picasso.get().load("https:".concat(weather.getWeatherIcon())).into(holder.weatherConditionIcon);

        //temperature in Celsius
        holder.temperatureTv.setText(weather.getTemperatureInCelsius() + "°");





    }

    @Override
    public int getItemCount() {
        return todaysForecast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView    timeTv;
        private TextView    temperatureTv;
        private ImageView   weatherConditionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTv               = itemView.findViewById(R.id.time);
            temperatureTv        = itemView.findViewById(R.id.todaysPerHourTemperature);
            weatherConditionIcon = itemView.findViewById(R.id.weatherIconPerHour);
        }
    }
}
