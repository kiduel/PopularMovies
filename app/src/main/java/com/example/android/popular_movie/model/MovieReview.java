package com.example.android.popular_movie.model;

public class MovieReview {
    private String id;
    private String writer;
    private String review;

    public MovieReview(String id, String writer, String review) {
        this.id = id;
        this.writer = writer;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getReview() {
        return review;
    }
}
