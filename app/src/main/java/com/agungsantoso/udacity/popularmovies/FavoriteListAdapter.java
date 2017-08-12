package com.agungsantoso.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agungsantoso.udacity.popularmovies.data.MovieParcel;
import com.squareup.picasso.Picasso;

/**
 * Created by agung.santoso on 08/08/2017.
 */

// lesson 3 - Recycler View
public class FavoriteListAdapter
        extends RecyclerView.Adapter<FavoriteListAdapter.MovieListAdapterViewHolder> {

    private final String TAG = FavoriteListAdapter.class.getSimpleName();

    private Context mContext;
    private Cursor mMovieData;

    private final FavoriteAdapterOnClickHandler mClickHandler;

    public interface FavoriteAdapterOnClickHandler {
        void onClick(MovieParcel movieId);
    }

    private Cursor mCursor;

    public FavoriteListAdapter(
            @NonNull Context context,
            FavoriteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class MovieListAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final ImageView mMovieImageView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.movie_thumbnail_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //String movieId = mMovieData[adapterPosition];
            mCursor.moveToPosition(adapterPosition);
            String id = mCursor.getString(FavoriteActivity.INDEX_ID);
            String title = mCursor.getString(FavoriteActivity.INDEX_TITLE);
            String poster = mCursor.getString(FavoriteActivity.INDEX_POSTER_PATH);
            String release_date = mCursor.getString(FavoriteActivity.INDEX_RELAESE);
            Integer vote_count = mCursor.getInt(FavoriteActivity.INDEX_VOTE);
            Double vote_average = mCursor.getDouble(FavoriteActivity.INDEX_AVERAGE);
            String plot = mCursor.getString(FavoriteActivity.INDEX_PLOT);

            MovieParcel movieParcel = new MovieParcel(
                    id,
                    title,
                    poster,
                    release_date,
                    vote_count,
                    vote_average,
                    plot
            );
            mClickHandler.onClick(movieParcel);
        }
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int viewType
    ) {
        Context context = viewGroup.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            MovieListAdapterViewHolder movieListAdapterViewHolder,
            int position
    ) {
        mCursor.moveToPosition(position);
        String posterPath = mCursor.getString(FavoriteActivity.INDEX_POSTER_PATH);

        // Implementation guide stage 1
        // https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.62ddnfafh87n
        ImageView imageView = movieListAdapterViewHolder.mMovieImageView;
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w185";
        String POSTER_URL = POSTER_BASE_URL + POSTER_SIZE + "/" + posterPath;
        Picasso.with(mContext)
                .load(POSTER_URL)
                .into(imageView);
    }

    @Override
    public int getItemCount(){
        if (null == mCursor) {
            return 0;
        }
        Log.d(TAG, "getItemCount = " + mCursor.getCount());
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void setMovieData(Cursor movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
