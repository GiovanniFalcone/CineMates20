package com.cinemates20.View;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.cinemates20.Model.Review;
import com.cinemates20.Presenter.MyReviewsPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class MyReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyReviewsPresenter myReviewsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reviews, container, false);

        recyclerView = view.findViewById(R.id.recyclerReview);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("My reviews");
        toolbar.setNavigationOnClickListener(view13 -> requireActivity().onBackPressed());

        myReviewsPresenter = new MyReviewsPresenter(this);
        myReviewsPresenter.myReviewsClicked();

        return view;
    }

    public void setRecycler(List<Review> reviewList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GenericAdapter<Review> personalReviewAdapter = new GenericAdapter<>(reviewList, getContext());
        recyclerView.setAdapter(personalReviewAdapter);
        clickListener(personalReviewAdapter);
    }

    public void clickListener(GenericAdapter<Review> personalReviewAdapter) {
        personalReviewAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Review personalReviewClicked) {
                myReviewsPresenter.onClickSeeReview(personalReviewClicked);
            }
        });
    }
}