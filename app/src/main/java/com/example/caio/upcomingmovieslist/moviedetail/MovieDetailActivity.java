package com.example.caio.upcomingmovieslist.moviedetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.caio.upcomingmovieslist.R;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;
import com.example.caio.upcomingmovieslist.data.source.network.MoviesNetworkDataSource;
import com.example.caio.upcomingmovieslist.util.ActivityUtils;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "MOVIE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Integer movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);

        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_content);

        if (movieDetailFragment == null) {
            movieDetailFragment = MovieDetailFragment.newInstance(movieId);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    movieDetailFragment,
                    R.id.layout_content);
        }

        //Create the presenter
        new MovieDetailPresenter(
                movieId,
                MoviesRepository.getInstance(MoviesNetworkDataSource.getInstance()),
                movieDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
