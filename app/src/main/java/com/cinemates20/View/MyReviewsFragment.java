package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Presenter.MyReviewsPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MyReviewsAdapter;

import java.util.List;

public class MyReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyReviewsAdapter myReviewsAdapter;
    private MyReviewsPresenter myReviewsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reviews, container, false);

        recyclerView = view.findViewById(R.id.recyclerReview);

        myReviewsPresenter = new MyReviewsPresenter(this);
        myReviewsPresenter.myReviewsClicked();

        return view;
    }

    public void setRecycler(List<MovieDb> movieDbList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MyReviewsAdapter myReviewsAdapter = new MyReviewsAdapter(getContext(), movieDbList);
        recyclerView.setAdapter(myReviewsAdapter);
        clickListener(myReviewsAdapter);
    }

    public void clickListener(MyReviewsAdapter myReviewsAdapter) {
        myReviewsAdapter.setOnItemClickListener(new MyReviewsAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String movieTitle, int position) {
                myReviewsPresenter.onClickSeeReview(movieTitle, position);
            }
        });
    }
}