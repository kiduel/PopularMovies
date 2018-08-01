package com.example.android.popular_movie.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieTrailer {
    private String id;
    private String trailerKey;

    public MovieTrailer(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.trailerKey = trailer.getString("key");
    }


    //Getters
    public String getId(){
        return id;
    }

    public String getKey(){
        return trailerKey;
    }
}
