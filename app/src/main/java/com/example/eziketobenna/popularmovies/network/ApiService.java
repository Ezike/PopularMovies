package com.example.eziketobenna.popularmovies.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.eziketobenna.popularmovies.model.Movie.Movie;
import com.example.eziketobenna.popularmovies.model.Movie.Result;
import com.example.eziketobenna.popularmovies.model.Review.Review;
import com.example.eziketobenna.popularmovies.model.Review.ReviewResponse;
import com.example.eziketobenna.popularmovies.model.Trailer.Trailer;
import com.example.eziketobenna.popularmovies.model.Trailer.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static ApiService apiService = null;
    private static ApiInterface apiInterface;

    private ApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

    }

    public synchronized static ApiService getInstance() {
        if (apiService == null) {
            apiService = new ApiService();
        }
        return apiService;
    }

    public LiveData<List<Movie>> getMovies(String sort, String apiKey) {
        final MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getMovies(sort, apiKey).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int statusCode = response.code();
                Log.d(ApiService.class.getSimpleName(), "onResponse: " + statusCode);
                mutableLiveData.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                mutableLiveData.setValue(null);
                Log.e(ApiService.class.getSimpleName(), "onResponse: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<Review>> getReviews(Integer id, String apiKey) {
        final MutableLiveData<List<Review>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getReviews(id, apiKey).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                int statusCode = response.code();
                mutableLiveData.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<List<Trailer>> getTrailers(Integer id, String apiKey) {
        final MutableLiveData<List<Trailer>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getTrailers(id, apiKey).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                int statusCode = response.code();
                mutableLiveData.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

}
