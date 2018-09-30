package com.example.android.popular_movie;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.popular_movie.adapters.GridAdapter;
import com.example.android.popular_movie.data.MovieContract;
import com.example.android.popular_movie.data.MovieCursorAdapter;
import com.example.android.popular_movie.model.Movie;
import com.example.android.popular_movie.utils.FetchMovies;
import com.example.android.popular_movie.utils.OnTaskCompleted;
import com.example.android.popular_movie.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Main_Activity";
    GridAdapter gridAdapter;
    Boolean isConnected;
    ArrayList <Movie> movies_list = new ArrayList<>();
    private MovieCursorAdapter mAdapter; //for displaying fav movies
    private static final int MOVIE_LOADER_ID = 0;

    public static int mNoOfColumns;

    /*
    We want to import the lists to an SQLDataBase only once,
    This integers will serve for conditional statement.
     */


    @BindView(R.id.rv_movies) RecyclerView rv_grid_movies;
    @BindView(R.id.movie_error)
    TextView movie_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_grid);
        ButterKnife.bind(this);

        Timber.plant(new Timber.DebugTree());
        GridLayoutManager grid_manager;

        mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());


        if (Utility.isConnected(this)){
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        mAdapter = new MovieCursorAdapter(this);}

        //If internet is available
        if (Utility.isConnected(this) ) {
            movie_error.setVisibility(View.GONE);
            // We initialize the loader
            // use a grid layout manager to display the movies
            grid_manager = new GridLayoutManager(this, mNoOfColumns);
            rv_grid_movies.setLayoutManager(grid_manager);

            FetchMovies task = new FetchMovies(MainActivity.this, Utility.prepareStringPopular());
            task.execute();

        } else {
            rv_grid_movies.setVisibility(View.GONE);
            movie_error.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onTaskCompleted(ArrayList<Movie> movies) {
        if (Utility.isConnected(this)) {
            gridAdapter = new GridAdapter(getBaseContext(), movies);
            rv_grid_movies.setAdapter(gridAdapter);
            movies_list = movies;
        }
    }
    
    /*
    Create a the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isConnected(this)) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }  else {rv_grid_movies.setVisibility(View.GONE);
           movie_error.setVisibility(View.VISIBLE);}
    }


    /*
        Execute action when the menu item is clicked
         */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort) {
            if (Utility.isConnected(this)) {
                FetchMovies task = new FetchMovies(MainActivity.this, Utility.prepareStringTopRated());
                task.execute();
                return true;
            }
            else {rv_grid_movies.setVisibility(View.GONE);
                movie_error.setVisibility(View.VISIBLE);}

        }

        if ( id == R.id.popularity ){
            if (Utility.isConnected(this)) {
                FetchMovies task = new FetchMovies(MainActivity.this, Utility.prepareStringPopular());
                task.execute();
                return true;
            }
            else {rv_grid_movies.setVisibility(View.GONE);
                movie_error.setVisibility(View.VISIBLE);}

        }
        if (id == R.id.fav) {
                movie_error.setVisibility(View.GONE);
                rv_grid_movies.setVisibility(View.VISIBLE);
                rv_grid_movies.setAdapter(mAdapter);
                GridLayoutManager grid_manager;
                grid_manager = new GridLayoutManager(this, mNoOfColumns);
                rv_grid_movies.setLayoutManager(grid_manager);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                          null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
