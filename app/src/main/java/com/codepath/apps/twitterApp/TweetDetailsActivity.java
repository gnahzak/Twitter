package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {

    // tag for all logging from this activity
    public final static String TAG = "TweetDetailsActivity";

    private TwitterClient client;
    ImageButton retweetButton;
    ImageButton favoriteButton;
    String toUser;
    long uid;
    boolean favorited;
    boolean retweeted;
    private int numRetweets;
    private int numFaves;
    private int position;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView timestamp;
    public TextView tvRetweets;
    public TextView tvFavorites;

    public Tweet returnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApplication.getRestClient();
        retweetButton = (ImageButton) findViewById(R.id.ibRetweet);
        favoriteButton = (ImageButton) findViewById(R.id.ibFavorite);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        timestamp = (TextView) findViewById(R.id.tvRelativeTime);
        tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        tvFavorites = (TextView) findViewById(R.id.tvFavorites);

        // unwrap tweet passed in via intent
        Intent i = getIntent();
        Tweet tweet = (Tweet) i.getParcelableExtra("Tweet");
        returnTweet = tweet;
        position = i.getIntExtra("Position", 0);

        // fill in tweet information
        String username = tweet.user.name;
        tvUserName.setText(username);
        tvBody.setText(tweet.body);
        timestamp.setText(tweet.timestamp);

        numRetweets = tweet.numRetweets;
        numFaves = tweet.numFaves;
        tvRetweets.setText(String.valueOf(numRetweets));
        tvFavorites.setText(String.valueOf(numFaves));

        // set favorited or retweeted appropriately
        favorited = tweet.favorited;
        retweeted = tweet.retweeted;
        if (favorited) {
            favoriteButton.setImageResource(R.drawable.ic_launcher);
        }
        if (retweeted) {
            retweetButton.setImageResource(R.drawable.ic_launcher);
        }

        // loading profile image
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

        // list reply name and ID
        toUser = tweet.user.screenName;
        uid = tweet.uid;

        // set up listeners for buttons
        setRetweetListener();
        setFavoriteListener();
    }

    private void setRetweetListener() {
        retweetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Retweeted");
                if (retweeted) {
                    unreTweet();
                } else {
                    retweetTweet();
                }
            }
        });

    }

    private void setFavoriteListener() {
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked favorite");
                if (favorited) {
                    unfavTweet();
                } else {
                    favoriteTweet();
                }
            }
        });

    }



    private void retweetTweet() {

        client.retweet(uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    returnTweet = tweet;

                    // set local changes
                    retweetButton.setImageResource(R.drawable.ic_launcher);
                    retweeted = true;
                    numRetweets += 1;
                    tvRetweets.setText(String.valueOf(numRetweets));

                    // change tweet itself
                    returnTweet.setRetweeted(true);
                    returnTweet.setNumRetweets(numRetweets);

                    // TODO: process retweeted tweet
                    // send back to the original activity
//                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
//                    i.putExtra("Tweet", tweet);
//                    setResult(RESULT_OK, i);
//                    finish();

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

    private void unreTweet() {

        client.unretweet(uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {

                    Tweet tweet = Tweet.fromJSON(response);
                    returnTweet = tweet;

                    // set local changes
                    retweetButton.setImageResource(R.drawable.redo_button);
                    retweeted = false;
                    numRetweets -= 1;
                    tvRetweets.setText(String.valueOf(numRetweets));

                    // change tweet itself
                    returnTweet.setRetweeted(false);
                    returnTweet.setNumRetweets(numRetweets);

                    // TODO: process retweeted tweet
                    // send back to the original activity
//                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
//                    i.putExtra("Tweet", tweet);
//                    setResult(RESULT_OK, i);
//                    finish();

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

    private void favoriteTweet() {

        client.favoriteTweet(uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    returnTweet = tweet;

                    // set local changes
                    favoriteButton.setImageResource(R.drawable.ic_launcher);
                    favorited = true;
                    numFaves += 1;
                    tvFavorites.setText(String.valueOf(numFaves));

                    // change tweet itself
                    returnTweet.setFavorited(true);
                    returnTweet.setNumFaves(numFaves);

                    // TODO: process result
                    // send back to the original activity
//                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
//                    i.putExtra("Tweet", tweet);
//                    setResult(RESULT_OK, i);
//                    finish();

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

    private void unfavTweet() {

        client.unfavTweet(uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {

                    Tweet tweet = Tweet.fromJSON(response);
                    returnTweet = tweet;

                    // set local changes
                    favoriteButton.setImageResource(R.drawable.empty_heart);
                    favorited = false;
                    numFaves -= 1;
                    tvFavorites.setText(String.valueOf(numFaves));

                    // change tweet itself
                    returnTweet.setFavorited(false);
                    returnTweet.setNumFaves(numFaves);

                    // TODO: process result
                    // send back to the original activity
//                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
//                    i.putExtra("Tweet", tweet);
//                    setResult(RESULT_OK, i);
//                    finish();

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

    @Override
    public void onBackPressed() {

        Log.i(TAG, "Pressed back button");

        // send back to the original activity
        Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
        i.putExtra("Tweet", returnTweet);
        i.putExtra("Position", position);
        setResult(RESULT_OK, i);
        finish();
    }
}


