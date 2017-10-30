package com.androidkurs.micke.i_had_fun;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hadideknache on 2017-10-20.
 * This class handles the connection to twitter for receiving a token
 * This token is later used for connecting to twitter and receiving information such as tweets etc..
 */

public class BearerToken extends AsyncTask<Void, Void, Void> {
    public boolean finished;
    private String consumerKey;
    private String consumerSecret;
    private String bearerToken;
    private TwitterActivity twitterActivity;
    private String tokenUrl = "https://api.twitter.com/oauth2/token";

    /**
     * Constructor used for setting some information for later process of getting the bearerToken
     * Starts the asynctask later after
     * @param twitterActivity an instance to twitterActivity
     * @param consumerKey key used for connecting to twitter
     * @param consumerSecret key used for connecting to twitter
     */
    public BearerToken(TwitterActivity twitterActivity, String consumerKey, String consumerSecret) {
        this.twitterActivity = twitterActivity;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        execute();
    }

    /**
     * parsing the result into a string that later is used for fetching timeline
     * @param response the jsonString containing the json information of bearerToken
     */
    public void setJSONresults(String response) {
        try {
            JSONObject jsonObj = new JSONObject(response);
            bearerToken = jsonObj.getString("token_type")+" "+jsonObj.getString("access_token");
        } catch (JSONException ex) {
            Log.v("setJSONresults","Failed to put result!");
        }
    }

    /**
     * doInBackground opens a connection to twitter api requesting the token for getting timelines etc...
     *
     * @param voids empty
     * @return null empty
     */
    @Override
    protected Void doInBackground(Void... voids) {
        finished = false;
        if (bearerToken == null) {
            URL url = null;
            HttpsURLConnection connection = null;
            InputStreamReader inputStreamReader;
            BufferedReader bufferReader;

            try {
                url = new URL(tokenUrl);
            } catch (MalformedURLException ex) {
                return null;
            }

            try {
                String urlApiKey = URLEncoder.encode(consumerKey, "UTF-8");
                String urlApiSecret = URLEncoder.encode(consumerSecret, "UTF-8");

                byte[] data = (urlApiKey + ":" + urlApiSecret).getBytes();
                String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);

                //Request Header for twitter
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Host", "api.twitter.com");
                connection.setRequestProperty("User-Agent", "1");
                connection.setRequestProperty("Authorization", "Basic " + base64);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                connection.setRequestProperty("Content-Length", "29");
                connection.setUseCaches(false);
                String urlParameters = "grant_type=client_credentials";

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                //Read the bearerToken
                inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                bufferReader = new BufferedReader(inputStreamReader);

                String inputLine = "";
                StringBuilder response = new StringBuilder();
                while ((inputLine = bufferReader.readLine()) != null) {
                    response.append(inputLine);
                }
                setJSONresults(response.toString());
            } catch (IOException ex) {
                Log.v("BearerToken", "Failed to fetch BearerToken");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

            }
        }
        return null;
    }

    /**
     * onPostExecute handles the received token
     * Sends to twitterActivity if there is a bearerToken and get all tweets
     * @param result the bearerToken that recently received from twitter
     */

    @Override
    protected void onPostExecute(Void result) {
        finished = true;
        if (bearerToken!=null){
            twitterActivity.fetchAllFunPosition(bearerToken);
        }
    }
}
