package com.example.display;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.example.display.databinding.ActivityFullscreenBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars()
                );
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                );
            }
        }
    };

    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private final Runnable mHideRunnable = () -> hide();
    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            mContentView = binding.fullscreenContent;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//            WeatherApi.getWeatherData(
//                59.30852649750069,
//                18.02908000662675,
//                weatherResponse -> {
//                    if (weatherResponse != null && !weatherResponse.properties.timeseries.isEmpty()) {
//                        WeatherResponse.Details instantData = weatherResponse
//                            .properties
//                            .timeseries.get(0)
//                            .data
//                            .instant
//                            .details;
//                        WeatherResponse.Units units = weatherResponse.properties.meta.units;
//
//                        TextView fullscreenTextView = findViewById(R.id.fullscreen_content);
//                        fullscreenTextView.setText("hello");
////                        fullscreenTextView.setText(
////                            "air_pressure_at_sea_level: " + instantData.air_pressure_at_sea_level + " " + units.air_pressure_at_sea_level + "\n" +
////                            "air_temperature: " + instantData.air_temperature + " " + units.air_temperature + "\n" +
////                            "cloud_area_fraction: " + instantData.cloud_area_fraction + " " + units.cloud_area_fraction + "\n" +
////                            "relative_humidity: " + instantData.relative_humidity + " " + units.relative_humidity + "\n" +
////                            "wind_from_direction: " + instantData.wind_from_direction + " " + units.wind_from_direction + "\n" +
////                            "wind_speed: " + instantData.wind_speed + " " + units.wind_speed
////                        );
//                    } else {
//                        System.out.println("Failed to retrieve weather data.");
//                    }
//                }
//            );
            LinearLayout temperatureContainer = findViewById(R.id.temperatureContainer);
            temperatureContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Remove the observer to avoid multiple calls
                    temperatureContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int tempHeight = temperatureContainer.getHeight();

                    addTempsToContainer(temperatureContainer, tempHeight);
                    // Now you can use the parentHeight as needed
                }
            });


        } catch (Exception ex) {
            System.err.println("Something went wrong: " + ex);
            ex.printStackTrace();
        }
    }

    private void addTempsToContainer(LinearLayout temperatureContainer, int tempHeight) {

        TextView fullscreenTextView = findViewById(R.id.fullscreen_content);
        fullscreenTextView.setText("hello");



        List<Integer> temperatures = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            temperatures.add(i);
        }
        int textWidgetSize = 60;
        int totalHeight = tempHeight - 4 * textWidgetSize;

        int maxTemp = Integer.MIN_VALUE;
        int minTemp = Integer.MAX_VALUE;
        for (int t: temperatures) {
            maxTemp = Math.max(maxTemp, t);
            minTemp = Math.min(minTemp, t);
        }
        int tempDiff = maxTemp - minTemp;
        for (int temperature : temperatures) {
            LinearLayout wrapperLayout = new LinearLayout(this);
            wrapperLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(this);
            textView.setText(String.valueOf(temperature));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(60);
            textView.setGravity(Gravity.CENTER);

            int strokeWidth = 5;
            int roundRadius = 15;
            int strokeColor = Color.BLUE;
            int fillColor = Color.BLUE;

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(fillColor);
            gd.setCornerRadius(roundRadius);
            gd.setStroke(strokeWidth, strokeColor);
            gd.setAlpha(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                wrapperLayout.setBackground(gd);
            } else {
                wrapperLayout.setBackgroundDrawable(gd);
            }

            wrapperLayout.addView(textView);
            LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(
                250,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );

            float percentage = 1f - ((float)temperature - (float)minTemp) / (float)tempDiff;
            int parentHeight = totalHeight;
            int height = (int) (parentHeight * percentage);

            int height2 = Integer.valueOf(temperature) * 20;
            System.out.println("percentage: " + percentage);
            System.out.println("parentHeight: " + parentHeight);
            System.out.println("height: " + height);
            System.out.println("height2: " + height2);
            System.out.println();
            wrapperParams.setMargins(0, height, 0, 0);
            wrapperLayout.setLayoutParams(wrapperParams);

            temperatureContainer.addView(wrapperLayout);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}