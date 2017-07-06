package com.codepath.apps.twitterApp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kazhang on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions", "Search"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // return total # of fragments
    @Override
    public int getCount() {
        return 3;
    }

    // return fragment to use, depending on position
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        } else if (position == 2) {
            return new SearchTimelineFragment();
        } else {
            return null;
        }
    }

    // return title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
