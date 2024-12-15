package com.khaleghian.masi.calculatorlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EasyMediumHardCalculator extends AppCompatActivity {
    TextView txt_showStr,txt_showAnswer,txt_correctAnswer;
    LinearLayout[]linearLayouts;
    String str_answer="";
    int num1,num2,int_operator,answer,countCorrectAnswer=0,countAnswerLevel,countIncorrectAnswer=0;
    SoundPool soundPool;
    int match=-1;
    int win=-1;
    int gameOver=-1;
    int wrong=-1;
    String record=null;
    String level=null;
    //timer
    TextView  txtTimer;
    CountDownTimer countDownTimer;
    private long mTimeLeftInMillis;
    boolean mTimerRunning;
    int minutes,seconds,minNum1,maxNum1,minNum2,maxNum2;
    //timer
    View white;
    Boolean flag=true;
    FloatingActionButton game;
    FloatingActionButton what;
    FloatingActionButton pause;
    Boolean sound;
    MediaPlayer mediaPlayer=MainActivity.getMediaPlayer();
    Context context;
    Resources resources;
    Button clear;
    Boolean dialogbox=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_medium_hard_calculator);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //play audio
        Boolean playagain=getIntent().getBooleanExtra("playagain",false);
        if (playagain) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.soundgame);
            mediaPlayer.setLooping(true);
            SharedPreferences per = getSharedPreferences("Data", MODE_PRIVATE);
            String sh_music = per.getString("music", "Unmute Music");
            if (sh_music.equals("Unmute Music")) {
                mediaPlayer.start();
            }
        }
        //end play audio

        //lan
        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        String lan=LocaleHelper.getLanguage(EasyMediumHardCalculator.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(EasyMediumHardCalculator.this, sh_language);
        resources = context.getResources();
        //end lan
        clear=findViewById(R.id.clear);
        clear.setText(resources.getString(R.string.clear));
        sound=getIntent().getBooleanExtra("sound",true);
        //FAB
        game=findViewById(R.id.floatingActionButtongame);
        what=findViewById(R.id.floatingActionButtonwhat);
        pause=findViewById(R.id.floatingActionButtonPause);
        what.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag=false;
                    what.animate().rotation(90);
                    game.setVisibility(View.VISIBLE);
                    game.animate().rotation(360);
                    pause.setVisibility(View.VISIBLE);
                    pause.animate().rotation(360);
                }
                else {
                    flag=true;
                    what.animate().rotation(0);
                    game.setVisibility(View.INVISIBLE);
                    game.animate().rotation(0);
                    pause.setVisibility(View.INVISIBLE);
                    pause.animate().rotation(0);
                }
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                finish();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialogbox) {
                    showExitDialog();
                }
            }
        });
        //FAB Finish
        txt_showAnswer=findViewById(R.id.txt_showAnswer);
        txt_showStr=findViewById(R.id.txt_showStr);
        txt_correctAnswer=findViewById(R.id.countCorrectAnswer);
        white=findViewById(R.id.view);
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

            AssetFileDescriptor descriptor=assetManager.openFd("match.mp3");
            match=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("win.mp3");
            win=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("game-over-sound-effect.mp3");
            gameOver=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("wrong.mp3");
            wrong=soundPool.load(descriptor,0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //get android version of user mobile for use sound
        Intent intent=getIntent();
        level=intent.getStringExtra("level");
        String easy=resources.getString(R.string.easy),medium=resources.getString(R.string.medium),hard=resources.getString(R.string.hard),impossible=resources.getString(R.string.impossible);
        if (level.equals(easy)) {
            level = ("easy"+getString(R.string.calc));
            record = per.getString(level, "0");
            mTimeLeftInMillis = 60000;//1 min
            countAnswerLevel = 20;
            minNum1 = 0;
            maxNum1 = 10;
            minNum2 = 0;
            maxNum2 = 10;
            txt_correctAnswer.setText((countCorrectAnswer + "/" + countAnswerLevel));
        } else if (level.equals(medium)) {
            level = ("medium"+getString(R.string.calc));
            record = per.getString(level, "0");
            mTimeLeftInMillis = 180000;//3 min
            countAnswerLevel = 30;
            minNum1 = 10;
            maxNum1 = 100;
            minNum2 = 0;
            maxNum2 = 10;
            txt_correctAnswer.setText((countCorrectAnswer + "/" + countAnswerLevel));
        } else if (level.equals(hard)) {
            level = ("hard"+getString(R.string.calc));
            record = per.getString(level, "0");
            mTimeLeftInMillis = 360000;//6 min
            countAnswerLevel = 40;
            minNum1 = 10;
            maxNum1 = 100;
            minNum2 = 10;
            maxNum2 = 100;
            txt_correctAnswer.setText((countCorrectAnswer + "/" + countAnswerLevel));
        } else if (level.equals(impossible)) {
            level = ("impossible"+getString(R.string.calc));
            record = per.getString(level, "0");
            mTimeLeftInMillis = 540000;//9 min
            countAnswerLevel = 50;
            minNum1 = 100;
            maxNum1 = 1000;
            minNum2 = 10;
            maxNum2 = 100;
            txt_correctAnswer.setText((countCorrectAnswer + "/" + countAnswerLevel));
        }
        //timer
        txtTimer=findViewById(R.id.txt_timer);
        startTimer();
        updateCountDownText();
        //timer

        getStr();


        linearLayouts=new LinearLayout[4];
        linearLayouts[0]=findViewById(R.id.linearLayout1);
        linearLayouts[1]=findViewById(R.id.linearLayout2);
        linearLayouts[2]=findViewById(R.id.linearLayout3);
        linearLayouts[3]=findViewById(R.id.linearLayout4);

        for (LinearLayout ll:linearLayouts)
            for (int row=0;row<ll.getChildCount();row++)
            {
                Button imageButton=(Button) ll.getChildAt(row);
                imageButton.setOnClickListener(imageButtonListener);
            }
    }


    View.OnClickListener imageButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn=(Button) v;
            btn.setBackgroundColor(getResources().getColor(R.color.blue));
            String clear=resources.getString(R.string.clear);
            if (!btn.getText().toString().equals(clear)) {
                str_answer += btn.getText().toString();
                txt_showAnswer.setText(str_answer);
                int int_answer = Integer.parseInt(str_answer);
                if (int_answer==answer) {
                    disableButtons();
                    ++countCorrectAnswer;
                    txt_showAnswer.setTextColor(Color.GREEN);
                    str_answer +=(" "+resources.getString(R.string.correct));
                    txt_showAnswer.setText(str_answer);
                    if (sound)
                        soundPool.play(match,1,1,0,0,1);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setBackColor();
                            str_answer="";
                            txt_showAnswer.setText(str_answer);
                            if (countCorrectAnswer==countAnswerLevel) {
                                if(Integer.parseInt(record)<countCorrectAnswer) {
                                    record=String.valueOf(countCorrectAnswer);
                                    SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                                    per.edit().putString(level,record).apply();
                                }
                                String mes=(" "+resources.getString(R.string.record)+" "+record);
                                showDialog(resources.getString(R.string.part1winn)+" "+countCorrectAnswer+"/"+countAnswerLevel+" "+resources.getString(R.string.part2winn)+" "+countIncorrectAnswer+mes);
                                if (sound)
                                    soundPool.play(win,1,1,0,0,1);
                            }
                            txt_showAnswer.setTextColor(Color.parseColor("#4B4B4B"));
                            getStr();
                            enableButtons();
                        }
                    },500);
                }
                else if ((str_answer.length())==((answer+"").length()))
                {
                    disableButtons();
                    ++countIncorrectAnswer;
                    if (sound)
                        soundPool.play(wrong,1,1,0,0,1);
                    txt_showAnswer.setTextColor(Color.RED);
                    str_answer +=(" "+resources.getString(R.string.incorrect));
                    txt_showAnswer.setText(str_answer);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setBackColor();
                            txt_showAnswer.setTextColor(Color.parseColor("#4B4B4B"));
                            str_answer = "";
                            txt_showAnswer.setText(str_answer);
                            enableButtons();
                        }
                    },500);
                }
            }
            else {
                str_answer = "";
                setBackColor();
            }
            txt_showAnswer.setText(str_answer);
            txt_correctAnswer.setText((countCorrectAnswer+"/"+countAnswerLevel));
        }
    };

    private void setBackColor()
    {
        for (LinearLayout ll:linearLayouts)
            for (int row=0;row<ll.getChildCount();row++)
            {
                Button button=(Button) ll.getChildAt(row);
                button.setBackgroundColor(getResources().getColor(R.color.yellow));
            }
    }
    private void disableButtons() {
        for (LinearLayout ll:linearLayouts)
            for (int row=0;row<ll.getChildCount();row++)
            {
                Button button=(Button) ll.getChildAt(row);
                button.setEnabled(false);
            }
    }
    private void enableButtons() {
        for (LinearLayout ll:linearLayouts)
            for (int row=0;row<ll.getChildCount();row++)
            {
                Button button=(Button) ll.getChildAt(row);
                button.setEnabled(true);
            }
    }
    private void getStr() {
        num1 = getRandomNumber(minNum1, maxNum1);
        num2 = getRandomNumber(minNum2, maxNum2);
        int_operator = getRandomNumber(0, 3);
        //"+","-","x","÷"
        switch (int_operator) {
            case 0:
                txt_showStr.setText((num1 + " + " + num2 + " = "));
                answer = num1 + num2;
                break;
            case 1:
                if (num1 < num2) {
                    int temp = num1;
                    num1 = num2;
                    num2 = temp;
                }
                txt_showStr.setText((num1 + " - " + num2 + " = "));
                answer = num1 - num2;
                break;
            case 2:
                txt_showStr.setText((num1 + " x " + num2 + " = "));
                answer = num1 * num2;
                break;
            case 3:
                while (num2==0)
                    num2 = getRandomNumber(minNum2, maxNum2);
                while (num1 % num2 != 0) {
                    num1 = getRandomNumber(minNum1, maxNum1);
                    num2 = getRandomNumber(minNum2, maxNum2);
                    while (num2==0)
                        num2 = getRandomNumber(minNum2, maxNum2);
                }
                txt_showStr.setText((num1 + " ÷ " + num2 + " = "));
                answer = num1 / num2;
                break;
        }
    }


    private int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    //timer
    private void startTimer(){
        countDownTimer=new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning=false;
                mTimeLeftInMillis=0;
                updateCountDownText();
                if (countCorrectAnswer<countAnswerLevel) {
                    if (sound)
                        soundPool.play(gameOver,1,1,0,0,1);
                    if(Integer.parseInt(record)<countCorrectAnswer) {
                        record=String.valueOf(countCorrectAnswer);
                        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                        per.edit().putString(level,record).apply();
                    }
                    String mes=(" "+resources.getString(R.string.record)+" "+record);
                    showDialog(resources.getString(R.string.part1losee)+" "+ countCorrectAnswer + "/" + countAnswerLevel+" "+ resources.getString(R.string.part2losee)+" "+countIncorrectAnswer+mes);
                }
            }
        }.start();
        mTimerRunning=true;
    }
    private void updateCountDownText(){
        minutes=(int)(mTimeLeftInMillis/1000)/60;
        seconds=(int)(mTimeLeftInMillis/1000)%60;
        String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        txtTimer.setText(timeLeftFormatted);
    }
    private void pauseTimer(){
        countDownTimer.cancel();
        mTimerRunning=false;
    }
    //timer

    //dialog exit
    private void showExitDialog() {
        dialogbox=true;
        white.setVisibility(View.VISIBLE);
        pauseTimer();
        final Dialog dialog = new Dialog(EasyMediumHardCalculator.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit4);
        dialog.setCancelable(false);
        dialog.show();

        Button exit = dialog.findViewById(R.id.yes_button);
        final Button dismiss = dialog.findViewById(R.id.no_button);
        TextView txt_title=dialog.findViewById(R.id.txt_title);

        exit.setText(resources.getString(R.string.yes_button));
        dismiss.setText(resources.getString(R.string.no_button));
        txt_title.setText(resources.getString(R.string.dialog_exit));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox=false;
                dialog.dismiss();
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogbox=false;
                white.setVisibility(View.GONE);
                dialog.dismiss();
                startTimer();
            }
        });
    }

    //dialog play game
    public void showDialog(String sate) {
        pauseTimer();
        final Dialog dialog = new Dialog(EasyMediumHardCalculator.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout4);

        TextView titleText = dialog.findViewById(R.id.title_text);
        dialog.setCancelable(false);
        dialog.show();

        titleText.setText(sate);

        Button homeButton = dialog.findViewById(R.id.home_button);
        Button playAgainButton = dialog.findViewById(R.id.play_again_button);

        homeButton.setText(resources.getString(R.string.home));
        playAgainButton.setText(resources.getString(R.string.play_again));

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                restartActivity(EasyMediumHardCalculator.this);
            }
        });
    }
    public static void restartActivity(Activity act){
        act.finish();
        Intent intent=act.getIntent();
        intent.putExtra("playagain",true);
        act.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (!dialogbox) {
            showExitDialog();
        }
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
        onBackPressed();
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
