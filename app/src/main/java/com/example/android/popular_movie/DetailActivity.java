package com.example.android.popular_movie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movie.adapters.MovieReviewAdapter;
import com.example.android.popular_movie.adapters.MovieTrailerAdapter;
import com.example.android.popular_movie.data.MovieContract;
import com.example.android.popular_movie.data.MovieContract.MovieEntry;
import com.example.android.popular_movie.model.MovieReview;
import com.example.android.popular_movie.model.MovieTrailer;
import com.example.android.popular_movie.utils.FetchReviews;
import com.example.android.popular_movie.utils.FetchTrailers;
import com.example.android.popular_movie.utils.OnTaskCompletedReview;
import com.example.android.popular_movie.utils.OnTaskCompletedTrailer;
import com.example.android.popular_movie.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements OnTaskCompletedReview, OnTaskCompletedTrailer {
    public static final String TITLE = "title_of_movie";
    public static final String RELEASE_DATE = "release_date";
    public static final String DESCRIPTION = "description";
    public static final String POSTER = "poster";
    public static final String RATE = "rating";
    public static final String MOVIE_ID = "movie_id";
    private static final String TAG = "DetailActivity";
    public String title, poster, description, date, rating, mov_id;
    public int movie_id;
    @BindView(R.id.movie_title_tv)
    TextView movie_title_tv;
    @BindView(R.id.movie_description_tv)
    TextView movie_description_tv;
    @BindView(R.id.release_date_tv)
    TextView release_date_tv;
    @BindView(R.id.rating_tv)
    TextView rating_tv;
    @BindView(R.id.textViewNoTrailer)
    TextView no_conection_trailer;
    @BindView(R.id.no_connection_reviews)
    TextView no_connection_review;
    @BindView(R.id.poster_of_movie_iv)
    ImageView poster_of_movie_iv;
    @BindView(R.id.liked_radio_button)
    RadioButton liked_rb;
    @BindView(R.id.disliked_radio_button)
    RadioButton disliked_rb;
    @BindView(R.id.review_rv)
    RecyclerView review_rv;
    @BindView(R.id.trailer_rv)
    RecyclerView trailer_rv;

    MovieReviewAdapter movieReviewAdapter;
    MovieTrailerAdapter movieTrailerAdapter;
    Boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(TITLE);
        poster = getIntent().getStringExtra(POSTER);
        description = getIntent().getStringExtra(DESCRIPTION);
        date = getIntent().getStringExtra(RELEASE_DATE);
        movie_id = getIntent().getIntExtra(MOVIE_ID, 0);
        rating = String.valueOf(getIntent().getExtras().get(RATE));
        mov_id = String.valueOf(getIntent().getExtras().get(MOVIE_ID));

        movie_title_tv.setText(title);
        movie_description_tv.setText(description);
        release_date_tv.setText(date);
        rating_tv.setText(rating);

        setTitle(title);
        Picasso
                .get()
                .load(Utility.preparePoster() + getIntent().getStringExtra(POSTER))
                .placeholder(R.drawable.download) // placeholder
                .into(poster_of_movie_iv);
        /*
         This will download the trailer
        */
        isFav = isFavorite(mov_id);

        if (Utility.isConnected(getApplicationContext())) {
            no_conection_trailer.setVisibility(View.GONE);
            trailer_rv.setVisibility(View.VISIBLE);

            no_connection_review.setVisibility(View.GONE);
            review_rv.setVisibility(View.VISIBLE);

            if ( movie_id == 0 ) {
                FetchTrailers fetchTrailers = new FetchTrailers(this, Utility.prepareStringMovieTrailers(Integer.valueOf(mov_id)));
                fetchTrailers.execute();
            } else {
                FetchTrailers fetchTrailers = new FetchTrailers(this, Utility.prepareStringMovieTrailers(movie_id));
                fetchTrailers.execute();
            }

        /*
         This will download the movieReviews
          */
            if ( movie_id == 0 ) {
                FetchReviews fetchReviews = new FetchReviews(this, Utility.prepareStringMovieReviews(Integer.valueOf(mov_id)));
                fetchReviews.execute();
            } else {
                FetchReviews fetchReviews = new FetchReviews(this, Utility.prepareStringMovieReviews(movie_id));
                fetchReviews.execute();
            }
        } else {
            trailer_rv.setVisibility(View.GONE);
            no_conection_trailer.setVisibility(View.VISIBLE);

            review_rv.setVisibility(View.GONE);
            no_connection_review.setVisibility(View.VISIBLE);
        }
    }

    /*
     This will save the data when users like a movie
      */
    public void onLikeStatusUpdated(View view) {
        if ( liked_rb.isChecked() ) {
            updateToLiked();

    /*
     This will remove the data when from the table users dislike a movie
      */
        } else if ( disliked_rb.isChecked() ) {
            updateToDisliked();
        }
    }

    /*
   This will delete the row of a movie when its disliked
     */
    private void updateToDisliked() {
        int x = getContentResolver().delete(MovieEntry.CONTENT_URI, "title=?", new String[]{title});
        if ( x > 0 ) {
            Toast.makeText(this, "removed " + title + " from favorite", Toast.LENGTH_SHORT).show();
        }
    }

    /*
       This will insert the row of a movie when its disliked
     */
    private void updateToLiked() {

        if (!isFav){
            // Create new empty ContentValues object
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie_id);
            contentValues.put(MovieEntry.COLUMN_TITLE, title);
            contentValues.put(MovieEntry.COLUMN_POSTER_PATH, poster);
            contentValues.put(MovieEntry.COLUMN_OVERVIEW, description);
            contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, date);
            contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, rating);

            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
            if ( uri != null ) {
                Toast.makeText(this, "added " + title + " to favorite", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The movie is already added to favorite", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    We get the data from the AsyncTask using an interface
     */
    @Override
    public void OnTaskCompletedReview(ArrayList<MovieReview> movieReviewArrayList) {
        if ( movieReviewArrayList == null ) {
            Toast.makeText(this, "movieReviewArrayList is null", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager layout_manger = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        review_rv.setLayoutManager(layout_manger);

        movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(), movieReviewArrayList);
        review_rv.setAdapter(movieReviewAdapter);

    }

    @Override
    public void OnTaskCompletedTrailer(ArrayList<MovieTrailer> movieTrailerArrayList) {
        if ( movieTrailerArrayList == null ) {
            Toast.makeText(this, "movieReviewArrayList is null", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager layout_manger = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        trailer_rv.setLayoutManager(layout_manger);

        movieTrailerAdapter = new MovieTrailerAdapter(getApplicationContext(), movieTrailerArrayList);
        trailer_rv.setAdapter(movieTrailerAdapter);
    }

    /*
    We count how many times the movie ID is on the table, if the movie_ID exists, we return false
     */
    public boolean isFavorite(String id) {
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null, MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id},
                null);
        cursor.close();
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else
            return false;
    }
}