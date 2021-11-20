package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.User;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.WriteReviewActivity;

import com.google.firebase.Timestamp;

import java.util.Date;

public class WriteReviewPresenter {

    private final WriteReviewActivity writeReviewActivity;

    public WriteReviewPresenter(WriteReviewActivity writeReviewActivity){
        this.writeReviewActivity = writeReviewActivity;
    }

    public void clickAddReview(int idMovie, String textReview) {
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);

        String author = User.getCurrentUser();

        Timestamp dateAndTime =  new Timestamp(new Date());

        reviewDAO.addReview(author, textReview, idMovie, dateAndTime, new ReviewCallback() {
            @Override
            public void onSuccess(String idReview) {
                if(idReview != null) {
                    Utils.showDialog(writeReviewActivity.getActivityContext(), "Done!",
                            "Your review has been successfully added.");

                    //save the news into feed
                    FeedDAO feedDAO = DAOFactory.getFeedDAO(DAOFactory.FIREBASE);
                    feedDAO.addNews(author, "", String.valueOf(idMovie), idReview, "review", 0f, dateAndTime);
                } else
                    Utils.showErrorDialog(writeReviewActivity.getActivityContext(), "Error", "Something went wrong.");
            }
        });

        writeReviewActivity.closeActivity();
    }
}