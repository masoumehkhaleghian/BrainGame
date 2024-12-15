package com.khaleghian.masi.end;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.khaleghian.masi.calculatorlib.EasyMediumHardCalculator;
import com.khaleghian.masi.end.LocaleHelper.*;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button playgame,setting,aboutgame;
    TextView braingame;
    static Boolean soundbool;
    static MediaPlayer mediaPlayer;
    Context context;
    Resources resources;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //language
        String lan=LocaleHelper.getLanguage(MainActivity.this);
        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(MainActivity.this, sh_language);
        resources = context.getResources();
        //end language

        //play audio
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.soundhome);
        mediaPlayer.setLooping(true);
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music"))
        {
            mediaPlayer.start();
        }
        String sh_sound=per.getString("sound","Unmute Sound");
        soundbool= sh_sound.equals("Unmute Sound");
        //end play audio

        //screen off
         BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(action))
                    mediaPlayer.pause();
            }
         };
            // to register the receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        getApplicationContext().registerReceiver(screenOffReceiver, filter);
        //end screen off
        final int[] androidColorss = getResources().getIntArray(R.array.androidcolors);
        playgame=findViewById(R.id.playgame);
        playgame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    int randomAndroidColor = androidColorss[new Random().nextInt(androidColorss.length)];
                    playgame.setTextColor(randomAndroidColor);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    playgame.setTextColor(Color.BLACK);
                }
                return false;
            }
        });
        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListGames.class);
                intent.putExtra("sound",soundbool);
                startActivity(intent);
            }
        });
        setting=findViewById(R.id.settings);
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    int randomAndroidColor = androidColorss[new Random().nextInt(androidColorss.length)];
                    setting.setTextColor(randomAndroidColor);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    setting.setTextColor(Color.BLACK);
                }
                return false;
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Settings.class));
            }
        });
        aboutgame=findViewById(R.id.aboutgame);
        aboutgame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    int randomAndroidColor = androidColorss[new Random().nextInt(androidColorss.length)];
                    aboutgame.setTextColor(randomAndroidColor);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    aboutgame.setTextColor(Color.BLACK);
                }
                return false;
            }
        });
        aboutgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AboutGame.class));
            }
        });
        braingame=findViewById(R.id.brainGame);
        braingame.setBackgroundResource(R.drawable.border_braingame);

        braingame.setText(resources.getString(R.string.app_name));
        playgame.setText(resources.getString(R.string.play));
        setting.setText(resources.getString(R.string.setting));
        aboutgame.setText(resources.getString(R.string.about));
        runTimer();
    }
    private void runTimer()
    {
        // Creates a new Handler
        final Handler handler =new Handler();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                braingame.setTextColor(randomAndroidColor);
                handler.postDelayed(this, 1000);
            }
        });
    }
    //dialog exit
    private void showExitDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit);
        dialog.setCancelable(false);

        dialog.show();

        Button exit = dialog.findViewById(R.id.yes_button);
        final Button dismiss = dialog.findViewById(R.id.no_button);
        TextView title=dialog.findViewById(R.id.title);

        exit.setText(resources.getString(R.string.yes_button));
        dismiss.setText(resources.getString(R.string.no_button));
        title.setText(resources.getString(R.string.dialog_exit));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                dialog.dismiss();
                finish();
                //onDestroy();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    //public static void setSoundbool(Boolean soundbooll) { soundbool = soundbooll; }
    //public static Boolean getSoundbool() { return soundbool; }
    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences per = getSharedPreferences("Data", MODE_PRIVATE);
        String sh_sound = per.getString("sound", "Unmute Sound");
        soundbool = sh_sound.equals("Unmute Sound");
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music"))
            mediaPlayer.start();

        String lan=LocaleHelper.getLanguage(MainActivity.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(MainActivity.this, sh_language);
        resources = context.getResources();
        braingame.setText(resources.getString(R.string.app_name));
        playgame.setText(resources.getString(R.string.play));
        setting.setText(resources.getString(R.string.setting));
        aboutgame.setText(resources.getString(R.string.about));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences per = getSharedPreferences("Data", MODE_PRIVATE);
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music")) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> services = activityManager
                    .getRunningTasks(Integer.MAX_VALUE);
            boolean isActivityFound = false;

            if (services.get(0).topActivity.getPackageName()
                    .equalsIgnoreCase(getPackageName())) {
                isActivityFound = true; // Activity belongs to your app is in foreground.
            }

            if (!isActivityFound) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    }
}


