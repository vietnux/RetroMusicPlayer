package com.mp3.player.retromusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mp3.player.retromusic.BuildConfig;
import com.mp3.player.retromusic.R;
import com.mp3.player.retromusic.ads.AdmobLib;
import com.mp3.player.retromusic.util.JsonParams;
import com.mp3.player.retromusic.util.RemoteJSONSource;

//Yeah, I know this is weird code.
public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        int header;
//        findViewById(R.id.splashscreen).setBackgroundResource(header);
        if (DEBUG) Log.e(TAG, "Error ads...là sao nh");
        if( JsonParams.DATA == null ) {
            try {
                new RemoteJSONSource(this).execute("");
            } catch ( Exception e ) {
                if (DEBUG) Log.e(TAG, "Error ads..." + e.toString() );
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.e(TAG, " start   " + JsonParams.getParamInt("openapp"));
        if( JsonParams.getParamInt("openapp") == 1 ) {
//            AdmobLib.getInstance(this).fetchAd();
            AdmobLib.getInstance(this).showAdIfAvailable();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " start  onResume " );
        if (JsonParams.DATA != null) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}