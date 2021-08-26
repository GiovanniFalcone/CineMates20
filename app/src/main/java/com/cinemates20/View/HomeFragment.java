package com.cinemates20.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.cinemates20.Presenter.HomePresenter;
import com.cinemates20.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private MovieAdapter movieAdapter, movieAdapter2, movieAdapter3;
    private RecyclerView recyclerView, recyclerView2, recyclerView3;
    private LinearLayoutManager horizontalLayoutManager, horizontalLayoutManager2, horizontalLayoutManager3;
    private HomePresenter homePresenter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homePresenter = (HomePresenter) new HomePresenter(this).execute();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView2 = view.findViewById(R.id.recycleView2);
        recyclerView3 = view.findViewById(R.id.recycleView3);

        setUpHomeInterface();

        return view;
    }

    public void setUpHomeInterface(){
        homePresenter.setHomeFragment(movie -> {
            setRecyclers(movie.getLeft(), recyclerView, movieAdapter, horizontalLayoutManager);
            setRecyclers(movie.getMiddle(), recyclerView2, movieAdapter2, horizontalLayoutManager2);
            setRecyclers(movie.getRight(), recyclerView3, movieAdapter3, horizontalLayoutManager3);
        });
    }

    public void setRecyclers(List<MovieDb> movieDbList, RecyclerView recyclerViewX,
                             MovieAdapter movieAdapterX, LinearLayoutManager horizontalLayoutManagerX){
        movieAdapterX = new MovieAdapter(movieDbList, getContext());
        horizontalLayoutManagerX = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewX.setLayoutManager(horizontalLayoutManagerX);
        recyclerViewX.setAdapter(movieAdapterX);
        clickListener(movieAdapterX);
    }

    public void clickListener(MovieAdapter movieAdapterX){
        movieAdapterX.setOnItemClickListener((movieDbList, position, view) ->
                homePresenter.onClickMovie(movieDbList, position));
    }
}
