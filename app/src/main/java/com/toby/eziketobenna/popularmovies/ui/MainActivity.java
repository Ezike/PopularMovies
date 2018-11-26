package com.toby.eziketobenna.popularmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.toby.eziketobenna.popularmovies.R;
import com.toby.eziketobenna.popularmovies.model.Movie.Movie;
import com.toby.eziketobenna.popularmovies.network.ApiConstants;
import com.toby.eziketobenna.popularmovies.ui.adapter.MovieAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREF = "pref";
    private List<Movie> movies;
    private MovieAdapter movieAdapter;
    private SharedPreferences preferences;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private String popular = ApiConstants.POPULAR_MOVIES;
    private String topRated = ApiConstants.TOP_RATED;
    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String apikey = ApiConstants.API_KEY;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    private MainViewModel mainViewModel;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    private Parcelable listState;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initViews();
        checkOrientation();
        if (savedInstanceState != null && savedInstanceState.containsKey(PREF)) {
            movies = savedInstanceState.getParcelable(PREF);
            movieAdapter.setMovieItem(movies);
        }
    }

    private void checkOrientation() {
        if (this.movieRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
            movieRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (this.movieRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 4);
            movieRecyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    private void initViews() {
        gridLayoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(getApplicationContext(), this);
        setSupportActionBar(toolbar);
        movieRecyclerView.setAdapter(movieAdapter);
        preferences = getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(this);
        if (isConnected()) {
            onSharedPreferenceChanged(preferences, getString(R.string.sort_by));
        } else {
            Snackbar.make(coordinatorLayout, "Check your network connection", Snackbar.LENGTH_LONG).show();
            loadFavorites();
            toolbar.setTitle("Favorite movies");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, gridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    //TODO: Add your Api key in the ApiConstants class
    private void loadMovies(String sort, String apiKey) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.loadAllMovies(sort, apiKey).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieAdapter.setMovieItem(movies);
                if (listState != null) {
                    gridLayoutManager.onRestoreInstanceState(listState);
                }
            }
        });
    }

    private void loadFavorites() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieAdapter.setMovieItem(movies);
                progressBar.setVisibility(View.GONE);
                if (listState != null) {
                    gridLayoutManager.onRestoreInstanceState(listState);
                }
            }
        });
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
            case R.id.favorites:
                setKey("favorites");
                return true;
            case R.id.refresh:
                getPreference();
                progressBar.setVisibility(View.VISIBLE);
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void getPreference() {
        String key = getOrderValue();
        if (key.equals(getString(R.string.popular))) {
            loadMovies(popular, apikey);
            toolbar.setTitle("Popular movies");
        } else if (key.equals(getString(R.string.top_rated))) {
            loadMovies(topRated, apikey);
            toolbar.setTitle("Top rated movies");
        } else {
            toolbar.setTitle("Favorite movies");
            loadFavorites();
        }
    }

    private String getOrderValue() {
        String key = getString(R.string.sort_by);
        String value = getString(R.string.popular);
        return preferences.getString(key, value);
    }

    private void setKey(String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.sort_by), value);
        editor.apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_by))) getPreference();
    }

    @Override
    public void onListClick(Movie movie) {
        Intent movieIntent = new Intent(this.getApplicationContext(), DetailActivity.class);
        movieIntent.putExtra(DetailActivity.EXTRA_VALUE, movie);
        startActivity(movieIntent);
    }
}
