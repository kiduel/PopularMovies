package com.example.android.popular_movie.utils;

import android.os.AsyncTask;

import com.example.android.popular_movie.model.Movie;

import java.util.ArrayList;

public class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
    private String to_be_fetched;
    private OnTaskCompleted taskCompleted;
    private ArrayList<Movie> movies_list =  new ArrayList<>();


    public FetchMovies(OnTaskCompleted taskCompleted, String movies_url){
        this.taskCompleted = taskCompleted;
        this.to_be_fetched = movies_url;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        movies_list = JsonUtils.fetchTheMovies(to_be_fetched);
        return movies_list;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        taskCompleted.onTaskCompleted(movies);
        super.onPostExecute(movies);
    }
}

