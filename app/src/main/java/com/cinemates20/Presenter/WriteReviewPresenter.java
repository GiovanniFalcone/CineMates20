package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.WriteReviewActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WriteReviewPresenter {

    private WriteReviewActivity writeReviewActivity;
    private ReviewDAO reviewDAO;
    private UserDAO userDAO;

    public WriteReviewPresenter(WriteReviewActivity writeReviewActivity){
        this.writeReviewActivity = writeReviewActivity;
    }

    public void clickAddReview() {
        reviewDAO = new ReviewDAO_Firestore(writeReviewActivity.getApplication());
        userDAO = new UserDAO_Firestore(writeReviewActivity.getApplication());

        int idMovie = writeReviewActivity.getIdMovie();
        String title = writeReviewActivity.getMovieTitle();
        String textReview = writeReviewActivity.getReviewText();
        String currentUser = writeReviewActivity.getCurrentUser();


        User author = userDAO.getUsername(currentUser);

        new MaterialAlertDialogBuilder(writeReviewActivity.getActivityContext())
                .setTitle("Conferma recensione")
                .setMessage(R.string.conferma_recensione)
                .setPositiveButton("Si", (dialogInterface, i) -> {
                    reviewDAO.saveReview(author.getUsername(), textReview, idMovie, title);
                    Utils.showDialog(writeReviewActivity.getActivityContext(), "Recensione scritta con successo",
                            "La tua recensione è stata salvata correttamente");
                    writeReviewActivity.finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();


        //MOSTRARE NEL RECYCLER DELLA MOVIE CARD LA RECENSIONE
        //AGGIUNGERE LA RECENSIONE NEL RECYCLER DELLE RECENSIONI PERSONALI
    }
}