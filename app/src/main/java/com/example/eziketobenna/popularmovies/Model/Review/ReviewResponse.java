package com.example.eziketobenna.popularmovies.Model.Review;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewResponse {
    @SerializedName("results")
    private ArrayList<Review> result;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("id")
    private Integer id;

    public ReviewResponse() {
    }

    public ReviewResponse(ArrayList<Review> review, int total_results, Integer id) {
        this.result = review;
        this.total_results = total_results;
        this.id = id;
    }

    public ArrayList<Review> getResult() {
        return result;
    }

    public void setResult(ArrayList<Review> result) {
        this.result = result;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
