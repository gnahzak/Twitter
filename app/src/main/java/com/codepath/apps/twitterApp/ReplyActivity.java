package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class ReplyActivity extends AppCompatActivity {

    // tag for all logging from this activity
    public final static String TAG = "ReplyActivity";

    public final static int CHAR_MAX = 140;

    EditText simpleEditText;
    private TwitterClient client;
    Button button;
    TextView tvCharCount;
    TextView replyTo;
    String toUser;
    long uid;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView timestamp;
    public ImageButton replyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "here1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        client = TwitterApplication.getRestClient();

        simpleEditText = (EditText) findViewById(R.id.etTweetBody);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        replyTo = (TextView) findViewById(R.id.tvAtReply);
        button = (Button) findViewById(R.id.btTweet);

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


        // loading profile image
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

        // list reply name correctly
        toUser = tweet.user.screenName;
        replyTo = (TextView) findViewById(R.id.tvAtReply);
        replyTo.setText(toUser);

        uid = tweet.uid;

        setCharCounter();
        setTweetListener();

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
                    Intent i = new Intent(ReplyActivity.this, TimelineActivity.class);
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
}
