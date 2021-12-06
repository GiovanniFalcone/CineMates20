package com.cinemates20.Model.DAO.Implements;

import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.people.PersonCast;

public class MovieDAO_TMDB implements MovieDAO, MovieCallback {

    @Override
    public Movie getMovieById(int idMovie) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies tmdbMovies = api.getMovies();

        Movie movie = new Movie();
        movie.setMovieDb(tmdbMovies.getMovie(idMovie, "en"));

        return movie;
    }

    @Override
    public List<Movie> getMovies(String query) {
        List<MovieDb> movieList = new ArrayList<>();

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbSearch search = api.getSearch();

        if (!query.isEmpty())
            movieList = search.searchMovie(query, 0, "", false, 0).getResults();

        List<Movie> myMovieList = new ArrayList<>();
        if(!movieList.isEmpty()){
            for(MovieDb movieDb: movieList){
                Movie movie = new Movie();
                movie.setMovieDb(movieDb);
                myMovieList.add(movie);
            }
        }

        return myMovieList;
    }

    @Override
    public List<Movie> getPopular() {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> popular = movies.getPopularMovies(null, 0).getResults();

        List<Movie> movieDbList = new ArrayList<>();
        for(MovieDb movieDb: popular){
            Movie movie = new Movie();
            movie.setMovieDb(movieDb);
            movieDbList.add(movie);
        }

        return movieDbList;
    }

    @Override
    public List<Movie> getNowPlaying() {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> nowPlaying = movies.getNowPlayingMovies(null, 0, null).getResults();

        List<Movie> movieDbList = new ArrayList<>();
        for(MovieDb movieDb: nowPlaying){
            Movie movie = new Movie();
            movie.setMovieDb(movieDb);

            movieDbList.add(movie);
        }

        return movieDbList;
    }

    @Override
    public List<Movie> getTopRated() {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> topRated = movies.getTopRatedMovies(null, 0).getResults();

        List<Movie> movieDbList = new ArrayList<>();
        for(MovieDb movieDb: topRated){
            Movie movie = new Movie();
            movie.setMovieDb(movieDb);

            movieDbList.add(movie);
        }

        return movieDbList;
    }

    @Override
    public List<Genre> getGenre(int idMovie) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();

        MovieDb movieDb = movies.getMovie(idMovie, "en", TmdbMovies.MovieMethod.lists);

        Movie movie = new Movie();
        movie.setGenreList(movieDb.getGenres());

        return movie.getGenreList();
    }

    @Override
    public List<Movie> getListMovieByGenre(int idGenre, int page) {
        List<MovieDb> movieDbList;

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        movieDbList = api.getGenre()
                .getGenreMovies(idGenre, "en", page, Boolean.TRUE)
                .getResults();

        List<Movie> movieList = new ArrayList<>();
        for(MovieDb movieDb: movieDbList){
            Movie movie = new Movie();
            movie.setMovieDb(movieDb);
            movieList.add(movie);
        }

        return movieList;
    }

    @Override
    public List<Artwork> getBackdrops(int idMovie) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        MovieImages res = api.getMovies().getImages(idMovie, "");

        return res.getBackdrops();
    }

    @Override
    public List<PersonCast> getCast(int idMovie) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        MovieDb result = api.getMovies().getMovie(idMovie, "en", TmdbMovies.MovieMethod.credits);

        Movie movie = new Movie();
        movie.setPersonCasts(result.getCast());

        return movie.getPersonCasts();
    }

    @Override
    public List<Movie> getMoviesOfList(List<Integer> idMoviesList) {
        List<MovieDb> movieDbList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");

        for (int i = 0; i < idMoviesList.size(); i++)
            movieDbList.add(api.getMovies()
                    .getMovie(idMoviesList.get(i), "en"));

        for(MovieDb movieDb: movieDbList){
            Movie movie = new Movie();
            movie.setMovieDb(movieDb);
            movieList.add(movie);
        }

        return movieList;
    }

    @Override
    public String getLogo(int idMovie) {
        String urlJason = "https://api.themoviedb.org/3/movie/" + idMovie +
                "/images?api_key=d33171b7b477fa9a989fcc5e242feec3&language=en";

        String current = "";

        try{
            URL url;

            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL(urlJason);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1){
                    current += (char)data;
                    data = inputStreamReader.read();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            JSONObject jsonObject = new JSONObject(current);
            JSONArray jsonArray = jsonObject.getJSONArray("logos");


            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            String logoPath = jsonObject1.getString("file_path");
            return "https://www.themoviedb.org/t/p/original" + logoPath;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
