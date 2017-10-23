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

public class TweetComposer {

    private TwitterAuthClient tAuthorClient;
    private Controller controller;
    private MainActivity main;
    private String token;
    private String secret;
    private String screen_name;
    private String bearerToken;

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

    private void init() {
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            fetchSession();
            BearerToken token = new BearerToken(this);
        } else {
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
                    login(session);
                }
                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(main,"Failure",Toast.LENGTH_SHORT).show();
                }
            };
            tAuthorClient.authorize(main,cb);
            main.twitterOnActivity(tAuthorClient);
        }


    }
    private void fetchSession(){
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        token = authToken.token;
        secret = authToken.secret;
        screen_name = session.getUserName();
        login(session);
    }
    public void login(TwitterSession session) {
        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, true).enqueue(new Callback<User>() {

            @Override
            public void success(Result<User> result) {
                if (result.response.isSuccessful()){
                    User user = result.data;
                    main.setUserProfile(user);
                    //textView.setText(user.name);
                    //String profileImage = user.profileImageUrl.replace("_normal", "");
                    //Glide.with(getApplicationContext()).load(profileImage).into(iv);
                }
            }
            @Override
            public void failure(TwitterException exception) {

            }
        });
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

    public void getData(String bearerToken) {
        //AsyncTaskGetTimeLine timeline = new AsyncTaskGetTimeLine("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="+screen_name+"&include_rts=false",bearerToken,this);
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
