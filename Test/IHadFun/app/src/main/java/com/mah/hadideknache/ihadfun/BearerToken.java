package com.mah.hadideknache.ihadfun;

import android.os.AsyncTask;
import android.util.Base64;

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
 */

public class BearerToken extends AsyncTask<Object, Object, Void> {
    public boolean finished;

    private String consumerKey = "Secret";
    private String consumerSecret = "Secret";
    public String bearerToken;
    public String tokenType;
    private MainActivity main;
    private String tokenUrl = "https://api.twitter.com/oauth2/token";

    public BearerToken(MainActivity main) {
        this.main = main;
        execute();
    }

    public void setJSONresults(String response) {
        try {
            JSONObject jsonObj = new JSONObject(response);
            bearerToken = jsonObj.getString("access_token");
            tokenType = jsonObj.getString("token_type");

        } catch (JSONException ex) {

        }
    }


    @Override
    protected Void doInBackground(Object... params) {
        finished = false;
        if (bearerToken == null) {
            URL url = null;
            HttpsURLConnection conn = null;
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

                conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Host", "api.twitter.com");
                conn.setRequestProperty("User-Agent", "1");
                conn.setRequestProperty("Authorization", "Basic " + base64);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                conn.setRequestProperty("Content-Length", "29");
                conn.setUseCaches(false);
                String urlParameters = "grant_type=client_credentials";

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParameters);

                wr.flush();
                wr.close();

                inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                bufferReader = new BufferedReader(inputStreamReader);

                String inputLine = "";
                StringBuilder response = new StringBuilder();
                while ((inputLine = bufferReader.readLine()) != null) {
                    response.append(inputLine);
                }
                setJSONresults(response.toString());
            } catch (IOException ex) {

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        finished = true;
        if (bearerToken!=null){
            main.getData(bearerToken);
            main.setBearerToken(this.bearerToken);
        }
    }
}
