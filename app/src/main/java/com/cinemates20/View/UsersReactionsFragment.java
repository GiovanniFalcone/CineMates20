package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Presenter.UsersReactionPresenter;
import com.cinemates20.Utils.Adapters.SearchUsersAdapter;

import java.util.ArrayList;
import java.util.List;


public class UsersReactionsFragment extends Fragment {

    private UsersReactionPresenter usersReactionPresenter;
    private RecyclerView recyclerView;
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

        recyclerView = view.findViewById(R.id.recyclerView);

        arg = getArguments();

        usersReactionPresenter.onClickReactionType();

        return view;
    }

    public String getIdReview(){
        return getArguments().getString("idReview");
    }

    public int getIndexTab(){
        return arg.getInt("index");
    }

    public void setRecycler(List<User> userList) {
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //Da aggiustare
        List<String> receivedList = new ArrayList<>(),
                friends = new ArrayList<>(),
                sentList = new ArrayList<>();
        SearchUsersAdapter searchUsersAdapter = new SearchUsersAdapter(getContext(), userList, sentList, receivedList, friends);
        recyclerView.setAdapter(searchUsersAdapter);
    }
}