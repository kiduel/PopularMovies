package com.example.android.popular_movie.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popular_movie.model.Movie;

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

public class JsonUtils {
    private static final String TAG = "JSON_TAG" ;
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_RATING = "vote_average";



    //the fetch do all the work and return ArrayList of News
    public static ArrayList<Movie> fetchTheMovies(String requestURL) {
        Log.i("Test", "fetchTheMovies: fetch is running ");
        //Create url object from the string
        URL url = createURL(requestURL);

        //jsonResponse is the Json response from the string
        //the long json string is the jsonString
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Error closing input stream", e);
        }


        // we pass the long String and then will get the extracted JSONArray list.
        return extractFeatureFromJson(jsonResponse);
    }


    private static ArrayList<Movie> extractFeatureFromJson(String moviesJSON) {
        Log.i("test", "extractFeatureFromJson: extract has begun");
        if (TextUtils.isEmpty(moviesJSON)) {
            Log.i("Movies", "is Empty");
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
                double rating_double = Double.parseDouble(rating);

                // Create a new {@link Movie} object with the title, overview, release_date and the rating
                // and url from the JSON response.
                Movie movies = new Movie(title, null,  overview, release_date, rating_double);

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
        Log.i("Test", "URL: url has been created ");
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Log.e("Error", "Error with creating URL ", e);
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
            Log.i(TAG, "makeHttpRequest: " + code);

            /**
             * If the request was successful (response code 200)
             * then read the input stream and parse the response.
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Error", "Error in response code");

            }
        } catch (IOException e) {
            Log.e("Error", "Problem downloading the book", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect(); //disconnect when done
            }
            if (inputStream != null) {
                inputStream.close();   //close
            }
            Log.i("test", "makeHttpRequest: jsonResponse has been made " + urlConnection.getResponseCode());
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
        Log.i("Test", "readFromStream: the stream has been converted");
        return output.toString();
    }

    public static Movie parseAMovieFromJson (String json) throws JSONException {
        //Here is where we are going to parse the details from the Json.
        //Here is the basics of the parsing,
        //This returns the movie, that is parsed

        JSONObject reader = new JSONObject(json);                   //getting a JSON reader object
        JSONArray json_movie_names = reader.getJSONArray(KEY_RESULTS); //Name is has two childeren, main name and also Known As
        JSONObject jsonObject = json_movie_names.getJSONObject(0); //This cannot be 0, this will only check the first element of the array
        String title = jsonObject.getString(KEY_TITLE);
        String overview = jsonObject.getString(KEY_OVERVIEW);
        String release_date = jsonObject.getString(KEY_RELEASE_DATE);
        String rating = jsonObject.getString(KEY_RATING);
        double rating_double = Double.parseDouble(rating);

        Movie movie = new Movie(title, null, overview, release_date, rating_double);
        return movie;
    }
}
