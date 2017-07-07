package com.codepath.apps.twitterApp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kazhang on 6/26/17.
 */

public class User implements Parcelable {

    // list attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public String tagLine;
    public int followersCount;
    public int followingCount;
    public boolean verified;

    public User() {
        name = "";
        uid = 0;
        screenName = "";
        profileImageUrl = "";
        tagLine = "";
        followersCount = 0;
        followingCount = 0;
        verified = false;
    }

    private User(Parcel in){
        name = in.readString();
        uid = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
        tagLine = in.readString();
        followersCount = in.readInt();
        followingCount = in.readInt();
        verified = (Boolean) in.readValue(null);
    }

    // deserialize JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract and fill values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.tagLine = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt("friends_count");
        user.verified = json.getBoolean("verified");

        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(name);
        out.writeLong(uid);
        out.writeString(screenName);
        out.writeString(profileImageUrl);
        out.writeString(tagLine);
        out.writeInt(followersCount);
        out.writeInt(followingCount);
        out.writeValue(verified);

    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // calls our new constructor and passes along the parcel, the returns the new object
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
