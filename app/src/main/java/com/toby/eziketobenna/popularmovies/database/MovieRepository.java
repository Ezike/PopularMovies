package com.toby.eziketobenna.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.toby.eziketobenna.popularmovies.model.Movie.Movie;
import com.toby.eziketobenna.popularmovies.model.Review.Review;
import com.toby.eziketobenna.popularmovies.model.Trailer.Trailer;
import com.toby.eziketobenna.popularmovies.network.ApiService;

import java.util.List;

public class MovieRepository {
    private ApiService apiService = ApiService.getInstance();
    private MovieDao mDao;
    private LiveData<List<Movie>> mFavMovies;
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public MovieRepository(Application application) {
        MovieDb db = MovieDb.getDatabase(application);
        mDao = db.movieDao();
        mFavMovies = mDao.getAllMovies();
    }

    //  for favorites
    public LiveData<List<Movie>> getFavMovies() {
        return mFavMovies;
    }

    public void insert(final Movie movie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertMovie(movie);
            }
        });
    }

    public void delete(final Movie movie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDao.delete(movie);
            }
        });
    }

    public LiveData<Movie> getMovieById(int id) {
        return mDao.getMovieById(id);
    }

    //for network
    public LiveData<List<Movie>> getMoviesFromNetwork(String sortType, String apiKey) {
        return apiService.getMovies(sortType, apiKey);
    }

    public LiveData<List<Review>> getReviews(Integer id, String apiKey) {
        return apiService.getReviews(id, apiKey);
    }

    public LiveData<List<Trailer>> getTrailers(Integer id, String apiKey) {
        return apiService.getTrailers(id, apiKey);
    }
}
