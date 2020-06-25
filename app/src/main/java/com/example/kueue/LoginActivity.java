package com.example.kueue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kueue.Utils.GeneralUtil;
import com.example.kueue.Utils.SharedPreferenceUtil;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID;
    private static String AUTH_TOKEN = null;

    private Button loginToSpotifyButton;

    private static final int REQUEST_CODE = 1337;

    private SharedPreferenceUtil sharedPreferenceUtil;
    private GeneralUtil generalUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());
        generalUtil = GeneralUtil.getInstance(getApplicationContext());
        loginToSpotifyButton = findViewById(R.id.button_logon);
        loginToSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSpotifyAccountConnect();
            }
        });
    }

    private void handleSpotifyAccountConnect() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, generalUtil.REDIRECT_URI);

        builder.setScopes(new String[]{"user-modify-playback-state"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            Intent resultIntent =  new Intent();

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    AUTH_TOKEN = response.getAccessToken();
                    sharedPreferenceUtil.setAuthToken(AUTH_TOKEN);
                    setResult(Activity.RESULT_OK,resultIntent);
                    finish();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(this,R.string.login_error,Toast.LENGTH_SHORT);
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(this,R.string.login_error_flow_cancelled,Toast.LENGTH_SHORT);

                    // Handle other cases
            }
        }
    }

}