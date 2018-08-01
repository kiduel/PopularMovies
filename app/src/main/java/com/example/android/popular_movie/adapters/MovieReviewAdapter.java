package com.example.android.popular_movie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movie.R;
import com.example.android.popular_movie.model.MovieReview;

import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private final ArrayList<MovieReview> movieReviews;
    private Context context;

    public MovieReviewAdapter(Context context, ArrayList<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
        this.context = context;
    }


    @NonNull
    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View review_v = layoutInflater.inflate(R.layout.review_single_row, parent, false);

        // Return a new holder instance
        return new MovieReviewAdapter.ViewHolder(review_v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.ViewHolder holder, int position) {

        MovieReview movieReview = movieReviews.get(position);
        holder.reviewer.setText(movieReview.getWriter());
        holder.review.setText(movieReview.getReview());
    }

    @Override
    public int getItemCount() {
        return movieReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View parentView;
        private TextView reviewer;
        private TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewer = itemView.findViewById(R.id.reviewer_tv);
            review = itemView.findViewById(R.id.review_tv);

        }
    }
}
