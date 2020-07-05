package com.example.kueue;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kueue.Utils.GeneralUtil;
import com.example.kueue.Utils.SharedPreferenceUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;



import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static String AUTH_TOKEN = null;

    private SharedPreferenceUtil sharedPreferenceUtil;
    private GeneralUtil generalUtil;

    private TextView loginStatusTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext());
        generalUtil = GeneralUtil.getInstance(getApplicationContext());
        getAndPopulateToken();


        if(AUTH_TOKEN.isEmpty() || AUTH_TOKEN == null) {
           // start login activity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent,generalUtil.REQUEST_LOGIN);
        }
        else {
            startLoggedInActivity();
            finish();
        }
    }


    private void startLoggedInActivity() {
        Intent intent = new Intent(this, LoggedInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
                startLoggedInActivity();
                finish();
            }
        }
    }
    
}