package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.View.PersonalReviewFragment;
import com.cinemates20.View.ReactionsTabFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class PersonalReviewPresenter {

    private PersonalReviewFragment personalReviewFragment;
    private String idReview;

    public PersonalReviewPresenter(PersonalReviewFragment personalReviewFragment){
        this.personalReviewFragment = personalReviewFragment;
    }

    public void viewPersonalReview() {
        UserDAO_Firestore userDAO = new UserDAO_Firestore(personalReviewFragment.getContext());
        String currentUser = userDAO.getUsername(
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();

        ReviewDAO reviewDAO = new ReviewDAO_Firestore(personalReviewFragment.getContext());
        Review review = reviewDAO.getReviewByAuthor(currentUser, personalReviewFragment.getMovieTitle());
        personalReviewFragment.setReview(review.getTextReview());
        idReview = review.getIdReview();
    }

    public String getId(){
        return idReview;
    }

    /**
     * Open bottom sheet dialog and pass idReview to it
     */
    public void onClickNumberReactions(){
        ReactionsTabFragment reactionsTabFragment = new ReactionsTabFragment();
        //Get id review
        String id = getId();
        Bundle arg = new Bundle();
        arg.putString("idReview", id);
        reactionsTabFragment.setArguments(arg);
        reactionsTabFragment.show(personalReviewFragment.getParentFragmentManager(), "TAG");
    }
}
