package com.androidkurs.micke.i_had_fun;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

import retrofit2.Call;

import static android.R.attr.data;

/**
 * Created by sonaj on 2017-10-23.
 */

public class TweetHandler {
    private boolean initResult;
    private boolean loginResult;
    private TwitterAuthClient tAuthorClient;
    private Controller controller;
    private TwitterActivity twitterActivity;
    private String token;
    private String secret;
    private String screen_name;
    private String bearerToken;

    public TweetHandler(TwitterActivity twitterActivity) {
        this.twitterActivity = twitterActivity;
    }
    public void tweet(String msg, double latitude, double longitude, String placeID) {
        TwitterSession session = twitterActivity.getSession();
        StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
        Call<Tweet> update = statusesService.update(msg, null, null, latitude, longitude, placeID, null, null, null);
        Log.d("TWITTERCOMPOSER","LAT: "+ latitude);
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(twitterActivity.main,"Tweet SUCCESSFUL",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(twitterActivity.main,"Tweet FAILED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void parseTimeLineJsonResult(String response){
        String text = "";
        try {
            JSONArray arr = new JSONArray(response);
            for (int i=0; i < arr.length(); i++){
                text += String.valueOf(i)+": "+arr.getJSONObject(i).getString("text")+"\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //timelineText = text;
    }
}