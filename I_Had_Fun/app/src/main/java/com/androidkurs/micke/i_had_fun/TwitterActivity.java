package com.androidkurs.micke.i_had_fun;

import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by sonaj on 2017-10-25.
 */

public class TwitterActivity {
    private boolean initResult;
    private boolean loginResult;
    private TwitterAuthClient tAuthorClient;
    private Controller controller;
    MainActivity main;
    private  TweetHandler tweetHandler;
    private TwitterSession twitterSession;
    private String token;
    private String secret;
    private String screen_name;
    private String bearerToken;

    public TwitterActivity(MainActivity main){
        this.main = main;
        TwitterConfig config = new TwitterConfig.Builder(main)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        main.getResources().getString(R.string.consumer_key),
                        main.getResources().getString(R.string.consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        this.tweetHandler = new TweetHandler(this);
    }
    public boolean initLogin() {
        tAuthorClient = new TwitterAuthClient();
        Callback<TwitterSession> cb;
        cb = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = twitterSession.getAuthToken();
                token = authToken.token;
                secret = authToken.secret;
                Toast.makeText(main,"Success",Toast.LENGTH_SHORT).show();
                initResult = login(twitterSession);
                //initResult = true;
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(main,"Failure",Toast.LENGTH_SHORT).show();
                initResult = false;
            }
        };
        tAuthorClient.authorize(main,cb);
        main.twitterOnActivity(tAuthorClient);
        main.setTwitterActivity(this);
        return loginResult;
    }
    public boolean fetchSession(){
        TwitterAuthToken authToken = getSession().getAuthToken();
        token = authToken.token;
        secret = authToken.secret;
        screen_name = getSession().getUserName();
        BearerToken token = new BearerToken(this);
        //main.setUserProfile(screen_name);

        return login(getSession());
    }
    public boolean login(final TwitterSession session) {
        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, true).enqueue(new Callback<User>() {

            @Override
            public void success(Result<User> result) {
                if (result.response.isSuccessful()){
                    User user = result.data;
                    main.setUserProfile(user);
                    //textView.setText(user.name);
                    //String profileImage = user.profileImageUrl.replace("_normal", "");
                    //Glide.with(getApplicationContext()).load(profileImage).into(iv);
                    loginResult = true;
                }
            }
            @Override
            public void failure(TwitterException exception) {
                loginResult = false;
            }
        });
        return loginResult;
    }
    public void logOut(){
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    public boolean isLoggedIn(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            return true;
        }
        return  false;
    }
    public void getData(String bearerToken) {
        //AsyncTaskGetTimeLine timeline = new AsyncTaskGetTimeLine("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="+screen_name+"&include_rts=false",bearerToken,this);
    }

    public TwitterSession getSession(){
        return (this.twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession());
    }
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public TweetHandler getTweetHandler(){
        return  this.tweetHandler;
    }
}