package com.khaleghian.masi.imgmemlib;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String level_imgMem="Easy";
    FloatingActionButton back;
    static MediaPlayer mediaPlayer;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

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
        //spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.level_imgMemm));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level_imgMem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                level_imgMem="Easy";
            }
        });
        //spinner
        GridView gridView=findViewById(R.id.gridview);
        final List<ItemObject> allItems=getAllItemObject();
        CustomAdapter customAdapter=new CustomAdapter(this.getLayoutInflater(),allItems,MainActivity.this);
        gridView.setAdapter(customAdapter);

        final Boolean sound=getIntent().getBooleanExtra("sound",true);
        //select items
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String easy=resources.getString(R.string.easy),medium=resources.getString(R.string.medium),hard=resources.getString(R.string.hard);
                if (level_imgMem.equals(easy)) {
                    Intent intent1 = new Intent(MainActivity.this, GameActivityEasy.class);
                    intent1.putExtra("itemNumber", position);
                    intent1.putExtra("sound", sound);
                    startActivity(intent1);
                } else if (level_imgMem.equals(medium)) {
                    Intent intent2 = new Intent(MainActivity.this, GameActivityMedium.class);
                    intent2.putExtra("itemNumber", position);
                    intent2.putExtra("sound", sound);
                    startActivity(intent2);
                } else if (level_imgMem.equals(hard)) {
                    Intent intent3 = new Intent(MainActivity.this, GameActivityHard.class);
                    intent3.putExtra("itemNumber", position);
                    intent3.putExtra("sound", sound);
                    startActivity(intent3);
                }
                finish();
            }
        });

    }

    private List<ItemObject> getAllItemObject() {
        List<ItemObject> items=new ArrayList<>();
        items.add(new ItemObject(resources.getString(R.string.flower),"flower"));
        items.add(new ItemObject(resources.getString(R.string.cartoon),"cartoon"));
        items.add(new ItemObject(resources.getString(R.string.car),"car"));
        items.add(new ItemObject(resources.getString(R.string.alphabet),"alphabet"));
        items.add(new ItemObject(resources.getString(R.string.flag),"flag"));
        items.add(new ItemObject(resources.getString(R.string.number),"number"));
        return items;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        finish();
    }
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
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
