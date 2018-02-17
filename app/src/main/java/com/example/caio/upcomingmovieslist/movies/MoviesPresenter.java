package com.example.caio.upcomingmovieslist.movies;

import android.support.annotation.NonNull;

import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.data.source.MoviesDataSource;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;

import java.util.List;

/**
 * Created by Caio on 13/02/2018.
 * Listens to user actions from the UI ({@link MoviesFragment}), retrieves the data and updates the
 * UI as required.
 */

public class MoviesPresenter implements MoviesContract.Presenter {
    private final MoviesRepository moviesRepository;

    private final MoviesContract.View moviesView;

    private int pageLoaded = 0;

    MoviesPresenter(@NonNull MoviesRepository moviesRepository,
                    @NonNull MoviesContract.View moviesView) {
        this.moviesRepository = moviesRepository;
        this.moviesView = moviesView;

        this.moviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies();
    }

    @Override
    public void loadMovies() {
        moviesView.setLoadingIndicator(true);

        moviesRepository.getMovies(++pageLoaded, new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                if (!moviesView.isActive()) {
                    return;
                }

                moviesView.setLoadingIndicator(false);

                processMovies(movies);
            }

            @Override
            public void onDataNotAvailable() {
                if (moviesView.isActive()) {
                    return;
                }

                moviesView.showLoadingMoviesError();
            }
        });
    }

    @Override
    public void openMovieDetails(@NonNull Movie requestedMovie) {
        moviesView.showMovieDetailsUi(requestedMovie.getId());
    }

    private void processMovies(final List<Movie> movies) {
        if (!movies.isEmpty()) {
            moviesView.showMovies(movies);
        }
    }
}
