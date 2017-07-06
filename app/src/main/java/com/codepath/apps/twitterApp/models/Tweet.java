package com.codepath.apps.twitterApp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.apps.twitterApp.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by kazhang on 6/26/17.
 */

public class Tweet implements Parcelable {

    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String timestamp;
    public boolean favorited;
    public boolean retweeted;
    public String media_url;
    public int numRetweets;
    public int numFaves;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.timestamp = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.numRetweets = jsonObject.getInt("retweet_count");
        tweet.numFaves = jsonObject.getInt("favorite_count");

        tweet.media_url = "";
        // get first url for media
        try {
            tweet.media_url = getImageUrl(jsonObject.getJSONObject("entities").getJSONArray("media"));
        } catch (JSONException e){
            Log.i(TAG, "No media found");
            e.printStackTrace();
        }

        return tweet;
    }

    private Tweet() {
        body = "";
        uid = 0;
        user = new User();
        createdAt = "";
        timestamp = "";
        favorited = false;
        retweeted = false;
        media_url = "";
        numRetweets = 0;
        numFaves = 0;
    }

    private Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
        timestamp = in.readString();
        favorited = (Boolean) in.readValue(null);
        retweeted = (Boolean) in.readValue(null);
        media_url = in.readString();
        numRetweets = in.readInt();
        numFaves = in.readInt();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // return relativeDate;

        return TimeFormatter.getTimeDifference(rawJsonDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(body);
        out.writeLong(uid);
        out.writeParcelable(user, flags);
        out.writeString(createdAt);
        out.writeString(timestamp);
        out.writeValue(favorited);
        out.writeValue(retweeted);
        out.writeString(media_url);
        out.writeInt(numRetweets);
        out.writeInt(numFaves);
    }

    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {

        // calls our new constructor and passes along the parcel, the returns the new object
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    private static String getImageUrl(JSONArray mediaArray) {
        if (mediaArray.length() < 1) {
            return "";
        } else {

            try {
                String baseUrl = mediaArray.getJSONObject(0).getString("media_url_https");
                String thumbUrl = String.format("%s:medium", baseUrl);
                Log.i("TWEET CLASS", thumbUrl);
                return thumbUrl;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setNumRetweets(int numRetweets) {
        this.numRetweets = numRetweets;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setNumFaves(int numFaves) {
        this.numFaves = numFaves;
    }
}
