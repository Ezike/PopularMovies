package com.example.eziketobenna.popularmovies.NetworkUtils;

import com.example.eziketobenna.popularmovies.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("movie/popular")
    Call<Result> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<Result> getTopRatedMovies(@Query("api_key") String apiKey);

}
