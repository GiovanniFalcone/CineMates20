package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private Context context;
    private List<MovieDb> movieDbList;
    //interface for click item
    private ClickListener clickListener;

    public MovieAdapter(List<MovieDb> movieDbList, Context context_) {
        this.movieDbList = movieDbList;
        this.context = context_;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView title;
        private final RatingBar valutation;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.txtName);
            image = v.findViewById(R.id.imageView);
            valutation = v.findViewById(R.id.valutation);
        }

        public ImageView getImage(){ return this.image;}
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_layout, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(230,500));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.image.setOnClickListener(view -> clickListener.onItemClickListener(movieDbList, position, view));

        MovieDb movieDb = movieDbList.get(position);
        holder.title.setText(movieDb.getTitle());

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 300)
                .transform(new RoundedCorners(30))
                .into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        return movieDbList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(List<MovieDb> movieDbList, int position, View view);
    }
}