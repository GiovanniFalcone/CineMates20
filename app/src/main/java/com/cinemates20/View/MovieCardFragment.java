package com.cinemates20.View;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cinemates20.Model.Review;
import com.cinemates20.Presenter.MovieCardPresenter;
import com.cinemates20.Presenter.WriteReviewPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.cinemates20.Utils.Adapters.ScreenAdapter;

import com.facebook.shimmer.ShimmerFrameLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.PersonCast;

public class MovieCardFragment extends Fragment{

    private View layoutNoReview;
    private ImageView background;
    private FloatingActionButton writeReviewButton, rateButton;
    private TextView overviewMovie , castTextView, moviePlotTextView;
    private MovieCardPresenter movieCardPresenter;
    private String title, url, overview;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerViewReview, recyclerViewCast, recyclerViewGenres, recyclerViewScreen;
    private FloatingActionButton buttonAddToList, buttonRemoveFromList;
    private CardView cardViewButtons;
    private RatingBar movieRating;
    private int idMovie;
    private float valuation;
    private boolean flag;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        valuation = requireArguments().getFloat("MovieRating", 3f);

        background = view.findViewById(R.id.background);

        overviewMovie = view.findViewById(R.id.movieOverview);
        cardViewButtons = view.findViewById(R.id.cardViewButtons);
        recyclerViewReview = view.findViewById(R.id.recyclerReview);
        recyclerViewCast = view.findViewById(R.id.recyclerReviewCast);
        recyclerViewGenres = view.findViewById(R.id.genreRecycler);
        recyclerViewScreen = view.findViewById(R.id.recyclerScreen);
        writeReviewButton = view.findViewById(R.id.imageView4);
        buttonAddToList = view.findViewById(R.id.buttonAddToList);
        buttonRemoveFromList = view.findViewById(R.id.buttonRemoveFromList);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        castTextView = view.findViewById(R.id.textViewCast);
        moviePlotTextView = view.findViewById(R.id.moviePlot);
        rateButton = view.findViewById(R.id.rateThis);
        layoutNoReview = view.findViewById(R.id.no_review_layout);
        movieRating = view.findViewById(R.id.valuationMovie);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        FloatingActionButton backButton = view.findViewById(R.id.backButtonToolbar);
        backButton.setOnClickListener(view15 -> requireActivity().onBackPressed());

        shimmerFrameLayout.startShimmer();
        movieCardPresenter.setMovieCard(idMovie, title);

        writeReviewButton.setOnClickListener(view2 -> movieCardPresenter.clickWriteReview(idMovie, title, url, overview));

        overviewMovie.setText(overview);

        buttonAddToList.setOnClickListener(view1 -> movieCardPresenter.onClickAddMovieToList(String.valueOf(idMovie)));
        buttonRemoveFromList.setOnClickListener(view12 -> movieCardPresenter.onClickRemoveMovieFromList(String.valueOf(idMovie)));

        rateButton.setOnClickListener(view14 -> {
            AlertDialog.Builder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeMyAppDialogAlertDay);
            builder.setTitle("Rate this movie");
            View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rate, (ViewGroup) getView(), false);
            RatingBar ratingBar = viewDialog.findViewById(R.id.ratingBar);
            builder.setView(viewDialog);
            builder.setMessage(R.string.confirm_valuation);
            builder.setPositiveButton("Rate", (dialogInterface, i) -> movieCardPresenter.saveValuation(idMovie, title, ratingBar.getRating()))
                    .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog alertDialog = builder.create();
            ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) ->
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(ratingBar1.getRating() != 0.0));
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        });

        animationItem();



        return view;
    }

    public void setWriteReviewButtonEnable(boolean value){
        writeReviewButton.setEnabled(value);
    }

    public void setRateButton(boolean value){
        rateButton.setEnabled(value);
    }

    @Override
    public void onResume() {
        super.onResume();
        movieCardPresenter.setUserReviewByMovie(title);
    }


    public void setHeader(String url){
        Glide.with(requireContext())
                .asBitmap()
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        background.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void setMovieRating(float rating){
        movieRating.setRating(rating);
    }

    public void setLayoutNoReview(boolean value){
        if(value)
            layoutNoReview.setVisibility(View.VISIBLE);
        else
            layoutNoReview.setVisibility(View.GONE);
    }

    public void setRecycler(List<Review> authorList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewReview.setLayoutManager(layoutManager);
        GenericAdapter<Review> reviewUserAdapter = new GenericAdapter<>(authorList, getContext());
        recyclerViewReview.setAdapter(reviewUserAdapter);
        clickListener(reviewUserAdapter);
    }

    public void setRecyclerCast(List<PersonCast> personCast){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewCast.setLayoutManager(layoutManager);
        GenericAdapter<PersonCast> castAdapter = new GenericAdapter<>(personCast, getContext());
        recyclerViewCast.setAdapter(castAdapter);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerViewCast.setVisibility(View.VISIBLE);
    }

    public void setRecyclerViewGenres(List<Genre> genreList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewGenres.setLayoutManager(layoutManager);
        GenericAdapter<Genre> genresAdapter = new GenericAdapter<>(genreList, getContext());
        recyclerViewGenres.setAdapter(genresAdapter);
    }

    public void setRecyclerScreen(List<Artwork> artworks) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewScreen.setLayoutManager(layoutManager);
        ScreenAdapter screenAdapter = new ScreenAdapter(artworks, getContext());
        recyclerViewScreen.setAdapter(screenAdapter);

        screenAdapter.setClickListener((url, screen, position) -> {
            Fragment screenFragment = ScreenFragment.newInstance(position, artworks);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(screen, "transitionScreen")
                    .addToBackStack(null)
                    .add(R.id.nav_host_fragment_activity_main, screenFragment)
                    .hide(MovieCardFragment.this)
                    .commit();
        });
    }

    public void clickListener(GenericAdapter<Review> reviewUserAdapter) {
        reviewUserAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Review review, String iconAuthor, ImageView authorIcon, TextView authorName) {
                movieCardPresenter.onClickSeeReview(review, iconAuthor, authorIcon, authorName);
            }
        });
    }

    public Context getFragmentContext(){
        return getContext();
    }

    public void setFlag(boolean value){
        flag = value;
        buttonRemoveFromList.setEnabled(flag);
    }

    private void animationItem() {
        recyclerViewGenres.setTranslationY(300);
        cardViewButtons.setTranslationY(300);
        overviewMovie.setTranslationY(300);
        recyclerViewCast.setTranslationY(300);
        castTextView.setTranslationY(300);
        moviePlotTextView.setTranslationY(300);
        movieRating.setTranslationY(300);

        recyclerViewGenres.setAlpha(0);
        cardViewButtons.setAlpha(0);
        overviewMovie.setAlpha(0);
        recyclerViewCast.setAlpha(0);
        castTextView.setAlpha(0);
        moviePlotTextView.setAlpha(0);
        movieRating.setAlpha(0);

        recyclerViewGenres.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        cardViewButtons.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        overviewMovie.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        recyclerViewCast.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        castTextView.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        moviePlotTextView.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
        movieRating.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(700).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            boolean isWritten = data.getBooleanExtra("written", false);
            setWriteReviewButtonEnable(!isWritten);
        }
    }
}