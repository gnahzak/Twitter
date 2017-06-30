package com.codepath.apps.twitterApp;

import android.app.Activity;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.ContentValues.TAG;

/**
 * Created by kazhang on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    public final static int DETAILS_REQUEST_CODE = 30;

    private final TwitterClient client = TwitterApplication.getRestClient();;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // use position to get data
        final Tweet tweet = mTweets.get(position);

        // populate views
        final String username = tweet.user.name;
        holder.tvUserName.setText(username);
        holder.tvBody.setText(tweet.body);
        holder.timestamp.setText(tweet.timestamp);
        holder.tvRetweets.setText(String.valueOf(tweet.numRetweets));
        holder.tvFavorites.setText(String.valueOf(tweet.numFaves));

        if (tweet.retweeted) {
            holder.retweetButton.setImageResource(R.drawable.option_retweet_selected);
        } else {
            holder.retweetButton.setImageResource(R.drawable.option_retweet);
        }

        if (tweet.favorited) {
            holder.favoriteButton.setImageResource(R.drawable.option_favorite_selected);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.option_favorite);
        }

        // loading profile image
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

        // load extra image
        String url = tweet.media_url;
        if (url.equals("")) {
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
//                        Log.i(TAG, username);
//                        Intent i = new Intent(context, TweetDetailsActivity.class);
//                        i.putExtra("Tweet", tweet);
//                        i.putExtra("Reply", true);
//                        context.startActivity(i);
//                        return;

                        int position = holder.getAdapterPosition();

                        // get tweet at that position in the list
                        if (position != RecyclerView.NO_POSITION) {
                            Tweet tweet = mTweets.get(position);
                            Intent intent = new Intent(context, TweetDetailsActivity.class);
                            intent.putExtra("Tweet", tweet);
                            intent.putExtra("Position", position);
                            intent.putExtra("Reply", true);

                            ((Activity) context).startActivityForResult(intent, DETAILS_REQUEST_CODE);
                        }
                    default:
                        Log.i(TAG, "Incorrect button chosen");
                }
            }
        });

        holder.retweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ibRetweet:
                        if (tweet.retweeted) {
                            unreTweet(holder, tweet);
                        } else {
                            retweetTweet(holder, tweet);
                        }
                        return;
                    default:
                        Log.i(TAG, "Incorrect button chosen");
                }
            }
        });

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ibFavorite:
                        if (tweet.favorited) {
                            unfavTweet(holder, tweet);
                        } else {
                            favoriteTweet(holder, tweet);
                        }
                        return;
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
        public ImageButton favoriteButton;
        public ImageView ivMedia;
        public TextView tvRetweets;
        public TextView tvFavorites;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            timestamp = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            replyButton = (ImageButton) itemView.findViewById(R.id.ibReply);
            retweetButton = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            favoriteButton = (ImageButton) itemView.findViewById(R.id.ibFavorite);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
            tvRetweets = (TextView) itemView.findViewById(R.id.tvRetweets);
            tvFavorites = (TextView) itemView.findViewById(R.id.tvFavorites);


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
                intent.putExtra("Position", position);
                intent.putExtra("Reply", false);

                ((Activity) context).startActivityForResult(intent, DETAILS_REQUEST_CODE);
            }

        }
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    private void retweetTweet(final ViewHolder holder, final Tweet tweet) {

        client.retweet(tweet.uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {
                    Tweet newTweet = Tweet.fromJSON(response);

                    // set local changes
                    int numRetweets = tweet.numRetweets;
                    holder.retweetButton.setImageResource(R.drawable.option_retweet_selected);
                    tweet.setRetweeted(true);
                    numRetweets += 1;
                    tweet.setNumRetweets(numRetweets);
                    holder.tvRetweets.setText(String.valueOf(numRetweets));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    private void unreTweet(final ViewHolder holder, final Tweet tweet) {

        client.unretweet(tweet.uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {

                    Tweet newTweet = Tweet.fromJSON(response);

                    // set local changes
                    int numRetweets = tweet.numRetweets;
                    holder.retweetButton.setImageResource(R.drawable.option_retweet);
                    tweet.setRetweeted(false);
                    numRetweets -= 1;
                    tweet.setNumRetweets(numRetweets);
                    holder.tvRetweets.setText(String.valueOf(numRetweets));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    private void favoriteTweet(final ViewHolder holder, final Tweet tweet) {

        client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {
                    Tweet newTweet = Tweet.fromJSON(response);

                    int numFaves = tweet.numFaves;
                    holder.favoriteButton.setImageResource(R.drawable.option_favorite_selected);
                    tweet.favorited = true;
                    numFaves += 1;
                    tweet.setNumFaves(numFaves);
                    holder.tvFavorites.setText(String.valueOf(numFaves));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    private void unfavTweet(final ViewHolder holder, final Tweet tweet) {

        client.unfavTweet(tweet.uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {

                    Tweet newTweet = Tweet.fromJSON(response);

                    int numFaves = tweet.numFaves;
                    holder.favoriteButton.setImageResource(R.drawable.option_favorite);
                    tweet.favorited = false;
                    numFaves -= 1;
                    tweet.setNumFaves(numFaves);
                    holder.tvFavorites.setText(String.valueOf(numFaves));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
