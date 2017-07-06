package com.codepath.apps.twitterApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    public final static int COMPOSE_REQUEST_CODE = 20;

    public final static int CHAR_MAX = 140;

    private TwitterClient client;
    ImageButton retweetButton;
    ImageButton favoriteButton;
    ImageButton replyButton;
    String toUser;
    long uid;
    boolean favorited;
    boolean retweeted;
    private int numRetweets;
    private int numFaves;
    private int position;

    EditText simpleEditText;
    Button button;
    TextView tvCharCount;
    TextView replyTo;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView timestamp;
    public TextView tvRetweets;
    public TextView tvFavorites;
    public ImageView ivMedia;
    public TextView tvAtName;

    public Tweet returnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApplication.getRestClient();
        retweetButton = (ImageButton) findViewById(R.id.ibRetweet);
        favoriteButton = (ImageButton) findViewById(R.id.ibFavorite);
        replyButton = (ImageButton) findViewById(R.id.ibReply);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        timestamp = (TextView) findViewById(R.id.tvRelativeTime);
        tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        tvFavorites = (TextView) findViewById(R.id.tvFavorites);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);
        tvAtName = (TextView) findViewById(R.id.tvAtName);

        simpleEditText = (EditText) findViewById(R.id.etTweetBody);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        replyTo = (TextView) findViewById(R.id.tvAtReply);
        button = (Button) findViewById(R.id.btTweet);

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
        tvAtName.setText("@" + tweet.user.screenName);

        numRetweets = tweet.numRetweets;
        numFaves = tweet.numFaves;
        tvRetweets.setText(String.valueOf(numRetweets));
        tvFavorites.setText(String.valueOf(numFaves));

        // set favorited or retweeted appropriately
        favorited = tweet.favorited;
        retweeted = tweet.retweeted;
        if (favorited) {
            favoriteButton.setImageResource(R.drawable.option_favorite_selected);
        }
        if (retweeted) {
            retweetButton.setImageResource(R.drawable.option_retweet_selected);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAG, "Found toolbar");

        // loading profile image
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);


        // load extra image
        String url = tweet.media_url;
        if (url.equals("")) {
            // remove image view
            ivMedia.setVisibility(View.GONE);
        } else {
            ivMedia.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.media_url)
                    .into(ivMedia);
        }

        // list reply name and ID
        toUser = tweet.user.screenName;
        uid = tweet.uid;

        replyTo = (TextView) findViewById(R.id.tvAtReply);
        replyTo.setText("@" + toUser);

        setCharCounter();
        setTweetListener();

        // check whether "reply" was clicked or not
        boolean reply = i.getBooleanExtra("Reply", false);
        Log.i(TAG, String.valueOf(reply));
        if (reply) {
            showSoftKeyboard(true);
        }


        // set up listeners for buttons
        setRetweetListener();
        setFavoriteListener();
        setReplyListener();
    }

    private void setReplyListener() {
        replyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Replied");

                Intent i = new Intent(TweetDetailsActivity.this, ComposeActivity.class);
                i.putExtra("to_user", toUser);
                i.putExtra("uid", uid);
                startActivityForResult(i, COMPOSE_REQUEST_CODE);
            }
        });

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
                    // returnTweet = tweet;

                    // set local changes
                    retweetButton.setImageResource(R.drawable.option_retweet_selected);
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
                    // returnTweet = tweet;

                    // set local changes
                    retweetButton.setImageResource(R.drawable.option_retweet);
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
                    favoriteButton.setImageResource(R.drawable.option_favorite_selected);
                    favorited = true;
                    numFaves = returnTweet.numFaves;
                    tvFavorites.setText(String.valueOf(numFaves));

                    // change tweet itself
                    // returnTweet.setFavorited(true);
                    // returnTweet.setNumFaves(numFaves);

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
                    favoriteButton.setImageResource(R.drawable.option_favorite);
                    favorited = false;
                    numFaves = returnTweet.numFaves;
                    tvFavorites.setText(String.valueOf(numFaves));

                    // change tweet itself
                    // returnTweet.setFavorited(false);
                    // returnTweet.setNumFaves(numFaves);

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


    private void setTweetListener() {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Replied");
                replyTweet();
            }
        });

    }

    private void setCharCounter() {
        simpleEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCharCount.setText(CHAR_MAX - s.toString().length() + " /140 characters");
            }
        });
    }

    private void replyTweet() {
        Log.i(TAG, "Sent a reply tweet.");
        final String message = simpleEditText.getText().toString();
        final String newMessage = String.format("@%s %s", toUser, message);

        client.replyTweet(newMessage, uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());
                Log.i(TAG, message);

                try {
                    Tweet tweet = Tweet.fromJSON(response);

                    // send back to the original activity
                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
                    i.putExtra("Tweet", tweet);
                    startActivity(i);

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

    private void showSoftKeyboard(boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
//            imm.showSoftInput(tvBody, InputMethodManager.SHOW_IMPLICIT);
            simpleEditText.requestFocus();
            imm.showSoftInput(simpleEditText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(tvBody.getWindowToken(), 0);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("Tweet");

            // prepare tweet to be passed back
            returnTweet = tweet;

        }

    }
}


