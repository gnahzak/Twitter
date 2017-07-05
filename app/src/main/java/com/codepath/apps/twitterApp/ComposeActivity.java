package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.Tweet;
import com.codepath.apps.twitterApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    // tag for all logging from this activity
    public final static String TAG = "ComposeActivity";

    public final static int CHAR_MAX = 140;

    EditText simpleEditText;
    private TwitterClient client;
    Button button;
    TextView tvCharCount;
    ImageButton ibProfile;
    ImageButton ibClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();

        simpleEditText = (EditText) findViewById(R.id.etTweetBody);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        ibProfile = (ImageButton) findViewById(R.id.ibProfile);
        ibClose = (ImageButton) findViewById(R.id.ibClose);
        button = (Button) findViewById(R.id.btTweet);

        setProfileImage();

        setFinishListener();
        setCharCounter();
    }

    private void composeTweet() {
        final String message = simpleEditText.getText().toString();

        client.sendTweet(message, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());
                Log.i(TAG, message);

                try {
                    Tweet tweet = Tweet.fromJSON(response);

                     // send back to the original activity
                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    i.putExtra("Tweet", tweet);
                    setResult(RESULT_OK, i);
                    finish();

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

    private void setFinishListener() {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Tweeted");
                composeTweet();
            }
        });

        ibClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Closed");
                finish();
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
                int textLength = s.toString().length();
                tvCharCount.setText(String.valueOf(CHAR_MAX - textLength));

                // change color appropriately
                if (textLength > 140) {
                    tvCharCount.setTextColor(Color.RED);
                } else {
                    tvCharCount.setTextColor(Color.GRAY);
                }
            }
        });
    }

    private void setProfileImage() {
        client.getUserInfo(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // deserialize user object and set title bar
                try {
                    User user = User.fromJSON(response);
                    Glide.with(ComposeActivity.this).load(user.profileImageUrl).into(ibProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
