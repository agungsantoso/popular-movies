package com.agungsantoso.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.agungsantoso.udacity.popularmovies.data.MovieContract.*;

/**
 * Created by agung.santoso on 09/08/2017.
 */

public class MovieDbHelper
    extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularmovie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MovieEntry.COLUMNN_ID + " VARCHAR(255) NOT NULL, " +
            MovieEntry.COLUMN_TITLE + " VARCHAR(255) NOT NULL, " +
            MovieEntry.COLUMN_RELEASE_DATE + " DATETIME NOT NULL, " +
            MovieEntry.COLUMN_MOVIE_POSTER + " VARCHAR(255) NOT NULL, " +
            MovieEntry.COLUMN_VOTE + " INTEGER NOT NULL, " +
            MovieEntry.COLUMN_AVERAGE + " DOUBLE NOT NULL, " +
            MovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL " +
            ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase sqLiteDatabase,
            int oldVersion,
            int newVersion
    ) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
