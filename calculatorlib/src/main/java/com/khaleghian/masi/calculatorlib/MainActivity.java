package com.khaleghian.masi.calculatorlib;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String level_calc;
    Button btn_start;
    TextView how;
    FloatingActionButton back;
    static MediaPlayer mediaPlayer;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //play audio
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.soundgame);
        mediaPlayer.setLooping(true);
        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music"))
        {
            mediaPlayer.start();
        }
        //end play audio

        //lan
        String lan=LocaleHelper.getLanguage(MainActivity.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(MainActivity.this, sh_language);
        resources = context.getResources();
        //end lan

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
        back=findViewById(R.id.floatingActionButtonBack2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
        how=findViewById(R.id.how_to_play);
        how.setText(resources.getString(R.string.play_calculator));
        //spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.level_calculator));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level_calc = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                level_calc=resources.getString(R.string.easy);
            }
        });
        //spinner
        final Boolean sound=getIntent().getBooleanExtra("sound",true);
        final String easy=resources.getString(R.string.easy),medium=resources.getString(R.string.medium),hard=resources.getString(R.string.hard),impossible=resources.getString(R.string.impossible);
        btn_start=findViewById(R.id.btn_start);
        btn_start.setText(resources.getString(R.string.start));
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level_calc.equals(easy)) {
                    Intent intent1 = new Intent(MainActivity.this, EasyMediumHardCalculator.class);
                    intent1.putExtra("level", easy);
                    intent1.putExtra("sound", sound);
                    startActivity(intent1);
                } else if (level_calc.equals(medium)) {
                    Intent intent2 = new Intent(MainActivity.this, EasyMediumHardCalculator.class);
                    intent2.putExtra("level", medium);
                    intent2.putExtra("sound", sound);
                    startActivity(intent2);
                } else if (level_calc.equals(hard)) {
                    Intent intent3 = new Intent(MainActivity.this, EasyMediumHardCalculator.class);
                    intent3.putExtra("level", hard);
                    intent3.putExtra("sound", sound);
                    startActivity(intent3);
                } else if (level_calc.equals(impossible)) {
                    Intent intent4 = new Intent(MainActivity.this, EasyMediumHardCalculator.class);
                    intent4.putExtra("level", impossible);
                    intent4.putExtra("sound", sound);
                    startActivity(intent4);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences per = getSharedPreferences("Data", MODE_PRIVATE);
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music"))
            mediaPlayer.start();

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
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
