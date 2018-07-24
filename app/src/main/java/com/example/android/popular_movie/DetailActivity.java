package com.example.android.popular_movie;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.popular_movie.adapters.GridAdapter;
import com.example.android.popular_movie.data.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.movie_title_tv)
    TextView movie_title_tv;
    @BindView(R.id.movie_description_tv)
    TextView movie_description_tv;
    @BindView(R.id.release_date_tv)
    TextView release_date_tv;
    @BindView(R.id.rating_tv)
    TextView rating_tv;
    @BindView(R.id.poster_of_movie_iv)
    ImageView poster_of_movie_iv ;
    @BindView(R.id.liked_radio_button)
    RadioButton liked_rb;
    @BindView(R.id.disliked_radio_button)
    RadioButton disliked_rb;

    public static final String TITLE = "title_of_movie";
    public static final String RELEASE_DATE = "release_date";
    public static final String DESCRIPTION = "description";
    public static final String POSTER = "poster";
    public static final String RATE = "rating";
    public static final String MOVIE_ID = "movie_id";
    public String title;
    public int movie_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra(TITLE);
        movie_id = getIntent().getIntExtra(MOVIE_ID, 0);
        movie_id = getIntent().getIntExtra(MOVIE_ID, 0);
        movie_title_tv.setText(title);
        movie_description_tv.setText(getIntent().getStringExtra(DESCRIPTION));
        release_date_tv.setText(getIntent().getStringExtra(RELEASE_DATE));
        rating_tv.setText(String.valueOf(getIntent().getExtras().getDouble(RATE)));


        setTitle(title);
        Picasso
                .get()
                .load(GridAdapter.preparePoster() + getIntent().getStringExtra(POSTER))
                .placeholder(R.drawable.download) // placeholder
                .into(poster_of_movie_iv);

    }

    public void onLikeStatusUpdated(View view) {
        if (liked_rb.isChecked()) {
           // Toast.makeText(this, "movies is liked", Toast.LENGTH_SHORT).show();
            updateToLiked();
        } else if (disliked_rb.isChecked()) {
           // Toast.makeText(this, "movies is not liked", Toast.LENGTH_SHORT).show();
            updateToDisliked();
        }
    }

    private void updateToDisliked() {
         ContentValues contentValues = new ContentValues();
         contentValues.put(MovieContract.MovieEntry.COLUMN_LIKED, MainActivity.NOT_LIKED);
        getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues,"_id=?",  new String[] {(String.valueOf(movie_id))});
    }

    private void updateToLiked() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_LIKED, MainActivity.LIKED);
        getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues,"_id=?",  new String[] {(String.valueOf(movie_id))});
    }
}
