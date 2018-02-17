package com.example.caio.upcomingmovieslist.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.caio.upcomingmovieslist.R;
import com.example.caio.upcomingmovieslist.data.source.MoviesRepository;
import com.example.caio.upcomingmovieslist.data.source.network.MoviesNetworkDataSource;
import com.example.caio.upcomingmovieslist.util.ActivityUtils;

public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_content);

        if (moviesFragment == null) {
            //Create the fragment
            moviesFragment = MoviesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    moviesFragment,
                    R.id.layout_content);
        }

        //Create the presenter
        new MoviesPresenter(
                MoviesRepository.getInstance(MoviesNetworkDataSource.getInstance()),
                moviesFragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
