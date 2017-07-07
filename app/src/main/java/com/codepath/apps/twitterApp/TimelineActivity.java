package com.codepath.apps.twitterApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterApp.fragments.TweetsPagerAdapter;

public class TimelineActivity extends AppCompatActivity {

    public final static String TAG = "TimelineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAG, "Found toolbar");

        final ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        final TweetsPagerAdapter tpAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(tpAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // vpPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(tpAdapter.getPageTitle(tab.getPosition()));
                //Set title here using setTitle()
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.miWriteTweet:
//                //TODO: fill in more menu buttons
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void onProfileView(MenuItem item) {
        // launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }



}
