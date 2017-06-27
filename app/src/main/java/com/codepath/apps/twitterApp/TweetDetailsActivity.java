package com.codepath.apps.twitterApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.Tweet;

public class TweetDetailsActivity extends AppCompatActivity {

    // tag for all logging from this activity
    public final static String TAG = "TweetDetailsActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

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

        uid = tweet.uid;
    }
}
