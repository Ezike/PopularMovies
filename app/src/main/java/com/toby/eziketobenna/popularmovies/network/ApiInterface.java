package com.toby.eziketobenna.popularmovies.network;

import com.toby.eziketobenna.popularmovies.model.Movie.Result;
import com.toby.eziketobenna.popularmovies.model.Review.ReviewResponse;
import com.toby.eziketobenna.popularmovies.model.Trailer.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{sorting}")
    Call<Result> getMovies(@Path("sorting") String sort, @Query(ApiConstants.API_KEY_LABEL) String apiKey);

//    @GET(ApiConstants.TOP_RATED)
//    Call<Result> getTopRatedMovies(@Query(ApiConstants.API_KEY_LABEL) String apiKey);

    @GET(ApiConstants.REVIEWS)
    Call<ReviewResponse> getReviews(@Path("id") int id, @Query(ApiConstants.API_KEY_LABEL) String apiKey);

    @GET(ApiConstants.TRAILERS)
    Call<TrailerResponse> getTrailers(@Path("id") int id, @Query(ApiConstants.API_KEY_LABEL) String apiKey);
}
