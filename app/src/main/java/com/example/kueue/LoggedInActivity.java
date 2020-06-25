package com.example.kueue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kueue.Utils.GeneralUtil;
import com.spotify.sdk.android.authentication.AuthenticationClient;

import com.example.kueue.Utils.SharedPreferenceUtil;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoggedInActivity extends AppCompatActivity {

    private SharedPreferenceUtil sharedPreferenceUtil;
    private GeneralUtil generalUtil;
    private static String userInformationUrl = "https://api.spotify.com/v1/me";

    private TextView username_textView ;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        username_textView = findViewById(R.id.username_textView);
        logoutButton = findViewById(R.id.button_logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutUI();
            }
        });

        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());
        generalUtil = GeneralUtil.getInstance(getApplicationContext());
        new NetworkCallTask().execute();
    }

    private void showLogoutUI() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(BuildConfig.SPOTIFY_CLIENT_ID, AuthenticationResponse.Type.TOKEN, generalUtil.REDIRECT_URI);

        builder.setScopes(new String[]{"user-modify-playback-state"});
        builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginInBrowser(this, request);
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