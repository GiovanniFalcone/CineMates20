package com.cinemates20.Presenter;

import android.content.Intent;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.View.MyReviewsFragment;
import com.cinemates20.View.ReviewCardActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MyReviewsPresenter {

    private final MyReviewsFragment myReviewsFragment;

    public MyReviewsPresenter(MyReviewsFragment myReviewsFragment){
        super();
        this.myReviewsFragment = myReviewsFragment;
    }

    public void myReviewsClicked(){
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        //Get all reviews of current user
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        ReviewDAO reviewDAO = daoFactory.getReviewDAO();
        List<Review> reviewList = reviewDAO.getListReviews(currentUser);

        myReviewsFragment.setRecycler(reviewList);
    }

    public void onClickSeeReview(Review personalReviewClicked) {
        Intent intent = new Intent(myReviewsFragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Review", personalReviewClicked);
        intent.putExtra("idMovie", personalReviewClicked.getIdMovie());
        intent.putExtra("PersonalReview", true);
        myReviewsFragment.startActivity(intent);
    }

}
