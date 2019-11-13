package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Models.WeatherModels;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView location,temp, status, last_update, wind, wind_dir, humidity;
    ImageView icon;
    EditText searchbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = findViewById(R.id.location);
        temp = findViewById(R.id.temp);
        status = findViewById(R.id.status);
        last_update = findViewById(R.id.last_update);
        wind = findViewById(R.id.wind);
        wind_dir = findViewById(R.id.wind_dir);
        humidity = findViewById(R.id.humidity);
        icon = findViewById(R.id.icon);
        searchbar = findViewById(R.id.search_city);
    }

    public void search(View view) {
        String city = searchbar.getText().toString();
        if (city.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        new Background().execute("http://api.apixu.com/v1/current.json?key=12064d954629488f832185500190407&q="+city);
    }

    public class Background extends AsyncTask<String, Void, WeatherModels>{


        @Override
        protected WeatherModels doInBackground(String... strings) {
            WeatherModels weatherModels = null;
            try {
                weatherModels = Utils.utils(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weatherModels;
        }

        @Override
        protected void onPostExecute(WeatherModels weatherModels) {
            if(weatherModels == null){
                Toast.makeText(getApplicationContext(), "Wrong Name", Toast.LENGTH_SHORT).show();
            }else{
                updataUI(weatherModels);
            }
        }
    }
    public void updataUI(WeatherModels weatherModels){
        location.setText(weatherModels.getCity());
        temp.setText(String.valueOf(weatherModels.getTemp()));
        status.setText(weatherModels.getStatus());
        last_update.setText(weatherModels.getLast_update());
        wind.setText(String.valueOf(weatherModels.getWind()));
        wind_dir.setText(weatherModels.getWinddir());
        humidity.setText(String.valueOf(weatherModels.getHumidity()));

        Picasso.get()
                .load(weatherModels.getIcon())
                .error(R.drawable.ic_cloudy)
                .placeholder(R.drawable.ic_cloudy)
                .into(icon);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
