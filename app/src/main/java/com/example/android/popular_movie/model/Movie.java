package com.example.android.popular_movie.model;

public class Movie {
    private String original_title;
    private String poster_path;
    private String overview;
    private String release_date;
    private double vote_average;
    private int movie_id;


    public Movie(String original_title, String poster_path, String overview, String release_date, double vote_average, int movie_id) {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.movie_id = movie_id;
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

    public int getMovie_id() { return movie_id; }

}
