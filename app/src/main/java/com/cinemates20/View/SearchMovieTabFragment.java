package com.cinemates20.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.Utils.Adapters.SearchMovieAdapter;
import com.cinemates20.Presenter.SearchMoviePresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class SearchMovieTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchMovieAdapter myRecyclerViewAdapter;
    private EditText searchView;
    private CharSequence search = "";
    private SearchMoviePresenter searchMoviePresenter;
    private GridLayout gridLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_search, container, false);

        searchMoviePresenter = new SearchMoviePresenter(this);

        gridLayout = root.findViewById(R.id.gridLayout);
        recyclerView = root.findViewById(R.id.recyclerViewSearch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<MovieDb> movielist = new ArrayList<>();
        setUserRecycler(movielist);

        searchView = root.findViewById(R.id.searchView);

        onSearchMovie();

        onClickGenre(root);

        return root;
    }

    public void onClickGenre(View view) {
        CardView action = view.findViewById(R.id.action);
        action.setOnClickListener(view1 -> searchMoviePresenter.onGenreClicked(28));

        CardView adventure = view.findViewById(R.id.adventure);
        adventure.setOnClickListener(view12 -> searchMoviePresenter.onGenreClicked(12));

        CardView animation = view.findViewById(R.id.animation);
        animation.setOnClickListener(view13 -> searchMoviePresenter.onGenreClicked(16));

        CardView comedy = view.findViewById(R.id.comedy);
        comedy.setOnClickListener(view14 -> searchMoviePresenter.onGenreClicked(35));

        CardView crime = view.findViewById(R.id.crime);
        crime.setOnClickListener(view15 -> searchMoviePresenter.onGenreClicked(80));

        CardView documentary = view.findViewById(R.id.documentary);
        documentary.setOnClickListener(view16 -> searchMoviePresenter.onGenreClicked(99));

        CardView drama = view.findViewById(R.id.drama);
        drama.setOnClickListener(view17 -> searchMoviePresenter.onGenreClicked(18));

        CardView family = view.findViewById(R.id.family);
        family.setOnClickListener(view18 -> searchMoviePresenter.onGenreClicked(10751));

        CardView fantasy = view.findViewById(R.id.fantasy);
        fantasy.setOnClickListener(view19 -> searchMoviePresenter.onGenreClicked(14));

        CardView history = view.findViewById(R.id.history);
        history.setOnClickListener(view110 -> searchMoviePresenter.onGenreClicked(36));

        CardView horror = view.findViewById(R.id.horror);
        horror.setOnClickListener(view111 -> searchMoviePresenter.onGenreClicked(27));

        CardView music = view.findViewById(R.id.music);
        music.setOnClickListener(view112 -> searchMoviePresenter.onGenreClicked(10402));

        CardView mistery = view.findViewById(R.id.mistery);
        mistery.setOnClickListener(view113 -> searchMoviePresenter.onGenreClicked(9648));

        CardView romance = view.findViewById(R.id.romance);
        romance.setOnClickListener(view114 -> searchMoviePresenter.onGenreClicked(10749));

        CardView scienceFiction = view.findViewById(R.id.scienceFiction);
        scienceFiction.setOnClickListener(view115 -> searchMoviePresenter.onGenreClicked(878));

        CardView thriller = view.findViewById(R.id.thriller);
        thriller.setOnClickListener(view116 -> searchMoviePresenter.onGenreClicked(53));

        CardView war = view.findViewById(R.id.war);
        war.setOnClickListener(view117 -> searchMoviePresenter.onGenreClicked(10752));

        CardView western = view.findViewById(R.id.western);
        western.setOnClickListener(view118 -> searchMoviePresenter.onGenreClicked(37));
    }

    public void onSearchMovie() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    gridLayout.setVisibility(View.GONE);
                else
                    gridLayout.setVisibility(View.VISIBLE);

                myRecyclerViewAdapter.getFilter().filter(charSequence);
                search = charSequence;
                searchMoviePresenter = (SearchMoviePresenter) new SearchMoviePresenter(SearchMovieTabFragment.this)
                        .execute(searchView.getText().toString().trim());
                setUpInterface();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals(""))
                    gridLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setUserRecycler(List<MovieDb> myTmdbArrayList) {
        myRecyclerViewAdapter = new SearchMovieAdapter(getContext(), myTmdbArrayList);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        clickListener(myRecyclerViewAdapter);
    }

    public void clickListener(SearchMovieAdapter searchMovieAdapter){
        searchMovieAdapter.setOnItemClickListener(filteredMovie -> {
            MovieCardFragment movieCardFragment = new MovieCardFragment();
            Bundle args = new Bundle();
            args.putInt("MovieID", filteredMovie.getId());
            args.putString("MovieTitle", filteredMovie.getTitle());
            args.putString("MovieUrl", filteredMovie.getPosterPath());
            args.putString("MovieOverview", filteredMovie.getOverview());
            args.putFloat("MovieRating", filteredMovie.getVoteAverage()/2);
            movieCardFragment.setArguments(args);
            Utils.changeFragment(this, movieCardFragment, R.id.nav_host_fragment_activity_main);
        });
    }

    public void setUpInterface(){
        searchMoviePresenter.setSearchMoviesCallback(this::setUserRecycler);
    }
}