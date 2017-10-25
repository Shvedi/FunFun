package com.androidkurs.micke.i_had_fun;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hadideknache on 2017-10-20.
 */

public class AsyncTaskGetTimeLine extends AsyncTask<Void, Void, Void> {
    String returnEntry;
    public boolean finished;

    private TweetHandler tweetHandler;
    private String urlGet,bearerToken;


    public AsyncTaskGetTimeLine(String url, String bearerToken, TweetHandler tweetHandler) {
        this.tweetHandler = tweetHandler;
        this.urlGet=url;
        this.bearerToken=bearerToken;
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
            connection.setRequestProperty("Authorization", ("bearer "+bearerToken));
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
            StringBuilder response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            tweetHandler.parseTimeLineJsonResult(response.toString());

        } catch (MalformedURLException e) {
            try {
                throw new IOException("Invalid endpoint URL specified.", e);
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
        //tweetHandler.setText();
    }

}
