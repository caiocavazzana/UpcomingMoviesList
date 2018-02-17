package com.example.caio.upcomingmovieslist.moviedetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caio.upcomingmovieslist.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Caio on 14/02/2018.
 */

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {
    private static final String ARGUMENT_MOVIE_ID = "MOVIE_ID";

    private MovieDetailContract.Presenter presenter;

    private TextView textViewTitle;
    private TextView textViewGenres;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ImageView imageViewPoster;

    public static MovieDetailFragment newInstance(Integer movieId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_MOVIE_ID, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        setHasOptionsMenu(true);

        textViewTitle = root.findViewById(R.id.textview_title);
        textViewGenres = root.findViewById(R.id.textview_genres);
        textViewReleaseDate = root.findViewById(R.id.textview_release_date);
        textViewOverview = root.findViewById(R.id.textview_overview);
        imageViewPoster = root.findViewById(R.id.imageview_poster);

        return root;
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showTitle(String title) {
        textViewTitle.setText(title);
    }

    @Override
    public void showGenres(List<String> genres) {
        StringBuilder genresBuilder = new StringBuilder();
        if (genres != null && genres.size() > 0) {
            genresBuilder.append("(");

            for (int i = 0; i < genres.size(); i++) {
                if (genres.get(i) != null) {
                    genresBuilder.append(genres.get(i));

                    if (i == genres.size() - 1) {
                        genresBuilder.append(")");
                    } else {
                        genresBuilder.append(", ");
                    }
                }
            }
        }

        textViewGenres.setText(genresBuilder.toString());
    }

    @Override
    public void showOverview(String overview) {
        textViewOverview.setText(overview);
    }

    @Override
    public void showReleaseDate(Date releaseDate) {
        textViewReleaseDate.setText(SimpleDateFormat.getDateInstance().format(releaseDate));
    }

    @Override
    public void showPoster(final String uriPoster) {
        if (uriPoster!= null) {
            Picasso.with(getContext())
                    .load(Uri.parse(uriPoster))
                    .placeholder(R.drawable.ic_image)
                    .into(imageViewPoster);
        }
    }

    @Override
    public void showMissingMovie() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.movie_not_available, Snackbar.LENGTH_LONG).show();
        }

        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
