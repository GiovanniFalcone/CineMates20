package com.cinemates20.View;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cinemates20.Model.Review;
import com.cinemates20.Presenter.MovieCardPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.CastAdapter;
import com.cinemates20.Utils.Adapters.ReviewUserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MovieCardFragment extends Fragment{

    private ImageView background;
    private Button writeReviewButton;
    private TextView titleMovie, overviewMovie, valutationText;
    private MovieCardPresenter movieCardPresenter;
    private String title, url, overview;
    private int idMovie;
    private float valutation;
    private RecyclerView recyclerViewReview, recyclerViewCast;
    private boolean flag;
    private FloatingActionButton buttonAddToList, buttonRemoveFromList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_card, container, false);

        movieCardPresenter = new MovieCardPresenter(this);

        idMovie = requireArguments().getInt("MovieID", 0);
        title = requireArguments().getString("MovieTitle");
        url = requireArguments().getString("MovieUrl");
        overview = requireArguments().getString("MovieOverview");
        valutation = requireArguments().getFloat("MovieRating", 3f);


        background = view.findViewById(R.id.background);
        titleMovie = view.findViewById(R.id.movieTitle);
        overviewMovie = view.findViewById(R.id.movieOverview);
        valutationText = view.findViewById(R.id.valutation);
        recyclerViewReview = view.findViewById(R.id.recyclerReview);
        recyclerViewCast = view.findViewById(R.id.recyclerReviewCast);
        writeReviewButton = view.findViewById(R.id.imageView4);
        buttonAddToList = view.findViewById(R.id.buttonAddToList);
        buttonRemoveFromList = view.findViewById(R.id.buttonRemoveFromList);

        movieCardPresenter.setMovieCard();

        writeReviewButton.setOnClickListener(view2 -> movieCardPresenter.clickWriteReview());

        titleMovie.setText(title);
        overviewMovie.setText(overview);
        valutationText.setText(String.valueOf(valutation));

        buttonAddToList.setOnClickListener(view1 -> movieCardPresenter.onClickAddMovieToList());
        buttonRemoveFromList.setOnClickListener(view12 -> movieCardPresenter.onClickRemoveMovieFromList());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        movieCardPresenter.setUserReviewByMovie();
    }

    public String getMovieTitle(){
        return title;
    }

    public String getImageURL(){
        return url;
    }

    public String getOverview(){
        return overview;
    }

    public int getIdMovie(){
        return idMovie;
    }

    public void setHeader(String url){
        Glide.with(requireContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(background);
    }

    public void setRecycler(List<Review> authorList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewReview.setLayoutManager(layoutManager);
        ReviewUserAdapter reviewUserAdapter = new ReviewUserAdapter(getContext(), authorList);
        recyclerViewReview.setAdapter(reviewUserAdapter);
        clickListener(reviewUserAdapter);
    }

    public void setRecyclerCast(List<String> urlCast){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManager);
        CastAdapter castAdapter = new CastAdapter(getContext(), urlCast);
        recyclerViewCast.setAdapter(castAdapter);
    }

    public void clickListener(ReviewUserAdapter reviewUserAdapter) {
        reviewUserAdapter.setOnItemClickListener((authorList, position, view) ->
                movieCardPresenter.onClickSeeReview(authorList, position));
    }

    public Context getFragmentContext(){
        return getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setFlag(boolean value){
        flag = value;
        buttonRemoveFromList.setEnabled(flag);
    }
    
}