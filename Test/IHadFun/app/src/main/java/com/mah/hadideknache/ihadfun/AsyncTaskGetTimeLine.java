package com.mah.hadideknache.ihadfun;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hadideknache on 2017-10-20.
 */

public class AsyncTaskGetTimeLine extends AsyncTask<Void, Void, Void> {
    String returnEntry;
    public boolean finished;

    private String consumerKey = "PpHjVX0LbKaJ4ztrYxTuO2R6y";
    private String consumerSecret = "6cQemVy1Kyk8Uswh0jLY80xDyiswm30MgzoHV5o8RnMBxPS7ZE";
    private MainActivity main;
    private String urlGet,bearerToken;


    public AsyncTaskGetTimeLine(String url, String bearerToken, MainActivity main) {
        this.main = main;
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
            main.parseTimeLineJsonResult(response.toString());

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
        main.setText();
    }

}
