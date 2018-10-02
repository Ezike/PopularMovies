package com.example.eziketobenna.popularmovies.network;

import com.example.eziketobenna.popularmovies.BuildConfig;

public class ApiConstants {

    public static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIES_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String MOVIES_BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/";
    public static final String MOVIES_DETAIL_BASE_URL = "https://image.tmdb.org/t/p/w342/";
    /* TODO: Add your api key here */
    public static final String API_KEY = BuildConfig.ApiKey;
    // api query properties
    public static final String API_KEY_LABEL = "api_key";
    public static final String POPULAR_MOVIES = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String REVIEWS = "movie/{id}/reviews";
    public static final String TRAILERS = "movie/{id}/videos";

}
