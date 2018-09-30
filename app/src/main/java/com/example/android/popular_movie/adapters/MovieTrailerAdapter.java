package com.example.android.popular_movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movie.R;
import com.example.android.popular_movie.model.MovieTrailer;
import com.example.android.popular_movie.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private final ArrayList<MovieTrailer> movieTrailers;
    private Context context;

    public MovieTrailerAdapter(Context context, ArrayList<MovieTrailer> movieTrailers) {
        this.movieTrailers = movieTrailers;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trailer_v = layoutInflater.inflate(R.layout.trailer_single_row, parent, false);

        // Return a new holder instance
        return new MovieTrailerAdapter.ViewHolder(trailer_v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.ViewHolder holder, int position) {
        MovieTrailer movieTrailer = movieTrailers.get(position);
        String urlStril = Utility.prepareMovieTrailersImages(movieTrailer.getKey());
        Picasso
                .get()
                .load(urlStril)
                .placeholder(R.drawable.download) // placeholder
                .error(R.drawable.error) //  error
                .into(holder.poster_trailer);

        holder.poster_name.setText(movieTrailer.getName());

    }

    @Override
    public int getItemCount() {
        return movieTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View parentView;
        private ImageView poster_trailer;
        private TextView poster_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            poster_trailer = itemView.findViewById(R.id.trailer_poster);
            poster_name = itemView.findViewById(R.id.trailer_name);

            this.parentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieTrailer movieTrailer = movieTrailers.get(getAdapterPosition());

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Utility.prepareMovieTrailersVideo(movieTrailer.getKey())));
            context.startActivity(intent);

        }
    }
}
