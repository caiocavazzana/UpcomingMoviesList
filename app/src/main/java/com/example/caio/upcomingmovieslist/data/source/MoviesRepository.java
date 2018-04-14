package com.example.caio.upcomingmovieslist.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.caio.upcomingmovieslist.data.Movie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caio on 10/02/2018.
 * Concrete implementation to load movies from the data sources into a cache.
 */

public class MoviesRepository implements MoviesDataSource {
    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource networkDataSource;

    private Map<Integer, Movie> cachedMovies;

    //Prevent direct instantiation.
    private MoviesRepository(@NonNull MoviesDataSource networkDataSource) {
        this.networkDataSource = networkDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param networkDataSource the backend data source
     * @return the {@link MoviesRepository} instance
     */
    public static MoviesRepository getInstance(MoviesDataSource networkDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(networkDataSource);
        }

        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(MoviesDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets movies from cache or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadMoviesCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getMovies(int page, @NonNull final LoadMoviesCallback callback) {
        networkDataSource.getMovies(page, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                refreshCache(movies);
                callback.onMoviesLoaded(new ArrayList<>(cachedMovies.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Gets movie from cache unless it not exists. In that case it uses the network data source.
     * <p>
     * Note: {@link GetMovieCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getMovie(@NonNull Integer movieId,
                         int page,
                         @NonNull final GetMovieCallback callback) {
        Movie cachedMovie = getMovieWithId(movieId);

        if (cachedMovie != null) {
            callback.onMovieLoaded(cachedMovie);
            return;
        }

        networkDataSource.getMovie(movieId, page, new GetMovieCallback() {
            @Override
            public void onMovieLoaded(Movie movie) {
                if (cachedMovies == null) {
                    cachedMovies = new LinkedHashMap<>();
                }
                cachedMovies.put(movie.getId(), movie);
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Movie> movies) {
        if (cachedMovies == null) {
            cachedMovies = new LinkedHashMap<>();
        }

        cachedMovies.clear();

        for (Movie movie: movies) {
            cachedMovies.put(movie.getId(), movie);
        }
    }

    @Nullable
    private Movie getMovieWithId(@NonNull Integer id) {
        if (cachedMovies == null || cachedMovies.isEmpty()) {
            return null;
        } else {
            return cachedMovies.get(id);
        }
    }
}
