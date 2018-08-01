package com.example.android.popular_movie.utils;

import android.text.TextUtils;

import com.example.android.popular_movie.model.Movie;
import com.example.android.popular_movie.model.MovieReview;

import org.json.JSONArray;
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
import java.util.ArrayList;

import timber.log.Timber;

public class JsonUtils {
    private static final String TAG = "JSON_TAG" ;
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_RATING = "vote_average";
    private static final String KEY_POSTER = "poster_path";
    private static final String MOVIE_ID = "id";

    private static final String KEY_REVIEWER = "author";
    private static final String KEY_REVIEW = "content";
    private static final String KEY_REVIEW_ID = "id";



    //the fetch do all the work and return ArrayList of Movies
    public static ArrayList<Movie> fetchTheMovies(String requestURL) {
        Timber.i("fetchTheMovies: fetch is running ");
        //Create url object from the string
        URL url = createURL(requestURL);

        //jsonResponse is the Json response from the string
        //the long json string is the jsonString
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Timber.i("Error closing input stream");
        }

      // we pass the long String and then will get the extracted JSONArray list.
        return extractFeatureFromJson(jsonResponse);
    }


    private static ArrayList<Movie> extractFeatureFromJson(String moviesJSON) {
        Timber.i("extractFeatureFromJson: extract has begun");
        if (TextUtils.isEmpty(moviesJSON)) {
            Timber.i("is Empty");
            return null;
        }

        ArrayList<Movie> movieArrayList = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(moviesJSON);                   //getting a JSON reader object
            JSONArray json_movie_names = reader.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < json_movie_names.length(); i ++) {
                JSONObject jsonObject = json_movie_names.getJSONObject(i); //This cannot be 0, this will only check the first element of the array
                String title = jsonObject.getString(KEY_TITLE);
                String overview = jsonObject.getString(KEY_OVERVIEW);
                String release_date = jsonObject.getString(KEY_RELEASE_DATE);
                String rating = jsonObject.getString(KEY_RATING);
                String poster = jsonObject.getString(KEY_POSTER);
                String movie_id = jsonObject.getString(MOVIE_ID);


                double rating_double = Double.parseDouble(rating);
                int movie_id_int = Integer.parseInt(movie_id);


                // Create a new {@link Movie} object with the title, overview, release_date and the rating
                // and url from the JSON response.
                Movie movies = new Movie(title, poster,  overview, release_date, rating_double, movie_id_int);

                // Add the new {@link Earthquake} to the list of earthquakes.
                movieArrayList.add(movies);
            }

    } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieArrayList;
    }


    /*
    This method will create a url from the string we pass to it.
     */
    private static URL createURL (String requestURL) {
        Timber.i("URL: url has been created ");
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Timber.e(e, "Error with creating URL ");
        }
        return url;
    }


    /* This method will make the actual HttpRequest.
     * Make an HTTP request from the given URL and returns the string as the response
     * the json response will be returned, a long text
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            final int READ_TIMEOUT = 10000;
            final int CONNECT_TIMEOUT = 15000;
            final int SUCCESS = 200;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* millisecond*/);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* millisecond*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            Timber.i("makeHttpRequest: " + code);

            /**
             * If the request was successful (response code 200)
             * then read the input stream and parse the response.
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Timber.e("Error in response code");

            }
        } catch (IOException e) {
            Timber.e(e, "Problem downloading the movies");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect(); //disconnect when done
            }
            if (inputStream != null) {
                inputStream.close();   //close
            }
            Timber.i("makeHttpRequest: jsonResponse has been made " + urlConnection.getResponseCode());
            return jsonResponse;
        }
    }

    /**
     * the Stream will just be zeros and ones as it downloaded directly from the server
     * We need to encode that to something we and understand, this will do that for us
     * This will read the given input stream and make it a sensible string
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output; /* String builder are like string but they can be modified internally
         we need this since we dont know how long the string that  is going to be*/
        output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Timber.i("readFromStream: the stream has been converted");
        return output.toString();
    }

/*
The following code is going to used to parse and return the movie reviews
 */



    //the fetch do all the work and return ArrayList of movieReviews
    public static ArrayList<MovieReview> fetchTheMovieReviews(String requestURL) {
        Timber.i("fetchTheMovieReviews: fetch is running ");
        //Create url object from the string
        URL url = createURL(requestURL);

        //jsonResponse is the Json response from the string
        //the long json string is the jsonString
        String jsonResponseforreview = null;
        try {
            jsonResponseforreview = makeHttpRequest(url);
        } catch (IOException e) {
            Timber.i("Error closing input stream");
        }

        // we pass the long String and then will get the extracted JSONArray list.
        return extractFeatureFromJsonReview(jsonResponseforreview);
    }

    private static ArrayList<MovieReview> extractFeatureFromJsonReview(String moviesReviewJSON) {
        Timber.i("extractFeatureFromJson: extract has begun");
        if (TextUtils.isEmpty(moviesReviewJSON)) {
            Timber.i("is Empty");
            return null;
        }

        ArrayList<MovieReview> movieReviewArrayList = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(moviesReviewJSON);                   //getting a JSON reader object
            JSONArray json_movie_reviews = reader.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < json_movie_reviews.length(); i ++) {
                JSONObject jsonObject = json_movie_reviews.getJSONObject(i); //This cannot be 0, this will only check the first element of the array
                String reviewer = jsonObject.getString(KEY_REVIEWER);
                String review = jsonObject.getString(KEY_REVIEW);
                String id = jsonObject.getString(KEY_REVIEW_ID);


                // Create a new {@link Movie} object with the title, overview, release_date and the rating
                // and url from the JSON response.
                MovieReview movieReview = new MovieReview(id, reviewer, review);

                // Add the new {@link Earthquake} to the list of earthquakes.
                movieReviewArrayList.add(movieReview);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieReviewArrayList;
    }







}
