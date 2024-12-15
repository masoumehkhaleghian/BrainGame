package com.khaleghian.masi.end;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Settings extends AppCompatActivity {
    Button language;
    Button music;
    Button sound;
    FloatingActionButton back;
    SeekBar seekBar;
    AudioManager audioManager;
    final MediaPlayer mediaPlayer=MainActivity.getMediaPlayer();
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String sh_sound=per.getString("sound","Unmute Sound");
        String sh_music=per.getString("music","Unmute Music");
        language=findViewById(R.id.lanuage);
        music=findViewById(R.id.music);
        sound=findViewById(R.id.sound);
        seekBar=findViewById(R.id.seekBarAudio);
        back=findViewById(R.id.floatingActionButtonBack8);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //set image and text

        //set language
        String lan=LocaleHelper.getLanguage(Settings.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(Settings.this, sh_language);
        resources = context.getResources();
        language.setText(resources.getString(R.string.language));
        //end set language

        if (sh_music.equals("Unmute Music"))
        {
            music.setText(resources.getString(R.string.mute_music));
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmusic );
            music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            seekBar.setVisibility(View.VISIBLE);
            audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBar.setMax(maxVolume);
            seekBar.setProgress(curVolume);
        }
        else
        {
            music.setText(resources.getString(R.string.unmute_music));
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmusic );
            music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            seekBar.setVisibility(View.GONE);
        }
        /*if (mediaPlayer.isPlaying())
        {
            music.setText("Mute Music");
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmusic );
            music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            seekBar.setVisibility(View.VISIBLE);
            audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBar.setMax(maxVolume);
            seekBar.setProgress(curVolume);
        }
        else
        {
            music.setText("Unmute Music");
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmusic );
            music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            seekBar.setVisibility(View.GONE);
        }*/
        if (sh_sound.equals("Unmute Sound"))
        {
            sound.setText(resources.getString(R.string.mute_sound));
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmute );
            sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        }
        else
        {
            sound.setText(resources.getString(R.string.unmute_sound));
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmute );
            sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        }
        /*final Boolean soundbool=MainActivity.getSoundbool();
        if (soundbool)
        {
            sound.setText("Mute Sound");
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmute );
            sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        }
        else
        {
            sound.setText("Unmute Sound");
            Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmute );
            sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        }*/
        //end
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                String lan=LocaleHelper.getLanguage(Settings.this);
                if (!lan.equals("fa"))
                {
                    per.edit().putString("language","fa").apply();
                    context= LocaleHelper.setLocale(Settings.this, "fa");
                    resources = context.getResources();
                }
                else {
                    per.edit().putString("language","en").apply();
                    context= LocaleHelper.setLocale(Settings.this, "en");
                    resources = context.getResources();
                }
                language.setText(resources.getString(R.string.language));
                String sh_sound=per.getString("sound","Unmute Sound");
                if (sh_sound.equals("Unmute Sound"))
                {
                    sound.setText(resources.getString(R.string.mute_sound));
                }
                else
                {
                    sound.setText(resources.getString(R.string.unmute_sound));
                }
                String sh_music=per.getString("music","Unmute Music");
                if (sh_music.equals("Unmute Music"))
                {
                    music.setText(resources.getString(R.string.mute_music));
                }
                else
                {
                    music.setText(resources.getString(R.string.unmute_music));
                }
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                String sh_music=per.getString("music","Unmute Music");
                if (sh_music.equals("Unmute Music"))
                {
                    mediaPlayer.pause();
                    per.edit().putString("music","Mute Music").apply();
                    music.setText(resources.getString(R.string.unmute_music));
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmusic );
                    music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    seekBar.setVisibility(View.GONE);
                }
                else
                {
                    mediaPlayer.start();
                    per.edit().putString("music","Unmute Music").apply();
                    music.setText(resources.getString(R.string.mute_music));
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmusic );
                    music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    seekBar.setVisibility(View.VISIBLE);
                    audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setMax(maxVolume);
                    seekBar.setProgress(curVolume);
                }
                /*if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    music.setText("Unmute Music");
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmusic );
                    music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    seekBar.setVisibility(View.GONE);
                }
                else
                {
                    mediaPlayer.start();
                    music.setText("Mute Music");
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmusic );
                    music.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    seekBar.setVisibility(View.VISIBLE);
                    audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setMax(maxVolume);
                    seekBar.setProgress(curVolume);
                }*/
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying())
                {
                    audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(curVolume);
                }
            }
        }, 0, 500);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                String sh_sound=per.getString("sound","Unmute Sound");
                if (sh_sound.equals("Unmute Sound"))
                {
                    per.edit().putString("sound","Mute Sound").apply();
                    //MainActivity.setSoundbool(false);
                    sound.setText(resources.getString(R.string.unmute_sound));
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconunmute );
                    sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }
                else
                {
                    per.edit().putString("sound","Unmute Sound").apply();
                    //MainActivity.setSoundbool(true);
                    sound.setText(resources.getString(R.string.mute_sound));
                    Drawable img = music.getContext().getResources().getDrawable( R.drawable.iconmute );
                    sound.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
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
}
