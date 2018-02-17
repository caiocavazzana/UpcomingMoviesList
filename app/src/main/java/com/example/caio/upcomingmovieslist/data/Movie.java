package com.example.caio.upcomingmovieslist.data;

import com.example.caio.upcomingmovieslist.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Caio on 10/02/2018.
 */

public class Movie {
    //Json atributes
    private int voteCount;
    private Integer id;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private List<Integer> genreIds;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private Date releaseDate;

    //Atributes to prevent multiple accesses to the API
    private List<String> genres;

    public Movie() {}

    public Movie(JsonObject jsonObject) {
        voteCount = jsonObject.has("vote_count") &&
                !jsonObject.get("vote_count").isJsonNull() ?
                jsonObject.get("vote_count").getAsInt() : 0;

        id = jsonObject.has("id") && !jsonObject.get("id").isJsonNull() ?
                jsonObject.get("id").getAsInt() : 0;

        video = (jsonObject.has("video") && !jsonObject.get("video").isJsonNull()) &&
                jsonObject.get("video").getAsBoolean();

        voteAverage = jsonObject.has("vote_average") &&
                !jsonObject.get("vote_average").isJsonNull() ?
                jsonObject.get("vote_average").getAsDouble() : 0;

        title = jsonObject.has("title") && !jsonObject.get("title").isJsonNull() ?
                jsonObject.get("title").getAsString() : null;

        popularity = jsonObject.has("popularity") &&
                !jsonObject.get("popularity").isJsonNull() ?
                jsonObject.get("popularity").getAsDouble() : 0;

        posterPath = jsonObject.has("poster_path") &&
                !jsonObject.get("poster_path").isJsonNull() ?
                Constants.IMAGE_BASE_URL + jsonObject.get("poster_path").getAsString() : null;

        originalLanguage = jsonObject.has("original_language") &&
                !jsonObject.get("original_language").isJsonNull() ?
                jsonObject.get("original_language").getAsString() : null;

        originalTitle = jsonObject.has("original_title") &&
                !jsonObject.get("original_title").isJsonNull() ?
                jsonObject.get("original_title").getAsString() : null;

        genreIds = new ArrayList<>();
        if (jsonObject.has("genre_ids") && !jsonObject.get("genre_ids").isJsonNull()) {
            JsonArray jsonArray = jsonObject.get("genre_ids").getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                genreIds.add(jsonArray.get(i).getAsInt());
            }
        }

        backdropPath = jsonObject.has("backdrop_path") &&
                !jsonObject.get("backdrop_path").isJsonNull() ?
                Constants.IMAGE_BASE_URL + jsonObject.get("backdrop_path").getAsString() : null;

        adult = (jsonObject.has("adult") && !jsonObject.get("adult").isJsonNull()) &&
                jsonObject.get("adult").getAsBoolean();

        overview = jsonObject.has("overview") &&
                !jsonObject.get("overview").isJsonNull() ?
                jsonObject.get("overview").getAsString() : null;

        if (jsonObject.has("release_date") &&
                !jsonObject.get("release_date").isJsonNull()) {
            String strDate = jsonObject.get("release_date").getAsString();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                releaseDate = dateFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            releaseDate = new Date();
        }
    }

    public Movie(int voteCount,
                 Integer id,
                 boolean video,
                 double voteAverage,
                 String title,
                 double popularity,
                 String posterPath,
                 String originalLanguage,
                 String originalTitle,
                 List<Integer> genreIds,
                 String backdropPath,
                 boolean adult,
                 String overview,
                 Date releaseDate) {
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean hasVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
