package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;

public class ScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Artwork> artworkList;
    private final Context context;
    private ClickListener clickListener;

    public ScreenAdapter(List<Artwork> artworkList, Context context){
        this.artworkList = artworkList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_layout, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT));
        return new MovieAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Artwork artwork = artworkList.get(position);
        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/original" + artwork.getFilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(400, 250)
                .transform(new RoundedCorners(30))
                .into(((MovieAdapter.ViewHolder)holder).getMoviePoster());


        ViewCompat.setTransitionName(((MovieAdapter.ViewHolder) holder).getMoviePoster(), "screenTransition");

        ((MovieAdapter.ViewHolder) holder).getMoviePoster().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.setOnItemClickListener("http://image.tmdb.org/t/p/original" + artwork.getFilePath(),
                        ((MovieAdapter.ViewHolder) holder).getMoviePoster(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artworkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView moviePoster;
        private final TextView movieTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
        }

        public ImageView getMoviePoster() {
            return moviePoster;
        }

        public TextView getMovieTitle() {
            return movieTitle;
        }
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void setOnItemClickListener(String url, ImageView screen, int position);
    }
}
