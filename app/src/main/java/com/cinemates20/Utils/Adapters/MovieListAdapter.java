package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<MovieDb> movieDbList;
    private Context context;
    //interface for click item
    private ClickListener clickListener;

    public MovieListAdapter(Context context, List<MovieDb> movieDbList){
        this.context = context;
        this.movieDbList = movieDbList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final FloatingActionButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.moviePosterList);
            removeButton = itemView.findViewById(R.id.removeMovieButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,600));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieDb movieDb = movieDbList.get(position);

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(500, 600)
                .transform(new RoundedCorners(30))
                .into(holder.poster);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClickListener(String.valueOf(movieDb.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieDbList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(String idMovie);
    }
}
