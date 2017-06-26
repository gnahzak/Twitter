package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.twitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    // tag for all logging from this activity
    public final static String TAG = "ComposeActivity";

    EditText simpleEditText;
    private TwitterClient client;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();

        simpleEditText = (EditText) findViewById(R.id.etTweetBody);

        button = (Button) findViewById(R.id.btTweet);
        setTweetListener();
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

    private void setTweetListener() {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Tweeted");
                composeTweet();
            }
        });

    }
}
