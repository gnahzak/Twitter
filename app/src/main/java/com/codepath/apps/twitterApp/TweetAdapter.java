package com.codepath.apps.twitterApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.Tweet;

import java.util.List;

import static android.content.ContentValues.TAG;

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
        final Tweet tweet = mTweets.get(position);

        // populate views
        final String username = tweet.user.name;
        holder.tvUserName.setText(username);
        holder.tvBody.setText(tweet.body);
        holder.timestamp.setText(tweet.timestamp);

        // loading profile image
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

        // load extra image
        String url = tweet.media_url;
        if (url == "") {
            // remove image view
            holder.ivMedia.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "HERE!!");
            holder.ivMedia.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(tweet.media_url)
                    .into(holder.ivMedia);
        }

        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ibReply:
                        Log.i(TAG, username);
                        Intent i = new Intent(context, ReplyActivity.class);
                        i.putExtra("Tweet", tweet);
                        context.startActivity(i);
                        return;
//                    case R.id.ibRetweet:
//                        //TODO
//                        return;
//                    case R.id.ibLike:
//                        //TODO
//                        return;
                    default:
                        Log.i(TAG, "Incorrect button chosen");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView timestamp;
        public ImageButton replyButton;
        public ImageButton retweetButton;
        public ImageButton likeButton;
        public ImageView ivMedia;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            timestamp = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            replyButton = (ImageButton) itemView.findViewById(R.id.ibReply);
            retweetButton = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            likeButton = (ImageButton) itemView.findViewById(R.id.ibFavorite);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            // get tweet at that position in the list
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = mTweets.get(position);
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra("Tweet", tweet);

                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }


}
