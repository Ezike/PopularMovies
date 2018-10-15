package com.toby.eziketobenna.popularmovies.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.toby.eziketobenna.popularmovies.database.MovieRepository;
import com.toby.eziketobenna.popularmovies.model.Movie.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MovieRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> loadAllMovies(String sort, String apiKey) {
        return mAllMovies = mRepository.getMoviesFromNetwork(sort, apiKey);
    }

    public LiveData<List<Movie>> getFavMovies() {
        return mRepository.getFavMovies();
    }
}
