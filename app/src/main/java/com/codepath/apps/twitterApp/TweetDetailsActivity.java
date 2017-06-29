package com.codepath.apps.twitterApp;

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

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView timestamp;

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

        // unwrap tweet passed in via intent
        Tweet tweet = (Tweet) getIntent().getParcelableExtra("Tweet");

        // fill in tweet information
        String username = tweet.user.name;
        tvUserName.setText(username);
        tvBody.setText(tweet.body);
        timestamp.setText(tweet.timestamp);

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
                    retweetButton.setImageResource(R.drawable.ic_launcher);
                    retweeted = true;
                    Tweet tweet = Tweet.fromJSON(response);

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
                    retweetButton.setImageResource(R.drawable.redo_button);
                    retweeted = false;
                    Tweet tweet = Tweet.fromJSON(response);

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
                    favorited = true;
                    favoriteButton.setImageResource(R.drawable.ic_launcher);
                    Tweet tweet = Tweet.fromJSON(response);

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
                    favoriteButton.setImageResource(R.drawable.empty_heart);
                    favorited = false;
                    Tweet tweet = Tweet.fromJSON(response);

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
}


