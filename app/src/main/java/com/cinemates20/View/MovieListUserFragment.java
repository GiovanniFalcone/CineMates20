package com.cinemates20.View;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cinemates20.Presenter.MovieListUserPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class MovieListUserFragment extends Fragment {

    private MovieListUserPresenter movieListUserPresenter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list_user, container, false);

        recyclerView = view.findViewById(R.id.movieListUserRecyclerView);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setTitle(getNameClickedList());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getNameClickedList());
        toolbar.setNavigationOnClickListener(view13 -> requireActivity().onBackPressed());

        TextView description = view.findViewById(R.id.descriptionTextView);
        description.setText(getDescriptionClickedList());

        movieListUserPresenter = new MovieListUserPresenter(this);
        movieListUserPresenter.seeMovieList();

        return view;
    }

    public void setRecycler(List<MovieDb> movieDbList) {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(movieDbList, getContext(), 3);
        recyclerView.setAdapter(movieAdapter);
        clickListener(movieAdapter, movieDbList);
    }

    private void clickListener(MovieAdapter movieAdapter, List<MovieDb> movieDbList) {
        movieAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void onItemClickListener(MovieDb movieClicked, int position) {
                movieListUserPresenter.removeMovieFromList(String.valueOf(movieClicked.getId()), getNameClickedList());

                movieDbList.remove(position);
                recyclerView.removeViewAt(position);
                movieAdapter.notifyItemRemoved(position);
                movieAdapter.notifyItemRangeChanged(position, movieAdapter.getItemCount());
            }
        });
    }

    public String getNameClickedList(){
        return requireArguments().getString("nameListClicked");
    }

    public String getIdClickedList(){ return requireArguments().getString("idListClicked"); }

    public String getDescriptionClickedList(){ return requireArguments().getString("descriptionListClicked"); }

}