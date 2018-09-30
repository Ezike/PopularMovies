package com.example.eziketobenna.popularmovies.Model.Trailer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerResponse {
    @SerializedName("results")
    private ArrayList<Trailer> results;

    @SerializedName("id")
    private Integer id;

    public TrailerResponse() {
    }

    public TrailerResponse(ArrayList<Trailer> trailer, Integer id) {
        this.results = trailer;
        this.id = id;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> trailers) {
        this.results = trailers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
