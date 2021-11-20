package com.cinemates20.Presenter;

import android.content.Intent;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.View.MyReviewsFragment;
import com.cinemates20.View.ReviewCardActivity;

import java.util.List;

public class MyReviewsPresenter {

    private final MyReviewsFragment myReviewsFragment;

    public MyReviewsPresenter(MyReviewsFragment myReviewsFragment){
        super();
        this.myReviewsFragment = myReviewsFragment;
    }

    public void myReviewsClicked(){
        String currentUser = User.getCurrentUser();

        //Get all reviews of current user
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
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
