package com.example.eziketobenna.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREF = "pref";
    List<Movie> movies;
    private MovieAdapter movieAdapter;
    private SharedPreferences preferences;

    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initViews();
        setOnRefreshAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initViews() {
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        setSupportActionBar(toolbar);
        movieRecyclerView.setAdapter(movieAdapter);
        preferences = getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(this);
        if (isConnected()) {
            onSharedPreferenceChanged(preferences, getString(R.string.sort_by));
        } else {
            Toast.makeText(getApplicationContext(), "No Network Detected", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    //TODO: Add your Api key in the ApiConstants class
    private void loadPopular() {
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
                progressBar.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(true);
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

    //TODO: Add your Api key in the ApiConstants class
    private void loadTopRated() {
        MovieRequestInterface movieRequestInterface = MovieApi.getClient().create(MovieRequestInterface.class);
        Call<Result> movieResult = movieRequestInterface.getTopRatedMovies(ApiConstants.API_KEY);
        movieResult.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int statusCode = response.code();
                Result result = response.body();
                movies = result.getResults();
                movieAdapter.setMovieItem(movies);
                Log.d(LOG_TAG, "network response code:" + statusCode);
                progressBar.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(true);
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
                        onSharedPreferenceChanged(preferences, getString(R.string.sort_by));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                setKey(getString(R.string.popular));
                return true;
            case R.id.topRated:
                setKey(getString(R.string.top_rated));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void getPreference() {

        String key = getOrderValue();

        if (key.equals(getString(R.string.popular))) {
            loadPopular();
        } else {

            setTitle(getString(R.string.top_rated));
            loadTopRated();
        }
    }

    private String getOrderValue() {
        String key = getString(R.string.sort_by);
        String value = getString(R.string.popular);
        return preferences.getString(key, value);
    }

    public void setKey(String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.sort_by), value);
        editor.apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_by))) getPreference();
    }
}
