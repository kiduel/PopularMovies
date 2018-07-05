package com.example.android.popular_movie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
    @BindView(R.id.poster_of_movie_iv)
    ImageView poster_of_movie_iv ;

    public static final String TITLE = "title_of_movie";
    public static final String RELEASE_DATE = "release_date";
    public static final String DESCRIPTION = "description";
    public static final String POSTER = "poster";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra(TITLE);
        movie_title_tv.setText(title);
        movie_description_tv.setText(getIntent().getStringExtra(DESCRIPTION));
        release_date_tv.setText(getIntent().getStringExtra(RELEASE_DATE));

        setTitle(title);
        Picasso
                .get()
                .load(getIntent().getStringExtra(POSTER))
                .placeholder(R.drawable.box) // placeholder
                .into(poster_of_movie_iv);

    }
}
