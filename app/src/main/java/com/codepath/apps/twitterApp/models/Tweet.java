package com.codepath.apps.twitterApp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kazhang on 6/26/17.
 */

public class Tweet {

    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }
}
