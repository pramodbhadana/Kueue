package com.example.kueue;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kueue.Utils.GeneralUtil;
import com.example.kueue.Utils.SharedPreferenceUtil;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;



import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID;
    private static String AUTH_TOKEN = null;
    private static String urlPrefix = "https://api.spotify.com/v1/me/player/queue?uri=";

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";

    private SharedPreferences sharedPref ;

    private TextView textViewStatus;
    private GeneralUtil generalUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());
        generalUtil = GeneralUtil.getInstance(getApplicationContext());
        getAndPopulateToken();

        if(AUTH_TOKEN == null) {
            handleSpotifyAccountConnect();
        }

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            String URL =  intent.getDataString();
            if(URL != null) {
                String URI = generalUtil.convertToURI(URL);
                new NetworkCallTask().execute(URI);
            }
            }
        }

    private void handleSpotifyAccountConnect() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-modify-playback-state"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    String handleCurlRequest(String urlToProcess) {
            StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(urlPrefix+ URLEncoder.encode(urlToProcess,"UTF-8"));
            //URL url = new URL("https://api.spotify.com/v1/me/player/queue?uri=spotify%3Atrack%3A4iV5W9uYEdYUVa79Axb7Rh");


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + AUTH_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");


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
            return handleCurlRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    
    private void getAndPopulateToken() {
        AUTH_TOKEN = sharedPreferenceUtil.getAuthToken();
    }

    private void writeSharedPreference(String token) {
            sharedPreferenceUtil.setAuthToken(token);
    }

//    public void handleButtonClick(View view) {
//        if(view.getId() == R.id.button) {
//            startActivity(new Intent(
//                    android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS));
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == GeneralUtil.REQUEST_LOGIN) {
            if(resultCode == Activity.RESULT_OK) {
                //Login successful
                loginStatusTextview = findViewById(R.id.login_status_textview);
                loginStatusTextview.setText("Spotify Login is successful");
            }
        }
    }
    
}