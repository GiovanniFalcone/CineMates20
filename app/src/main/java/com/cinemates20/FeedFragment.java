package com.cinemates20;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cinemates20.DAO.Implements.FeedDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.Feed;
import com.cinemates20.Model.Notification;
import com.cinemates20.Model.Review;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class FeedFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        TextView name = root.findViewById(R.id.textView5);
        String mUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        name.setText(mUser);

        UserDAO_Firestore userDAO = new UserDAO_Firestore(getContext());
        List<String> friendList = userDAO.getFriends(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FeedDAO feedDAO = new FeedDAO_Firestore(getContext());
        List<Review> feedDAOReviewList = feedDAO.getReviewList(friendList);
        List<Notification> feedDAONewFriendshipList = feedDAO.getNewFriendship(friendList);


        Feed feed = new Feed(feedDAOReviewList, null, feedDAONewFriendshipList);
        Log.d("Feed", "" + feed);

        return root;
    }
}