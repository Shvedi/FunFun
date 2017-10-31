package com.androidkurs.micke.i_had_fun;

import android.util.Log;
import android.widget.Spinner;
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
    private String baseUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

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
        this.tweetHandler = new TweetHandler(this, main);
    }
    public void initLogin() {
        tAuthorClient = new TwitterAuthClient();
        Callback<TwitterSession> cb;
        cb = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                controller.getDialogFrag().dismiss();
                controller.getDialogFrag().onDestroyView();
                fetchSession();
                Toast.makeText(main,"Success",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void failure(TwitterException exception) {

                Toast.makeText(main,"Failure",Toast.LENGTH_SHORT).show();

            }
        };
        Log.d("AUTH","Authorizing");
        tAuthorClient.authorize(main,cb);
        main.twitterOnActivity(tAuthorClient);
        main.setTwitterActivity(this);
    }
    public boolean fetchSession(){
        TwitterAuthToken authToken = getSession().getAuthToken();
        token = authToken.token;
        secret = authToken.secret;
        screen_name = getSession().getUserName();
        controller.dataBaseinit();
        BearerToken token = new BearerToken(this,main.getString(R.string.consumer_key),main.getString(R.string.consumer_secret));
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
    public void fetchAllFunPosition(String bearerToken){
        this.bearerToken = bearerToken;
        AsyncTaskGetTimeLine timeline = new AsyncTaskGetTimeLine(baseUrl+screen_name+"&count=3200"+"&include_rts=false",bearerToken,getTweetHandler());
    }

    public TwitterSession getSession(){
        return (this.twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public TweetHandler getTweetHandler(){
        return  this.tweetHandler;
    }

    public String getScreenName() {
        return screen_name;
    }


}
