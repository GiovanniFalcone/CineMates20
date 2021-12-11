package com.cinemates20.Presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.Movie;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.HomeFragment;
import com.cinemates20.View.MovieCardFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HomePresenter{

    private final HomeFragment homeFragment;

    public HomePresenter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    /**
     * This method will get the list of popular, top rated and now playing movie and will set
     * the lists into home.
     */
    public void setHome() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            AtomicInteger idMovie_nowPlaying = new AtomicInteger();

            //Set popularMovie
            List<Movie> popular = movieDAO.getPopular();
            handler.post(() -> homeFragment.setFeaturedMovie(popular));

            //Set topRatedMovie
            List<Movie> topRated = movieDAO.getTopRated();
            handler.post(() -> homeFragment.setRecycler(topRated));

            //Set featured movie
            List<Movie> nowPlaying = movieDAO.getNowPlaying();
            int randomNum = (int) ((Math.random() * (20 - 1)) + 1);
            Movie movieDb = nowPlaying.get(randomNum);
            idMovie_nowPlaying.set(movieDb.getId());
            handler.post(() -> homeFragment.setRandomMovie(movieDb));


            String pathLogo = movieDAO.getLogo(idMovie_nowPlaying.get());
            if(pathLogo != null)
                homeFragment.setLogoMovie(pathLogo);
        });
    }

    /**
     * Open the movie card of clicked movie
     * @param movieClicked the movie clicked by user
     */
    public void onClickMovie(Movie movieClicked) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Fragment current = homeFragment.requireActivity()
                .getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (!current.getClass().equals(movieCardFragment.getClass())) {
            Bundle args = new Bundle();
            args.putInt("MovieID", movieClicked.getId());
            args.putString("MovieTitle", movieClicked.getTitle());
            args.putString("MovieUrl", movieClicked.getPosterPath());
            args.putString("MovieImg", movieClicked.getPosterPath());
            args.putString("MovieOverview", movieClicked.getOverview());
            args.putString("MoviePoster", movieClicked.getPosterPath());
            movieCardFragment.setArguments(args);
            Utils.changeFragment_BottomAnim(homeFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
        } else
            Toast.makeText(homeFragment.getContext(), "Be patient, the card is loading", Toast.LENGTH_SHORT).show();
    }
}
