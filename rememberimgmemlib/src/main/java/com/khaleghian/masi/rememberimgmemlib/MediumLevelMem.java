package com.khaleghian.masi.rememberimgmemlib;

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
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MediumLevelMem extends AppCompatActivity {
    List<String> fileNameList; //selected item with user
    String [] pictures; //added name image of choices folder
    List <String> matrix=new ArrayList<>();
    LinearLayout[] linearLayouts;
    ImageView heart1,heart2,heart3;
    SoundPool soundPool;
    int match=-1;
    int win=-1;
    int gameOver=-1;
    int wrong=-1;
    int counter=0;
    int heart=3;
    int counter_correct_answer=0;
    ImageButton img_show_Q;
    TextView txt_correct_answer;
    int counter_image_show=3;
    Handler handler;
    Runnable runnable;
    View white;
    Boolean aBoolean=false;
    String record="";
    Boolean flag=true;
    FloatingActionButton game;
    FloatingActionButton what;
    FloatingActionButton pause;
    Boolean sound;
    MediaPlayer mediaPlayer=MainActivity.getMediaPlayer();
    Context context;
    Resources resources;
    Boolean dialogbox=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_level_mem5);

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

        sound=getIntent().getBooleanExtra("sound",true);
        //FAB
        game=findViewById(R.id.floatingActionButtongame7);
        what=findViewById(R.id.floatingActionButtonwhat7);
        pause=findViewById(R.id.floatingActionButtonPause7);
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
                finish();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBoolean)
                    handler.removeCallbacks(runnable);
                if (!dialogbox) {
                    showExitDialog();
                }
            }
        });
        //FAB Finish


        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
        record=per.getString("mediumRemImgMem","0");

        //lan
        String lan=LocaleHelper.getLanguage(MediumLevelMem.this);
        String sh_language=per.getString("language",lan);
        context= LocaleHelper.setLocale(MediumLevelMem.this, sh_language);
        resources = context.getResources();
        //end lan

        heart1 = findViewById(R.id.btn_heart1);
        heart2 = findViewById(R.id.btn_heart2);
        heart3 = findViewById(R.id.btn_heart3);
        img_show_Q = findViewById(R.id.img_show_Q);
        white =findViewById(R.id.view);
        txt_correct_answer = findViewById(R.id.txt_correct_answer);

        //get android version of user mobile for use sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) //when user mobile is higher than lollipop
        {
            soundPool = new SoundPool.Builder().setMaxStreams(10).build(); //max sound is play together is 10

        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        try {
            AssetManager assetManager = getBaseContext().getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("match.mp3");
            match = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("win.mp3");
            win = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("game-over-sound-effect.mp3");
            gameOver = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("wrong.mp3");
            wrong = soundPool.load(descriptor, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //get android version of user mobile for use sound


        linearLayouts = new LinearLayout[4];
        linearLayouts[0] = findViewById(R.id.linearLayout1);
        linearLayouts[1] = findViewById(R.id.linearLayout2);
        linearLayouts[2] = findViewById(R.id.linearLayout3);
        linearLayouts[3] = findViewById(R.id.linearLayout4);
        disableButton();
        showImage(counter_image_show);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                hideImage();
                enableButton();
                askImage(counter_image_show);
                aBoolean=false;
            }
        };
        handler.postDelayed(runnable,5000);//5 sec
    }
    public void showImage(int count)
    {
        aBoolean=true;
        int count2=count;
        img_show_Q.setVisibility(View.INVISIBLE);
        fileNameList=new ArrayList<>();
        AssetManager assetManager=getBaseContext().getAssets();
        fileNameList.clear();
        try {
            pictures=assetManager.list("cartoon");
            Collections.shuffle(Arrays.asList(pictures));
            Collections.shuffle(Arrays.asList(pictures));
            for (String picture:pictures)
            {
                fileNameList.add(picture);
                if (count==1)
                    break;
                --count;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Collections.shuffle(fileNameList);
        getRandomMatrix(count2);
        int x=0;
        for (LinearLayout ll:linearLayouts) {
            for (int column = 0; column < ll.getChildCount(); column++) {
                ImageButton imageButton = (ImageButton) ll.getChildAt(column);
                imageButton.setOnClickListener(imageButtonListener);
                if (matrix.indexOf((x+","+column))>=0) {
                    imageButton.setContentDescription(fileNameList.get(counter));//name image of image button
                    try {
                        InputStream inputStream=getAssets().open("cartoon/"+fileNameList.get(counter));
                        Drawable drawable=Drawable.createFromStream(inputStream,null);
                        imageButton.setImageDrawable(drawable);
                        inputStream.close();
                    }
                    catch (IOException e)
                    {
                        return;
                    }
                    counter++;
                }
                else
                {
                    imageButton.setContentDescription("Pattern");
                }
            }
            ++x;
        }
        counter=0;
    }
    public void getRandomMatrix(int count)
    {
        matrix.clear();
        Random random=new Random();
        for (int i=count;i>0;i--)
        {
            int x=random.nextInt(4);
            int y=random.nextInt(4);
            String m=(x+","+y);
            if (matrix.indexOf(m)<0)
            {
                matrix.add(m);
            }
            else
                ++i;
        }
    }
    public void askImage(int count)
    {
        img_show_Q.setVisibility(View.VISIBLE);
        try {
            String str=fileNameList.get(new Random().nextInt(count));
            InputStream inputStream = getAssets().open("cartoon/" + str);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            img_show_Q.setImageDrawable(drawable);
            img_show_Q.setTag(str);
            inputStream.close();
        } catch (IOException e) {
            return;
        }
    }
    public void hideImage()
    {
        for (LinearLayout ll:linearLayouts) {
            for (int column = 0; column < ll.getChildCount(); column++) {
                ImageButton imageButton = (ImageButton) ll.getChildAt(column);
                imageButton.setImageResource(R.drawable.pattern);
            }
        }
    }
    public void disableButton()
    {
        for (LinearLayout ll:linearLayouts) {
            for (int column = 0; column < ll.getChildCount(); column++) {
                ImageButton imageButton = (ImageButton) ll.getChildAt(column);
                imageButton.setEnabled(false);
            }
        }
    }
    public void enableButton()
    {
        for (LinearLayout ll:linearLayouts) {
            for (int column = 0; column < ll.getChildCount(); column++) {
                ImageButton imageButton = (ImageButton) ll.getChildAt(column);
                imageButton.setEnabled(true);
            }
        }
    }
    View.OnClickListener imageButtonListener=new View.OnClickListener() {
        @SuppressLint({"SetTextI18n", "ResourceType"})
        @Override
        public void onClick(View v) {
            disableButton();
            ImageButton imageButton=(ImageButton)v;
            String fileName= (String) imageButton.getContentDescription();
            if (fileName.equals(img_show_Q.getTag()) && heart>=0)
            {
                counter_correct_answer++;
                counter_image_show++;
                showImageOfEachButton(imageButton,true);
                txt_correct_answer.setText(counter_correct_answer+"/10");
                Toast toast=Toast.makeText(MediumLevelMem.this, resources.getString(R.string.correct), Toast.LENGTH_SHORT);
                toast.getView().setBackgroundResource(R.layout.toast_background_color1);
                toast.show();
                if (sound)
                    soundPool.play(match,1,1,0,0,1);
                if (counter_correct_answer==10)
                {
                    if (Integer.parseInt(record)<counter_correct_answer)
                    {
                        record=String.valueOf(counter_correct_answer);
                        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                        per.edit().putString("mediumRemImgMem",record).apply();
                    }
                    String mes=(" "+resources.getString(R.string.record)+" "+record);
                    showDialog(resources.getString(R.string.win)+" "+counter_correct_answer+"/10"+mes);
                    if (sound)
                        soundPool.play(win,1,1,0,0,1);
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showImage(counter_image_show);
                        }
                    },3000);
                    runnable=new Runnable() {
                        @Override
                        public void run() {
                            hideImage();
                            enableButton();
                            askImage(counter_image_show);
                            aBoolean=false;
                        }
                    };
                    handler.postDelayed(runnable,8000);
                }
            }
            else
            {
                showImageOfEachButton(imageButton,false);
                Toast toast=Toast.makeText(MediumLevelMem.this, resources.getString(R.string.incorrect), Toast.LENGTH_SHORT);
                toast.getView().setBackgroundResource(R.layout.toast_background_color1);
                toast.show();
                if (heart>0)
                {
                    if (sound)
                        soundPool.play(wrong,1,1,0,0,1);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
                    scaleAnimation.setDuration(500);
                    BounceInterpolator bounceInterpolator = new BounceInterpolator();
                    scaleAnimation.setInterpolator(bounceInterpolator);
                    switch (heart)
                    {
                        case 3:
                            heart1.startAnimation(scaleAnimation);
                            heart1.setImageResource(R.drawable.border_heart);
                            break;
                        case 2:
                            heart2.startAnimation(scaleAnimation);
                            heart2.setImageResource(R.drawable.border_heart);
                            break;
                        case 1:
                            heart3.startAnimation(scaleAnimation);
                            heart3.setImageResource(R.drawable.border_heart);
                            break;

                    }
                    --heart;
                    enableButton();
                }
                else
                {
                    if (Integer.parseInt(record)<counter_correct_answer)
                    {
                        record=String.valueOf(counter_correct_answer);
                        SharedPreferences per=getSharedPreferences("Data",MODE_PRIVATE);
                        per.edit().putString("mediumRemImgMem",record).apply();
                    }
                    String mes=(" "+resources.getString(R.string.record)+" "+record);
                    showDialog(resources.getString(R.string.lose)+" "+counter_correct_answer+"/10"+mes);
                    if (sound)
                        soundPool.play(gameOver,1,1,0,0,1);
                }
            }
        }
    };
    public void showImageOfEachButton(final ImageButton imageButton, Boolean bool) {
        try {
            InputStream inputStream = getAssets().open("cartoon/" + imageButton.getContentDescription().toString());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageButton.setImageDrawable(drawable);
            if (bool)
                imageButton.setBackgroundResource(R.drawable.border_green);
            else
                imageButton.setBackgroundResource(R.drawable.border_red);
            inputStream.close();
            Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            imageButton.startAnimation(shakeAnimation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageButton.setBackgroundResource(android.R.drawable.btn_default_small);
                    hideImage();
                }
            },2000);
        }
        catch (IOException e) {
            imageButton.setBackgroundResource(R.drawable.border_red);
            Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            imageButton.startAnimation(shakeAnimation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageButton.setBackgroundResource(android.R.drawable.btn_default_small);
                }
            },2000);
            return;
        }

    }
    //dialog exit
    private void showExitDialog() {
        dialogbox=true;
        white.setVisibility(View.VISIBLE);
        final Dialog dialog = new Dialog(MediumLevelMem.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit5);
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
                if (aBoolean) {
                    handler.postDelayed(runnable, 5000);
                }
                white.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
    }

    //dialog play game
    public void showDialog(String sate) {
        final Dialog dialog = new Dialog(MediumLevelMem.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout5);

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
                restartActivity(MediumLevelMem.this);
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
        if (aBoolean)
            handler.removeCallbacks(runnable);
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
