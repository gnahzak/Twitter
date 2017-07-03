package com.codepath.apps.twitterApp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterApp.ComposeActivity;
import com.codepath.apps.twitterApp.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterApp.R;
import com.codepath.apps.twitterApp.TweetAdapter;
import com.codepath.apps.twitterApp.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codepath.apps.twitterApp.R.id.rvTweet;

/**
 * Created by kazhang on 7/3/17.
 */

public class TweetsListFragment extends Fragment {

    public final static int COMPOSE_REQUEST_CODE = 20;
    public final static int DETAILS_REQUEST_CODE = 30;
    public final static String TAG = "TweetsListFragment";

    private TweetAdapter tweetAdapter;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton btCompose;

    public long maxId;

    // inflation inside onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        // find RecyclerView
        rvTweets = (RecyclerView) v.findViewById(rvTweet);

        // initialize the data source
        tweets = new ArrayList<>();

        // construct adapter
        tweetAdapter = new TweetAdapter(tweets);

        // set up RecyclerView (layout manager, using adapter)

        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTweets.setAdapter(tweetAdapter);

        // set up compose button and listener
        btCompose = (FloatingActionButton) v.findViewById(R.id.btCompose);
        setComposeListener();

        maxId = Long.MAX_VALUE;

        // adds refresh
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                tweetAdapter.clear();
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // set up infinite scroll
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                loadNextDataFromApi();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        return v;
    }

    public void addItems(JSONArray response) {
        Log.d("TwitterClient", response.toString());

        // iterate through JSON array and deserialize each entry
        for (int i = 0; i < response.length(); i++) {
            // convert each JSON object to a Tweet model and add to our data source

            // notify adapter that we added an item
            Tweet tweet;
            try {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);

                // modify max_id
                if (tweet.uid < maxId) {
                    maxId = tweet.uid;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("Tweet");

            // insert the new tweet and notify the adapter
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
        else if (resultCode == RESULT_OK && requestCode == DETAILS_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("Tweet");
            int position = data.getIntExtra("Position", 0);
            tweets.set(position, tweet);
            tweetAdapter.notifyItemChanged(position);

            Log.i(TAG, tweet.media_url);
        }

    }

    public void populateTimeline() { }

    public void loadNextDataFromApi() { }

    private void setComposeListener() {
        btCompose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked compose");
                Intent i = new Intent(getContext(), ComposeActivity.class);
                startActivityForResult(i, COMPOSE_REQUEST_CODE);
            }
        });

        Log.i(TAG, "Finished setting compose listener");

    }
}
