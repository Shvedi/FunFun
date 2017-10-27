package com.androidkurs.micke.i_had_fun;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    private MainActivity main;
    BitmapDescriptor bitDesc;

    public TweetHandler(TwitterActivity twitterActivity, MainActivity main) {
        this.twitterActivity = twitterActivity;

        this.main = main;
    }

    public void tweet(String msg, double latitude, double longitude, String placeID) {
        Log.d("TWEETHANDLER ","TWEET PLACEID: " +placeID);
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
        String text1 = "";
        String text = "";
        String date = "";
        String placeID = "";
        String tweetID = "";
        Double latitude = null;
        Double longitude = null;
        try {
            JSONArray arr = new JSONArray(response);
            JSONArray coordArr;
            JSONObject jsonObject;
            JSONObject coordObj;
            String id = null;
            String url = null;

            for (int i=0; i < arr.length(); i++){
                jsonObject = arr.getJSONObject(i);
                if (jsonObject.getString("source").contains("placeholder.com")){
                    //latitude = Double.parseDouble(obj.getString("coordinates"));
                    text1 = jsonObject.getString("text");
                    bitDesc = translateHappiness(text1);
                    text = text1.substring(text1.indexOf("at")+3 ,text1.length());
                    date = jsonObject.getString("created_at");
                    date = date.substring(8,10)+"-"+ date.substring(4,7) + "-" + date.substring(date.length()-4,date.length());
                    placeID = jsonObject.getString("place");
                    Log.d("TWEETHANDLER","PLACEID: "+placeID);
                    tweetID = jsonObject.getString("id");
                    coordObj = jsonObject.getJSONObject("coordinates");
                    coordArr = coordObj.getJSONArray("coordinates");
                    longitude = coordArr.getDouble(0);
                    latitude = coordArr.getDouble(1);
/*
                    twitterActivity.main.setMarker(latitude,longitude,text, date, bitDesc);
                    id = jsonObject.getString("id");

                    tweets.put(jsonObject.getString("id"),new mPlace(text,date,latitude,longitude));*/

                    twitterActivity.main.setMarker(latitude,longitude,text, date, bitDesc,tweetID);
                    main.getController().getDataFrag().addToTweetsMap(tweetID,new mPlace(text,date,latitude,longitude,placeID,text1));

                }
                else{
                    //Fetched Message isnt from this application!
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public BitmapDescriptor translateHappiness(String text) {
        BitmapDescriptor bit;
        if(text.contains("had fun")){
            bit = BitmapDescriptorFactory.fromResource(R.drawable.happymarker1);
        }
        else if(text.contains("very fun")){
            bit = BitmapDescriptorFactory.fromResource(R.drawable.happymarker2);
        }
        else if(text.contains("blast")){
            bit = BitmapDescriptorFactory.fromResource(R.drawable.happymarker3);
        }
        else if(text.contains("amazing")){
            bit = BitmapDescriptorFactory.fromResource(R.drawable.happymarker4);
        }
        else{
            return null;
        }
        return bit;
    }
}
