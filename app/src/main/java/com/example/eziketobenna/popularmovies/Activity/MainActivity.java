package com.example.eziketobenna.popularmovies.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListClickListener {

    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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
        setOnRefreshAction();
    }

    private void initViews() {
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        movieRecyclerView.setAdapter(movieAdapter);
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
                movieAdapter.setMovieItem(movies);
                Log.d(LOG_TAG, "network response code:" + statusCode);

                //set up recycler view adapter

                progressBar.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Data not available", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
                progressBar.setVisibility(View.VISIBLE);
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(true);
            }
        });
    }

    /*
     * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
     * performs a swipe-to-refresh gesture.
     */
    public void setOnRefreshAction() {
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener()

                {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadJSON();
                    }
                }
        );
    }


    @Override
    public void onListClick(Movie movie) {
        Intent movieIntent = new Intent(this.getApplicationContext(), DetailActivity.class);
        movieIntent.putExtra(DetailActivity.EXTRA_VALUE, movie);
        startActivity(movieIntent);
    }
}
