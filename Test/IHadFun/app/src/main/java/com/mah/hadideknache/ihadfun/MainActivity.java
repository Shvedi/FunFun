package com.mah.hadideknache.ihadfun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private String consumerKey = "Secreeeeeet";
    private String consumerSecret = "Secreeeeeet";

    private TextView mTextMessage;
    private Button btn;
    private String bearerToken;
    private TwitterLoginButton loginButton;
    private String token,secret,screen_name,timelineText;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText( R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                .debug(true)
                .build();

        Twitter.initialize(config);
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(bearerToken);
            }
        });
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            btn.setEnabled(true);
            loginButton.setEnabled(false);
            BearerToken token = new BearerToken(MainActivity.this);
        } else {
            loginButton.setEnabled(true);
            btn.setEnabled(false);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    btn.setEnabled(true);
                    loginButton.setEnabled(false);
                    fetchSession();
                    Log.v("Success",String.valueOf(result.data.getUserId()));
                    BearerToken token = new BearerToken(MainActivity.this);
                }
                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void fetchSession(){
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        token = authToken.token;
        secret = authToken.secret;
        screen_name = session.getUserName();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void tweet(Double latitude,Double longitude,String twtText){
        StatusesService statusesService  = TwitterCore.getInstance().getApiClient().getStatusesService();
        //statusesService.userTimeline()
        statusesService.update(twtText,null,false,latitude,longitude,null,true,false, String.valueOf(new Callback<Twitter>(){
            @Override
            public void success(Result<Twitter> result) {
                Log.v("Success","Updated Message");
            }
            @Override
            public void failure(TwitterException exception) {
                Log.v("Fail","Failed to update");
            }
        }));
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
        timelineText = text;
    }

    public void getData(String bearerToken) {

        AsyncTaskGetTimeLine timeline = new AsyncTaskGetTimeLine("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="+screen_name+"&include_rts=false",bearerToken,this);
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public void setText() {
        mTextMessage.setText(timelineText);
    }
}
