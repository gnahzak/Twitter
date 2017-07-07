package com.codepath.apps.twitterApp;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
//	public static final String REST_CONSUMER_KEY = "PWwLC2Oeg6HrxA2P5SYFzn8IQ";
//	public static final String REST_CONSUMER_SECRET = "HCQB44QvFahdnUS4QYwUeP4PqXV55WCwaBCuAXXRqmuHEpAO92";

	public static final String REST_CONSUMER_KEY = "7LhzRIVP9ohvtCz9uN0nOBkvC";
	public static final String REST_CONSUMER_SECRET = "wHe7JDoFAMIWCuybdhOh6JkDGB1FjbMItLAqfwJJeUag1DWpxL";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id - 1);
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		// getHomeTimeline(0, handler);

		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);

	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id - 1);
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void sendTweet(String message, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", message);
		client.post(apiUrl, params, handler);
	}

	public void replyTweet(String message, long uid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", message);
		params.put("in_reply_to_status_id", uid);
		client.post(apiUrl, params, handler);
	}

	public void retweet(long uid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/retweet.json");
		RequestParams params = new RequestParams();
		params.put("id", uid);
		client.post(apiUrl, params, handler);
	}

	public void unretweet(long uid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/unretweet.json");
		RequestParams params = new RequestParams();
		params.put("id", uid);
		client.post(apiUrl, params, handler);
	}

	public void favoriteTweet(long uid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", uid);
		client.post(apiUrl, params, handler);
	}

	public void unfavTweet(long uid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", uid);
		client.post(apiUrl, params, handler);
	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("count", 25);
		client.get(apiUrl, params, handler);
	}

	public void getUserTimeline(long max_id, String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("max_id", max_id - 1);
		params.put("screen_name", screenName);
		params.put("count", 25);
		client.get(apiUrl, params, handler);
	}

	public void getUserInfo(@Nullable String screenName, AsyncHttpResponseHandler handler) {

		// if looking for another user's profile
		if (screenName != null && !screenName.isEmpty()) {
			String apiUrl = getApiUrl("users/show.json");
			RequestParams params = new RequestParams();
			params.put("screen_name", screenName);
			client.get(apiUrl, params, handler);
		} else {
			String apiUrl = getApiUrl("account/verify_credentials.json");
			// Can specify query string params directly or through RequestParams.
			client.get(apiUrl, null, handler);
		}
	}

	public void getSearchTimeline(String query, AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("search/tweets.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		params.put("q", query);
		params.put("result_type", "popular");
		client.get(apiUrl, params, handler);

	}

	public void getFollowersList(long uid, AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("followers/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		params.put("count", 25);
		client.get(apiUrl, params, handler);

	}
}
