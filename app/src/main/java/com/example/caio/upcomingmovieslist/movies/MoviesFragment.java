package com.example.caio.upcomingmovieslist.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caio.upcomingmovieslist.R;
import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.moviedetail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caio on 14/02/2018.
 */

public class MoviesFragment extends Fragment implements MoviesContract.View {
    private MoviesContract.Presenter presenter;
    private MoviesAdapter adapter;
    private SwipeRefreshLayout layoutMovies;
    private LinearLayout layoutLoadingError;

    public MoviesFragment() {
        //Requires empty public constructor
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MoviesAdapter(
                new ArrayList<Movie>(0),
                getContext(),
                new MovieItemListener() {
                    @Override
                    public void onMovieClick(Movie clickedMovie) {
                        presenter.openMovieDetails(clickedMovie);
                    }
                },
                new OnBottomReachedListener() {
                    @Override
                    public void onBottomReached(int position) {
                        setLoadingIndicator(true);
                        presenter.loadMovies();
                        setLoadingIndicator(false);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movies_fragment, container, false);

        layoutMovies = root.findViewById(R.id.layout_movies);
        layoutMovies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadMovies();
                setLoadingIndicator(false);
            }
        });

        RecyclerView recyclerViewMovies = root.findViewById(R.id.recyclerview_movies);
        recyclerViewMovies.setAdapter(adapter);

        layoutLoadingError = root.findViewById(R.id.layout_loading_error);

        Button buttonRetry = root.findViewById(R.id.button_retry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePresenterStart();
            }
        });

        validatePresenterStart();

        return root;
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }

        layoutMovies.post(new Runnable() {
            @Override
            public void run() {
                layoutMovies.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMovies(List<Movie> movies) {
        adapter.replaceData(movies);

        layoutMovies.setVisibility(View.VISIBLE);
        layoutLoadingError.setVisibility(View.GONE);
    }

    @Override
    public void showMovieDetailsUi(Integer movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Override
    public void showLoadingMoviesError() {
        layoutMovies.setVisibility(View.GONE);
        layoutLoadingError.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void validatePresenterStart() {
        showMovies(new ArrayList<Movie>());

        if (getContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null) {
                    if (presenter != null) {
                        presenter.start();
                    } else {
                        showLoadingMoviesError();
                    }
                } else {
                    showLoadingMoviesError();
                }
            } else {
                showLoadingMoviesError();
            }
        } else {
            showLoadingMoviesError();
        }
    }

    private static class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
        private List<Movie> movies;
        private Context context;
        private MovieItemListener itemListener;
        private OnBottomReachedListener bottomReachedListener;

        public MoviesAdapter(List<Movie> movies,
                             Context context,
                             MovieItemListener itemListener,
                             OnBottomReachedListener bottomReachedListener) {
            setLists(movies);
            this.context = context;
            this.itemListener = itemListener;
            this.bottomReachedListener = bottomReachedListener;
        }

        public Movie getItem(int i) {
            return movies != null && movies.size() > i ? movies.get(i) : null;
        }

        public void replaceData(List<Movie> movies) {
            setLists(movies);
            notifyDataSetChanged();
        }

        private void setLists(List<Movie> movies) {
            this.movies = movies;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Movie movie = getItem(position);

            holder.textViewName.setText(movie.getTitle());

            StringBuilder genresBuilder = new StringBuilder();
            if (movie.getGenres() != null && movie.getGenres().size() > 0) {
                genresBuilder.append("(");

                for (int i = 0; i < movie.getGenres().size(); i++) {
                    if (movie.getGenres().get(i) != null) {
                        genresBuilder.append(movie.getGenres().get(i));

                        if (i == movie.getGenres().size() - 1) {
                            genresBuilder.append(")");
                        } else {
                            genresBuilder.append(", ");
                        }
                    }
                }
            }
            holder.textViewGenres.setText(genresBuilder.toString());
            holder.textViewReleaseDate.setText(
                    SimpleDateFormat.getDateInstance().format(movie.getReleaseDate()));

            if (movie.getPosterPath() != null) {
                Picasso.with(context)
                        .load(Uri.parse(movie.getBackdropPath()))
                        .placeholder(R.drawable.ic_image)
                        .into(holder.imageViewPoster);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onMovieClick(movie);
                }
            });

            if (position == movies.size() - 1) {
                bottomReachedListener.onBottomReached(position);
            }
        }

        @Override
        public int getItemCount() {
            return movies != null ? movies.size() : 0;
        }

        @Override
        public long getItemId(int position) {
            return movies != null && movies.size() > position ? movies.get(position).getId() : 0;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            View itemView;

            TextView textViewName;
            TextView textViewGenres;
            TextView textViewReleaseDate;
            ImageView imageViewPoster;

            public ViewHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                textViewName = itemView.findViewById(R.id.textview_title);
                textViewGenres = itemView.findViewById(R.id.textview_genres);
                textViewReleaseDate = itemView.findViewById(R.id.textview_release_date);
                imageViewPoster = itemView.findViewById(R.id.imageview_poster);
            }
        }
    }

    public interface MovieItemListener {
        void onMovieClick(Movie clickedMovie);
    }

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }
}
