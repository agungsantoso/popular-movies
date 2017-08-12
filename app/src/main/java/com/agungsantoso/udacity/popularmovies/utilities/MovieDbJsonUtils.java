package com.agungsantoso.udacity.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.agungsantoso.udacity.popularmovies.data.MovieParcel;
import com.agungsantoso.udacity.popularmovies.data.ReviewParcel;
import com.agungsantoso.udacity.popularmovies.data.VideoParcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by agung.santoso on 08/08/2017.
 */

public final class MovieDbJsonUtils {

    private static final String TAG = MovieDbJsonUtils.class.getSimpleName();

    public static String[] getSimpleMovieStringsFromJson(
            Context context,
            String movieJsonStr
    ) throws JSONException {

        final String MDB_LIST = "results";

        final String MDB_ID = "id";
        final String MDB_TITLE = "title";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_MOVIE_POSTER = "poster_path";
        final String MDB_VOTE = "vote_count";
        final String MDB_AVERAGE = "vote_average";
        final String MDB_PLOT_SYNOPSIS = "overview";


        String[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MDB_LIST);

        parsedMovieData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String posterPath;

            JSONObject movieObj = movieArray.getJSONObject(i);

            posterPath = movieObj.getString(MDB_MOVIE_POSTER);

            parsedMovieData[i] = posterPath;
        }

        Log.e(TAG, movieArray.toString());

        return parsedMovieData;
    }

    public static MovieParcel[] getFullMovieDataFromJson(
            Context context,
            String movieJsonStr
    ) throws JSONException {
        final String MDB_LIST = "results";

        final String MDB_ID = "id";
        final String MDB_TITLE = "title";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_MOVIE_POSTER = "poster_path";
        final String MDB_VOTE = "vote_count";
        final String MDB_AVERAGE = "vote_average";
        final String MDB_PLOT_SYNOPSIS = "overview";

        MovieParcel[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MDB_LIST);

        parsedMovieData = new MovieParcel[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObj = movieArray.getJSONObject(i);

            String id = movieObj.getString(MDB_ID);
            String title = movieObj.getString(MDB_TITLE);
            String poster = movieObj.getString(MDB_MOVIE_POSTER);
            String release_date = movieObj.getString(MDB_RELEASE_DATE);
            Integer vote_count = movieObj.getInt(MDB_VOTE);
            Double vote_average = movieObj.getDouble(MDB_AVERAGE);
            String plot = movieObj.getString(MDB_PLOT_SYNOPSIS);

            parsedMovieData[i] = new MovieParcel(
                    id,
                    title,
                    poster,
                    release_date,
                    vote_count,
                    vote_average,
                    plot
            );
        }

        return  parsedMovieData;
    }

    public static VideoParcel[] getVideoDataFromJson(
            Context context,
            String movieJsonStr
    ) throws JSONException {
        final String VID_LIST = "results";

        final String VID_KEY = "key";
        final String VID_NAME = "name";
        final String VID_SITE = "site";

        VideoParcel[] parsedVideoData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(VID_LIST);

        parsedVideoData = new VideoParcel[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObj = movieArray.getJSONObject(i);

            String key = movieObj.getString(VID_KEY);
            String name = movieObj.getString(VID_NAME);
            String site = movieObj.getString(VID_SITE);

            Log.d(TAG, "name =" + name);

            parsedVideoData[i] = new VideoParcel(
                    key,
                    name,
                    site
            );
        }

        return  parsedVideoData;
    }

    public static ReviewParcel[] getReviewsDataFromJson(
            Context context,
            String movieJsonStr
    ) throws JSONException {
        final String VID_LIST = "results";

        final String REV_AUTHOR = "author";
        final String REV_REVIEW = "content";

        ReviewParcel[] parsedVideoData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(VID_LIST);

        parsedVideoData = new ReviewParcel[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObj = movieArray.getJSONObject(i);

            String author = movieObj.getString(REV_AUTHOR);
            String review = movieObj.getString(REV_REVIEW);

            Log.d(TAG, "author=" + author);

            parsedVideoData[i] = new ReviewParcel(
                    author,
                    review
            );
        }

        return  parsedVideoData;
    }
}
