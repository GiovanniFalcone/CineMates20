package com.cinemates20.Presenter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieCardFragment;
import com.cinemates20.View.ReviewCardActivity;
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
     */
    public void clickWriteReview(){
        Intent intent = new Intent(movieCardFragment.getContext(), WriteReviewActivity.class);
        intent.putExtra("MovieID", movieCardFragment.getIdMovie());
        intent.putExtra("MovieTitle", movieCardFragment.getMovieTitle());
        intent.putExtra("MovieUrl", movieCardFragment.getImageURL());
        intent.putExtra("MovieOverview", movieCardFragment.getOverview());
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
    public void onClickSeeReview(Review reviewClicked, String iconAuthor, ImageView authorIcon, TextView authorName) {
        Intent intent = new Intent(movieCardFragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Icon", iconAuthor);
        intent.putExtra("Review", reviewClicked);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        if(username.equals(reviewClicked.getAuthor()))
            intent.putExtra("PersonalReview", true);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String> (authorIcon, "authorIcon");
        pairs[1] = new Pair<View, String>(authorName, "authorName");

        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(movieCardFragment.requireActivity(), pairs);
        movieCardFragment.startActivity(intent, optionsCompat.toBundle());
    }

    /**
     * This method set the movie card: menu to add/remove the movie, the cast and
     * users who had written a review about it.
     */
    public void setMovieCard(){
        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        /*
         * Check if the movie is saved in at least one list. If it is true enable button to remove it eventually.
         * Then get the cast of current movie.
         */
        MovieListDAO movieListDAO = daoFactory.getMovieListDAO();
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, username);
        movieCardFragment.setFlag(!nameList.isEmpty());

        //set the backdrop into movie card
        setBackdrop();

        //set movie genres
        setGenres();

        //set cast of current movie
        setCast();

        //set movie's backgrounds
        setBackgrounds();

        ReviewDAO reviewDAO = daoFactory.getReviewDAO();
        Review review = reviewDAO.getReviewByAuthor(username, movieCardFragment.getIdMovie());

        if(review.getIdReview() != null){
            if(!review.getTextReview().equals(""))
                movieCardFragment.setWriteReviewButtonEnable(false);

            movieCardFragment.setRateButton(false);
        } else
            movieCardFragment.setWriteReviewButtonEnable(false);

        reviewDAO.getMovieRating(movieCardFragment.getIdMovie(), new ReviewCallback() {
            @Override
            public void setRating(float rating, int total) {
                movieCardFragment.setMovieRating(rating);
            }
        });

        setUserReviewByMovie();
    }

    /**
     * This method will get and set the movie's backgrounds
     */
    private void setBackgrounds() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            movieDAO.getBackdrops(movieCardFragment.getIdMovie(), new MovieCallback() {
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
    private void setGenres() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            movieDAO.getGenre(movieCardFragment.getIdMovie(), new MovieCallback() {
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
    private void setCast (){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            movieDAO.getCast(movieCardFragment.getIdMovie(), new MovieCallback() {
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
    private void setBackdrop(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            movieDAO.getBackdrops(movieCardFragment.getIdMovie(), new MovieCallback() {
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
    public void setUserReviewByMovie() {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        ReviewDAO reviewDAO = daoFactory.getReviewDAO();
        List<Review> listAuthor = reviewDAO.getUserReviewByMovie(movieCardFragment.getMovieTitle(), user.getFriends(), user.getUsername());
        if(!listAuthor.isEmpty()) {
            movieCardFragment.setLayoutNoReview(false);
            movieCardFragment.setRecycler(listAuthor);
        } else
            movieCardFragment.setLayoutNoReview(true);
    }

    /**
     * Open a dialog which contain all list of the user
     * except the list that already contain the current movie
     */
    public void onClickAddMovieToList() {
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());
        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        // create the required DAO Factory
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        //Get all list of user
        MovieListDAO movieListDAO = daoFactory.getMovieListDAO();
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

                        movieListDAO.addMovieToList(username, customLists[selected.get()], idMovie, dateAndTime);
                        movieCardFragment.setFlag(true);
                        dialogInterface.dismiss();
                        Utils.showDialog(movieCardFragment.getFragmentContext(), "Done!", "Movie successfully added to the list ");

                        // save news into feed
                        FeedDAO feedDAO = daoFactory.getFeedDAO();
                        feedDAO.addNews(username, "", String.valueOf(movieCardFragment.getIdMovie()), "", "movieList", 0f, dateAndTime);
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
    public void onClickRemoveMovieFromList() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        MovieListDAO movieListDAO = daoFactory.getMovieListDAO();

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

    public void saveValuation(float valuation) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        Timestamp dateAndTime = new Timestamp(new Date());

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        ReviewDAO reviewDAO = daoFactory.getReviewDAO();
        reviewDAO.saveReview(currentUser, valuation, "", movieCardFragment.getIdMovie(), movieCardFragment.getMovieTitle(), dateAndTime, new ReviewCallback() {
            @Override
            public void onSuccess(String idReview) {
                movieCardFragment.setWriteReviewButtonEnable(true);
                movieCardFragment.setRateButton(false);

                Utils.showDialog(movieCardFragment.getFragmentContext(), "Done!",
                        "Your valuation has been successfully added.");

                // add to feed
                FeedDAO feedDAO = daoFactory.getFeedDAO();
                feedDAO.addNews(currentUser, "", String.valueOf(movieCardFragment.getIdMovie()), idReview, "valuation", valuation, dateAndTime);
            }
        });
    }
}
