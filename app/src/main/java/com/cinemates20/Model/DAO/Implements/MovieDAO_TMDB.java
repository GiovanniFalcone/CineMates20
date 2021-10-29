package com.cinemates20.Model.DAO.Implements;

import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;

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
    public void getMovieById(int idMovie, MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies tmdbMovies = api.getMovies();

        MovieDb movieDb = tmdbMovies.getMovie(idMovie, "en");

        movieCallback.setResult(movieDb);
    }

    @Override
    public void getMovies(String query, MovieCallback movieCallback) {
        List<MovieDb> movieList = new ArrayList<>();

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbSearch search = api.getSearch();

        if (!query.isEmpty())
            movieList = search.searchMovie(query, 0, "", false, 0).getResults();


        movieCallback.setMovie(movieList);
    }

    @Override
    public void getPopular(MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> popular = movies.getPopularMovies(null, 0).getResults();

        movieCallback.setMovie(popular);
    }

    @Override
    public void getNowPlaying(MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> nowPlaying = movies.getNowPlayingMovies(null, 0, null).getResults();

        movieCallback.setMovie(nowPlaying);
    }

    @Override
    public void getTopRated(MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> topRated = movies.getTopRatedMovies(null, 0).getResults();

        movieCallback.setMovie(topRated);
    }

    @Override
    public void getGenre(int idMovie, MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();

        MovieDb movieDb = movies.getMovie(idMovie, "en", TmdbMovies.MovieMethod.lists);
        List<Genre> genres = movieDb.getGenres();

        movieCallback.setGenre(genres);
    }

    @Override
    public void getListMovieByGenre(int idGenre, int page, MovieCallback movieCallback) {
        List<MovieDb> movieDbList;

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        movieDbList = api.getGenre()
                .getGenreMovies(idGenre, "en", page, Boolean.TRUE)
                .getResults();

        movieCallback.setMovie(movieDbList);
    }

    @Override
    public void getBackdrops(int idMovie, MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        MovieImages res = api.getMovies().getImages(idMovie, "");
        List<Artwork> artworks = res.getBackdrops();

        movieCallback.setArtworks(artworks);
    }

    @Override
    public void getCast(int idMovie, MovieCallback movieCallback) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        MovieDb result = api.getMovies().getMovie(idMovie, "en", TmdbMovies.MovieMethod.credits);
        List<PersonCast> personCast = result.getCast();

        movieCallback.setCast(personCast);
    }

    @Override
    public List<MovieDb> getMoviesOfList(List<Integer> idMoviesList) {
        List<MovieDb> movieDbList = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");

            for (int i = 0; i < idMoviesList.size(); i++)
                movieDbList.add(api.getMovies()
                        .getMovie(idMoviesList.get(i), "en"));

        });

        //Waits for the computation to complete, and then retrieves its result.
        try {
            future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        return movieDbList;
    }

    @Override
    public void getLogo(int idMovie, MovieCallback movieCallback) {
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
            movieCallback.setLogo("https://www.themoviedb.org/t/p/original" + logoPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
