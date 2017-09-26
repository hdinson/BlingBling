package dinson.customview.model;

import dinson.customview.R;

/**
 * google VR 数据模型
 */
public class HomeWeatherModelUtil {

    private static final int[] weatherArr = {
        R.string.if_sunny_0,
        R.string.if_clear_1,
        R.string.if_fair_2,
        R.string.if_fair_3,
        R.string.if_cloudy_4,
        R.string.if_partly_cloudy_5,
        R.string.if_partly_cloudy_6,
        R.string.if_mostly_cloudy_7,
        R.string.if_mostly_cloudy_8,
        R.string.if_overcast_9,
        R.string.if_shower_10,
        R.string.if_thundershower_11,
        R.string.if_thundershower_with_hail_12,
        R.string.if_light_rain_13,
        R.string.if_moderate_rain_14,
        R.string.if_heavy_rain_15,
        R.string.if_storm_16,
        R.string.if_heavy_storm_17,
        R.string.if_severe_storm_18,
        R.string.if_ice_rain_19,
        R.string.if_sleet_20,
        R.string.if_snow_flurry_21,
        R.string.if_light_snow_22,
        R.string.if_moderate_snow_23,
        R.string.if_heavy_snow_24,
        R.string.if_snowstorm_25,
        R.string.if_dust_26,
        R.string.if_sand_27,
        R.string.if_dust_storm_28,
        R.string.if_sand_storm_29,
        R.string.if_foggy_30,
        R.string.if_haze_31,
        R.string.if_windy_32,
        R.string.if_blustery_33,
        R.string.if_hurricane_34,
        R.string.if_tropical_storm_35,
        R.string.if_tornado_36,
        R.string.if_cold_37,
        R.string.if_hot_38,
        R.string.if_unknown_99
    };


    public static int getWeatherFont(int code) {
        return weatherArr[code];
    }
}
