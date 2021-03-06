package com.cinemates20.Presenter;

import android.widget.Toast;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.CommentDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReportDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.ReviewCardActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public class ReportPresenter {

    private final ReviewCardActivity reviewCardActivity;

    public ReportPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    /**
     * This method allows the user to report comments for spoilers and inappropriate language.
     * A dialog will be displayed and the user will can select the reason for the report.
     * @param commentSelected the comment that user want to report
     */
    public void onClickOption(Comment commentSelected){
        final CharSequence[] charSequence = new CharSequence[] {"Report for spoiler","Report for inappropriate language"};
        AtomicInteger selected = new AtomicInteger();

        String reporter = User.getCurrentUser();

        CommentDAO commentDAO = DAOFactory.getCommentDAO(DAOFactory.FIREBASE);

        new MaterialAlertDialogBuilder(reviewCardActivity.getActivityContext(), R.style.ThemeMyAppDialogAlertDay)
                .setTitle("Report this comment")
                .setSingleChoiceItems(charSequence, 0, (dialogInterface, i) -> selected.set(i))
                .setPositiveButton("Send", (dialogInterface, i) -> {

                    ReportDAO reportDAO = DAOFactory.getReportDAO(DAOFactory.FIREBASE);
                    switch (selected.get()){
                        case 0:
                            commentDAO.updateCounter(commentSelected.getIdComment(), "spoiler");
                            Toast.makeText(reviewCardActivity.getActivityContext(), "Report for spoiler sent.", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            boolean reportExist = reportDAO.checkIfReportExist(commentSelected.getIdComment());
                            if (!reportExist)
                                reportDAO.addReport(commentSelected.getIdComment(), commentSelected.getAuthor(), reporter, "comment");
                            else
                                reportDAO.updateReport(commentSelected.getIdComment(), reporter);
                            commentDAO.updateCounter(commentSelected.getIdComment(), "language");
                            int counterLanguage = commentDAO.getCounter(commentSelected.getIdComment(), "language").intValue();
                            if(counterLanguage > 2)
                                commentDAO.changeState(commentSelected.getIdComment(), "language");
                            Toast.makeText(reviewCardActivity.getActivityContext(), "Report for inappropriate language successfully send to admin. " +
                                    "We will send you a notification with the result of report.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.dismiss())
        .show();
    }

    /**
     * This method allows the user to report the review for spoilers and inappropriate language
     * @param reportType the type of report: spoiler/language
     */
    public void reportClicked(String idReview, String reportType) {
        String currentUser = User.getCurrentUser();

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        Review review = reviewDAO.getReviewById(idReview);

        ReportDAO reportDAO = DAOFactory.getReportDAO(DAOFactory.FIREBASE);

        if(reportType.equals("spoiler"))
            Utils.showDialog(reviewCardActivity.getActivityContext(), "Done!", "Report for spoiler successfully added.");
        else{
            if(!reportDAO.checkIfReportExist(idReview))
                reportDAO.addReport(idReview, review.getAuthor(), currentUser, "review");
            else
                reportDAO.updateReport(idReview, currentUser);
            Utils.showDialog(reviewCardActivity.getActivityContext(), "Done!", "Report for inappropriate language successfully send to admin." +
                    "We will send you a notification with the result of report.");
        }

        reviewDAO.updateCounter(idReview, reportType);

        if(reportType.equals("language")){
            if(review.getCounterForLanguage() >= 2)
                reviewDAO.changeState(idReview, reportType);
        }
    }
}
