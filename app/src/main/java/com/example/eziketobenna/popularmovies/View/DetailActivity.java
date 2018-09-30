package com.example.eziketobenna.popularmovies.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eziketobenna.popularmovies.Adapter.ReviewAdapter;
import com.example.eziketobenna.popularmovies.Adapter.TrailerAdapter;
import com.example.eziketobenna.popularmovies.Model.Movie.Movie;
import com.example.eziketobenna.popularmovies.Model.Review.Review;
import com.example.eziketobenna.popularmovies.Model.Trailer.Trailer;
import com.example.eziketobenna.popularmovies.NetworkUtils.ApiConstants;
import com.example.eziketobenna.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener {

    public final static String EXTRA_VALUE = "extraMovie";
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.release_date)
    TextView dateTv;
    @BindView(R.id.overview)
    TextView overviewTv;
    @BindView(R.id.main_backdrop)
    ImageView backdropIv;
    @BindView(R.id.poster)
    ImageView posterIv;
    @BindView(R.id.movie_rating)
    RatingBar ratingBar;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.review_rv)
    RecyclerView reviewView;
    @BindView(R.id.trailer_rv)
    RecyclerView trailerView;
    String title, date, overview, backdrop, poster;
    Movie movie;
    double rating;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private String apiKey = ApiConstants.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        if (intent.hasExtra(DetailActivity.EXTRA_VALUE)) {
            movie = getIntent().getParcelableExtra(DetailActivity.EXTRA_VALUE);
            populateUI(movie);
        }

        initView();
        loadTrailers();
        loadReviews();
    }

    private void initView() {
        reviewView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(this);
        reviewView.setAdapter(reviewAdapter);
        trailerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trailerAdapter = new TrailerAdapter(this, this);
        trailerView.setAdapter(trailerAdapter);
        trailerView.setHasFixedSize(false);
    }

    void populateUI(Movie movie) {
        title = movie.getOriginalTitle();
        date = movie.getReleaseDate();
        overview = movie.getOverview();
        poster = movie.getMovieImagePath();
        backdrop = movie.getBackdropPath();
        rating = movie.getVoteAverage();
        //set movie detail on respective views
        titleTv.setText(title);
        dateTv.setText(date.substring(0, 4));
        overviewTv.setText(overview);
        //Load image
        String posterUrl = ApiConstants.MOVIES_DETAIL_BASE_URL;
        String backdropUrl = ApiConstants.MOVIES_BACKDROP_BASE_URL;
        loadImage(posterIv, poster, posterUrl);
        loadImage(backdropIv, backdrop, backdropUrl);
        /* set movie rating
         * divide the vote average by 2 to fit 5 star rating bar
         * since vote average is /10
         */
        float rated = (((float) rating) / 2);
        ratingBar.setRating(rated);
        setTitle(movie.getOriginalTitle());
        Log.d(LOG_TAG, "vote average:" + rated);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.movie_data_unavailable, Toast.LENGTH_SHORT).show();
    }

    private void loadImage(ImageView imageView, String imageUrl, String posterUrl) {
        Picasso.get()
                .load(posterUrl + imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    private void loadTrailers() {
        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getTrailers(movie.getId(), apiKey).observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                trailerAdapter.setTrailer(trailers);
            }
        });
    }

    private void loadReviews() {
        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getReviews(movie.getId(), apiKey).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });

    }

    @Override
    public void onItemClick(Trailer trailer) {
        final String url = "https://www.youtube.com/watch?v=";
        final String key = trailer.getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url + key));
        startActivity(intent);
    }
}