package com.toby.eziketobenna.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toby.eziketobenna.popularmovies.R;
import com.toby.eziketobenna.popularmovies.model.Movie.Movie;
import com.toby.eziketobenna.popularmovies.network.ApiConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> mMovies;
    final private ListClickListener mListClickListener;
    private Context context;
    private MovieViewHolder holder;

    public MovieAdapter(Context context, ListClickListener onListClickListener) {
        this.context = context;
        this.mListClickListener = onListClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        this.holder = movieViewHolder;
        bindViews(movieViewHolder);
    }

    /**
     * Helper method for binding views to data
     * to be called in onBindViewHolder method
     *
     * @param holder movie viewHolder object
     */
    private void bindViews(MovieViewHolder holder) {
        Movie movie = mMovies.get(holder.getAdapterPosition());
        loadImagePoster(holder, movie.getMovieImagePath());
        holder.movieTitle.setText(movie.getOriginalTitle());

    }

    /**
     * Helper method to load poster image with picasso
     * to be called in bindViews method
     *
     * @param holder   the movieViewHolder argument
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
        if (mMovies == null) {
            return 0;
        }
        return mMovies.size();
    }

    public void setMovieItem(List<Movie> movie) {
        this.mMovies = movie;
        notifyDataSetChanged();

    }

    public interface ListClickListener {
        void onListClick(Movie movie);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_imageView)
        ImageView movieImageView;
        @BindView(R.id.movie_title)
        TextView movieTitle;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListClickListener.onListClick(mMovies.get(position));
        }
    }

}
