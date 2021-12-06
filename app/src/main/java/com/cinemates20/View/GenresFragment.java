package com.cinemates20.View;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Model.Movie;
import com.cinemates20.Presenter.GenresPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class GenresFragment extends Fragment {

    private GenresPresenter genresPresenter;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private NestedScrollView nestedScrollView;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        recyclerView = view.findViewById(R.id.movieListByGenre);
        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        nestedScrollView = view.findViewById(R.id.nestedScroll);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(requireArguments().getString("genreName"));
        toolbar.setNavigationOnClickListener(view13 -> requireActivity().onBackPressed());
        toolbar.setOnClickListener(view1 -> {
            nestedScrollView.fling(0);
            nestedScrollView.smoothScrollTo(0, 0);
        });

        genresPresenter = new GenresPresenter(this);

        genresPresenter.onGenreClicked(1, movieAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View v = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

            int diff = (v.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                    .getScrollY()));

            if (diff == 0) {
                page = page + 1;
                genresPresenter.onGenreClicked(page, movieAdapter);
            }
        });

        return view;
    }

    public int idGenre(){
        return requireArguments().getInt("idGenre");
    }

    public void setRecycler(List<Movie> movieDbList) {
        movieAdapter = new MovieAdapter(movieDbList, getContext(), 4);
        recyclerView.setAdapter(movieAdapter);
        clickItemRecycler(movieAdapter);
    }

    public void clickItemRecycler(MovieAdapter movieAdapter) {
        movieAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Movie movieClicked) {
                genresPresenter.onMovieClicked(movieClicked);
            }
        });
    }
}