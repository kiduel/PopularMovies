package com.example.android.popular_movie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    Prepares the url String for Popular movies
     */
    public static String prepareStringPopular() {
        String base_url = "https://api.themoviedb.org/3/movie/popular?api_key=";
        return base_url + Utility.API_KEY;
    }
    /*
      Prepares the url String for parsing the top rated
       */
    public static String prepareStringTopRated() {
        String base_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        return base_url + Utility.API_KEY;
    }

    /*
   Prepares the url String for parsing the review
    */
    public static String prepareStringMovieReviews(int id) {
        String base_url = "http://api.themoviedb.org/3/movie/";
        String url_with_id = base_url + id;
        String base_url_second = "/reviews?api_key=";
        return url_with_id + base_url_second + Utility.API_KEY;
    }

    /*
  Prepares the url String for parsing the traulers
   */
    public static String prepareStringMovieTrailers(int id) {
        String base_url = "http://api.themoviedb.org/3/movie/";
        String url_with_id = base_url + id;
        String base_url_second = "/videos?api_key=";
        return url_with_id + base_url_second + Utility.API_KEY;
    }

    /*
    Prepares the url String for parsing the traulers
   */
    public static String prepareMovieTrailersImages(String key) {
        String base_url = "http://img.youtube.com/vi/";
        String url_with_id = base_url + key;
        String base_url_second = "/hqdefault.jpg";
        return url_with_id + base_url_second;
    }

    /*
   Prepares the url String for parsing the traulers
  */
    public static String prepareMovieTrailersVideo(String key) {
        String base_url = "http://www.youtube.com/watch/?v=";
        return base_url + key;
    }

    //To check the connection,

    public static Boolean isConnected(Context context){
    ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();
    }
}
