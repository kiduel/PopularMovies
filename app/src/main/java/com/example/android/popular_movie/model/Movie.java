package com.example.android.popular_movie.model;

public class Movie {
    private String original_title;
    private String poster_path;
    private String overview;
    private String release_date;
    private double vote_average;


    public Movie(String original_title, String poster_path, String overview, String release_date, double vote_average) {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }


}
