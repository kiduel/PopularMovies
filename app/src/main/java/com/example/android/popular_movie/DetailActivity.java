package com.example.android.popular_movie;

import android.content.ContentValues;
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
import com.example.android.popular_movie.data.MovieContract.MovieEntry;
import com.example.android.popular_movie.model.MovieReview;
import com.example.android.popular_movie.utils.FetchReviews;
import com.example.android.popular_movie.utils.OnTaskCompletedReview;
import com.example.android.popular_movie.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements OnTaskCompletedReview{
    private static final String TAG = "DetailActivity" ;
    @BindView(R.id.movie_title_tv)
    TextView movie_title_tv;
    @BindView(R.id.movie_description_tv)
    TextView movie_description_tv;
    @BindView(R.id.release_date_tv)
    TextView release_date_tv;
    @BindView(R.id.rating_tv)
    TextView rating_tv;
    @BindView(R.id.poster_of_movie_iv)
    ImageView poster_of_movie_iv;
    @BindView(R.id.liked_radio_button)
    RadioButton liked_rb;
    @BindView(R.id.disliked_radio_button)
    RadioButton disliked_rb;
    @BindView(R.id.review_rv)
    RecyclerView review_rv;


    public static final String TITLE = "title_of_movie";
    public static final String RELEASE_DATE = "release_date";
    public static final String DESCRIPTION = "description";
    public static final String POSTER = "poster";
    public static final String RATE = "rating";
    public static final String MOVIE_ID = "movie_id";
    public String title, poster, description, date, rating, mov_id;
    ArrayList <MovieReview> movie_review_list = new ArrayList<>();
    MovieReviewAdapter movieReviewAdapter;

    public int movie_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        movie_review_list = new ArrayList<>();

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

        // This will download the movieReviews
        if (movie_id == 0) {
            FetchReviews task = new FetchReviews(this,Utility.prepareStringMovieReviews(Integer.valueOf(mov_id)));
            task.execute();
        } else {
            FetchReviews task = new FetchReviews(this,Utility.prepareStringMovieReviews(movie_id));
            task.execute();

        }


    }

    public void onLikeStatusUpdated(View view) {
        if ( liked_rb.isChecked() ) {
            updateToLiked();


        } else if ( disliked_rb.isChecked() ) {
            updateToDisliked();
        }
    }

    private void updateToDisliked() {
        int x = getContentResolver().delete(MovieEntry.CONTENT_URI, "title=?", new String[]{title});
        if ( x < 0 ) {
            Toast.makeText(this, "removed " + title + " from favorite", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateToLiked() {
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
        if (uri != null){
            Toast.makeText(this, "added " + title + " to favorite", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnTaskCompletedReview(ArrayList<MovieReview> movieReviewArrayList) {
      if (movieReviewArrayList != null){
         // movie_review_list = movieReviewArrayList;
          } else {
          Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
      }

        LinearLayoutManager layout_manger = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        review_rv.setLayoutManager(layout_manger);

        movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(), movieReviewArrayList);
        review_rv.setAdapter(movieReviewAdapter);

    }
}