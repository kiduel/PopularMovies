package com.example.android.popular_movie.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.popular_movie.BuildConfig;

public class Utility {
    //API Key
    public static String API_KEY = BuildConfig.API_KEY;
    // We are going to calculate the number of columns depending on the screen
    // The number of columns will be adjested based on the screen (including if its horizontal or vertical)
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
