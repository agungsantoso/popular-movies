package com.agungsantoso.udacity.popularmovies;

import android.content.Context;
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
public class MovieListAdapter
    extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private final String TAG = MovieListAdapter.class.getSimpleName();

    private Context mContext;
    private MovieParcel[] mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieParcel movieParcel);
    }

    public MovieListAdapter(
            @NonNull Context context,
            MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class MovieListAdapterViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        public final ImageView mMovieImageView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.movie_thumbnail_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieParcel movie = mMovieData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int viewType
    ) {
        Context context = viewGroup.getContext();
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
        String posterPath = mMovieData[position].getPoster();

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
        if (null == mMovieData) {
            return 0;
        }

        return mMovieData.length;
    }

    public void setMovieData(MovieParcel[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
