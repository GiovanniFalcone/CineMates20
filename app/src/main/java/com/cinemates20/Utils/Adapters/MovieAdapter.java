package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieDb> filteredMovie; //for movie search
    private final List<MovieDb> movieDbList;
    private final Context context;
    private final int FLAG;
    private final static int MOVIE_FEATURE_HOME = 0;
    private final static int MOVIE_RECYCLER_HOME = 1;
    private final static int MOVIE_SEARCH = 2;
    private final static int MOVIE_LIST = 3;
    private final static int MOVIE_GENRES = 4;
    //interface for click item
    private ClickListener clickListener;

    public MovieAdapter(List<MovieDb> movieDbList, Context context, int FLAG){
        this.movieDbList = movieDbList;
        this.filteredMovie = movieDbList;
        this.context = context;
        this.FLAG = FLAG;
    }

    @Override
    public int getItemViewType(int position) {
        return FLAG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        switch (viewType){
            case MOVIE_FEATURE_HOME:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_layout, parent, false);
                return new ViewHolder(v);
            case MOVIE_RECYCLER_HOME:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_layout, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT));
                return new ViewHolder(v);
            case MOVIE_SEARCH:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycler_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_Search(v);
            case MOVIE_LIST:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_user_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_MovieList(v);
            case MOVIE_GENRES:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_layout, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder(v);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        switch (itemType){
            case MOVIE_FEATURE_HOME:
                MovieDb movieDb = movieDbList.get(position);

                Glide.with(this.context)
                        .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(750, 850)
                        .transform(new RoundedCorners(30))
                        .into(((ViewHolder)holder).getMoviePoster());

                ViewCompat.setTransitionName(((ViewHolder) holder).getMoviePoster(), movieDb.getTitle());

                ((ViewHolder)holder).getMoviePoster().setOnClickListener(view -> clickListener.onItemClickListener(movieDb));
                break;

            case MOVIE_RECYCLER_HOME:
                movieDb = movieDbList.get(position);

                ((ViewHolder)holder).getMovieTitle().setText(movieDb.getTitle());

                Glide.with(this.context)
                        .load("http://image.tmdb.org/t/p/original" + movieDb.getBackdropPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(400, 250)
                        .transform(new RoundedCorners(30))
                        .into(((ViewHolder)holder).getMoviePoster());

                ViewCompat.setTransitionName(((ViewHolder) holder).getMoviePoster(),"prova");
                ((ViewHolder)holder).getMoviePoster().setOnClickListener(view -> clickListener.onItemClickListener(movieDb));
                break;

            case MOVIE_SEARCH:
                MovieDb movie = filteredMovie.get(position);
                ((ViewHolder_Search)holder).getMovieTitle().setText(movie.getTitle());

                if(movie.getPosterPath() != null) {
                    Glide.with(this.context)
                            .load("http://image.tmdb.org/t/p/original" + movie.getPosterPath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(250, 350)
                            .transform(new RoundedCorners(30))
                            .into(((ViewHolder_Search) holder).getMoviePoster());
                } else {
                    Glide.with(this.context)
                            .load(R.drawable.no_image)
                            .override(200, 350)
                            .transform(new RoundedCorners(30))
                            .into(((ViewHolder_Search) holder).getMoviePoster());
                }

                ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
                reviewDAO.getMovieRating(movie.getId(), new ReviewCallback() {
                    @Override
                    public void setRating(float rating, int total) {
                        ((ViewHolder_Search) holder).getRatingBar().setRating(rating);
                        ((ViewHolder_Search) holder).getTotalReview().setText("Total review: " + total);
                    }
                });

                holder.itemView.setOnClickListener(view -> clickListener.onItemClickListener(movie));
                break;

            case MOVIE_LIST:
                movieDb = movieDbList.get(position);

                Glide.with(this.context)
                        .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(350, 450)
                        .transform(new RoundedCorners(30))
                        .into(((ViewHolder_MovieList)holder).getMoviePoster());

                ((ViewHolder_MovieList)holder).getRemoveButton().setOnClickListener(view ->
                        clickListener.onItemClickListener(movieDb, position));
                break;

            case MOVIE_GENRES:
                movieDb = movieDbList.get(position);

                Glide.with(this.context)
                        .load("http://image.tmdb.org/t/p/original" + movieDb.getPosterPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(350, 450)
                        .transform(new RoundedCorners(30))
                        .into(((ViewHolder)holder).getMoviePoster());

                ((ViewHolder)holder).getMoviePoster().setOnClickListener(view -> clickListener.onItemClickListener(movieDb));
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (FLAG == MOVIE_FEATURE_HOME || FLAG == MOVIE_RECYCLER_HOME
                || FLAG == MOVIE_LIST || FLAG == MOVIE_GENRES)
            return movieDbList.size();
        else
            return filteredMovie.size();
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

    public static class ViewHolder_Search extends RecyclerView.ViewHolder{

        private final ImageView moviePoster;
        private final TextView movieTitle, totalReview;
        private final RatingBar ratingBar;

        public ViewHolder_Search(@NonNull View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            totalReview = itemView.findViewById(R.id.totalReview);
        }

        public ImageView getMoviePoster() {
            return moviePoster;
        }

        public TextView getMovieTitle() {
            return movieTitle;
        }

        public RatingBar getRatingBar() { return ratingBar; }

        public TextView getTotalReview() {
            return totalReview;
        }
    }

    public static class ViewHolder_MovieList extends RecyclerView.ViewHolder{

        private final ImageView moviePoster;
        private final FloatingActionButton removeButton;

        public ViewHolder_MovieList(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePosterList);
            removeButton = itemView.findViewById(R.id.removeMovieButton);
        }

        public ImageView getMoviePoster() {
            return moviePoster;
        }

        public FloatingActionButton getRemoveButton() {
            return removeButton;
        }
    }
    
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if(key.isEmpty())
                    filteredMovie = movieDbList;
                else{
                    List<MovieDb> stdFiltered = new ArrayList<>();
                    for(MovieDb row: movieDbList){
                        if (row.getTitle().toLowerCase().contains(key.toLowerCase()))
                            stdFiltered.add(row);
                    }
                    filteredMovie = stdFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMovie;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
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
        default void onItemClickListener(MovieDb movieClicked) {}
        default void onItemClickListener(MovieDb movieClicked, int position) {}
    }
}
