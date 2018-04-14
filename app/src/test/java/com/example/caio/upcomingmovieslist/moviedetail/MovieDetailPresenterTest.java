package com.example.caio.upcomingmovieslist.moviedetail;

import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.data.source.MoviesDataSource;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Caio on 17/02/2018.
 */

public class MovieDetailPresenterTest {
    private static final int VOTE_COUNT_TEST =804;
    private static final int ID_TEST = 403119;
    private static final boolean VIDEO_TEST = false;
    private static final double VOTE_AVERAGE_TEST = 5.3;
    private static final String TITLE_TEST = "47 Meters Down";
    private static final double POPULARITY_TEST = 132.640002;
    private static final String POSTER_PATH_TEST = "https://image.tmdb.org/t/p/w500/2IgdRUTdHyoI3nFORcnnYEKOGIH.jpg";
    private static final String ORIGINAL_LANGUAGE_TEST = "pt-BR";
    private static final String ORIGINAL_TITLE_TEST = "47 Meters Down";
    private static final List<Integer> GENRE_IDS_TEST = new ArrayList<>();
    private static final String BACKDROP_PATH_TEST = "https://image.tmdb.org/t/p/w500/6j8B3BqTuNrpAJoa0JIc7nZzOUn.jpg";
    private static final boolean ADULT_TEST = true;
    private static final String OVERVIEW_TEST = "Two sisters on Mexican vacation are trapped in a shark observation cage at the bottom of the ocean, with oxygen running low and great whites circling nearby, they have less than an hour of air left to figure out how to get to the surface.";
    private static final Date RELEASE_DATE_TEST = new Date();

    private static final List<String> GENRES_TEST = new ArrayList<>();

    private static final Movie SELECTED_MOVIE = new Movie(
            VOTE_COUNT_TEST,
            ID_TEST,
            VIDEO_TEST,
            VOTE_AVERAGE_TEST,
            TITLE_TEST,
            POPULARITY_TEST,
            POSTER_PATH_TEST,
            ORIGINAL_LANGUAGE_TEST,
            ORIGINAL_TITLE_TEST,
            GENRE_IDS_TEST,
            BACKDROP_PATH_TEST,
            ADULT_TEST,
            OVERVIEW_TEST,
            RELEASE_DATE_TEST);

    @Mock
    private MoviesRepository moviesRepository;

    @Mock
    private MovieDetailContract.View movieDetailView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<MoviesDataSource.GetMovieCallback> getMovieCallbackCaptor;

    private MovieDetailPresenter movieDetailPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        //The presenter won't update the view unless it's active
        Mockito.when(movieDetailView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        //Get a reference to the class under test
        movieDetailPresenter = new MovieDetailPresenter(
                SELECTED_MOVIE.getId(), moviesRepository, movieDetailView);

        //Then the presenter is set to the view
        Mockito.verify(movieDetailView).setPresenter(movieDetailPresenter);
    }
}
