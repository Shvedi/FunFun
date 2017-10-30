package com.androidkurs.micke.i_had_fun;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hadideknache on 2017-10-20.
 *
 * This class handles the connection to twitter through the api,
 * Makes a connection to twitter requesting timeline and later receives the data as Json objects
 * Json objects is later handled in TweetHandler
 */

public class AsyncTaskGetTimeLine extends AsyncTask<Void, Void, String> {
    public boolean finished;
    private TweetHandler tweetHandler;
    private String urlGet,bearerToken;
    private StringBuilder response;
    private String status;

    /**
     * This Constructor runs at the beginning of the Asynctask for fetching the timeline
     * Used for instantiating stuff that is needed later during the process of connectiong and handling the input
     * @param url the url used for fetching tweets
     * @param bearerToken token that authenticates at twitter and give us permission to get timelines from api
     * @param tweetHandler an instance to tweethandler
     */
    public AsyncTaskGetTimeLine(String url, String bearerToken, TweetHandler tweetHandler) {
        this.tweetHandler = tweetHandler;
        this.urlGet=url;
        this.bearerToken=bearerToken;
        execute();
    }

    /**
     * doInBackground handle the connection to twitter and receiving the data of the tweets as json
     * For receiving JsonObjects need to set a header for the HttpConnection
     * @param voids
     * @return connectionResponseCode of the connection to twitter 200=success
     */
    @Override
    protected String doInBackground(Void... voids) {
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
            status = String.valueOf(connection.getResponseCode());
            Log.v("ConnectionStatus:",status);
            InputStream inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = "";
            response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null){
                connection.disconnect();
            }
        }
        return status;
    }

    /**
     * onPostExecute checks whether the connection was successfull or not
     * if successful sends the input message from twitter to tweethandler
     * @param result the connectionResponseCode which tells if connected successfully to twitter api or not
     */
    @Override
    protected void onPostExecute(String result) {
        finished = true;
        if (result.equals("200")){
            tweetHandler.parseTimeLineJsonResult(response.toString());
        }
        else{
            Log.v("AsynctaskPost","Failed to getTweetsFromTimeLine");
        }
    }


}
