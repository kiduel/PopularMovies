package com.example.android.popular_movie;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.popular_movie.adapters.GridAdapter;
import com.example.android.popular_movie.data.MovieContract.MovieEntry;
import com.example.android.popular_movie.model.Movie;
import com.example.android.popular_movie.utils.FetchMovies;
import com.example.android.popular_movie.utils.OnTaskCompleted;
import com.example.android.popular_movie.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    private static final String TAG = "Main_Activity";
    GridAdapter gridAdapter;
    Boolean isConnected;
    ArrayList <Movie> movies_list = new ArrayList<>();

    public static int NOT_LIKED = 0;
    public static int LIKED = 1;

    /*
    We want to import the lists to an SQLDataBase only once,
    This integers will serve for conditional statement.
     */
    public int import_for_popular = 0;
    public int import_for_top_rated = 1;

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
        //To check the connection, we created isConnected
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        //If internet is available
        if (isConnected) {
            movie_error.setVisibility(View.GONE);
            // We initialize the loader
            // use a grid layout manager to display the movies
            grid_manager = new GridLayoutManager(this, mNoOfColumns);
            rv_grid_movies.setLayoutManager(grid_manager);

            FetchMovies task = new FetchMovies(MainActivity.this, prepareStringPopular());
            task.execute();

        } else {
            rv_grid_movies.setVisibility(View.GONE);
            movie_error.setVisibility(View.VISIBLE);
        }

    }

    /*
    Prepare the url String for Popular movies
     */
    private static String prepareStringPopular() {
        String base_url = "https://api.themoviedb.org/3/movie/popular?api_key=";
        return base_url + Utility.API_KEY;
    }

    /*
    @param = This function will add movies to our sqlDatabse
     */
    public void addMoviesToDatabase (ArrayList<Movie> movies){
        //I want to add the movies to an SQL table
        for (int x = 0; x < movies.size(); x++) {
            Movie movie = movies.get(x);



        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
            contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
            contentValues.put(MovieEntry.COLUMN_TITLE, movie.getOriginal_title());
            contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
            contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
            contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, String.valueOf(movie.getVote_average()));
            contentValues.put(MovieEntry.COLUMN_LIKED, NOT_LIKED);


            // Insert the content values via a ContentResolver
            getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        }
    }
    /*
   Prepare the url String for Popular movies
    */
    private static String prepareStringTopRated() {
        String base_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        return base_url + Utility.API_KEY;
    }

    @Override
    public void onTaskCompleted(ArrayList<Movie> movies) {
        gridAdapter = new GridAdapter(getBaseContext(), movies);
        rv_grid_movies.setAdapter(gridAdapter);
        movies_list = movies;

          /*
    We want to import the lists to an SQLDataBase only once,
    This integers will serve for conditional statement.
     */
        if (import_for_top_rated == 0 || import_for_popular == 0){
            addMoviesToDatabase(movies_list);
            import_for_popular ++;
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

    /*
    Execute action when the menu item is clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort) {
            import_for_top_rated --;
            FetchMovies task = new FetchMovies(MainActivity.this, prepareStringTopRated());
            task.execute();
            return true;
        }

        if ( id == R.id.popularity ){
            import_for_popular ++;
            FetchMovies task = new FetchMovies(MainActivity.this, prepareStringPopular());
            task.execute();
            return true;
        }
        if (id == R.id.fav) {
            //  Create arraylist and add movies with liked == 1 to it
            //  Display the movies.
        }
        return super.onOptionsItemSelected(item);
    }
}

