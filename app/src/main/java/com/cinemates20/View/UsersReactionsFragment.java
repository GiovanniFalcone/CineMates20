package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cinemates20.Model.User;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.R;
import com.cinemates20.Presenter.UsersReactionPresenter;
import com.cinemates20.Utils.Adapters.SearchUsersAdapter;

import java.util.List;

public class UsersReactionsFragment extends Fragment {

    private UsersReactionPresenter usersReactionPresenter;
    private FriendsRequestPresenter friendsRequestPresenter;
    private RecyclerView recyclerView;
    private TextView numberReactions;
    private Bundle arg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_reactions, container, false);

        usersReactionPresenter = new UsersReactionPresenter(this);
        friendsRequestPresenter = new FriendsRequestPresenter(this, getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        numberReactions = view.findViewById(R.id.totalReaction);

        arg = getArguments();

        usersReactionPresenter.onClickReactionType();

        return view;
    }

    public String getIdReview(){
        return requireArguments().getString("idReview");
    }

    public int getIndexTab(){
        return arg.getInt("index");
    }

    public User getUser(){
        return (User) arg.getSerializable("User");
    }

    public List<String> getSentList() {
        return arg.getStringArrayList("sentList");
    }

    public List<String> getReceivedList() {
        return arg.getStringArrayList("receivedList");
    }

    public void setNumberReactions(int number) {
        numberReactions.setText(String.format("Total reaction: %d", number));
    }

    public void setRecycler(List<User> userList, List<String> sentList, List<String> receivedList, List<String> friends, String username) {
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SearchUsersAdapter searchUsersAdapter = new SearchUsersAdapter(getContext(), userList, sentList, receivedList, friends, username);
        recyclerView.setAdapter(searchUsersAdapter);
        clickListener(searchUsersAdapter);
    }

    public void clickListener(SearchUsersAdapter searchUserAdapter) {
        searchUserAdapter.setOnItemClickListener((user, buttonState, position, view) -> {
            if(buttonState.equals("friend"))
                friendsRequestPresenter.manageRemoveFriendship(user);
            else if(buttonState.equals("appendRequestReceived"))
                friendsRequestPresenter.manageAcceptOrDeclineFriendRequest(user, "confirm");
            else
                friendsRequestPresenter.manageSentOrDeleteFriendRequest(user, buttonState);
        });
    }
}