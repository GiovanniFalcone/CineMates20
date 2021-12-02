package com.cinemates20.Presenter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieCardFragment;
import com.cinemates20.View.ReviewCardActivity;
import com.cinemates20.View.SeeAllReviewFragment;
import com.cinemates20.View.WriteReviewActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.fragment.app.Fragment;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.PersonCast;

public class MovieCardPresenter {

    private final MovieCardFragment movieCardFragment;

    public MovieCardPresenter(MovieCardFragment movieCardFragment){
        this.movieCardFragment = movieCardFragment;
    }

    /**
     * After click, open the write review activity and pass
     * movieId, movieTitle, movieUrl and movieOverview to it
     * @param idMovie id of current movie
     * @param title title of current movie
     * @param url image poster of current movie
     * @param overview plot of current movie
     */
    public void clickWriteReview(int idMovie, String title, String url, String overview){
        Intent intent = new Intent(movieCardFragment.getContext(), WriteReviewActivity.class);
        intent.putExtra("MovieID", idMovie);
        intent.putExtra("MovieTitle", title);
        intent.putExtra("MovieUrl", url);
        intent.putExtra("MovieOverview", overview);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(0);
        movieCardFragment.startActivityForResult(intent, 0);
    }

    /**
     * Open the review clicked of the current user's friend
     * and pass author name and movie title to it
     * @param reviewClicked the review that user want to see
     * @param iconAuthor the icon's url of review's author
     * @param authorIcon the image of review's author, it's used for animation
     * @param authorName the name of review's author, it's used for animation
     */
    public void onClickSeeReview(Review reviewClicked, String iconAuthor, ImageView authorIcon, TextView authorName, Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Icon", iconAuthor);
        intent.putExtra("Review", reviewClicked);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        if(username.equals(reviewClicked.getAuthor()))
            intent.putExtra("PersonalReview", true);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String> (authorIcon, "authorIcon");
        pairs[1] = new Pair<View, String>(authorName, "authorName");

        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(fragment.requireActivity(), pairs);
        fragment.startActivity(intent, optionsCompat.toBundle());
    }

    /**
     * This method set the movie card: menu to add/remove the movie, the cast and
     * users who had written a review about it.
     */
    public void setMovieCard(int idMovie){
        String username = User.getCurrentUser();

        /*
         * Check if the movie is saved in at least one list. If it is true enable button to remove it eventually.
         * Then get the cast of current movie.
         */
        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(String.valueOf(idMovie), username);
        movieCardFragment.setFlag(!nameList.isEmpty());

        //set the backdrop into movie card
        setBackdrop(idMovie);

        //set movie genres
        setGenres(idMovie);

        //set cast of current movie
        setCast(idMovie);

        //set movie's backgrounds
        setBackgrounds(idMovie);

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        Review review = reviewDAO.getReviewByAuthor(username, idMovie);

        if(review.getIdReview() != null){
            if(!review.getTextReview().equals(""))
                movieCardFragment.setWriteReviewButtonEnable(false);

            movieCardFragment.setRateButton(false);
        } else
            movieCardFragment.setWriteReviewButtonEnable(false);

        reviewDAO.getMovieRating(idMovie, new ReviewCallback() {
            @Override
            public void setRating(float rating, int total) {
                movieCardFragment.setMovieRating(rating);
            }
        });

        setUserReviewByMovie(idMovie);
    }

    /**
     * This method will get and set the movie's backgrounds
     */
    private void setBackgrounds(int idMovie) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            movieDAO.getBackdrops(idMovie, new MovieCallback() {
                @Override
                public void setArtworks(List<Artwork> artworks) {
                    handler.post(() -> movieCardFragment.setRecyclerScreen(artworks));
                }
            });
        });
    }

    /**
     * This method will get and set the movie's genres
     */
    private void setGenres(int idMovie) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            movieDAO.getGenre(idMovie, new MovieCallback() {
                @Override
                public void setGenre(List<Genre> genreList) {
                    handler.post(() -> movieCardFragment.setRecyclerViewGenres(genreList));
                }
            });
        });
    }

    /**
     * This method will return the cast of the movie.
     */
    private void setCast (int idMovie){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            movieDAO.getCast(idMovie, new MovieCallback() {
                @Override
                public void setCast(List<PersonCast> personCast) {
                    handler.post(() -> movieCardFragment.setRecyclerCast(personCast));
                }
            });
        });
    }

    /**
     * This method will set the landscape poster of current movie
     */
    private void setBackdrop(int idMovies){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            movieDAO.getBackdrops(idMovies, new MovieCallback() {
                @Override
                public void setArtworks(List<Artwork> artworks) {
                    handler.post(() -> {
                        if(artworks.size() > 1){
                            int randomNum = (int) ((Math.random() * (artworks.size() - 1)) + 1);
                            Artwork artwork = artworks.get(randomNum);
                            movieCardFragment.setHeader("http://image.tmdb.org/t/p/original" + artwork.getFilePath());
                        }
                        else if(artworks.size() == 1){
                            Artwork artwork = artworks.get(0);
                            movieCardFragment.setHeader("http://image.tmdb.org/t/p/original" + artwork.getFilePath());
                        }
                    });
                }
            });
        });
    }

    /**
     * Set all users who has written review about clicked movie and show them
     */
    public void setUserReviewByMovie(int idMovie) {
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        User user = userDAO.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        List<Review> listAuthor = reviewDAO.getUserReviewByMovie(idMovie, user.getFriends(), user.getUsername(), false);
        if(!listAuthor.isEmpty()) {
            movieCardFragment.setLayoutNoReview(false);
            movieCardFragment.setRecycler(listAuthor);
            movieCardFragment.setSeeAllReviewButton(true);
        } else
            movieCardFragment.setLayoutNoReview(true);
    }

    /**
     * Open a dialog which contain all list of the user
     * except the list that already contain the current movie
     * @param idMovie id of movie to add to list
     */
    public void onClickAddMovieToList(String idMovie) {
        String username = User.getCurrentUser();

        //Get all list of user
        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        List<MovieList> allCustomLists = movieListDAO.getMovieListsNameByUser(username);

        //get all list that contains the current movie
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, username);

        //remove from the list all lists that already contains the current movie
        List<String> list = new ArrayList<>();
        for(MovieList movieList: allCustomLists){
            list.add(movieList.getNameList());
        }
        list.removeAll(nameList);

        String[] customLists = list.toArray(new String[0]);

        if(customLists.length != 0){
            AtomicInteger selected = new AtomicInteger();
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(movieCardFragment.getFragmentContext(), R.style.ThemeMyAppDialogAlertDay);
            builder.setTitle("Select a list");
            builder.setSingleChoiceItems(customLists, 0, (dialogInterface, i) -> selected.set(i))
                    .setPositiveButton("Add", (dialogInterface, i) -> {
                        Timestamp dateAndTime = new Timestamp(new Date());

                        movieListDAO.addMovieToList(username, customLists[selected.get()], idMovie);
                        movieCardFragment.setFlag(true);
                        dialogInterface.dismiss();
                        Utils.showDialog(movieCardFragment.getFragmentContext(), "Done!", "Movie successfully added to the list ");

                        // save news into feed
                        FeedDAO feedDAO = DAOFactory.getFeedDAO(DAOFactory.FIREBASE);
                        feedDAO.addNews(username, "", idMovie, "", "movieList", 0f, dateAndTime);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }else{
            Utils.showErrorDialog(movieCardFragment.getFragmentContext(),"Operation could not be completed","There are no other lists to which you can add the movie  ");
        }
    }

    /**
     * Open a dialog which contain all list of the user
     * that contains the current movie in order to remove it
     */
    public void onClickRemoveMovieFromList(String idMovie) {
        String currentUser = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);

        //get all list that contains the current movie
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, currentUser);

        nameList.toArray(new String[0]);

        AtomicInteger selected = new AtomicInteger();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(movieCardFragment.getFragmentContext(), R.style.ThemeMyAppDialogAlertDay);
        builder.setTitle("Select a list");
        builder.setSingleChoiceItems(nameList.toArray(new String[0]), 0, (dialogInterface, i) -> selected.set(i))
                .setPositiveButton("Remove", (dialogInterface, i) -> {
                    movieListDAO.removeMovieFromList(idMovie, nameList.toArray(new String[0])[selected.get()], currentUser);
                    movieCardFragment.setFlag(nameList.size() - 1 != 0);
                    dialogInterface.dismiss();
                    Utils.showDialog(movieCardFragment.getFragmentContext(), "Done!", "Movie successfully deleted from the list.");
                })
                .setNegativeButton("Cancel", (dialogInterface, i) ->
                        dialogInterface.dismiss())
                .show();
    }

    public void saveValuation(int idMovie, float valuation) {
        String currentUser = User.getCurrentUser();
        Timestamp dateAndTime = new Timestamp(new Date());

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        reviewDAO.saveReview(currentUser, valuation, "", idMovie, dateAndTime, new ReviewCallback() {
            @Override
            public void onSuccess(String idReview) {
                movieCardFragment.setWriteReviewButtonEnable(true);
                movieCardFragment.setRateButton(false);

                Utils.showDialog(movieCardFragment.getFragmentContext(), "Done!",
                        "Your valuation has been successfully added.");

                // add to feed
                FeedDAO feedDAO = DAOFactory.getFeedDAO(DAOFactory.FIREBASE);
                feedDAO.addNews(currentUser, "", String.valueOf(idMovie), idReview, "valuation", valuation, dateAndTime);
            }
        });
    }

    public void seeAllReviewClicked(int idMovie) {
        SeeAllReviewFragment seeAllReviewFragment = new SeeAllReviewFragment();

        Bundle args = new Bundle();
        args.putInt("MovieID", idMovie);
        seeAllReviewFragment.setArguments(args);

        Utils.changeFragment_SlideAnim(movieCardFragment.getFragment(), seeAllReviewFragment, R.id.fragment);
    }
}
