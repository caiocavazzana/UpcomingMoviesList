package com.example.caio.upcomingmovieslist.movies;

import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.data.source.MoviesDataSource;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Caio on 17/02/2018.
 * Unit tests for the implementation of {@link MoviesPresenter}
 */

public class MoviesPresenterTest {
    private static List<Movie> MOVIES = new ArrayList<>();

    @Mock
    private MoviesRepository moviesRepository;

    @Mock
    private MoviesContract.View moviesView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<MoviesDataSource.LoadMoviesCallback> loadMoviesCallbackCaptor;

    @Captor
    private ArgumentCaptor<Integer> pageArgumentCaptor;

    private MoviesPresenter moviesPresenter;

    @Before
    public void setupMoviesPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        moviesPresenter = new MoviesPresenter(moviesRepository, moviesView);

        when(moviesView.isActive()).thenReturn(true);

        MOVIES.add(new Movie(
                1446,
                406990,
                true,
                7.2,
                "What Happened to Monday",
                106.695513,
                "https://image.tmdb.org/t/p/w500/o6EsOqITcSzcdwD1zxBM9imdxjr.jpg",
                "en-US",
                "What Happened to Monday",
                new ArrayList<Integer>(),
                "https://image.tmdb.org/t/p/w500/wwZ2uXOwPkMrZSeFn9s7WFXEMg6.jpg",
                false,
                "In a world where families are limited to one child due to overpopulation, a set of identical septuplets must avoid being put to a long sleep by the government and dangerous infighting while investigating the disappearance of one of their own.",
                new Date()));

        MOVIES.add(new Movie(
                804,
                403119,
                false,
                5.3,
                "47 Meters Down",
                132.640002,
                "https://image.tmdb.org/t/p/w500/2IgdRUTdHyoI3nFORcnnYEKOGIH.jpg",
                "pt-BR",
                "47 Meters Down",
                new ArrayList<Integer>(),
                "https://image.tmdb.org/t/p/w500/6j8B3BqTuNrpAJoa0JIc7nZzOUn.jpg",
                true,
                "Two sisters on Mexican vacation are trapped in a shark observation cage at the bottom of the ocean, with oxygen running low and great whites circling nearby, they have less than an hour of air left to figure out how to get to the surface.",
                new Date()));
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        //Get a referente to the class under test
        moviesPresenter = new MoviesPresenter(moviesRepository, moviesView);

        //Then the presenter is set to the view
        verify(moviesView).setPresenter(moviesPresenter);
    }

    @Test
    public void loadMoviesFromRepositoryAndLoadIntoView() {
        //Given an initialized MoviesPresenter with initialized movies
        //when loading of movies is requested
        moviesPresenter.loadMovies();

        //Callback is captured and invoked with stubbed movies
        verify(moviesRepository)
                .getMovies(pageArgumentCaptor.capture(), loadMoviesCallbackCaptor.capture());
        loadMoviesCallbackCaptor.getValue().onMoviesLoaded(MOVIES);

        //Then progress indicator is shown
        InOrder inOrder = Mockito.inOrder(moviesView);
        inOrder.verify(moviesView).setLoadingIndicator(true);

        //Then progress indicator is hidden and all movies are shown in UI
        inOrder.verify(moviesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showMoviesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(moviesView).showMovies(showMoviesArgumentCaptor.capture());
        assertTrue(showMoviesArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void clickOnMovie_openMovieDetails() {
        //When open movie details is requested
        moviesPresenter.openMovieDetails(MOVIES.get(0));

        //When movie detail UI is shown
        verify(moviesView).showMovieDetailsUi(any(Integer.class));
    }

    @Test
    public void unavailableMovies_ShowsError() {
        //When movies are loaded
        moviesPresenter.loadMovies();

        //And the movies are not available in the repository
        verify(moviesRepository)
                .getMovies(pageArgumentCaptor.capture(), loadMoviesCallbackCaptor.capture());
        loadMoviesCallbackCaptor.getValue().onDataNotAvailable();
    }
}
