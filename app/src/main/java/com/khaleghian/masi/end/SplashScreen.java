package com.khaleghian.masi.end;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 5000; //This is 5 seconds
    TextView welcome;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        welcome=findViewById(R.id.welcome);

        //lan
        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String lan=LocaleHelper.getLanguage(SplashScreen.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(SplashScreen.this, sh_language);
        resources = context.getResources();
        //end lan
        welcome.setText(resources.getText(R.string.welcome));

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();
        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mySuperIntent);

                finish();

            }
        }, SPLASH_TIME);
    }
    //Method to run progress bar for 5 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }
}
