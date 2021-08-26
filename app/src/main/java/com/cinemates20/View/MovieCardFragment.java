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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MovieCardFragment extends Fragment{

    private ImageView poster, background, imageButton;
    private TextView titleMovie, overviewMovie;
    private RatingBar ratingBar;
    private MovieCardPresenter movieCardPresenter;
    private String title, url, overview;
    private int idMovie;
    private float valutation;
    private RecyclerView recyclerViewReview, recyclerViewCast;
    private Boolean [] flag = new Boolean[] {false, false, false};

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

        //poster = view.findViewById(R.id.moviePoster);
        background = view.findViewById(R.id.background);
        titleMovie = view.findViewById(R.id.movieTitle);
        overviewMovie = view.findViewById(R.id.movieOverview);
        ratingBar = view.findViewById(R.id.valutation);
        recyclerViewReview = view.findViewById(R.id.recyclerReview);
        recyclerViewCast = view.findViewById(R.id.recyclerReviewCast);
        imageButton = view.findViewById(R.id.imageView4);

        setHasOptionsMenu(true);

        movieCardPresenter.setMovieCard();

        imageButton.setOnClickListener(view2 -> movieCardPresenter.clickWriteReview());

        titleMovie.setText(title);
        //Utils.putImage(requireContext(), url, poster);
        overviewMovie.setText(overview);
        ratingBar.setRating(valutation);

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

    public void setBackground(String url){
        //constraintLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gold));
        //BitmapDrawable d = new BitmapDrawable(getResources(), url);
        //constraintLayout.setBackground(d);

        /*try {
            URL u = new URL(url);
            Bitmap bitmap = BitmapFactory.decodeStream(u.openConnection().getInputStream());
            Drawable image = new BitmapDrawable(requireContext().getResources(), bitmap);
            constraintLayout.setDrawingCacheEnabled(false);
            constraintLayout.setBackground(image);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //setImagebackground(constraintLayout, url);

    }

    public static void setImagebackground(ConstraintLayout constraintLayout,String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL x2 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) x2.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Drawable dr = new BitmapDrawable(myBitmap);
            constraintLayout.setBackgroundDrawable(dr);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setVisible(!flag[0]);
        menu.getItem(1).setVisible(flag[0]);

        menu.getItem(2).setVisible(!flag[1]);
        menu.getItem(3).setVisible(flag[1]);

        menu.getItem(5).setVisible(flag[2]);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.addToFavorite:
                movieCardPresenter.onClickAddMovieToList("Favorite List");
                break;
            case R.id.addToWatch:
                movieCardPresenter.onClickAddMovieToList("Watch List");
                break;
            case R.id.addToCustom:
                movieCardPresenter.onClickAddMovieToCustomList();
                break;
            case R.id.removeFromFavorite:
                movieCardPresenter.onClickRemoveFromList("Favorite List");
                break;
            case R.id.removeFromWatch:
                movieCardPresenter.onClickRemoveFromList("Watch List");
                break;
            case R.id.removeFromCustom:
                movieCardPresenter.onClickRemoveMovieFromCustomList();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setFlag(Boolean [] flag){
        this.flag = flag;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    
}