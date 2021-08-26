package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Presenter.GenresPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MovieAdapter;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class GenresFragment extends Fragment {

    private GenresPresenter genresPresenter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        recyclerView = view.findViewById(R.id.movieListByGenre);

        genresPresenter = new GenresPresenter(this);

        genresPresenter.onGenreClicked();

        return view;
    }

    public int idGenre(){
        return requireArguments().getInt("idGenre");
    }

    public void setRecycler(List<MovieDb> movieDbList) {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(movieDbList, getContext());
        recyclerView.setAdapter(movieAdapter);
        clickItemRecycler(movieAdapter);
    }

    public void clickItemRecycler(MovieAdapter movieAdapter) {
        movieAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void onItemClickListener(List<MovieDb> movieDbList, int position, View view) {
                genresPresenter.onMovieClicked(movieDbList, position);
            }
        });
    }
}