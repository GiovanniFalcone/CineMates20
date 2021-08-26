package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.View.MyReviewsFragment;
import com.cinemates20.R;
import com.cinemates20.View.PersonalReviewFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

public class MyReviewsPresenter {

    private MyReviewsFragment myReviewsFragment;


    public MyReviewsPresenter(MyReviewsFragment myReviewsFragment){
        super();
        this.myReviewsFragment = myReviewsFragment;
    }

    public void myReviewsClicked(){
        //Get username of CurrentUser
        UserDAO_Firestore userDAO = new UserDAO_Firestore(myReviewsFragment.getContext());
        String currentUser = userDAO.getUsername
                (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())
                .getUsername();

        //Get all reviews of current user
        ReviewDAO reviewDAO = new ReviewDAO_Firestore(myReviewsFragment.getContext());
        List<Review> reviewList = reviewDAO.getListReviews(currentUser);

        List<MovieDb> movieDbList = getListMoviesReviews(reviewList);

        myReviewsFragment.setRecycler(movieDbList);

    }

    public List<MovieDb> getListMoviesReviews(List<Review> reviewList) {
        List<MovieDb> movieDbList = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");

                for (int i = 0; i < reviewList.size(); i++)
                    movieDbList.add(api.getMovies()
                            .getMovie(reviewList.get(i).getIdMovie(), "en"));

            }
        });

        //Waits for the computation to complete, and then retrieves its result.
        try {
            future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        return movieDbList;
    }

    public void onClickSeeReview(String movieTitle, int position) {
        PersonalReviewFragment personalReviewFragment = new PersonalReviewFragment();
        Bundle args = new Bundle();
        args.putString("movieTitle", movieTitle);
        personalReviewFragment.setArguments(args);
        myReviewsFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, personalReviewFragment)
                .addToBackStack(null)
                .commit();
    }

}
