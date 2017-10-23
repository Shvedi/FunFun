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
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.Map;

import retrofit2.Call;

import static android.R.attr.data;

/**
 * Created by sonaj on 2017-10-23.
 */

public class TweetComposer {

    private TwitterAuthClient tAuthorClient;
    private Controller controller;
    private MainActivity main;
    private String token;
    private String secret;

    public TweetComposer(MainActivity main){
        this.main = main;
        TwitterConfig config = new TwitterConfig.Builder(main)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        main.getResources().getString(R.string.consumer_key),
                        main.getResources().getString(R.string.consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        init();

    }

    public void init() {
        tAuthorClient = new TwitterAuthClient();
        Callback<TwitterSession> cb;
        cb = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                token = authToken.token;
                secret = authToken.secret;
                Toast.makeText(main,"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(main,"Failure",Toast.LENGTH_SHORT).show();
            }
        };
        tAuthorClient.authorize(main,cb);
        main.twitterOnActivity(tAuthorClient);
    }

    public void tweet(String msg) {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
        Call<Tweet> update = statusesService.update(msg, null, null, null, null, null, null, null, null);
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(main,"Tweet SUCCESSFUL",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(main,"Tweet FAILED",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }
}
