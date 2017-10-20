package se.mah.af6028.twittercompose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.util.Map;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private TwitterLoginButton btnLogin;
    private TwitterAuthClient tac;
    private TextView tvResult;
    private Button btnTweet;
    private Button btnLogin2;
    private String strTweet;
    private String token;
    private String secret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getResources().getString(R.string.CONSUMER_KEY),
                        getResources().getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        tac  = new TwitterAuthClient();
        strTweet = "1";
        tvResult = (TextView)findViewById(R.id.textView);
        btnTweet = (Button)findViewById(R.id.btnTweet);
        btnLogin = (TwitterLoginButton)findViewById(R.id.login_button);
        Callback<TwitterSession> cb;
        cb = new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
                        TwitterAuthToken authToken = session.getAuthToken();
                        token = authToken.token;
                        secret = authToken.secret;
                        OAuthSigning oauthSign = new OAuthSigning(authConfig, authToken);
                        Map<String, String> authHeaders = oauthSign.getOAuthEchoHeadersForVerifyCredentials();
                        tvResult.setText("Success!");
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        tvResult.setText("Failure...");
                    }
                };
        btnLogin.setCallback(cb);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                tweet(session);
            }
        });
    }

    public void tweet(TwitterSession session) {
        strTweet+="b";
        StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
        Call<Tweet> update = statusesService.update(strTweet, null, null, null, null, null, null, null, null);
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(MainActivity.this,"Tweet SUCCESSFUL",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this,"Tweet FAILED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        btnLogin.onActivityResult(requestCode, resultCode, data);
    }
}
