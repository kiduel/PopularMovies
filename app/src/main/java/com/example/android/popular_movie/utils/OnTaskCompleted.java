package com.example.android.popular_movie.utils;

import com.example.android.popular_movie.model.Movie;

import java.util.ArrayList;

public interface OnTaskCompleted {
    void onTaskCompleted(ArrayList<Movie> movies);
}
