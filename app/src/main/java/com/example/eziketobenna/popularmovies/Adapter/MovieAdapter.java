package com.example.eziketobenna.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eziketobenna.popularmovies.Model.Movie;
import com.example.eziketobenna.popularmovies.NetworkUtils.ApiConstants;
import com.example.eziketobenna.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> mMovies;
    private final Context context;

    public MovieAdapter(List<Movie> movies, Context context) {
        this.mMovies = movies;
        this.context = context;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        bindViews(movieViewHolder, position);
    }

    /**
     * Helper method for binding views to data
     * to be called in onBindViewHolder method
     *
     * @param holder   movie viewHolder object
     * @param position adapter position
     */

    private void bindViews(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(holder.getAdapterPosition());
        loadImagePoster(holder, movie.getMovieImagePath());
        holder.movieTitle.setText(movie.getOriginalTitle());

    }

    /**
     * Helper method to load poster image with picasso
     * to be called in bindViews method
     * @param holder the movieViewHolder argument
     * @param imageUrl poster path from movieDB api
     */

    private void loadImagePoster(final MovieViewHolder holder, String imageUrl) {
        String posterUrl = ApiConstants.MOVIES_POSTER_BASE_URL;
        Picasso.get()
                .load(posterUrl + imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(holder.movieImageView);

    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_imageView)
        ImageView movieImageView;
        @BindView(R.id.movie_title)
        TextView movieTitle;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
