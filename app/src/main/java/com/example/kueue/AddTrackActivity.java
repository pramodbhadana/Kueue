package com.example.kueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.kueue.Utils.GeneralUtil;
import com.example.kueue.Utils.SharedPreferenceUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddTrackActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private AddTrackFragment addTrackFragment;
    private AddTrackFailureFragment addTrackFailureFragment;
    private GeneralUtil generalUtil;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private static String AddTrackFragmentTag = "AddTrackFragment";
    private static String AddTrackFailureFragmentTag = "AddTrackFailureFragment";

    private static Track addedTrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        generalUtil = GeneralUtil.getInstance(getApplicationContext());
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());

        addTrackFragment = new AddTrackFragment();
        addTrackFailureFragment = new AddTrackFailureFragment();
        fragmentTransaction.add(R.id.parentConstraintLayout,addTrackFragment,AddTrackFragmentTag);
        fragmentTransaction.commit();

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

    Integer handleCurlRequest(String urlToProcess) {
        Integer responseCode = -1;
        try {
            String encodedUrl = URLEncoder.encode(urlToProcess,"UTF-8");
            URL url = new URL(generalUtil.urlPrefixForTrack+ encodedUrl);
            //URL url = new URL("https://api.spotify.com/v1/me/player/queue?uri=spotify%3Atrack%3A4iV5W9uYEdYUVa79Axb7Rh");


            Log.d("Pramod",url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + sharedPreferenceUtil.getAuthToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");

            responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                //operation successful
                SpotifyApi spotifyApi = new SpotifyApi();
                spotifyApi.setAccessToken(sharedPreferenceUtil.getAuthToken());
                SpotifyService spotifyService = spotifyApi.getService();


                String trackLink = urlToProcess.substring(urlToProcess.indexOf(":track:")+7);
                addedTrack = spotifyService.getTrack(trackLink);
                //Track track = spotifyService.getTrack("60GK6ultL3skUR60O9haSJ");
                Log.d("pramod",""+addedTrack.album );
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseCode;

    }
    class NetworkCallTask extends AsyncTask<String,Void,Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            return handleCurlRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            super.onPostExecute(responseCode);
            if(responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragmentManager.findFragmentByTag(AddTrackFragmentTag));
                fragmentTransaction.commit();
                fragmentTransaction.add(R.id.parentConstraintLayout, addTrackFailureFragment);
                fragmentTransaction.commit();
            }
            else {
                //add artist and album information.
                //start activity.
            }
        }
    }

}