package com.example.android.popular_movie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popular_movie.adapters.GridAdapter;
import com.example.android.popular_movie.model.Movie;
import com.example.android.popular_movie.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    GridAdapter gridAdapter;
    private ArrayList<Movie> popular_movies;
    @BindView(R.id.rv_movies) RecyclerView rv_grid_movies;
    private int load_id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_grid);
        ButterKnife.bind(this);

        GridLayoutManager grid_manager;


        // We initialize the loader
        // use a grid layout manager to display the movies
        grid_manager = new GridLayoutManager(this, 2);
        rv_grid_movies.setLayoutManager(grid_manager);

        gridAdapter = new GridAdapter(this, popular_movies);
        rv_grid_movies.setAdapter(gridAdapter);
    }
    private static String prepareString() {
        String base_url = "api.themoviedb.org/3/movie/popular?api_key=";
        String api_key = "2300212519d886d20b216d33f77452ee";
        return base_url + api_key;
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            popular_movies = JsonUtils.fetchTheMovies("api.themoviedb.org/3/movie/popular?api_key=2300212519d886d20b216d33f77452ee");
            return popular_movies;
        }
    }
}

