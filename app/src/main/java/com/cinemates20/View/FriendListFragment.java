package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Presenter.FriendListPresenter;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.FriendAdapter;

import java.util.List;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendListPresenter friendListPresenter;
    private FriendsRequestPresenter friendsRequestPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        recyclerView = view.findViewById(R.id.friendsRv);

        friendsRequestPresenter = new FriendsRequestPresenter(this);

        friendListPresenter = new FriendListPresenter(this);
        friendListPresenter.friendListClicked();

        return view;
    }

    public void setRecycler(List<String> friendList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        FriendAdapter friendAdapter = new FriendAdapter(getContext(), friendList);
        recyclerView.setAdapter(friendAdapter);
        clickListner(friendAdapter);
    }

    public void clickListner(FriendAdapter friendAdapter) {
        friendAdapter.setOnItemClickListener(new FriendAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String user, String buttonState, int position, View view) {
                friendsRequestPresenter.manageRemoveFriendship(user, getContext());
            }
        });
    }

}