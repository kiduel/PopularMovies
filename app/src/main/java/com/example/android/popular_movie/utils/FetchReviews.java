package com.example.android.popular_movie.utils;

import android.os.AsyncTask;

import com.example.android.popular_movie.model.MovieReview;

import java.util.ArrayList;

public class FetchReviews extends AsyncTask <String, Void, ArrayList <MovieReview>> {
    private String to_be_fetched;
    private OnTaskCompletedReview taskCompletedReview;
    private ArrayList<MovieReview> reviews_list =  new ArrayList<>();

    public FetchReviews(OnTaskCompletedReview taskCompletedReview, String movie_reviews_url){
        this.taskCompletedReview = taskCompletedReview;
        this.to_be_fetched = movie_reviews_url;
    }

    @Override
    protected ArrayList<MovieReview> doInBackground(String... strings) {
        reviews_list = JsonUtils.fetchTheMovieReviews(to_be_fetched);
        return reviews_list;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
        taskCompletedReview.OnTaskCompletedReview(movieReviews);
        super.onPostExecute(movieReviews);
    }
}
