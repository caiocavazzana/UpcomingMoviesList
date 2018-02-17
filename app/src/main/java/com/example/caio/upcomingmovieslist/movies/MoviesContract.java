package com.example.caio.upcomingmovieslist.movies;

import android.support.annotation.NonNull;

import com.example.caio.upcomingmovieslist.BasePresenter;
import com.example.caio.upcomingmovieslist.BaseView;
import com.example.caio.upcomingmovieslist.data.Movie;

import java.util.List;

/**
 * Created by Caio on 13/02/2018.
 */

public interface MoviesContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Integer movieId);

        void showLoadingMoviesError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadMovies();

        void openMovieDetails(@NonNull Movie requestedMovie);
    }
}
