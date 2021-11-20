package com.cinemates20.Presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.FeedCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.FeedFragment;
import com.cinemates20.Model.Feed;
import com.cinemates20.Model.Review;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieCardFragment;
import com.cinemates20.View.ReviewCardActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import info.movito.themoviedbapi.model.MovieDb;

public class FeedPresenter {

    private final FeedFragment feedFragment;

    public FeedPresenter(FeedFragment feedFragment) {
        this.feedFragment = feedFragment;
    }

    /**
     * This method will take and set the recent actions of the current user's friends.
     */
    public void onClickFeed() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        List<String> friends = userDAO.getFriends(currentUser);

        if(!friends.isEmpty()){
            FeedDAO feedDAO = DAOFactory.getFeedDAO(DAOFactory.FIREBASE);

            feedDAO.getNews(friends, new FeedCallback() {
                @Override
                public void onSuccess(List<Feed> news) {
                    if(!news.isEmpty()) {
                feedFragment.setRecyclerFeed(news);
                feedFragment.setNoFeed(false);
            }else
                feedFragment.setNoFeed(true);
                }
            });
        } else
            feedFragment.setNoFriends(true);
    }

    /**
     * This method will open the news that current user has clicked.
     *
     * If the current user has clicked on a review reported as a spoiler,
     * the dialog box will be shown to warn them. The user can decide to continue or not.
     *
     * If the current user has clicked on a review reported for inappropriate language,
     * the dialog box will be shown to warn them that they cannot continue.
     *
     * In the case of the evaluation, the movie card will be opened.
     *
     * @param object the object clicked by the current user
     * @param iconAuthor the icon's user of the news that will be shown in the review card
     * @param movieDb the movie that will be shown in the movie card
     */
    public void onClickItem(Feed object, String iconAuthor, MovieDb movieDb) {
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        switch (object.getItemNewsType()){
            case "review":
            case "comment":
                Review review = reviewDAO.getReviewById(object.getIdItemNews());

                List<String> friends = userDAO.getFriends(User.getCurrentUser());
                friends.add(User.getCurrentUser());
                if(!friends.contains(review.getAuthor())) {
                    Utils.showErrorDialog(feedFragment.getContext(), "You can't see this review", "You can't see this review: you must be friends with " + object.getSecondUser() + "!");
                    break;
                }

                if(review.isIsInappropriate()){
                    Utils.showErrorDialog(feedFragment.getContext(), "Error", "This review is not visible anymore.");
                } else if(review.getCounterForSpoiler() > 2){
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(feedFragment.getFragmentContext(), R.style.ThemeMyAppDialogAlertDay);
                builder.setTitle("This review may contains spoiler!");
                builder.setMessage("Do you wanna see it anyway?");
                builder.setIcon(R.drawable.ic_baseline_error_24)
                        .setPositiveButton("Go anyway", (dialogInterface, i) -> {
                            if(!review.getAuthor().equals(object.getUserOfTheNews())){
                                Uri uri = userDAO.getImageUri(review.getAuthor());
                                startActivity(object, uri.toString(), review);
                            } else
                                startActivity(object, iconAuthor, review);
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) ->
                                dialogInterface.dismiss())
                        .show();
                } else {
                    if(!review.getAuthor().equals(object.getUserOfTheNews())){
                        Uri uri = userDAO.getImageUri(review.getAuthor());
                        startActivity(object, uri.toString(), review);
                    } else
                        startActivity(object, iconAuthor, review);
                }
                break;

            case "valuation":
                MovieCardFragment movieCardFragment = new MovieCardFragment();
                Bundle args = new Bundle();
                args.putInt("MovieID", movieDb.getId());
                args.putString("MovieTitle", movieDb.getTitle());
                args.putString("MovieUrl", movieDb.getPosterPath());
                args.putString("MovieImg", movieDb.getPosterPath());
                args.putString("MovieOverview", movieDb.getOverview());
                args.putFloat("MovieRating", movieDb.getVoteAverage());
                args.putString("MoviePoster", movieDb.getPosterPath());
                movieCardFragment.setArguments(args);
                Utils.changeFragment_BottomAnim(feedFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
                break;
        }
    }

    private void startActivity(Feed object, String iconAuthor, Review review) {
        Intent intent = new Intent(feedFragment.getContext(), ReviewCardActivity.class);
        intent.putExtra("Icon", iconAuthor);
        intent.putExtra("idMovie", object.getMovie());
        intent.putExtra("Review", review);

        String username = User.getCurrentUser();

        if(Objects.requireNonNull(username).equals(object.getUserOfTheNews()))
            intent.putExtra("PersonalReview", true);

        feedFragment.startActivity(intent);
    }
}
