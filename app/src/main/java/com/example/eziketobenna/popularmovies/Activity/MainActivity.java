package com.example.eziketobenna.popularmovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.eziketobenna.popularmovies.Adapter.MovieAdapter;
import com.example.eziketobenna.popularmovies.Model.Movie;
import com.example.eziketobenna.popularmovies.Model.Result;
import com.example.eziketobenna.popularmovies.NetworkUtils.ApiConstants;
import com.example.eziketobenna.popularmovies.NetworkUtils.MovieApi;
import com.example.eziketobenna.popularmovies.NetworkUtils.MovieRequestInterface;
import com.example.eziketobenna.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    List<Movie> movies;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecyclerView.setHasFixedSize(true);
        loadJSON();
    }

    //TODO: Add your Api key in the ApiConstants class
    private void loadJSON() {
        MovieRequestInterface movieRequestInterface = MovieApi.getClient().create(MovieRequestInterface.class);
        Call<Result> movieResult = movieRequestInterface.getPopularMovies(ApiConstants.API_KEY);
        movieResult.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int statusCode = response.code();
                Result result = response.body();
                movies = result.getResults();
                Log.d(LOG_TAG, "network response code:" + statusCode);

                //set up recycler view adapter
                movieAdapter = new MovieAdapter(movies, getApplicationContext());
                movieRecyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Data not available", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());

            }
        });
    }
}
