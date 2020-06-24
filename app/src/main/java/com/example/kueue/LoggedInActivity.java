package com.example.kueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.kueue.Utils.GeneralUtil;
import com.example.kueue.Utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoggedInActivity extends AppCompatActivity {

    private SharedPreferenceUtil sharedPreferenceUtil;
    private static String userInformationUrl = "https://api.spotify.com/v1/me";
    private TextView username_textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        username_textView = findViewById(R.id.username_textView);

        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());
        new NetworkCallTask().execute();
    }

    String makeCurlRequest() {
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(userInformationUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + sharedPreferenceUtil.getAuthToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;

            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();

    }
    class NetworkCallTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            return makeCurlRequest();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                username_textView.setText(jsonObject.getString("display_name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}