package com.agungsantoso.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agungsantoso.udacity.popularmovies.data.MovieParcel;
import com.agungsantoso.udacity.popularmovies.data.PopularMoviePreferences;
import com.agungsantoso.udacity.popularmovies.utilities.MovieDbJsonUtils;
import com.agungsantoso.udacity.popularmovies.utilities.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.net.URL;

public class MainActivity
        extends AppCompatActivity
        implements
            MovieListAdapter.MovieAdapterOnClickHandler,
            LoaderCallbacks<MovieParcel[]>,
            SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private MovieListAdapter mMovieListAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        // Simple Android grid example using RecyclerView with GridLayoutManager (like the old GridView)
        // https://stackoverflow.com/a/40587169/448050
        int numberOfColumns = 2;

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        //showLoading();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        int loaderId = MOVIE_LOADER_ID;
        LoaderCallbacks<MovieParcel[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(
                loaderId,
                bundleForLoader,
                callback
        );
    }

    // Lesson 5 - Lifecycle
    @Override
    public Loader<MovieParcel[]> onCreateLoader(
            int id,
            final Bundle loaderArgs
    ) {
        return new AsyncTaskLoader<MovieParcel[]>(this) {
            MovieParcel[] mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieParcel[] loadInBackground() {
                String sortBy = PopularMoviePreferences.getPreferredSortBy(MainActivity.this);

                String appName = getResources().getString(R.string.app_name);
                title = appName + " (Most Popular)";
                if(sortBy == "toprated") {
                    title = appName + " (Highest Rated)";
                }

                // https://stackoverflow.com/a/5162096/448050
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(title);
                    }
                });

                URL movieDbUrl = NetworkUtils.buildUrl(sortBy);
                try {
                    String jsonMovieDbResponse = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                    MovieParcel[] movieDbData = MovieDbJsonUtils.getFullMovieDataFromJson(
                            MainActivity.this,
                            jsonMovieDbResponse
                    );
                    return movieDbData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(MovieParcel[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(
            Loader<MovieParcel[]> Loader,
            MovieParcel[] data
    ) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieListAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(
            Loader<MovieParcel[]> Loader
    ) {
    }

    private void invalidateData() {
        mMovieListAdapter.setMovieData(null);
    }

    @Override
    public void onClick(MovieParcel movieParcel) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        // Lesson 4 - Intents
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieParcel);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

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
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

        if (id == R.id.action_popular) {
            PopularMoviePreferences.setSortBy(MainActivity.this, "popular");
            invalidateData();
            getSupportLoaderManager().restartLoader(
                    MOVIE_LOADER_ID,
                    null,
                    this
            );
            return true;
        }

        if (id == R.id.action_toprated) {
            PopularMoviePreferences.setSortBy(MainActivity.this, "toprated");
            invalidateData();
            getSupportLoaderManager().restartLoader(
                    MOVIE_LOADER_ID,
                    null,
                    this
            );
            return true;
        }

        if(id == R.id.action_favorite) {
            Context context = this;
            Class destinationClass = FavoriteActivity.class;
            Intent intentToStartFavoriteActivity = new Intent(context, destinationClass);
            startActivity(intentToStartFavoriteActivity);
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
