package com.khaleghian.masi.imgmem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameActivityEasy extends AppCompatActivity {

    String folderName;
    List<String> fileNameList; //selected item with user
    String [] pictures; //added name image of choices folder
    LinearLayout[] linearLayouts;//4 linear layouts of activity game medium
    int counter=0;//access to image button and set image
    int clicks=0;
    ImageButton img1,img2; //two button clicked
    String image1,image2; //name image of two button clicked
    List<String> isolate; //list of images choices when images is same
    Handler handler;
    int CounterOfChoice=0;
    Animation shakeAnimation;
    SoundPool soundPool;
    int match=-1;
    int win=-1;
    int gameOver=-1;
    int wrong=-1;
    //timer
    TextView  txtTimer;
    CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS=30000;//30 sec
    private long mTimeLeftInMillis=START_TIME_IN_MILLIS;
    boolean mTimerRunning;
    int minutes,seconds;
    //timer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_easy);

        //timer
        txtTimer=findViewById(R.id.txt_timer);
        startTimer();
        updateCountDownText();
        //timer

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
        int itemNumber=intent.getIntExtra("itemNumber",0);
        fileNameList=new ArrayList<>();


        isolate=new ArrayList<>();
        handler=new Handler();


        switch (itemNumber)
        {
            case 0:
                folderName="flower";
                break;
            case 1:
                folderName="cartoon";
                break;
            case 2:
                folderName="car";
                break;
            case 3:
                folderName="alphabet";
                break;
            case 4:
                folderName="flag";
                break;
            case 5:
                folderName="number";
                break;
        }
        AssetManager assetManager=getBaseContext().getAssets();
        fileNameList.clear();
        try {
            pictures=assetManager.list(folderName);
            Collections.shuffle(Arrays.asList(pictures));
            int i=0;
            for (String picture:pictures)
            {
                fileNameList.add(picture);
                fileNameList.add(picture);
                if (i==2)
                    break;
                ++i;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Collections.shuffle(fileNameList);
        Collections.shuffle(fileNameList);
        linearLayouts=new LinearLayout[2];
        linearLayouts[0]=findViewById(R.id.linearLayout1);
        linearLayouts[1]=findViewById(R.id.linearLayout2);

        for (LinearLayout ll:linearLayouts)
            for (int column=0;column<ll.getChildCount();column++)
            {
                ImageButton imageButton=(ImageButton)ll.getChildAt(column);
                imageButton.setOnClickListener(imageButtonListener);
                imageButton.setContentDescription(fileNameList.get(counter));//name image of image button
                counter++;
            }
        //Toast.makeText(this, itemNumber+"", Toast.LENGTH_SHORT).show();
    }
    View.OnClickListener imageButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ++clicks;
            if (clicks==1)
            {
                img1=(ImageButton) v;
                img1.setEnabled(false);
                showImage(img1);
            }
            else if (clicks==2)
            {
                ++CounterOfChoice;
                img2=(ImageButton) v;
                img2.setEnabled(false);
                showImage(img2);
                if (image1.equals(image2))
                {
                    shakeAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    img1.startAnimation(shakeAnimation);
                    img2.startAnimation(shakeAnimation);
                    isolate.add(image1);
                    if (isolate.size()==3)
                    {
                        pauseTimer();
                        soundPool.play(win,1,1,0,0,1);
                        seconds=29-seconds;
                        //Toast.makeText(GameActivityEasy.this, "You Win! And Your Choice Is "+CounterOfChoice, Toast.LENGTH_LONG).show();
                        showDialog("You Win! And Your Choice Is "+CounterOfChoice+" & Time Spend Is : 0:"+seconds);
                    }
                    else
                    {
                        soundPool.play(match,1,1,0,0,1);
                    }
                }
                else
                {
                    soundPool.play(wrong,1,1,0,0,1);
                    disableButtons();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img1.setImageResource(R.drawable.pattern);
                            img2.setImageResource(R.drawable.pattern);
                            enableButtons();
                        }
                    },1000);
                }
                clicks=0;
            }
        }
    };
    private void enableButtons() {
        for (LinearLayout ll:linearLayouts)
            for (int column=0;column<ll.getChildCount();column++)
            {
                ImageButton imageButton=(ImageButton)ll.getChildAt(column);
                boolean flag=true;
                for(int i = 0; i < isolate.size(); ++i)
                {
                    if(isolate.get(i) == imageButton.getContentDescription())
                    {
                        flag=false;
                        break;
                    }
                }
                if (flag)
                    imageButton.setEnabled(true);
            }
    }

    private void disableButtons() {
        for (LinearLayout ll:linearLayouts)
            for (int column=0;column<ll.getChildCount();column++)
            {
                ImageButton imageButton=(ImageButton)ll.getChildAt(column);
                imageButton.setEnabled(false);
            }
    }

    private void showImage(ImageButton view) {
        String fileName= (String) view.getContentDescription();
        if (clicks==1)
            image1=fileName;
        else if (clicks==2)
            image2=fileName;
        try {
            InputStream inputStream=getAssets().open(folderName+"/"+fileName);
            Drawable drawable=Drawable.createFromStream(inputStream,null);
            view.setImageDrawable(drawable);
            inputStream.close();
        }
        catch (IOException e)
        {
            return;
        }
    }
    //dialog exit
    private void showExitDialog() {
        pauseTimer();
        final Dialog dialog = new Dialog(GameActivityEasy.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit);
        dialog.setCancelable(false);

        dialog.show();

        Button exit = dialog.findViewById(R.id.yes_button);
        final Button dismiss = dialog.findViewById(R.id.no_button);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startTimer();
            }
        });
    }

    //dialog play game
    public void showDialog(String sate) {
        pauseTimer();
        final Dialog dialog = new Dialog(GameActivityEasy.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);

        TextView titleText = dialog.findViewById(R.id.title_text);
        dialog.setCancelable(false);
        dialog.show();

        titleText.setText(sate);

        Button homeButton = dialog.findViewById(R.id.home_button);
        Button playAgainButton = dialog.findViewById(R.id.play_again_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                restartActivity(GameActivityEasy.this);
            }
        });
    }
    public static void restartActivity(Activity act){
        act.finish();
        act.startActivity(act.getIntent());
    }
    @Override
    public void onBackPressed() {
        showExitDialog();
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
                disableButtons();
                if (isolate.size()!=3) {
                    soundPool.play(gameOver,1,1,0,0,1);
                    showDialog("You Lose! Time Off");
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
}


