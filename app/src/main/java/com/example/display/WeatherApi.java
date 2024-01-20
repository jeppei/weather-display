package com.example.display;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApi {

    public interface WeatherDataCallback {
        void onWeatherDataReceived(WeatherResponse weatherResponse);
    }

    public static void getWeatherData(double latitude, double longitude, WeatherDataCallback callback) {
        new AsyncTask<Double, Void, WeatherResponse>() {
            @Override
            protected WeatherResponse doInBackground(Double... coordinates) {
                HttpURLConnection connection = null;
                try {
                    String url = "https://api.met.no/weatherapi/locationforecast/2.0/compact";
                    String query = String.format("?lat=%s&lon=%s", latitude, longitude);

                    URL apiUrl = new URL(url + query);

                    connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty(
                        "User-Agent",
                        "WeatherDisplay/0.5 https://github.com/test/test"
                    );

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        return new Gson().fromJson(response.toString(), WeatherResponse.class);
                    } else {
                        System.err.println("Error: HTTP response code - " + responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(WeatherResponse weatherResponse) {
                if (callback != null) {
                    callback.onWeatherDataReceived(weatherResponse);
                }
            }
        }.execute(latitude, longitude);
    }

    private static String getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        } else {
            System.err.println("Error: HTTP response code - " + responseCode);
        }

        return null;
    }
}
