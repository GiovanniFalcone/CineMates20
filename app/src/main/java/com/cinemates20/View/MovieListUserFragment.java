package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Presenter.MovieListUserPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MovieListAdapter;

import java.util.List;

public class MovieListUserFragment extends Fragment {

    private MovieListUserPresenter movieListUserPresenter;
    private RecyclerView recyclerView;
    private String clickedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list_user, container, false);

        recyclerView = view.findViewById(R.id.movieListUserRecyclerView);

        clickedList = requireArguments().getString("listClicked");

        movieListUserPresenter = new MovieListUserPresenter(this);
        movieListUserPresenter.seeMovieList();

        return view;
    }

    public void setRecycler(List<MovieDb> movieDbList) {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), movieDbList);
        recyclerView.setAdapter(movieListAdapter);
        clickListener(movieListAdapter);
    }

    private void clickListener(MovieListAdapter movieListAdapter) {
        movieListAdapter.setOnItemClickListener(new MovieListAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String idMovie) {
                movieListUserPresenter.removeMovieFromList(idMovie, clickedList);
            }
        });
    }

    public String getClickedList(){
        return clickedList;
    }

}