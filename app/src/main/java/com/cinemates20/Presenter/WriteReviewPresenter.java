package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.WriteReviewActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class WriteReviewPresenter {

    private final WriteReviewActivity writeReviewActivity;

    public WriteReviewPresenter(WriteReviewActivity writeReviewActivity){
        this.writeReviewActivity = writeReviewActivity;
    }

    public void clickAddReview() {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        ReviewDAO reviewDAO = daoFactory.getReviewDAO();

        int idMovie = writeReviewActivity.getIdMovie();
        String textReview = writeReviewActivity.getReviewText();
        String author = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        new MaterialAlertDialogBuilder(writeReviewActivity.getActivityContext(), R.style.ThemeMyAppDialogAlertDay)
                .setTitle("Confirm review")
                .setMessage(R.string.confirm_review)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    Timestamp dateAndTime =  new Timestamp(new Date());

                    reviewDAO.updateReview(author, textReview, idMovie, dateAndTime, new ReviewCallback() {
                        @Override
                        public void onSuccess(String idReview) {
                            if(idReview != null) {
                                Utils.showDialog(writeReviewActivity.getActivityContext(), "Done!",
                                        "Your review has been successfully added.");

                                //save the news into feed
                                FeedDAO feedDAO = daoFactory.getFeedDAO();
                                feedDAO.addNews(author, "", String.valueOf(idMovie), idReview, "review", 0f, dateAndTime);
                            } else
                                Utils.showErrorDialog(writeReviewActivity.getActivityContext(), "Error", "Something went wrong.");
                        }
                    });

                    writeReviewActivity.closeActivity();
                })
                .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }
}