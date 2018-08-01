package com.example.android.popular_movie.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movie.DetailActivity;
import com.example.android.popular_movie.R;
import com.example.android.popular_movie.utils.Utility;
import com.squareup.picasso.Picasso;

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieViewHolder>{
    // Class variables for the Cursor that holds movie data and the Context
    private Cursor mCursor;
    private Context mContext;
    private String title, description, releaseDate, poster,rating, _id, movieId;
    Intent intent;
    int tag;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MovieCursorAdapter(Context mContext) {
        this.mContext = mContext;

    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new MovieViewHolder that holds the view for each movie
     */
    @NonNull
    @Override
    public MovieCursorAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.grid_view_poster, parent, false);

        return new  MovieCursorAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCursorAdapter.MovieViewHolder holder, int position) {
        // Indices for the _id, title, and poster
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
//        int descriptionIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
//        int ratingIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
//        int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        int posterIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
//        int movieIDIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
//        int _idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);

        //you may need to add the rest of the item here

        mCursor.moveToPosition(position); // get to the right location in the cursor
        // Determine the values of the wanted data
        holder.itemView.setTag(position);

        title = mCursor.getString(titleIndex);
        poster = mCursor.getString(posterIndex);


        Picasso
                .get()
                .load(Utility.preparePoster() + poster)
                .placeholder(R.drawable.download) // placeholder
                .error(R.drawable.error) //  error
                .into(holder.poster_cover);
        holder.movie_title.setText(title);
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View parentView;
        private ImageView poster_cover;
        private TextView movie_title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            poster_cover = itemView.findViewById(R.id.image_poster);
            movie_title = itemView.findViewById(R.id.movie_title_cardview);
            this.parentView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            tag = (int) view.getTag();
            mCursor.moveToPosition(tag); // get to the right location in the cursor

            int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int descriptionIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int ratingIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            int posterIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            int movieIDIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int _idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);

            title = mCursor.getString(titleIndex);
            poster = mCursor.getString(posterIndex);
            description = mCursor.getString(descriptionIndex);
            rating = String.valueOf(mCursor.getDouble(ratingIndex));
            releaseDate = mCursor.getString(releaseDateIndex);
            movieId = mCursor.getString(movieIDIndex);
            _id = mCursor.getString(_idIndex);

          //  rating = String.valueOf(getIntent().getExtras().getDouble(RATE));


            intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.TITLE, title);
            intent.putExtra(DetailActivity.DESCRIPTION,description);
            intent.putExtra(DetailActivity.RELEASE_DATE,releaseDate);
            intent.putExtra(DetailActivity.POSTER, poster);
            intent.putExtra(DetailActivity.RATE, rating);
            intent.putExtra(DetailActivity.MOVIE_ID, movieId);
            intent.putExtra(MovieContract.MovieEntry._ID, _id);

            mContext.startActivity(intent);
        }
    }
}
