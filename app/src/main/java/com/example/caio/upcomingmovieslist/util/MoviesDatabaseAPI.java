package com.example.caio.upcomingmovieslist.util;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Caio on 10/02/2018.
 */

public interface MoviesDatabaseAPI {
    @GET("movie/upcoming?")
    Call<JsonObject> getUpComingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page);

    @GET("genre/movie/list?")
    Call<JsonObject> getMovieGenres(
            @Query("api_key") String apiKey);
}
