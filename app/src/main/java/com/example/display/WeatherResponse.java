package com.example.display;
import java.util.List;

public class WeatherResponse {
    public String type;
    public Geometry geometry;
    public Properties properties;

    public static class Geometry {
        public String type;
        public List<Double> coordinates;
    }

    public static class Properties {
        public Meta meta;
        public List<TimeSeries> timeseries;
    }

    public static class Meta {
        public String updated_at;
        public Units units;
    }

    public static class Units {
        public String air_pressure_at_sea_level;
        public String air_temperature;
        public String cloud_area_fraction;
        public String precipitation_amount;
        public String relative_humidity;
        public String wind_from_direction;
        public String wind_speed;
    }

    public static class TimeSeries {
        public String time;
        public Data data;
    }

    public static class Data {
        public Instant instant;
        public NextHours next_1_hours;
        public NextHours next_6_hours;
        public NextHours next_12_hours;
    }

    public static class Instant {
        public Details details;
    }

    public static class NextHours {
        public Summary summary;
        public Details details;
    }

    public static class Summary {
        public String symbol_code;
    }

    public static class Details {
        public double air_pressure_at_sea_level;
        public double air_temperature;
        public double cloud_area_fraction;
        public double relative_humidity;
        public double wind_from_direction;
        public double wind_speed;
        public double precipitation_amount;
    }
}
