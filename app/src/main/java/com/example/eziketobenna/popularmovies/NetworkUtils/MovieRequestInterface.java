package com.example.eziketobenna.popularmovies.NetworkUtils;

import com.example.eziketobenna.popularmovies.Model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieRequestInterface {

    @GET(ApiConstants.POPULAR_MOVIES)
    Call<Result> getPopularMovies(@Query(ApiConstants.API_KEY_LABEL) String apiKey);

    @GET(ApiConstants.TOP_RATED)
    Call<Result> getTopRatedMovies(@Query(ApiConstants.API_KEY_LABEL) String apiKey);

}
