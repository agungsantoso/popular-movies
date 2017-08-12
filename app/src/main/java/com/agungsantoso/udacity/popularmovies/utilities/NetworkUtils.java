package com.agungsantoso.udacity.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.agungsantoso.udacity.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by agung.santoso on 08/08/2017.
 */

// lesson 2 - Internet
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";
    private static final String apiKey = BuildConfig.MOVIEDB_API;

    public static URL buildUrl(String sortBy) {
        String SORT = "popular/";
        if(sortBy == "toprated") {
            SORT = "top_rated/";
        }
        String BASE_URL = MOVIEDB_BASE_URL + SORT;
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildVideoUrl(String id) {

        String BASE_URL = MOVIEDB_BASE_URL + id + "/videos";
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String id) {

        String BASE_URL = MOVIEDB_BASE_URL + id + "/reviews";
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url)
            throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String next = null;
            if(hasInput) {
                next = scanner.next();
            }
            return next;
        } finally {
            urlConnection.disconnect();
        }
    }
}
