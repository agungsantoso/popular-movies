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

import com.agungsantoso.udacity.popularmovies.data.VideoParcel;
import com.agungsantoso.udacity.popularmovies.data.VideoParcel;
import com.squareup.picasso.Picasso;

/**
 * Created by agung.santoso on 08/08/2017.
 */

// lesson 3 - Recycler View
public class VideoListAdapter
        extends RecyclerView.Adapter<VideoListAdapter.VideoListAdapterViewHolder> {

    private final String TAG = VideoListAdapter.class.getSimpleName();

    private Context mContext;
    private VideoParcel[] mVideoData;

    private final VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(VideoParcel videoParcel);
    }

    public VideoListAdapter(
            @NonNull Context context,
            VideoAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class VideoListAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mVideoTextView;

        public VideoListAdapterViewHolder(View view) {
            super(view);
            mVideoTextView = view.findViewById(R.id.video_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            VideoParcel video = mVideoData[adapterPosition];
            mClickHandler.onClick(video);
        }
    }

    @Override
    public VideoListAdapterViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int viewType
    ) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            VideoListAdapterViewHolder videoListAdapterViewHolder,
            int position
    ) {
        String name = mVideoData[position].getName();
        TextView videoName = videoListAdapterViewHolder.mVideoTextView;
        videoName.setText(name);
    }

    @Override
    public int getItemCount(){
        if (null == mVideoData) {
            return 0;
        }

        Log.d(TAG, "length = " + mVideoData.length);
        return mVideoData.length;
    }

    public void setVideoData(VideoParcel[] videoData) {
        mVideoData = videoData;
        notifyDataSetChanged();
    }
}
