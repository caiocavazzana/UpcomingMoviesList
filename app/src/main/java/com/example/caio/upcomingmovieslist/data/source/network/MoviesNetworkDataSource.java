package com.example.caio.upcomingmovieslist.data.source.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.caio.upcomingmovieslist.data.GenreBean;
import com.example.caio.upcomingmovieslist.data.Movie;
import com.example.caio.upcomingmovieslist.data.source.MoviesDataSource;
import com.example.caio.upcomingmovieslist.util.Constants;
import com.example.caio.upcomingmovieslist.util.MoviesDatabaseAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Caio on 10/02/2018.
 */

public class MoviesNetworkDataSource implements MoviesDataSource {
    private static volatile MoviesNetworkDataSource INSTANCE;

    private static Map<Integer, Movie> MOVIES_SERVICE_DATA = new LinkedHashMap<>();

    private static Map<Integer, GenreBean> GENRES_SERVICE_DATA = new LinkedHashMap<>();

    private static MoviesDatabaseAPI mDbApi;

    //Prevent direct instantiation
    private MoviesNetworkDataSource() {
        //Creates MoviesDatabaseAPI instance
        Gson gson = new GsonBuilder().create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        mDbApi = retrofit.create(MoviesDatabaseAPI.class);

        //Need to load all genres when the data source is created.
        getGenres();
    }

    public static MoviesNetworkDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesNetworkDataSource();
        }

        return INSTANCE;
    }

    /**
     * Query the API for upcoming movies
     *
     * @param callback dispatch query results
     */
    @Override
    public void getMovies(int page, @NonNull final LoadMoviesCallback callback) {
        Call<JsonObject> call = mDbApi.getUpComingMovies(Constants.API_KEY, page);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.body().size() > 0) {
                    if (response.body().has("results") &&
                            !response.body().get("results").isJsonNull() &&
                            response.body().get("results").isJsonArray()) {
                        JsonArray jsonArray = response.body().getAsJsonArray("results");

                        processJsonArray(jsonArray);

                        callback.onMoviesLoaded(new ArrayList<>(MOVIES_SERVICE_DATA.values()));
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                throwError(t);
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Query the cached movies for a specific one. If not exists query the API for all movies first,
     * then search for the required movie.
     *
     * @param movieId required movie
     * @param page next page to be loaded in the cache
     * @param callback dispatch query results
     */
    @Override
    public void getMovie(@NonNull final Integer movieId,
                         final int page,
                         @NonNull final GetMovieCallback callback) {
        if (MOVIES_SERVICE_DATA != null && MOVIES_SERVICE_DATA.containsKey(movieId)) {
            callback.onMovieLoaded(MOVIES_SERVICE_DATA.get(movieId));
        } else {
            Call<JsonObject> call = mDbApi.getUpComingMovies(Constants.API_KEY, page);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null && response.body().size() > 0) {
                        if (response.body().has("results") &&
                                !response.body().get("results").isJsonNull() &&
                                response.body().get("results").isJsonArray()) {
                            JsonArray jsonArray = response.body().getAsJsonArray("results");

                            processJsonArray(jsonArray);

                            if (MOVIES_SERVICE_DATA != null &&
                                    MOVIES_SERVICE_DATA.containsKey(movieId)) {
                                callback.onMovieLoaded(MOVIES_SERVICE_DATA.get(movieId));
                            } else {
                                callback.onDataNotAvailable();
                            }
                        } else {
                            callback.onDataNotAvailable();
                        }
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    throwError(t);
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    private void getGenres() {
        Call<JsonObject> call = mDbApi.getMovieGenres(Constants.API_KEY);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.body().size() > 0) {
                    if (response.body().has("genres") &&
                            !response.body().get("genres").isJsonNull() &&
                            response.body().get("genres").isJsonArray()) {
                        JsonArray jsonArray = response.body().get("genres").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            if (jsonArray.get(i).getAsJsonObject() != null) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                GenreBean genre = new GenreBean(jsonObject);

                                GENRES_SERVICE_DATA.put(genre.getId(), genre);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                throwError(t);
            }
        });
    }

    private void processJsonArray(JsonArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getAsJsonObject() != null) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                if (jsonObject != null) {
                    Movie movie = new Movie(jsonObject);

                    //Load genre names
                    List<Integer> genreIds = movie.getGenreIds();

                    if (genreIds != null && genreIds.size() > 0) {
                        List<String> genreNames = new ArrayList<>();

                        for (int j = 0; j < genreIds.size(); j++) {
                            if (GENRES_SERVICE_DATA.containsKey(genreIds.get(j))) {
                                GenreBean tempGenre = GENRES_SERVICE_DATA.get(
                                        genreIds.get(j));

                                genreNames.add(tempGenre.getName());
                            }
                        }

                        movie.setGenres(genreNames);
                    }

                    MOVIES_SERVICE_DATA.put(movie.getId(), movie);
                }
            }
        }
    }

    private void throwError(Throwable t) {
        String error = "";

        if (t instanceof SocketTimeoutException) {
            error = "connection timeOut";
        } else if (t.getMessage() != null) {
            error = t.getMessage();
        }

        Log.v(this.getClass().getSimpleName(), error);
    }
}
