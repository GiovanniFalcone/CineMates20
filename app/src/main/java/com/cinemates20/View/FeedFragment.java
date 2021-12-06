package com.cinemates20.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.Model.Feed;
import com.cinemates20.Model.Movie;
import com.cinemates20.Presenter.FeedPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private View layout_noResults, layout_noFriends;
    private FeedPresenter feedPresenter;
    private RelativeLayout loadingPanel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = root.findViewById(R.id.recyclerFeed);
        layout_noResults = root.findViewById(R.id.layout_noResults);
        layout_noFriends = root.findViewById(R.id.layout_noFriends);
        loadingPanel = root.findViewById(R.id.loadingPanel);

        feedPresenter = new FeedPresenter(this);
        feedPresenter.onClickFeed();

        NestedScrollView nestedScrollView = root.findViewById(R.id.fragment_feed);
        button = root.findViewById(R.id.onScrollButton);
        // go immediately to the top
        button.setOnClickListener(view -> nestedScrollView.fullScroll(View.FOCUS_UP));
        // hide button while scrolling down and show it if scrolling top
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(scrollY>oldScrollY){
                button.setVisibility(View.INVISIBLE);
            } else button.setVisibility(View.VISIBLE);
        });

        return root;
    }

    public void setRecyclerFeed(List<Feed> news) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GenericAdapter<Feed> feedAdapter = new GenericAdapter<>(news, getContext());
        recyclerView.setAdapter(feedAdapter);
        button.setVisibility(View.VISIBLE);
        loadingPanel.setVisibility(View.GONE);
        clickListener(feedAdapter);
    }

    private void clickListener(GenericAdapter<Feed> feedAdapter) {
        feedAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Feed object, String iconAuthor, Movie movieDb) {
                feedPresenter.onClickItem(object, iconAuthor, movieDb);
            }
        });
    }

    public void setNoFeed(boolean value){
        if(value) {
            layout_noResults.setVisibility(View.VISIBLE);
            loadingPanel.setVisibility(View.GONE);
        }else
            layout_noResults.setVisibility(View.GONE);
    }

    public void setNoFriends(boolean value){
        if(value) {
            layout_noFriends.setVisibility(View.VISIBLE);
            loadingPanel.setVisibility(View.GONE);
        }else
            layout_noFriends.setVisibility(View.GONE);
    }

    public Context getFragmentContext() {
        return getContext();
    }
}