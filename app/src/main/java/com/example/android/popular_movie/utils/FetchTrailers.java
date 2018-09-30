package com.example.android.popular_movie.utils;

import android.os.AsyncTask;

import com.example.android.popular_movie.model.MovieTrailer;

import java.util.ArrayList;

public class FetchTrailers extends AsyncTask<String, Void, ArrayList<MovieTrailer>> {
    private String to_be_fetched;
    private OnTaskCompletedTrailer taskCompletedTrailer;
    private ArrayList<MovieTrailer> rtrailer_list =  new ArrayList<>();

    public FetchTrailers(OnTaskCompletedTrailer taskCompletedTrailer, String movie_trailers_url){
        this.taskCompletedTrailer = taskCompletedTrailer;
        this.to_be_fetched = movie_trailers_url;
    }

    @Override
    protected ArrayList<MovieTrailer> doInBackground(String... strings) {
        rtrailer_list = JsonUtils.fetchTheMovieTrailer(to_be_fetched);
        return rtrailer_list;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
        super.onPostExecute(movieTrailers);
        taskCompletedTrailer.OnTaskCompletedTrailer(movieTrailers);
    }
}
