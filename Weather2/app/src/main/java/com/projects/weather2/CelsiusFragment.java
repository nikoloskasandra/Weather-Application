package com.projects.weather2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.projects.weather2.Adapter.CurrentDateForecastAdapter;
import com.projects.weather2.Adapter.NextDateForecastAdapter;
import com.projects.weather2.Model.Weather;
import com.projects.weather2.Model.WeatherForecast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CelsiusFragment extends Fragment {

    View view ;

    private RelativeLayout mainPageRl;

    private TextView locationTv;
    private TextView                    temperatureTv;
    private TextView                    weatherConditionTv;

    private ImageView weatherIconIv;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private int                         PERMISSION = 1;
    private Double                      la, ln;
    private String                      s, e;

    private ArrayList<Weather> todaysWeatherForecastArrayList;
    private CurrentDateForecastAdapter currentDateForecastAdapter;

    private ArrayList<WeatherForecast>  weatherForecastArrayList;
    private NextDateForecastAdapter nextDateForecastAdapter;

    //RecyclerView - today
    private RecyclerView recyclerViewTodayForecast;

    //RecyclerView - next days
    private RecyclerView                getRecyclerViewNextDaysForecast;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_celsius, container, false);

        setHasOptionsMenu(true);

        mainPageRl                      = view.findViewById(R.id.mainPage);

        locationTv                      = view.findViewById(R.id.location);
        temperatureTv                   = view.findViewById(R.id.temperature);
        weatherConditionTv              = view.findViewById(R.id.weatherCondition);

        weatherIconIv                   = view.findViewById(R.id.weatherIcon);

        // current forecast Recycler View
        todaysWeatherForecastArrayList  = new ArrayList<>();
        currentDateForecastAdapter      = new CurrentDateForecastAdapter(getActivity(),todaysWeatherForecastArrayList);

        recyclerViewTodayForecast       = view.findViewById(R.id.todaysforecastRv);

        //for next days forecast Recycler View
        weatherForecastArrayList        = new ArrayList<>();
        nextDateForecastAdapter         = new NextDateForecastAdapter(getActivity(), weatherForecastArrayList);

        getRecyclerViewNextDaysForecast = view.findViewById(R.id.nextDaysForecastRv);

        //FUNCTION: findLocation() -> used to find the current location of the user device
        findLocation();



        return view;
    }

    private void findLocation() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //if the permissions are not granted -> (ask for permissions)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);

        }else {
            //if the permissions are granted
            //CURRENT LOCATION

            CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder()
                    .setGranularity(Granularity.GRANULARITY_FINE)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setDurationMillis(5000)
                    .setMaxUpdateAgeMillis(0)
                    .build();

            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();


            fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, cancellationTokenSource.getToken()).addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    la   = task.getResult().getLatitude();
                    ln   = task.getResult().getLongitude();

                    //geocoder - will return list of addresses (We use Geocoder to get the location name)
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(task.getResult().getLatitude(), task.getResult().getLongitude(), 1);
                        Address address         = addresses.get(0);
                        String location         = address.getLocality();

                        //FUNCTION: bindData(location) -> send request to (API), get response: JSON object -> set needed data to be shown  to the user.
                        bindData(location);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                }

                //FUNCTION: bindData(location) -> send request to (API), get response: JSON object -> set needed data to be shown  to the user.
                private void bindData(String location) {

                    locationTv.setText(location);

                    //TEST API REQUEST   ----   ONLY FOR TESTING ---- HERE IS THE START
                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    String url ="https://api.weatherapi.com/v1/forecast.json?key=dc769b8610274aac857130428231604&q="+location+"&days=6&aqi=yes&alerts=no";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //background
                                        int isDay = response.getJSONObject("current").getInt("is_day");

                                        if(isDay == 1){
                                            mainPageRl.setBackgroundResource(R.drawable.day);
                                        }else {
                                            mainPageRl.setBackgroundResource(R.drawable.night1);
                                        }


                                        //we need clear the array of weatherforecast for today
                                        todaysWeatherForecastArrayList.clear();
                                        weatherForecastArrayList.clear();

                                        //current temperature info

                                        String temperatureC     =  response.getJSONObject("current").getString("temp_c");
                                        String temperatureF     =  response.getJSONObject("current").getString("temp_f");

                                        temperatureTv.setText(temperatureC + "°");

                                        //current weather image
                                        String weatherImg       = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                                        Picasso.get().load("https:".concat(weatherImg)).into(weatherIconIv);

                                        //current weather  condition
                                        String weatherCondition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                                        weatherConditionTv.setText(weatherCondition);

                                        //RECYCLER VIEW -- CURRENT DAY FORECAST
                                        rvCurrentDayForecast(response);

                                        //RECYCLER VIEW -- NEXT DAYS FORECAST
                                        rvNextDaysForecast(response);



                                    } catch (JSONException jsonException) {
                                        jsonException.printStackTrace();
                                    }

                                }


                                private void rvCurrentDayForecast(JSONObject response) throws JSONException {

                                    //we need to get forecast per hour
                                    JSONObject JSONObjectForecast = response.getJSONObject("forecast");
                                    JSONObject JSONObjectForecastCurrentDay = JSONObjectForecast.getJSONArray("forecastday").getJSONObject(0);
                                    JSONArray JSONArrayForcastPerHour = JSONObjectForecastCurrentDay.getJSONArray("hour");


                                    for(int i = 0; i<JSONArrayForcastPerHour.length(); i++){
                                        JSONObject h_obj = JSONArrayForcastPerHour.getJSONObject(i);

                                        String timeHour                 = h_obj.getString("time");
                                        String temperatureCelsius       = h_obj.getString("temp_c");
                                        String temperatureFahrenheit    = h_obj.getString("temp_f");
                                        String weatherIcon              = h_obj.getJSONObject("condition").getString("icon");
                                        String condition                = h_obj.getJSONObject("condition").getString("text");


                                        todaysWeatherForecastArrayList.add(new Weather(timeHour, temperatureCelsius, temperatureFahrenheit, weatherIcon, condition));
                                        recyclerViewTodayForecast.setAdapter(currentDateForecastAdapter);

                                    }

                                    //we should notify that the data for adapter is changed
                                    currentDateForecastAdapter.notifyDataSetChanged();



                                }

                                private void rvNextDaysForecast(JSONObject response) throws JSONException {



                                    JSONObject JSONObjectForecast = response.getJSONObject("forecast");
                                    JSONArray JSONArrayNextDays = JSONObjectForecast.getJSONArray("forecastday");


                                    for(int i=0; i< JSONArrayNextDays.length(); i++) {

                                        WeatherForecast weatherForecast = new WeatherForecast();

                                        JSONObject dayInfo              = JSONArrayNextDays.getJSONObject(i); // one whole info for one day

                                        //day
                                        String timestamp                = dayInfo.getString("date");
                                        //HERE WE WILL NEED TO GET THE DAY "NAME" FROM STRING - DATE --> CHECK AGAIN

                                        SimpleDateFormat inFormat    = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat outFormat   = new SimpleDateFormat("EEEE");

                                        try {
                                            Date date    = inFormat.parse(timestamp);
                                            assert date != null;
                                            String goal  = outFormat.format(date);

                                            //set the day value to the object weatherForecast
                                            weatherForecast.setDay(goal);


                                        }catch (Exception e){
                                            e.getStackTrace();
                                        }

                                        //Temperature -> avg (celsius / fahrenheit)
                                        String c = dayInfo.getJSONObject("day").getString("avgtemp_c");
                                        String f = dayInfo.getJSONObject("day").getString("avgtemp_f");

                                        weatherForecast.setTemperatureInCelsius(c+"°");
                                        weatherForecast.setGetTemperatureInFahrenheit(f);

                                        //weather icon
                                        String icon = dayInfo.getJSONObject("day").getJSONObject("condition").getString("icon");

                                        weatherForecast.setWeatherIcon(icon);

                                        //weather condition
                                        String condition = dayInfo.getJSONObject("day").getJSONObject("condition").getString("text");

                                        weatherForecast.setCondition(condition);

                                        //Add the weatherForecast object to the array list
                                        weatherForecastArrayList.add(weatherForecast);
                                        getRecyclerViewNextDaysForecast.setAdapter(nextDateForecastAdapter);


                                    }
                                    nextDateForecastAdapter.notifyDataSetChanged();


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Toast.makeText(getActivity(), "Something wnt wrong!", Toast.LENGTH_SHORT).show();

                                }
                            });

                    // Access the RequestQueue through your singleton class.
                    queue.add(jsonObjectRequest);

                    //ONLY FOR TESTING --- HERE IS THE END

                }
            });

        }

    }
}