package com.androidkurs.micke.i_had_fun;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hadideknache on 2017-10-20.
 */

public class AsyncTaskGetTimeLine extends AsyncTask<Void, Void, Void> {
    public boolean finished;
    private TweetHandler tweetHandler;
    private String urlGet,bearerToken;
    private String consumerKey, consumerSecret;
    private StringBuilder response;


    public AsyncTaskGetTimeLine(String url, String bearerToken, TweetHandler tweetHandler,String consumerKey, String consumerSecret) {
        this.tweetHandler = tweetHandler;
        this.urlGet=url;
        this.bearerToken=bearerToken;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpsURLConnection connection = null;
        BufferedReader bufferedReader;

        try {
            URL url = new URL(urlGet);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setRequestProperty("Authorization", (bearerToken));
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();
            Log.v("ConnectionStatus:",String.valueOf(connection.getResponseCode()));
            InputStream inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = "";
            response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (MalformedURLException e) {
            try {
                throw new IOException("Invalid endpoint", e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null){
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        finished = true;
        tweetHandler.parseTimeLineJsonResult(response.toString());
    }


}
