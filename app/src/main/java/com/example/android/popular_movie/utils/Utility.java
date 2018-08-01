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
    public static String preparePoster() {
        String base_url = "http://image.tmdb.org/t/p/";
        String size = "w185/";
        return base_url + size;
    }

    /*
    Prepare the url String for Popular movies
     */
    public static String prepareStringPopular() {
        String base_url = "https://api.themoviedb.org/3/movie/popular?api_key=";
        return base_url + Utility.API_KEY;
    }

    public static String prepareStringTopRated() {
        String base_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        return base_url + Utility.API_KEY;
    }

    /*
   Prepare the url String for Popular movies
    */
    public static String prepareStringMovieReviews(int id) {
        String base_url = "http://api.themoviedb.org/3/movie/";
        String url_with_id = base_url + id;
        String base_url_second = "/reviews?api_key=";
        return url_with_id + base_url_second + Utility.API_KEY;
    }
}
