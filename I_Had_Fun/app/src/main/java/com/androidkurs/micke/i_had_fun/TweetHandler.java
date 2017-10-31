package com.androidkurs.micke.i_had_fun;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;

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
        Call<Tweet> update = statusesService.update(msg, null, false, latitude, longitude, placeID, true, null, null);
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
    /**
     * Optional method for getting tweet instead of the asynctask method
     **/
    /*public void getUserTimeline(String screen_name) {
        String url = "923234863158788096";
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(twitterActivity.getSession());
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<List<Tweet>> userTimeline = statusesService.userTimeline(null,screen_name,3200,null,null,false,true,true,true);
        userTimeline.enqueue(new Callback<java.util.List<Tweet>>() {

            @Override
            public void success(Result<List<Tweet>> result) {

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }*/

    public void DestroyTweet(Long id){
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(twitterActivity.getSession());
        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<Tweet> destroytweet = statusesService.destroy(id,null);
        destroytweet.enqueue(new Callback<Tweet>() {

            @Override
            public void success(Result<Tweet> result) {
                Log.v("DESTROYTWEET","Successfully removed Tweet?="+result.response.isSuccessful());
                Toast.makeText(main,"Successfully removed Tweet!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.v("DESTROYTWEET","Failed to remove Tweet!!!!");
                Toast.makeText(main,"Failed!",Toast.LENGTH_SHORT).show();
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
        mPlace mPlace;
        try {
            JSONArray arr = new JSONArray(response);
            JSONArray coordArr;
            JSONObject jsonObject;
            JSONObject coordObj;

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
                    if (text.contains("&amp")){
                        text = text.replace("&amp;","&");
                    }
                    //mPlace = (mPlace) main.getController().getPlaces().get(new LatLng(latitude,longitude));

                    //twitterActivity.main.setMarker(latitude,longitude,text, date, bitDesc,mPlace.getId());

                }
                else{
                    //Fetched Message isnt from this application!
                }
                /*

                * Dangerous method down below Deletes all tweets!
                //twitterActivity.getTweetHandler().DestroyTweet(Long.parseLong(tweetID));

                */


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
