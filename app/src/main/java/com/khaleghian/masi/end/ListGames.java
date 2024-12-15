package com.khaleghian.masi.end;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khaleghian.masi.calculatorlib.EasyMediumHardCalculator;
import com.khaleghian.masi.rememberimgmemlib.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListGames extends AppCompatActivity {
    FloatingActionButton back;
    final MediaPlayer mediaPlayer= com.khaleghian.masi.end.MainActivity.getMediaPlayer();
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        back=findViewById(R.id.floatingActionButtonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String lan=LocaleHelper.getLanguage(ListGames.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(ListGames.this, sh_language);
        resources = context.getResources();


        LinearLayout linearLayouts=findViewById(R.id.linGame);
        for (int column=0;column<linearLayouts.getChildCount();column++)
        {
            Button imageButton=(Button) linearLayouts.getChildAt(column);
            imageButton.setOnClickListener(ButtonListener);
            imageButton.setContentDescription(column+"");
            if (column == 0) {
                imageButton.setText(resources.getString(R.string.find_me));
            } else if (column == 1) {
                imageButton.setText(resources.getString(R.string.lose_me));
            } else if (column == 2) {
                imageButton.setText(resources.getString(R.string.match_me));
            } else if (column == 3) {
                imageButton.setText(resources.getString(R.string.calc_me));
            }
        }
    }
    View.OnClickListener ButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean sound=getIntent().getBooleanExtra("sound",true);
            int position=Integer.parseInt((String) v.getContentDescription());
            if (position == 0) {
                Intent intent=new Intent(ListGames.this,com.khaleghian.masi.rememberimgmemlib.MainActivity.class);
                intent.putExtra("sound",sound);
                startActivity(intent);
            } else if (position == 1) {
                Intent intent=new Intent(ListGames.this, com.khaleghian.masi.xolib.MainActivity.class);
                intent.putExtra("sound",sound);
                startActivity(intent);
            } else if (position == 2) {
                Intent intent=new Intent(ListGames.this, com.khaleghian.masi.imgmemlib.MainActivity.class);
                intent.putExtra("sound",sound);
                startActivity(intent);
            } else if (position == 3) {
                Intent intent=new Intent(ListGames.this, com.khaleghian.masi.calculatorlib.MainActivity.class);
                intent.putExtra("sound",sound);
                startActivity(intent);
            }

        }
    };

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
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences per = getSharedPreferences("Data", MODE_PRIVATE);
        String sh_music=per.getString("music","Unmute Music");
        if (sh_music.equals("Unmute Music")) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

}









