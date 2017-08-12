package com.agungsantoso.udacity.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.agungsantoso.udacity.popularmovies.data.MovieContract.*;
import static com.agungsantoso.udacity.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by agung.santoso on 09/08/2017.
 */

// Lesson 9 - Building a Content Provider
public class MovieProvider extends ContentProvider {

    private static final String TAG = "MovieProvider";

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    private MovieDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_ID, CODE_MOVIE);
        matcher.addURI(authority, PATH_ID + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(
        @NonNull Uri uri,
        @NonNull ContentValues[] values
    ) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                   for (ContentValues value : values) {
                       Long _id = db.insert(
                           TABLE_NAME,
                           null,
                           value
                       );
                       if(_id != -1) {
                           rowsInserted++;
                       }
                   }
                   db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver()
                            .notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(
            @NonNull Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {
       Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_WITH_ID: {
                String normalizedId = uri.getLastPathSegment();
                String[] selectionArguments = new String[] {normalizedId};

                cursor = mOpenHelper.getReadableDatabase()
                        .query(
                            TABLE_NAME,
                            projection,
                            MovieEntry.ID + " = ? ",
                            selectionArguments,
                            null,
                            null,
                            sortOrder
                        );
                break;
            }
            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase()
                        .query(
                            TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                        );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            String selection,
            String[] selectionArgs
    ) {
        int numRowsDeleted;

        if (null == selection) {
            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                numRowsDeleted = mOpenHelper.getWritableDatabase()
                        .delete(
                                TABLE_NAME,
                                selection,
                                selectionArgs
                        );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Uri insert(
            @NonNull Uri uri,
            ContentValues values
    ) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        Log.d(TAG, "match = " + match);
        switch (match) {
            case CODE_MOVIE:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "returnUri = "+ returnUri);
        return returnUri;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs
    ) {
        throw new RuntimeException("Not implemented");
    }
}
