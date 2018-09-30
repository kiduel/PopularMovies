package com.example.android.popular_movie.model;

import org.json.JSONException;

public class MovieTrailer {
    private String id;
    private String trailerKey;
    private String name;

    public MovieTrailer(String id, String key, String name) throws JSONException {
        this.id = id;
        this.trailerKey = key;
        this.name = name;
    }


    //Getters
    public String getId(){
        return id;
    }

    public String getKey(){
        return trailerKey;
    }

    public String getName() {
        return name;
    }
}
