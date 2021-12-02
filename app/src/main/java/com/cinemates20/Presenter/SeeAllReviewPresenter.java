package com.cinemates20.Presenter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.View.ReviewCardActivity;
import com.cinemates20.View.SeeAllReviewFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class SeeAllReviewPresenter {

    private final SeeAllReviewFragment seeAllReviewFragment;

    public SeeAllReviewPresenter(SeeAllReviewFragment seeAllReviewFragment) {
        this.seeAllReviewFragment = seeAllReviewFragment;
    }

    public void seeAllReview(int idMovie) {
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        User user = userDAO.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        List<Review> listAuthor = reviewDAO.getUserReviewByMovie(idMovie, user.getFriends(), user.getUsername(), true);
        seeAllReviewFragment.setRecycler(listAuthor);
    }

    /**
     * Open the review clicked of the current user's friend
     * and pass author name and movie title to it
     * @param reviewClicked the review that user want to see
     * @param iconAuthor the icon's url of review's author
     * @param authorIcon the image of review's author, it's used for animation
     * @param authorName the name of review's author, it's used for animation
     */
    public void onClickSeeReview(Review reviewClicked, String iconAuthor, ImageView authorIcon, TextView authorName, Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Icon", iconAuthor);
        intent.putExtra("Review", reviewClicked);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        if(username.equals(reviewClicked.getAuthor()))
            intent.putExtra("PersonalReview", true);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String> (authorIcon, "authorIcon");
        pairs[1] = new Pair<View, String>(authorName, "authorName");

        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(fragment.requireActivity(), pairs);
        fragment.startActivity(intent, optionsCompat.toBundle());
    }
}
