package com.example.weather;

import com.example.weather.Models.WeatherModels;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {
    static String city,status,icon,last_update,winddir;
    static double temp,wind;
    static int humidity;

    public static WeatherModels utils(String api) throws Exception{
        URL url = createURL(api);
        String json = makeHttpRequest(url);
        WeatherModels weatherModels = extractDataFromJson(json);
        return weatherModels;
    }
    public static URL createURL(String api) throws MalformedURLException {
        URL url = new URL(api);
        return url;
    }
    public static String makeHttpRequest(URL url) throws IOException {
        String response = "";
        if(url == null){
            return response;
        }
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        if(urlConnection.getResponseCode() == 200){
            inputStream = urlConnection.getInputStream();
            response = readFromStream(inputStream);
        }
        urlConnection.disconnect();
        inputStream.close();
        return response;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream == null){
            return "";
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }
    public static WeatherModels extractDataFromJson(String response) throws JSONException {
        if(response.isEmpty()){
            return null;
        }
        JSONObject root = new JSONObject(response);
        city = root.getJSONObject("location").getString("name");
        JSONObject current = root.getJSONObject("current");
        status = current.getJSONObject("condition").getString("text");
        icon = current.getJSONObject("condition").getString("icon");
        last_update = current.getString("last_updated");
        wind = current.getDouble("wind_kph");
        winddir = current.getString("wind_dir");
        temp = current.getDouble("temp_c");
        humidity = current.getInt("humidity");

        WeatherModels weatherModels = new WeatherModels(city, status, icon, last_update, winddir, temp, wind, humidity);
        return weatherModels;
    }
}
