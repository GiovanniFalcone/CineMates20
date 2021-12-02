package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinemates20.Model.Review;

import com.cinemates20.Presenter.SeeAllReviewPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;

import java.util.List;

public class SeeAllReviewFragment extends Fragment {

    private SeeAllReviewPresenter seeAllReview;
    private RecyclerView recyclerViewReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_see_all_review, container, false);

        recyclerViewReview = view.findViewById(R.id.recyclerSeeAllReview);

        seeAllReview = new SeeAllReviewPresenter(this);

        int idMovie = requireArguments().getInt("MovieID", 0);

        seeAllReview.seeAllReview(idMovie);

        return view;
    }

    public void setRecycler(List<Review> authorList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewReview.setLayoutManager(layoutManager);
        GenericAdapter<Review> reviewUserAdapter = new GenericAdapter<>(authorList, getContext());
        recyclerViewReview.setAdapter(reviewUserAdapter);
        clickListener(reviewUserAdapter);
    }

    public void clickListener(GenericAdapter<Review> reviewUserAdapter) {
        reviewUserAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Review review, String iconAuthor, ImageView authorIcon, TextView authorName) {
                seeAllReview.onClickSeeReview(review, iconAuthor, authorIcon, authorName, SeeAllReviewFragment.this);
            }
        });
    }
}