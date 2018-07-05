package com.example.android.popular_movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popular_movie.DetailActivity;
import com.example.android.popular_movie.R;
import com.example.android.popular_movie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private final ArrayList<Movie> movies;
    private Context context;
    private String title, description;

    public GridAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View parentView;
        private ImageView poster_cover;


        public ViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            poster_cover = itemView.findViewById(R.id.image_poster);

            this.parentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetailActivity.class);
            Movie movie = movies.get(getAdapterPosition());
            intent.putExtra(DetailActivity.TITLE, movie.getOriginal_title());
            intent.putExtra(DetailActivity.DESCRIPTION, movie.getOverview());
            intent.putExtra(DetailActivity.RELEASE_DATE, movie.getRelease_date());
            intent.putExtra(DetailActivity.POSTER, movie.getPoster_path());
            context.startActivity(intent);
        }
    }


    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View poster = layoutInflater.inflate(R.layout.grid_view_poster, parent, false);

        // Return a new holder instance
        return new GridAdapter.ViewHolder(poster);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso
                .get()
                .load(movie.getPoster_path())
                .placeholder(R.drawable.box) // placeholder
                .into(holder.poster_cover);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}

