# Project 4 - *Twitter*

**Twitter** is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **45** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **sign in to Twitter** using OAuth login process
* [x] User can **view the tweets from their home timeline**
  * [x] RecyclerView is used to display listings of any tweets
  * [x] User is displayed the username, name, and body for each tweet
  * [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
* [x] User can **compose and post a new tweet**
  * [x] User can click a "Compose" icon in the App Bar on the top right
  * [x] User can then enter a new tweet from a second activity and then post this to twitter
  * [x] User is taken back to home timeline with new tweet visible in timeline
  * [x] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
  
 * [x] User can switch between Timeline and Mention views using tabs.
 * [x] User can navigate to view their own profile.
 * [x] User can click on the profile image in any tweet to see another user's profile.

The following **optional** features are implemented:

* [x] While composing a tweet, user can see a character counter with characters remaining for tweet out of 140
* [x] User can **pull down to refresh tweets** in either timeline.
* [x] Improve the user interface and theme the app to feel twitter branded with colors and styles
* [x] User can **search for tweets matching a particular query** and see results.
* [ ] When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* [x] User can **"reply" to any tweet on their home timeline**
  * [x] The user that wrote the original tweet is automatically "@" replied in compose
* [x] User can click on a tweet to be **taken to a "detail view"** of that tweet
 * [x] User can take favorite (and unfavorite) or retweet actions on a tweet
* [x] User can see embedded image media within the tweet item in list or detail view.
* [ ] Compose activity is replaced with a modal compose overlay.
* [x] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [x] Used Parcelable instead of Serializable leveraging the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler) when passing data between activities.
* [x] Replaced all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [x] User can view following / followers list through the profile of a user
* [ ] Apply the popular Butterknife annotation library to reduce view boilerplate.
* [ ] Implement collapse scrolling effects on the Twitter profile view using `CoordinatorLayout`.
* [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in an offline mode.

The following **additional** features are implemented:

* [x] User can view more tweets as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of tweets is unlimited.
* [x] Tweets show whether the user is or is not verified. 

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/gnahzak/Twitter/blob/master/walkthrough2.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Making the UI consistent across different activities was difficult. Fragments were also an interesting/challenging topic.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
