package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import info.movito.themoviedbapi.model.MovieDb;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>{

    private Context context;
    private List<MovieDb> movieDb;
    private List<MovieDb> filteredMovie;
    //interface for click item
    private ClickListener clickListener;

    public SearchMovieAdapter(Context context, List<MovieDb> movieDb) {
        this.context = context;
        this.movieDb = movieDb;
        this.filteredMovie = movieDb;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView image;
        private final TextView title;

        public ViewHolder(View v)
        {
            super(v);
            title = v.findViewById(R.id.txtName);
            image = v.findViewById(R.id.imageView);
        }

        public ImageView getImage(){
            return this.image;
        }
    }

    @NonNull
    @Override
    public SearchMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycler_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(500,350));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMovieAdapter.ViewHolder holder, int position) {

        //Open new page if user click movie
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClickListener(filteredMovie.get(position));
            }
        });

        MovieDb movie = filteredMovie.get(position);
        holder.title.setText(movie.getTitle());

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/original" + movie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 300)
                .transform(new RoundedCorners(30))
                .into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        return filteredMovie.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if(key.isEmpty())
                    filteredMovie = movieDb;
                else{
                    List<MovieDb> stdFiltered = new ArrayList<>();
                    for(MovieDb row: movieDb){
                        if (row.getTitle().toLowerCase().contains(key.toLowerCase()))
                            stdFiltered.add(row);
                    }
                    filteredMovie = stdFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMovie;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredMovie = (List<MovieDb>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(MovieDb filteredMovie);
    }
}
