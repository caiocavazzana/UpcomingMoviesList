package com.example.caio.upcomingmovieslist.moviedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.data.source.MoviesDataSource;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;

/**
 * Created by Caio on 14/02/2018.
 * Listens to user actions from the UI ({@link MovieDetailFragment}), retrieves the data and updates
 * the UI as required.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private final MoviesRepository moviesRepository;

    private final MovieDetailContract.View movieDetailView;

    private Integer movieId;

    public MovieDetailPresenter(@Nullable Integer movieId,
                                @NonNull MoviesRepository moviesRepository,
                                @NonNull MovieDetailContract.View movieDetailView) {
        this.movieId = movieId;
        this.moviesRepository = moviesRepository;
        this.movieDetailView = movieDetailView;

        movieDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openMovie();
    }

    private void openMovie() {
        if (movieId == null || movieId == 0) {
            movieDetailView.showMissingMovie();
            return;
        }

        moviesRepository.getMovie(movieId, 1, new MoviesDataSource.GetMovieCallback() {
            @Override
            public void onMovieLoaded(Movie movie) {
                //The view may not be able to handle UI updates anymore
                if (!movieDetailView.isActive()) {
                    return;
                }

                if (movie == null) {
                    movieDetailView.showMissingMovie();
                } else {
                    showMovie(movie);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!movieDetailView.isActive()) {
                    return;
                }

                movieDetailView.showMissingMovie();
            }
        });
    }

    private void showMovie(@NonNull Movie movie) {
        movieDetailView.showTitle(movie.getTitle());
        movieDetailView.showGenres(movie.getGenres());
        movieDetailView.showReleaseDate(movie.getReleaseDate());
        movieDetailView.showOverview(movie.getOverview());
        movieDetailView.showPoster(movie.getPosterPath());
    }
}
