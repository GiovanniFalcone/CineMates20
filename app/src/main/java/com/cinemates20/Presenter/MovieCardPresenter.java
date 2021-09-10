package com.cinemates20.Presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cinemates20.DAO.Implements.MovieListDAO_Firestore;
import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieCardFragment;
import com.cinemates20.View.ReviewCardActivity;
import com.cinemates20.View.WriteReviewActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.people.Person;
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
        movieCardFragment.startActivity(intent);
    }

    /**
     * Open the review clicked of the current user's friend
     * and pass author name and movie title to it
     * @param authorList - the authors who has written the review of the current movie
     * @param position - the position of author into list
     */
    public void onClickSeeReview(List<Review> authorList, int position) {
        Intent intent = new Intent(movieCardFragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Author", authorList.get(position).getAuthor());
        intent.putExtra("titleMovie", authorList.get(position).getTitleMovie());
        intent.putExtra("idReview", authorList.get(position).getIdReview());
        movieCardFragment.startActivity(intent);
    }

    /**
     * Open a dialog which contain all list of the user
     * except the list that already contain the current movie
     */
    public void onClickAddMovieToList() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());

        UserDAO userDAO = new UserDAO_Firestore(movieCardFragment.getContext());
        //Get all list of user
        List<String> allCustomLists = userDAO.getMovieListsNameByUser(currentUser);

        //get all list that contains the current movie
        MovieListDAO movieListDAO = new MovieListDAO_Firestore(movieCardFragment.getContext());
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, currentUser);

        //remove from the list all lists that already contains the current movie
        allCustomLists.removeAll(nameList);
        String[] customLists = allCustomLists.toArray(new String[0]);

        if(customLists.length != 0){
            AtomicInteger selected = new AtomicInteger();
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(movieCardFragment.getFragmentContext());
            builder.setTitle("Seleziona una lista");
            builder.setSingleChoiceItems(customLists, 0, (dialogInterface, i) -> selected.set(i))
                    .setPositiveButton("Aggiungi", (dialogInterface, i) -> {
                        movieListDAO.addMovieToList(currentUser, customLists[selected.get()], idMovie);
                        movieCardFragment.setFlag(true);
                        dialogInterface.dismiss();
                        Utils.showDialog(movieCardFragment.getFragmentContext(), "Fatto!", "Film aggiunto correttamente alla lista");
                    })
                    .setNegativeButton("Annulla", (dialogInterface, i) ->
                            dialogInterface.dismiss())
                    .show();
        }else{
            Utils.showErrorDialog(movieCardFragment.getFragmentContext(),"Impossibile completare l'operazione","Devi prima creare una lista personalizzata per poter aggiungere il film ");
        }
    }

    /**
     * This method set the movie card: menu to add/remove the movie, the cast and
     * users who had written a review about it.
     */
    public void setMovieCard(){
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());
        /*
         * Check if the movie is saved in at least one list. If it is true enable button to remove it eventually.
         * Then get the cast of current movie.
         */
        MovieListDAO movieListDAO = new MovieListDAO_Firestore(movieCardFragment.getContext());
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, currentUser);
        movieCardFragment.setFlag(!nameList.isEmpty());

        //set the backdrop into movie card
        setBackdrop();

        //Set cast of current movie and show it
        setCast();

        setUserReviewByMovie();
    }

    /**
     * This method will set the cast of the movie.
     */
    private void setCast (){
        List<String> urlPerson = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");

            MovieDb result = api.getMovies().getMovie(movieCardFragment.getIdMovie(), "en", TmdbMovies.MovieMethod.credits);
            List<PersonCast> personCasts = result.getCast();
            for(Person person : personCasts) {
                List<Artwork> resultImg = api.getPeople().getPersonImages(person.getId());
                if(resultImg.size() != 0) {
                    Log.d("KTM", "" + person.getName());
                    urlPerson.add("http://image.tmdb.org/t/p/w300" + resultImg.get(0).getFilePath());
                }
            }

            handler.post(() -> movieCardFragment.setRecyclerCast(urlPerson));
        });
    }

    /**
     * This method will set the landscape poster of current movie
     */
    private void setBackdrop(){
        AtomicReference<String> backdrop = new AtomicReference<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
            MovieImages res = api.getMovies().getImages(movieCardFragment.getIdMovie(), "");
            backdrop.set("http://image.tmdb.org/t/p/original" + res.getBackdrops().get(0).getFilePath());
            movieCardFragment.setHeader(backdrop.get());
        });
        executorService.shutdown();
    }


    /**
     * Open a dialog which contain all list of the user
     * that contains the current movie in order to remove it
     */
    public void onClickRemoveMovieFromList() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String idMovie = String.valueOf(movieCardFragment.getIdMovie());

        MovieListDAO movieListDAO = new MovieListDAO_Firestore(movieCardFragment.getContext());

        //get all list that contains the current movie
        List<String> nameList = movieListDAO.getListsThatContainsCurrentMovie(idMovie, currentUser);

        nameList.toArray(new String[0]);

        AtomicInteger selected = new AtomicInteger();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(movieCardFragment.getFragmentContext());
        builder.setTitle("Seleziona una lista");
        builder.setSingleChoiceItems(nameList.toArray(new String[0]), 0, (dialogInterface, i) -> selected.set(i))
                .setPositiveButton("Rimuovi", (dialogInterface, i) -> {
                    movieListDAO.removeMovieFromList(idMovie, nameList.toArray(new String[0])[selected.get()], currentUser);
                    movieCardFragment.setFlag(nameList.size() - 1 != 0);
                    dialogInterface.dismiss();
                    Utils.showDialog(movieCardFragment.getFragmentContext(), "Fatto!", "Film eliminato correttamente dalla lista");
                })
                .setNegativeButton("Annulla", (dialogInterface, i) ->
                        dialogInterface.dismiss())
                .show();
    }

    /**
     * Set all users who has written review about clicked movie and show them
     */
    public void setUserReviewByMovie() {
        ReviewDAO reviewDAO = new ReviewDAO_Firestore(movieCardFragment.getContext());
        List<Review> listAuthor = reviewDAO.getUserReviewByMovie(movieCardFragment.getMovieTitle());
        movieCardFragment.setRecycler(listAuthor);
    }
}
