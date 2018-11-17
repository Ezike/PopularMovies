package com.toby.eziketobenna.popularmovies.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.toby.eziketobenna.popularmovies.R;
import com.toby.eziketobenna.popularmovies.model.Trailer.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private Context context;
    private List<Trailer> trailers;
    private ItemClickListener itemClickListener;

    public TrailerAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        final String imageURL = "https://img.youtube.com/vi/";
        final String imgSize = "/0.jpg";
        Trailer trailer = trailers.get(position);
        Picasso.get().load(imageURL + trailer.getKey() + imgSize)
                .error(R.mipmap.ic_launcher).into(holder.trailerView);
    }

    @Override
    public int getItemCount() {
        if (trailers == null) {
            return 0;
        }
        return trailers.size();
    }

    public void setTrailer(List<Trailer> trailer) {
        this.trailers = trailer;
        notifyDataSetChanged();

    }

    public interface ItemClickListener {
        void onItemClick(Trailer trailer);
    }

    class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.trailer_IV)
        ImageView trailerView;

        private TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            itemClickListener.onItemClick(trailers.get(position));
        }
    }
}
