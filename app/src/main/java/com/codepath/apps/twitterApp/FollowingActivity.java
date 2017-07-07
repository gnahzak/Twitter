package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.twitterApp.R.id.rvTweet;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class FollowingActivity extends AppCompatActivity {

    public final static String TAG = "FollowingActivity";
    public final static int COMPOSE_REQUEST_CODE = 20;

    private TwitterClient client;
    private UserAdapter userAdapter;
    private ArrayList<User> users;
    private RecyclerView rvUsers;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton btCompose;

    long uid;
    public long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        client = TwitterApplication.getRestClient();

        // unwrap user ID passed in via intent
        Intent i = getIntent();
        uid = i.getLongExtra("uid", 0);

        // set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAG, "Found toolbar");

        // find RecyclerView
        rvUsers = (RecyclerView) findViewById(rvTweet);

        // initialize the data source
        users = new ArrayList<>();

        // construct adapter
        userAdapter = new UserAdapter(users);

        // set up RecyclerView (layout manager, using adapter)

        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(userAdapter);

        // set up compose button and listener
        btCompose = (FloatingActionButton) findViewById(R.id.btCompose);
        setComposeListener();

        maxId = Long.MAX_VALUE;

        // adds refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                userAdapter.clear();
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
        rvUsers.setLayoutManager(linearLayoutManager);
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
        rvUsers.addOnScrollListener(scrollListener);

        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_following, menu);
        return true;
    }

    private void setComposeListener() {
        btCompose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked compose");
                Intent i = new Intent(FollowingActivity.this, ComposeActivity.class);
                startActivityForResult(i, COMPOSE_REQUEST_CODE);
            }
        });

        Log.i(TAG, "Finished setting compose listener");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
            //Tweet tweet = data.getParcelableExtra("Tweet");

            Toast.makeText(this, "Composed", Toast.LENGTH_LONG);
        }

    }

    public void addItems(JSONArray response) {
        Log.d("TwitterClient", response.toString());

        // iterate through JSON array and deserialize each entry
        for (int i = 0; i < response.length(); i++) {
            // convert each JSON object to a User model and add to our data source

            // notify adapter that we added an item
            User user;
            try {
                user = User.fromJSON(response.getJSONObject(i));
                users.add(user);
                userAdapter.notifyItemInserted(users.size() - 1);

                // modify max_id
                if (user.uid < maxId) {
                    maxId = user.uid;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void populateTimeline() {
        Toast.makeText(this, "Populate", Toast.LENGTH_LONG);
        client.getFollowingList(uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
                try {
                    addItems(response.getJSONArray("users"));
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

    public void loadNextDataFromApi() { }
}
