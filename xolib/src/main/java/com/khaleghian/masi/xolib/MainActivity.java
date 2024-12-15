package com.khaleghian.masi.xolib;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText plyr1;
    public EditText plyr2;

    public Spinner difficulty;
    public CharSequence player1 = "Player 1";
    public CharSequence player2 = "Player 2";
    public CharSequence cloneplayer2;
    boolean player1ax = true;
    boolean selectedSinglePlayer;
    boolean easy = true;
    boolean medium = false;
    boolean hard = false;
    boolean impossible = false;
    public CheckBox p1x, p1o, p2x, p2o, singleplayer, twoplayer;
    Button startGame;
    FloatingActionButton back;
    static MediaPlayer mediaPlayer;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //play audio
        mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.soundgame);
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

        back=findViewById(R.id.floatingActionButtonBack7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        addItemToDifficultySpinner();

        player1=resources.getString(R.string.player1);
        player2=resources.getString(R.string.player2);

        plyr1 = findViewById(R.id.playerone);
        plyr1.setHint(resources.getString(R.string.player1nameHint));
        plyr2 = findViewById(R.id.playertwo);
        plyr2.setHint(resources.getString(R.string.player2nameHint));

        p1x = findViewById(R.id.player1x);
        p1o = findViewById(R.id.player1o);
        p2x = findViewById(R.id.player2x);
        p2o = findViewById(R.id.player2o);

        singleplayer = findViewById(R.id.splayer);
        singleplayer.setText(resources.getString(R.string.single_player));
        twoplayer = findViewById(R.id.tplayer);
        twoplayer.setText(resources.getString(R.string.two_player));

        startGame=findViewById(R.id.start);
        startGame.setText(resources.getString(R.string.start));

        p1x.setOnClickListener(checkboxClickListener);
        p1o.setOnClickListener(checkboxClickListener);
        p2x.setOnClickListener(checkboxClickListener);
        p2o.setOnClickListener(checkboxClickListener);
        singleplayer.setOnClickListener(checkboxClickListener);
        twoplayer.setOnClickListener(checkboxClickListener);

        difficulty.setEnabled(false);


        p1x.setChecked(true);
        p2o.setChecked(true);
        twoplayer.setChecked(true);


        plyr1.addTextChangedListener(new TextWatcher() {                               /*this code take player1's name characterwise i.e it takes one character at a time and
                                                                                         saved to string variable player1*/
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player1 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        plyr2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player2 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void addItemToDifficultySpinner() {
        difficulty = findViewById(R.id.difficulty);

        List<String> list = new ArrayList<String>();
        list.add(resources.getString(R.string.easy));
        list.add(resources.getString(R.string.medium));
        list.add(resources.getString(R.string.hard));
        list.add(resources.getString(R.string.impossible));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(dataAdapter);


        difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                String easyspinner=resources.getString(R.string.easy),mediumspinner=resources.getString(R.string.medium),hardspinner=resources.getString(R.string.hard),impossiblespinner=resources.getString(R.string.impossible);
                if (temp.equals(easyspinner)) {
                    easy = true;
                    medium = false;
                    hard = false;
                    impossible = false;
                } else if (temp.equals(mediumspinner)) {
                    easy = false;
                    medium = true;
                    hard = false;
                    impossible = false;
                } else if (temp.equals(hardspinner)) {
                    easy = false;
                    medium = false;
                    hard = true;
                    impossible = false;
                } else if (temp.equals(impossiblespinner)) {
                    easy = false;
                    medium = false;
                    hard = false;
                    impossible = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                medium = true;
                easy = false;
                hard = false;
                impossible = false;
            }
        });
    }


    View.OnClickListener checkboxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();
            if (checked) {
                int id = view.getId();
                if (id == R.id.player1x) {
                    p1o.setChecked(false);
                    p2x.setChecked(false);
                    p2o.setChecked(true);
                    player1ax = true;
                } else if (id == R.id.player1o) {
                    p1x.setChecked(false);
                    p2o.setChecked(false);
                    p2x.setChecked(true);
                    player1ax = false;
                } else if (id == R.id.player2x) {
                    p2o.setChecked(false);
                    p1x.setChecked(false);
                    p1o.setChecked(true);
                    player1ax = false;
                } else if (id == R.id.player2o) {
                    p2x.setChecked(false);
                    p1o.setChecked(false);
                    p1x.setChecked(true);
                    player1ax = true;
                } else if (id == R.id.splayer) {
                    twoplayer.setChecked(false);
                    selectedSinglePlayer = true;
                    cloneplayer2 = player2;
                    plyr2.setText(resources.getString(R.string.cpu));

                    plyr1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    plyr1.setImeActionLabel("DONE", EditorInfo.IME_ACTION_DONE);
                    difficulty.setEnabled(true);
                } else if (id == R.id.tplayer) {
                    singleplayer.setChecked(false);
                    selectedSinglePlayer = false;
                    plyr2.setText(cloneplayer2);
                    plyr1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    plyr1.setImeActionLabel("NEXT", EditorInfo.IME_ACTION_NEXT);
                    difficulty.setEnabled(false);
                }

            } else {
                int id = view.getId();
                if (id == R.id.player1x) {
                    p1o.setChecked(true);
                    p2x.setChecked(true);
                    p2o.setChecked(false);
                    player1ax = false;
                } else if (id == R.id.player1o) {
                    p1x.setChecked(true);
                    p2o.setChecked(true);
                    p2x.setChecked(false);
                    player1ax = true;
                } else if (id == R.id.player2x) {
                    p2o.setChecked(true);
                    p1x.setChecked(true);
                    p1o.setChecked(false);
                    player1ax = true;
                } else if (id == R.id.player2o) {
                    p2x.setChecked(true);
                    p1o.setChecked(true);
                    p1x.setChecked(false);
                    player1ax = false;
                } else if (id == R.id.splayer) {
                    twoplayer.setChecked(true);
                    selectedSinglePlayer = false;
                    plyr2.setText(cloneplayer2);
                    difficulty.setEnabled(false);
                    plyr1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    plyr1.setImeActionLabel("NEXT", EditorInfo.IME_ACTION_NEXT);
                } else if (id == R.id.tplayer) {
                    singleplayer.setChecked(true);
                    selectedSinglePlayer = true;
                    plyr2.setText(resources.getString(R.string.cpu));
                    plyr1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    plyr1.setImeActionLabel("DONE", EditorInfo.IME_ACTION_DONE);
                    difficulty.setEnabled(true);
                }

            }

        }
    };


    public void startgame(View view) {

        if (!selectedSinglePlayer)
            if (player2.length() == 0)
                player2 = resources.getString(R.string.player2);
        if (player1.length() == 0)
            player1 = resources.getString(R.string.player1);

        CharSequence[] players = {player1, player2};
        final Boolean sound=getIntent().getBooleanExtra("sound",true);
        Intent i = new Intent(this, Afterstart.class);
        i.putExtra("easy", easy);
        i.putExtra("medium", medium);
        i.putExtra("hard", hard);
        i.putExtra("impossible", impossible);
        i.putExtra("playersnames", players);
        i.putExtra("player1ax", player1ax);
        i.putExtra("selectedsingleplayer", selectedSinglePlayer);
        i.putExtra("sound",sound);
        startActivity(i);
        finish();
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