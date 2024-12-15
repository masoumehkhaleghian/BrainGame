package com.khaleghian.masi.xolib;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Afterstart extends AppCompatActivity {
    boolean easy;
    boolean medium;
    boolean hard;
    boolean impossible;
    String record="";
    Random r = new Random();
    int flag = 0, ax = 10, zero = 1, win = 0, i, game = 1;
    int summ = 0, ctrflag = 0, resetchecker = 1, currentgamedonechecker = 0;
    int score1 = 0, score2 = 0, drawchecker = 0;
    static int[][] tracker = new int[3][3];
    int[] sum = new int[8];
    static int[][] buttonpressed = new int[3][3];
    boolean player1ax;
    boolean selectedsingleplayer;
    TextView p1;
    TextView p2;
    TextView gameCounter;
    CharSequence player1 = "Player 1";
    CharSequence player2 = "Player 2";
    Boolean flagg=true;
    FloatingActionButton gamee;
    FloatingActionButton what;
    FloatingActionButton pause;
    Boolean sound;
    SoundPool soundPool;
    int winn=-1;
    int gameOver=-1;
    int match=-1;
    MediaPlayer mediaPlayer=MainActivity.getMediaPlayer();
    Context context;
    Resources resources;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterstart);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //lan
        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String lan=LocaleHelper.getLanguage(Afterstart.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(Afterstart.this, sh_language);
        resources = context.getResources();
        //end lan

        player1=resources.getString(R.string.player1);
        player2=resources.getString(R.string.player2);

        gameCounter=findViewById(R.id.gameCounter);
        gameCounter.setText(resources.getString(R.string.game));

        sound=getIntent().getBooleanExtra("sound",true);
        //FAB
        gamee=findViewById(R.id.floatingActionButtongame7);
        what=findViewById(R.id.floatingActionButtonwhat7);
        pause=findViewById(R.id.floatingActionButtonPause7);
        what.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagg) {
                    flagg=false;
                    what.animate().rotation(90);
                    gamee.setVisibility(View.VISIBLE);
                    gamee.animate().rotation(360);
                    pause.setVisibility(View.VISIBLE);
                    pause.animate().rotation(360);
                }
                else {
                    flagg=true;
                    what.animate().rotation(0);
                    gamee.setVisibility(View.INVISIBLE);
                    gamee.animate().rotation(0);
                    pause.setVisibility(View.INVISIBLE);
                    pause.animate().rotation(0);
                }
            }
        });
        gamee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doreset();
                finish();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });
        //FAB Finish

        //get android version of user mobile for use sound
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) //when user mobile is higher than lollipop
        {
            soundPool=new SoundPool.Builder().setMaxStreams(10).build(); //max sound is play together is 10

        }
        else
        {
            soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        }
        try
        {
            AssetManager assetManager=getBaseContext().getAssets();

            AssetFileDescriptor descriptor=assetManager.openFd("win.mp3");
            winn=soundPool.load(descriptor,0);


            descriptor=assetManager.openFd("game-over-sound-effect.mp3");
            gameOver=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("match.mp3");
            match=soundPool.load(descriptor,0);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //get android version of user mobile for use sound
        CharSequence[] players = getIntent().getCharSequenceArrayExtra("playersnames");
        player1ax = getIntent().getBooleanExtra("player1ax", true);
        selectedsingleplayer = getIntent().getBooleanExtra("selectedsingleplayer", true);

        easy = getIntent().getBooleanExtra("easy", false);
        medium = getIntent().getBooleanExtra("medium", false);
        hard = getIntent().getBooleanExtra("hard", false);
        impossible = getIntent().getBooleanExtra("impossible", false);


        if (player1ax) {
            ax = 1;
            zero = 10;
        }


        player1 = players[0];
        player2 = players[1];
        p1 = findViewById(R.id.playerone);
        p2 = findViewById(R.id.playertwo);

        p1.setText(player1);
        p2.setText(player2);

        Toast toast=Toast.makeText(this, "" + player1 +" "+resources.getString(R.string.turn), Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.layout.toast_background_color);
        toast.show();

    }


    public void kzz(View view) {


        if (win == 0 && buttonpressed[0][0] == 0) {
            if (flag % 2 == 0)
                tracker[0][0] = ax;
            else
                tracker[0][0] = zero;

            printBoard();
            winchecker();
            cpuplay();
            flag++;
            buttonpressed[0][0]++;
        }
    }

    public void kzo(View view) {

        if (win == 0 && buttonpressed[0][1] == 0) {
            if (flag % 2 == 0) tracker[0][1] = ax;
            else tracker[0][1] = zero;

            printBoard();
            winchecker();
            cpuplay();
            buttonpressed[0][1]++;
            flag++;
        }
    }

    public void kzt(View view) {
        if (win == 0 && buttonpressed[0][2] == 0) {
            if (flag % 2 == 0) tracker[0][2] = ax;
            else tracker[0][2] = zero;

            printBoard();
            winchecker();
            cpuplay();
            buttonpressed[0][2]++;
            flag++;
        }
    }

    public void koz(View v) {
        if (win == 0 && buttonpressed[1][0] == 0) {
            if (flag % 2 == 0) tracker[1][0] = ax;
            else tracker[1][0] = zero;

            printBoard();
            winchecker();
            cpuplay();

            ++buttonpressed[1][0];
            flag++;
        }
    }

    public void koo(View v) {
        if (win == 0 && buttonpressed[1][1] == 0) {
            if (flag % 2 == 0) tracker[1][1] = ax;
            else tracker[1][1] = zero;
            printBoard();
            winchecker();
            cpuplay();
            ++buttonpressed[1][1];
            flag++;
        }
    }

    public void kot(View v) {
        if (win == 0 && buttonpressed[1][2] == 0) {
            if (flag % 2 == 0) tracker[1][2] = ax;
            else tracker[1][2] = zero;

            printBoard();
            winchecker();
            cpuplay();
            ++buttonpressed[1][2];
            flag++;
        }
    }

    public void ktz(View v) {
        if (win == 0 && buttonpressed[2][0] == 0) {
            if (flag % 2 == 0) tracker[2][0] = ax;
            else tracker[2][0] = zero;

            printBoard();
            winchecker();
            cpuplay();
            ++buttonpressed[2][0];
            flag++;
        }
    }

    public void kto(View v) {
        if (win == 0 && buttonpressed[2][1] == 0) {
            if (flag % 2 == 0) tracker[2][1] = ax;
            else tracker[2][1] = zero;
            printBoard();
            winchecker();
            cpuplay();
            ++buttonpressed[2][1];
            flag++;
        }
    }

    public void ktt(View v) {
        if (win == 0 && buttonpressed[2][2] == 0) {
            if (flag % 2 == 0) tracker[2][2] = ax;
            else tracker[2][2] = zero;

            printBoard();
            winchecker();
            cpuplay();
            ++buttonpressed[2][2];
            flag++;
        }
    }

    public void cpuplay() {
        if ((selectedsingleplayer) && (win == 0)) {


            if (ifcpuwin()) ;
            else if (ifopowin()) ;
            else if (emptycentre()) ;
            else if (emptycorner()) ;
            else emptyany();


            printBoard();
            winchecker();

            flag++;
            return;
        }
    }

    public boolean ifcpuwin() {
        if (!easy) {
            for (i = 0; i < 8; i++) {
                if (sum[i] == 2 * zero) {
                    if (i == 0) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[0][x] == 0)
                                tracker[0][x] = zero;
                    }

                    if (i == 1) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[1][x] == 0)
                                tracker[1][x] = zero;
                    }
                    if (i == 2) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[2][x] == 0)
                                tracker[2][x] = zero;
                    }

                    if (i == 3) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[x][0] == 0)
                                tracker[x][0] = zero;
                    }

                    if (i == 4) {

                        for (int x = 0; x < 3; x++)
                            if (tracker[x][1] == 0)
                                tracker[x][1] = zero;
                    }

                    if (i == 5) {

                        for (int x = 0; x < 3; x++)
                            if (tracker[x][2] == 0)
                                tracker[x][2] = zero;
                    }
                    if (i == 6) {

                        for (int y = 0; y < 3; y++)
                            for (int x = 0; x < 3; x++)
                                if (x == y)
                                    if (tracker[x][y] == 0)
                                        tracker[x][y] = zero;
                    }
                    if (i == 7) {
                        if (tracker[0][2] == 0)
                            tracker[0][2] = zero;
                        else if (tracker[1][1] == 0)
                            tracker[1][1] = zero;
                        else tracker[2][0] = zero;

                    }
                    return true;
                }
            }
        }
        return false;
    }


    public boolean ifopowin() {
        if ((!easy) || (!medium)) {

            for (i = 0; i < 8; i++) {
                if (sum[i] == 2 * ax) {
                    if (i == 0) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[0][x] == 0) {
                                tracker[0][x] = zero;
                                buttonpressed[0][x]++;
                            }
                    }

                    if (i == 1) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[1][x] == 0) {
                                tracker[1][x] = zero;
                                buttonpressed[1][x]++;
                            }
                    }
                    if (i == 2) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[2][x] == 0) {
                                tracker[2][x] = zero;
                                buttonpressed[2][x]++;
                            }
                    }

                    if (i == 3) {
                        for (int x = 0; x < 3; x++)
                            if (tracker[x][0] == 0) {
                                tracker[x][0] = zero;
                                buttonpressed[x][0]++;
                            }
                    }

                    if (i == 4) {

                        for (int x = 0; x < 3; x++)
                            if (tracker[x][1] == 0) {
                                tracker[x][1] = zero;
                                buttonpressed[x][1]++;
                            }
                    }

                    if (i == 5) {

                        for (int x = 0; x < 3; x++)
                            if (tracker[x][2] == 0) {
                                tracker[x][2] = zero;
                                buttonpressed[x][2]++;
                            }
                    }
                    if (i == 6) {

                        for (int y = 0; y < 3; y++)
                            for (int x = 0; x < 3; x++)
                                if (x == y)
                                    if (tracker[x][y] == 0) {
                                        tracker[x][y] = zero;
                                        buttonpressed[x][y]++;
                                    }


                    }
                    if (i == 7) {
                        if (tracker[0][2] == 0) {
                            tracker[0][2] = zero;
                            buttonpressed[0][2]++;
                        } else if (tracker[1][1] == 0) {
                            tracker[1][1] = zero;
                            buttonpressed[1][1]++;
                        } else {
                            tracker[2][0] = zero;
                            buttonpressed[2][0]++;
                        }


                    }
                    return true;
                }
            }

        }
        return false;
    }

    public boolean emptycentre() {
        if (impossible || hard) {
            if (tracker[1][1] == 0) {
                tracker[1][1] = zero;
                buttonpressed[1][1]++;
                return true;
            }
        }
        return false;
    }

    public boolean emptycorner() {


        if (hard || impossible)
            if (((tracker[0][0] + tracker[2][2]) == 2 * ax) || ((tracker[0][2] + tracker[2][0]) == 2 * ax)) {
                for (int k = 0; k < 3; k++)
                    for (int j = 0; j < 3; j++)
                        if ((k + j) % 2 == 1) {
                            if (tracker[k][j] == 0)
                                tracker[k][j] = zero;
                            buttonpressed[k][j]++;
                            return true;
                        }
            }


        if (impossible)
            if (sum[6] == zero || sum[7] == zero) {
                if (sum[6] == zero) {
                    if ((sum[0] + sum[3]) > (sum[2] + sum[5])) {
                        tracker[0][0] = zero;
                        buttonpressed[0][0]++;
                    } else {
                        tracker[2][2] = zero;
                        buttonpressed[2][2]++;
                    }
                    return true;
                }

                if (sum[7] == zero) {
                    if ((sum[0] + sum[5]) > (sum[3] + sum[2])) {
                        tracker[0][2] = zero;
                        buttonpressed[0][2]++;
                    } else {
                        tracker[2][0] = zero;
                        buttonpressed[2][0]++;
                    }
                    return true;
                }

            }


        for (int i = 0; i < 3; i++) {
            if (tracker[0][i] == ax) {
                if (tracker[0][0] == 0) {
                    tracker[0][0] = zero;
                    buttonpressed[0][0]++;
                    return true;
                }
                if (tracker[0][2] == 0) {
                    tracker[0][2] = zero;
                    buttonpressed[0][2]++;
                    return true;
                }
            }
        }

        for (int i = 0; i < 3; i++) {

            if (tracker[2][i] == ax) {
                if (tracker[2][0] == 0) {
                    tracker[2][0] = zero;
                    buttonpressed[2][0]++;
                    return true;
                }
                if (tracker[2][2] == 0) {
                    tracker[2][2] = zero;
                    buttonpressed[2][2]++;
                    return true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (tracker[i][0] == ax) {
                if (tracker[0][0] == 0) {
                    tracker[0][0] = zero;
                    buttonpressed[0][0]++;
                    return true;
                }
                if (tracker[2][0] == 0) {
                    tracker[2][0] = zero;
                    buttonpressed[2][0]++;
                    return true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (tracker[i][2] == ax) {
                if (tracker[0][2] == 0) {
                    tracker[0][2] = zero;
                    buttonpressed[0][2]++;
                    return true;
                }
                if (tracker[2][2] == 0) {
                    tracker[2][2] = zero;
                    buttonpressed[2][2]++;
                    return true;
                }
            }
        }
        return false;

    }

    public void emptyany() {

        if (ctrflag == 0)
            while (true) {
                int x = rand();
                int y = rand();

                if (tracker[x][y] == 0) {
                    tracker[x][y] = zero;
                    buttonpressed[x][y]++;
                    return;

                }
            }

        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                if (tracker[x][y] == 0) {
                    tracker[x][y] = zero;
                    buttonpressed[x][y]++;
                    return;
                }


    }

    public int rand() {
        return r.nextInt(3);
    }

    public void printBoard() {
        ImageView q1, q2, q3, q4, q5, q6, q7, q8, q9;

        q1 = findViewById(R.id.tzz);
        q2 = findViewById(R.id.tzo);
        q3 = findViewById(R.id.tzt);
        q4 = findViewById(R.id.toz);
        q5 = findViewById(R.id.too);
        q6 = findViewById(R.id.tot);
        q7 = findViewById(R.id.ttz);
        q8 = findViewById(R.id.tto);
        q9 = findViewById(R.id.ttt);


        if (tracker[0][0] == 1) q1.setImageResource(R.drawable.x);
        if (tracker[0][0] == 10) q1.setImageResource(R.drawable.oo);


        if (tracker[0][1] == 1) q2.setImageResource(R.drawable.x);
        if (tracker[0][1] == 10) q2.setImageResource(R.drawable.oo);


        if (tracker[0][2] == 1) q3.setImageResource(R.drawable.x);
        if (tracker[0][2] == 10) q3.setImageResource(R.drawable.oo);


        if (tracker[1][0] == 1) q4.setImageResource(R.drawable.x);
        if (tracker[1][0] == 10) q4.setImageResource(R.drawable.oo);

        if (tracker[1][1] == 1) q5.setImageResource(R.drawable.x);
        if (tracker[1][1] == 10) q5.setImageResource(R.drawable.oo);


        if (tracker[1][2] == 1) q6.setImageResource(R.drawable.x);
        if (tracker[1][2] == 10) q6.setImageResource(R.drawable.oo);

        if (tracker[2][0] == 1) q7.setImageResource(R.drawable.x);
        if (tracker[2][0] == 10) q7.setImageResource(R.drawable.oo);


        if (tracker[2][1] == 1) q8.setImageResource(R.drawable.x);
        if (tracker[2][1] == 10) q8.setImageResource(R.drawable.oo);

        if (tracker[2][2] == 1) q9.setImageResource(R.drawable.x);
        if (tracker[2][2] == 10) q9.setImageResource(R.drawable.oo);

        resetchecker++;
    }


    public void showDialog(String whoWon, String scoreWon, String whoLose, String scoreLose) {

        final Dialog dialog = new Dialog(Afterstart.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout2);
//        TextView playerOneScore = dialog.findViewById(R.id.player_one_score);
//        TextView playerTwoScore = dialog.findViewById(R.id.player_two_score);
        TextView titleText = dialog.findViewById(R.id.title_text);
        dialog.setCancelable(false);
        dialog.show();

        titleText.setText(whoWon+scoreWon+whoLose+scoreLose);

        Button resetButton = dialog.findViewById(R.id.reset_button);
        Button playAgainButton = dialog.findViewById(R.id.play_again_button);

        resetButton.setText(resources.getString(R.string.reset));
        playAgainButton.setText(resources.getString(R.string.play_again));


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                doreset();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                playmore();
            }
        });
    }

    public void winchecker() {
        ctrflag++;
        sum[0] = tracker[0][0] + tracker[0][1] + tracker[0][2];
        sum[1] = tracker[1][0] + tracker[1][1] + tracker[1][2];
        sum[2] = tracker[2][0] + tracker[2][1] + tracker[2][2];
        sum[3] = tracker[0][0] + tracker[1][0] + tracker[2][0];
        sum[4] = tracker[0][1] + tracker[1][1] + tracker[2][1];
        sum[5] = tracker[0][2] + tracker[1][2] + tracker[2][2];
        sum[6] = tracker[0][0] + tracker[1][1] + tracker[2][2];
        sum[7] = tracker[0][2] + tracker[1][1] + tracker[2][0];


        currentgamedonechecker++;
        resetchecker++;

        for (int i = 0; i < 8; i++)
            if (sum[i] == 3 || sum[i] == 30) {
                win++;
                if ((sum[i] == 3) && (ax == 1)) {
                    score1++;
                    TextView q1 = findViewById(R.id.p1score);
                    q1.setText("" + score1);
                    String mes="";
                    if (getIntent().getBooleanExtra("selectedsingleplayer", false))
                    {
                        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                        record=per.getString("xo","0");
                        if (Integer.parseInt(record)<score1)
                        {
                            record=String.valueOf(score1);
                            per.edit().putString("xo",record).apply();
                        }
                        mes=(" "+resources.getString(R.string.recordd)+" "+record);
                        if (sound)
                            soundPool.play(winn,1,1,0,0,1);
                    }
                    showDialog("" + player1 + " "+resources.getString(R.string.won)+mes, " "+resources.getString(R.string.score)+" " + score1, "" + player2+" "+resources.getString(R.string.loseee)+" ", ""+resources.getString(R.string.score)+" " + score2);

                }
                if ((sum[i] == 3) && (zero == 1)) {
                    score2++;
                    TextView q1 = findViewById(R.id.p2score);
                    q1.setText("" + score2);
                    if (getIntent().getBooleanExtra("selectedsingleplayer", false))
                    {
                        if (sound)
                            soundPool.play(gameOver,1,1,0,0,1);
                        showDialog("" + player1 +" "+resources.getString(R.string.loseee), " "+resources.getString(R.string.score)+" "+ score1, " " + player2+" "+resources.getString(R.string.won)+" ", ""+resources.getString(R.string.score)+" " + score2);
                    }
                    else
                        showDialog("" + player2 +" "+resources.getString(R.string.won), " "+resources.getString(R.string.score)+" "+ score2, " " + player1+" "+resources.getString(R.string.loseee)+" ", ""+resources.getString(R.string.score)+" " + score1);

                }
                if ((sum[i] == 30) && (ax == 10)) {
                    score1++;
                    TextView q1 = findViewById(R.id.p1score);
                    q1.setText("" + score1);
                    String mes="";
                    if (getIntent().getBooleanExtra("selectedsingleplayer", false))
                    {
                        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                        record=per.getString("xo","0");
                        if (Integer.parseInt(record)<score1)
                        {
                            record=String.valueOf(score1);
                            per.edit().putString("xo",record).apply();
                        }
                        mes=(" "+resources.getString(R.string.recordd)+" "+record);
                        if (sound)
                            soundPool.play(winn,1,1,0,0,1);
                    }
                    showDialog("" + player1 + " "+resources.getString(R.string.won)+mes, " "+resources.getString(R.string.score)+" " + score1, " " + player2+" "+resources.getString(R.string.loseee)+" ", ""+resources.getString(R.string.score)+" " + score2);

                }
                if ((sum[i] == 30) && (zero == 10)) {
                    score2++;
                    TextView q1 = findViewById(R.id.p2score);
                    q1.setText("" + score2);
                    if (getIntent().getBooleanExtra("selectedsingleplayer", false))
                    {
                        if (sound)
                            soundPool.play(gameOver,1,1,0,0,1);
                        showDialog("" + player1 +" "+resources.getString(R.string.loseee), " "+resources.getString(R.string.score)+" "+ score1, " " + player2+" "+resources.getString(R.string.won)+" ", ""+resources.getString(R.string.score)+" " + score2);
                    }
                    else
                        showDialog("" + player2 + " "+resources.getString(R.string.won), " " +resources.getString(R.string.score)+" "+ score2, " " + player1+" "+resources.getString(R.string.loseee)+" ", ""+resources.getString(R.string.score)+" " + score1);

                }

            }

        if ((ctrflag == 9) && (win == 0)) {
            if (getIntent().getBooleanExtra("selectedsingleplayer", false))
            {
                if (sound)
                    soundPool.play(match,1,1,0,0,1);
            }
            showDialog(resources.getString(R.string.draw)+" "+player1, " " +resources.getString(R.string.score)+" "+ score1, " " + player2, " " +resources.getString(R.string.score)+" "+ score2);
            drawchecker++;
        }


    }

    @SuppressLint("ResourceType")
    private void playmore() {
        if ((drawchecker > 0) || (win > 0)) {
            game++;
            TextView qq = findViewById(R.id.gamenumber);
            qq.setText("" + game);

            for (int i = 0; i < 8; i++)
                sum[i] = 0;

            drawchecker = 0;


            ImageView q1, q2, q3, q4, q5, q6, q7, q8, q9;
            q1 = findViewById(R.id.tzz);
            q2 = findViewById(R.id.tzo);
            q3 = findViewById(R.id.tzt);
            q4 = findViewById(R.id.toz);
            q5 = findViewById(R.id.too);
            q6 = findViewById(R.id.tot);
            q7 = findViewById(R.id.ttz);
            q8 = findViewById(R.id.tto);
            q9 = findViewById(R.id.ttt);
            q1.setImageDrawable(null);
            q2.setImageDrawable(null);
            q3.setImageDrawable(null);
            q4.setImageDrawable(null);
            q5.setImageDrawable(null);
            q6.setImageDrawable(null);
            q7.setImageDrawable(null);
            q8.setImageDrawable(null);
            q9.setImageDrawable(null);

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    buttonpressed[i][j] = 0;

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    tracker[i][j] = 0;


            if ((game + 1) % 2 == 0) {
                Toast toast=Toast.makeText(this, "" + player1 + " "+resources.getString(R.string.turn), Toast.LENGTH_SHORT);
                toast.getView().setBackgroundResource(R.layout.toast_background_color);
                toast.show();
            }
            else {
                Toast toast=Toast.makeText(this, "" + player2 + " "+resources.getString(R.string.turn), Toast.LENGTH_SHORT);
                toast.getView().setBackgroundResource(R.layout.toast_background_color);
                toast.show();
            }
            win = 0;
            summ = 0;
            ctrflag = 0;
            flag = (game + 1) % 2;
            currentgamedonechecker = 0;

            if (selectedsingleplayer && (game % 2 == 0))
                cpuplay();
        }
    }


    @SuppressLint("ResourceType")
    public void doreset() {

        TextView qq = findViewById(R.id.gamenumber);
        qq.setText("" + 1);


        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tracker[i][j] = 0;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttonpressed[i][j] = 0;

        ImageView q1, q2, q3, q4, q5, q6, q7, q8, q9;

        q1 = findViewById(R.id.tzz);
        q2 = findViewById(R.id.tzo);
        q3 = findViewById(R.id.tzt);
        q4 = findViewById(R.id.toz);
        q5 = findViewById(R.id.too);
        q6 = findViewById(R.id.tot);
        q7 = findViewById(R.id.ttz);
        q8 = findViewById(R.id.tto);
        q9 = findViewById(R.id.ttt);
        q1.setImageDrawable(null);
        q2.setImageDrawable(null);
        q3.setImageDrawable(null);
        q4.setImageDrawable(null);
        q5.setImageDrawable(null);
        q6.setImageDrawable(null);
        q7.setImageDrawable(null);
        q8.setImageDrawable(null);
        q9.setImageDrawable(null);


        win = 0;
        drawchecker = 0;
        summ = 0;
        resetchecker = 0;
        ctrflag = 0;
        score1 = 0;
        score2 = 0;
        game = 1;
        flag = 0;
        currentgamedonechecker = 0;
        TextView qqq = findViewById(R.id.p1score);
        qqq.setText("" + score1);
        TextView qqqq = findViewById(R.id.p2score);
        qqqq.setText("" + score2);

        Toast toast=Toast.makeText(this, "" + player1 + " "+resources.getString(R.string.turn), Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.layout.toast_background_color);
        toast.show();


    }


    private void showExitDialog() {
        final Dialog dialog = new Dialog(Afterstart.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit2);
        dialog.setCancelable(false);

        dialog.show();

        Button exit = dialog.findViewById(R.id.yes_button);
        final Button dismiss = dialog.findViewById(R.id.no_button);
        TextView txt_title=dialog.findViewById(R.id.title);

        exit.setText(resources.getString(R.string.yes_button));
        dismiss.setText(resources.getString(R.string.no_button));
        txt_title.setText(resources.getString(R.string.dialog_exit));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doreset();
                dialog.dismiss();
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}


