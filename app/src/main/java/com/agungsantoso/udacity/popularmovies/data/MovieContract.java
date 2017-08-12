package com.agungsantoso.udacity.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by agung.santoso on 09/08/2017.
 */

// Lesson 7 - Storing Data in SQLite
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.agungsantoso.udacity.popularmovie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ID = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ID)
                .build();

        public static final String TABLE_NAME = "movie";
        public static final String ID = "id";
        public static final String COLUMNN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_POSTER = "poster_path";
        public static final String COLUMN_VOTE = "vote_count";
        public static final String COLUMN_AVERAGE = "vote_average";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";

        public static Uri buildMovieUriWithId(
                String id
        ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }
}
