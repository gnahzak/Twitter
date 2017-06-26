package com.codepath.apps.twitterApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.Tweet;

import java.util.List;

/**
 * Created by kazhang on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    // pass in Tweets array in constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // inflate layout for each row and cache references into ViewHolder

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind values based on position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // use position to get data
        Tweet tweet = mTweets.get(position);

        // populate views
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.timestamp.setText(tweet.timestamp);

        // loading profile image
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            timestamp = (TextView) itemView.findViewById(R.id.tvRelativeTime);
        }
    }
}
