package com.example.caio.upcomingmovieslist.data.source;

import android.support.annotation.NonNull;

import com.example.caio.upcomingmovieslist.data.Movie;

import java.util.List;

/**
 * Created by Caio on 10/02/2018.
 * Main entry point for accessing movies data.
 */

public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {
        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    void getMovies(int page, @NonNull LoadMoviesCallback callback);

    void getMovie(@NonNull Integer movieId, int page, @NonNull GetMovieCallback callback);
}
