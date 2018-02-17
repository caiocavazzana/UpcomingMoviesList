package com.example.caio.upcomingmovieslist.moviedetail;

import com.example.caio.upcomingmovieslist.BasePresenter;
import com.example.caio.upcomingmovieslist.BaseView;

import java.util.Date;
import java.util.List;

/**
 * Created by Caio on 14/02/2018.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void showTitle(String title);

        void showGenres(List<String> genres);

        void showOverview(String overview);

        void showReleaseDate(Date releaseDate);

        void showPoster(String uriPoster);

        void showMissingMovie();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {}
}
