package com.cinemates20.View;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Presenter.FriendListPresenter;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layout_noFriends;
    private FriendListPresenter friendListPresenter;
    private FriendsRequestPresenter friendsRequestPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        recyclerView = view.findViewById(R.id.friendsRv);
        layout_noFriends = view.findViewById(R.id.layout_noFriends);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Friends list");
        toolbar.setNavigationOnClickListener(view13 -> requireActivity().onBackPressed());

        friendsRequestPresenter = new FriendsRequestPresenter(this, getContext());

        friendListPresenter = new FriendListPresenter(this);
        friendListPresenter.friendListClicked();

        return view;
    }

    public void setRecycler(List<String> friendList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GenericAdapter<String> friendAdapter = new GenericAdapter<>(friendList, getContext());
        recyclerView.setAdapter(friendAdapter);
        clickListener(friendAdapter, friendList);
    }

    public void clickListener(GenericAdapter<String> friendAdapter, List<String> friendList) {
        friendAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String friend, int position) {
                friendsRequestPresenter.manageRemoveFriendship(friend);

                friendList.remove(position);
                recyclerView.removeViewAt(position);
                friendAdapter.notifyItemRemoved(position);
                friendAdapter.notifyItemRangeChanged(position, friendAdapter.getItemCount());
            }
        });
    }

    public void setNoFriend(boolean value) {
        if(value)
            layout_noFriends.setVisibility(View.VISIBLE);
        else
            layout_noFriends.setVisibility(View.GONE);
    }

}