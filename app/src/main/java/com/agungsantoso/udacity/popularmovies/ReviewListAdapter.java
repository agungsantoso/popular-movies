package com.agungsantoso.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agungsantoso.udacity.popularmovies.data.ReviewParcel;
import com.agungsantoso.udacity.popularmovies.data.ReviewParcel;
import com.agungsantoso.udacity.popularmovies.data.ReviewParcel;
import com.squareup.picasso.Picasso;

/**
 * Created by agung.santoso on 08/08/2017.
 */

// lesson 3 - Recycler View
public class ReviewListAdapter
        extends RecyclerView.Adapter<ReviewListAdapter.ReviewListAdapterViewHolder> {

    private final String TAG = ReviewListAdapter.class.getSimpleName();

    private Context mContext;
    private ReviewParcel[] mVideoData;

    private final ReviewAdapterOnClickHandler mClickHandler;

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewParcel ReviewParcel);
    }

    public ReviewListAdapter(
            @NonNull Context context,
            ReviewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class ReviewListAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mAuthorTextView;
        public final TextView mContentTextView;

        public ReviewListAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.author_text);
            mContentTextView = view.findViewById(R.id.review_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ReviewParcel video = mVideoData[adapterPosition];
            mClickHandler.onClick(video);
        }
    }

    @Override
    public ReviewListAdapterViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int viewType
    ) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ReviewListAdapterViewHolder ReviewListAdapterViewHolder,
            int position
    ) {
        String author = mVideoData[position].getAuthor();
        String contents = mVideoData[position].getReview();
        
        TextView authorName = ReviewListAdapterViewHolder.mAuthorTextView;
        TextView videoName = ReviewListAdapterViewHolder.mContentTextView;

        authorName.setText(author);
        videoName.setText(contents);
    }

    @Override
    public int getItemCount(){
        if (null == mVideoData) {
            return 0;
        }

        Log.d(TAG, "length = " + mVideoData.length);
        return mVideoData.length;
    }

    public void setReviewData(ReviewParcel[] videoData) {
        mVideoData = videoData;
        notifyDataSetChanged();
    }
}
