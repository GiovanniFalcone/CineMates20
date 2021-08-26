package com.cinemates20.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.cinemates20.Model.User;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.Utils.Adapters.SearchUsersAdapter;
import com.cinemates20.Presenter.SearchUserPresenter;
import com.cinemates20.R;

import java.util.List;

public class SearchUserTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchUserPresenter searchUserPresenter;
    private FriendsRequestPresenter friendsRequestPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchUserPresenter = new SearchUserPresenter(this);
        friendsRequestPresenter = new FriendsRequestPresenter(this);

        View root = inflater.inflate(R.layout.fragment_user_tab, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewSearch);
        searchView = root.findViewById(R.id.searchView);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        searchViewPopulation();
        return root;
    }

    public void searchViewPopulation() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.equals("")) {
                    searchUserPresenter.onSearchUsers(query);
                }else
                    recyclerView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    public void setRecycler(List<User> searchedUserList, List<String> sentList, List<String> receivedList,List<String> friends) {
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SearchUsersAdapter searchUsersAdapter = new SearchUsersAdapter(getContext(), searchedUserList, sentList, receivedList, friends);
        recyclerView.setAdapter(searchUsersAdapter);
        searchUsersAdapter.notifyDataSetChanged();
        clickListener(searchUsersAdapter);
    }

    public void clickListener(SearchUsersAdapter searchUserAdapter) {
        searchUserAdapter.setOnItemClickListener((user, buttonState, position, view) -> {
            Log.d("msg", "mi trovo in clickListner " + user);
            if(buttonState.equals("friend"))
                friendsRequestPresenter.manageRemoveFriendship(user, getContext());
            else if(buttonState.equals("appendRequestReceived"))
                friendsRequestPresenter.manageAcceptOrDeclineFriendRequest(user, "confirm", getContext());
            else
                friendsRequestPresenter.manageSentOrDeleteFriendRequest(user, buttonState);
        });

    }
}