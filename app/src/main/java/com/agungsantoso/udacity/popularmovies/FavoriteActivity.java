package com.agungsantoso.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.agungsantoso.udacity.popularmovies.data.MovieContract;
import com.agungsantoso.udacity.popularmovies.data.MovieParcel;
import com.agungsantoso.udacity.popularmovies.data.PopularMoviePreferences;

public class FavoriteActivity
        extends AppCompatActivity
        implements
        FavoriteListAdapter.FavoriteAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;

    private FavoriteListAdapter mFavoriteListAdapter;

    //private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMNN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_AVERAGE,
            MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_RELAESE = 3;
    public static final int INDEX_VOTE = 4;
    public static final int INDEX_AVERAGE = 5;
    public static final int INDEX_PLOT = 6;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        //mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        // Simple Android grid example using RecyclerView with GridLayoutManager (like the old GridView)
        // https://stackoverflow.com/a/40587169/448050
        int numberOfColumns = 2;

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mFavoriteListAdapter = new FavoriteListAdapter(this, this);
        mRecyclerView.setAdapter(mFavoriteListAdapter);

        showLoading();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        int loaderId = MOVIE_LOADER_ID;
        LoaderManager.LoaderCallbacks<Cursor> callback = FavoriteActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(
                loaderId,
                bundleForLoader,
                callback
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(
            int loaderId,
            Bundle bundle
    ) {
        switch (loaderId) {
            case MOVIE_LOADER_ID:

                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                String sortOrder = MovieContract.MovieEntry.ID + " ASC";

                return new CursorLoader(
                        this,
                        movieQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        sortOrder
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(
            Loader<Cursor> Loader,
            Cursor data
    ) {
        mFavoriteListAdapter.swapCursor(data);
        if(mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {
            showMovieDataView();
        }

    }

    @Override
    public void onLoaderReset(
            Loader<Cursor> Loader
    ) {
        mFavoriteListAdapter.swapCursor(null);
    }

    private void invalidateData() {
        mFavoriteListAdapter.setMovieData(null);
    }

    @Override
    public void onClick(MovieParcel movieParcel) {
        Context context = this;
        Class destinationClass = FavoriteDetailActivity.class;
        Intent intentToStartFavoriteDetailActivity = new Intent(context, destinationClass);
        intentToStartFavoriteDetailActivity.putExtra(Intent.EXTRA_TEXT, movieParcel);
        startActivity(intentToStartFavoriteDetailActivity);
    }

    private void showMovieDataView() {
        //mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /*private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(
                    MOVIE_LOADER_ID,
                    null,
                    this
            );
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        //mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Context context = this;
        Class destinationClass = MainActivity.class;
        Intent intentToStartMainActivity = new Intent(context, destinationClass);

        if (id == R.id.action_popular) {
            PopularMoviePreferences.setSortBy(FavoriteActivity.this, "popular");
            startActivity(intentToStartMainActivity);
            return true;
        }

        if (id == R.id.action_toprated) {
            PopularMoviePreferences.setSortBy(FavoriteActivity.this, "toprated");
            startActivity(intentToStartMainActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Lesson 6 - Preferences
    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences,
            String s
    ) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}
