package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsAdapter.ViewHolder> {

    private List<MovieDb> movieDbList;
    private Context context;
    //interface for click item
    private ClickListener clickListener;

    public MyReviewsAdapter(Context context, List<MovieDb> movieDbList){
        this.context = context;
        this.movieDbList = movieDbList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView, imageViewButton;
        private final TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitleMovieReview);
            imageView = itemView.findViewById(R.id.moviePosterReview);
            imageViewButton = itemView.findViewById(R.id.imageView5);
        }
    }

    @NonNull
    @Override
    public MyReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_review_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));

        return new MyReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MovieDb movieDb = movieDbList.get(position);

        holder.title.setText(movieDb.getTitle());

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 300)
                .transform(new RoundedCorners(30))
                .into(holder.imageView);

        holder.imageViewButton.setOnClickListener(view ->
                clickListener.onItemClickListener(movieDb.getTitle(), position));

        holder.imageView.setOnClickListener(view ->
                clickListener.onItemClickListener(movieDb.getTitle(), position));
    }

    @Override
    public int getItemCount() {
        return movieDbList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(String movieTitle, int position);
    }
}
