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
    public boolean finished;
    private TweetHandler tweetHandler;
    private String urlGet,bearerToken;
    private StringBuilder response;


    public AsyncTaskGetTimeLine(String url, String bearerToken, TweetHandler tweetHandler) {
        this.tweetHandler = tweetHandler;
        this.urlGet=url;
        this.bearerToken=bearerToken;
        execute();
    }

    public AsyncTaskGetTimeLine(Boolean test,String url,String bearerToken, TweetHandler tweetHandler) {
        AsyncTaskDestroyTweet destroy = new AsyncTaskDestroyTweet(url,bearerToken,tweetHandler);

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
        String url ="https://api.twitter.com/1.1/statuses/destroy/923234863158788096.json";
        AsyncTaskGetTimeLine task = new AsyncTaskGetTimeLine(true,url,bearerToken,tweetHandler);
    }

    public class AsyncTaskDestroyTweet extends AsyncTask<Void, Void, Void>{
        private String bearerToken;
        public boolean finished;
        private TweetHandler tweetHandler;
        private String urlPost;
        private StringBuilder response;

        public AsyncTaskDestroyTweet(String url,String bearerToken, TweetHandler tweetHandler) {
            this.tweetHandler = tweetHandler;
            this.urlPost=url;
            this.bearerToken=bearerToken;
            execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpsURLConnection connection = null;
            BufferedReader bufferedReader;

            try {
                URL url = new URL(urlPost);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setRequestProperty("Authorization", (this.bearerToken));
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Host", "api.twitter.com");
                //connection.setRequestProperty("Accept-Encoding","gzip");
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();
                Log.v("ConnectionStatus:",String.valueOf(connection.getResponseCode()));
                /*InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine = "";
                response = new StringBuilder();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }*/

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

    }

}
