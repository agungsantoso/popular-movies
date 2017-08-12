package com.agungsantoso.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agungsantoso.udacity.popularmovies.data.MovieContract;
import com.agungsantoso.udacity.popularmovies.data.MovieParcel;
import com.agungsantoso.udacity.popularmovies.data.PopularMoviePreferences;
import com.agungsantoso.udacity.popularmovies.data.ReviewParcel;
import com.agungsantoso.udacity.popularmovies.data.VideoParcel;
import com.agungsantoso.udacity.popularmovies.utilities.MovieDbJsonUtils;
import com.agungsantoso.udacity.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class FavoriteDetailActivity
        extends AppCompatActivity
        implements
        VideoListAdapter.VideoAdapterOnClickHandler,
        ReviewListAdapter.ReviewAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayMap> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String MOVIE_SHARE_HASHTAG = "#PopularMovieApp";

    private MovieParcel mMovie;
    private TextView mTitleDisplay;
    private ImageView mPosterDisplay;
    private TextView mRelaseDisplay;
    private TextView mVoteDisplay;
    private TextView mAverageDisplay;
    private TextView mPlotDisplay;

    private RecyclerView mRecyclerView;
    private VideoListAdapter mVideoListAdapter;
    private ReviewListAdapter mReviewListAdapter;

    private String KEY;

    private Menu mMenu;
    private static boolean IS_FAVORITE_MOVIE;

    private static final int VIDEO_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleDisplay = (TextView) findViewById(R.id.title);
        mPosterDisplay = (ImageView) findViewById(R.id.detail_movie_poster);
        mRelaseDisplay = (TextView) findViewById(R.id.release_date);
        mVoteDisplay = (TextView) findViewById(R.id.vote_count);
        mAverageDisplay = (TextView) findViewById(R.id.vote_average);
        mPlotDisplay = (TextView) findViewById(R.id.plot_synopsis);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                Bundle data = getIntent().getExtras();
                mMovie = data.getParcelable(Intent.EXTRA_TEXT);

                String title = mMovie.getTitle();
                String poster = mMovie.getPoster();
                String release = mMovie.getReleaseDate();
                Integer vote = mMovie.getVoteCount();
                Double average = mMovie.getVoteAverage();
                String plot = mMovie.getPlot();

                mTitleDisplay.setText(title);

                final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
                final String POSTER_SIZE = "w185";
                String POSTER_URL = POSTER_BASE_URL + POSTER_SIZE + "/" + poster;
                Picasso.with(FavoriteDetailActivity.this)
                        .load(POSTER_URL)
                        .into(mPosterDisplay);

                mRelaseDisplay.setText(release);
                mVoteDisplay.setText(vote.toString());
                mAverageDisplay.setText(average.toString());
                mPlotDisplay.setText(plot);

                showVideos();
                showReviews();

                int loaderId = VIDEO_LOADER_ID;
                LoaderManager.LoaderCallbacks<ArrayMap> callback = FavoriteDetailActivity.this;
                Bundle bundleForLoader = null;
                getSupportLoaderManager().initLoader(
                        loaderId,
                        bundleForLoader,
                        callback
                );
            }
        }

    }

    private void showVideos() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_video);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mVideoListAdapter = new VideoListAdapter(this, this);
        mRecyclerView.setAdapter(mVideoListAdapter);
    }

    private void showReviews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mReviewListAdapter = new ReviewListAdapter(this, this);
        mRecyclerView.setAdapter(mReviewListAdapter);
    }

    @Override
    public Loader<ArrayMap> onCreateLoader(
            int id,
            final Bundle loaderArgs
    ) {
        return new AsyncTaskLoader<ArrayMap>(this) {
            ArrayMap mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    //mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayMap loadInBackground() {
                String id = mMovie.getId();
                Log.d(TAG, "fav id = " + id);
                URL videoUrl = NetworkUtils.buildVideoUrl(id);
                URL reviewUrl = NetworkUtils.buildReviewUrl(id);
                try {
                    String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(videoUrl);
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);

                    Log.d(TAG, "jsonVideoResponse" + jsonVideoResponse);
                    Log.d(TAG, "jsonReviewResponse" + jsonReviewResponse);
                    VideoParcel[] videoData = MovieDbJsonUtils.getVideoDataFromJson(
                            FavoriteDetailActivity.this,
                            jsonVideoResponse
                    );
                    ReviewParcel[] reviewData = MovieDbJsonUtils.getReviewsDataFromJson(
                            FavoriteDetailActivity.this,
                            jsonReviewResponse
                    );
                    ArrayMap<String, Parcelable[]> movieData = new ArrayMap(1);
                    movieData.put("videos", videoData);
                    movieData.put("reviews", reviewData);

                    KEY = videoData[0].getKey();
                    Log.d(TAG, "movieData=" + movieData.toString());
                    return movieData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayMap data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(
            Loader<ArrayMap> Loader,
            ArrayMap data
    ) {
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        VideoParcel[] videoData = (VideoParcel[]) data.get("videos");
        ReviewParcel[] reviewData = (ReviewParcel[]) data.get("reviews");

        mVideoListAdapter.setVideoData(videoData);
        mReviewListAdapter.setReviewData(reviewData);

        if (null == data) {
            //showErrorMessage();
        } else {
            //showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(
            Loader<ArrayMap> Loader
    ) {
    }

    private Intent createShareMovieIntent() {
        String MOVIE_NAME = "Watch the trailer of " + mMovie.getTitle() + " at ";
        String TRAILER_URL = "http://www.youtube.com/watch?v=" + KEY;
        String TEXT = MOVIE_NAME + " " + TRAILER_URL + " ";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(TEXT + MOVIE_SHARE_HASHTAG)
                .getIntent();

        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_detail, menu);
        MenuItem menuShare = menu.findItem(R.id.action_share);
        MenuItem menuStar = menu.findItem(R.id.action_favorite);
        menuShare.setIntent(createShareMovieIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {

            String title = (String) mTitleDisplay.getText();
            String[] selectionArgs = {
                    title
            };

            getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    "title = ?",
                    selectionArgs
            );
            item.setIcon(android.R.drawable.star_big_off);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(VideoParcel videoParcel) {
        String id = videoParcel.getKey();
        // https://stackoverflow.com/a/12439378/448050
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onClick(ReviewParcel reviewParcel) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        // Lesson 4 - Intents
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, reviewParcel);
        startActivity(intentToStartDetailActivity);
    }
}
