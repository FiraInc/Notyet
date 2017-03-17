package com.unknown.notyet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_VERTICAL;

public class HatchEgg extends Activity {

    MediaPlayer mp;
    ImageView blackScreen;
    TextView subtitles;
    ImageView blackEgg;
    ImageView customEgg;
    TextView tapMeText;
    ImageView thinkBubble;
    RelativeLayout eggHatcher;
    ImageView hatchStatus;
    Boolean explosion1 = true;
    Boolean explosion2 = false;
    Boolean explosion3 = false;
    Boolean explosion4 = false;
    Boolean explosion5 = false;

    RelativeLayout.LayoutParams layoutParamsSmall;
    RelativeLayout.LayoutParams layoutParamsBig;

    Boolean eggClickEnabled = false;

    int eggClickCounter = 0;
    int newCreatureNumber = 1;

    ImageView pGif;

    Boolean stopHandler = false;

    Bitmap bm;

    String creatureName = "Unknown";
    String currentLevel = "";
    Calendar calendar;
    int currentMil;

    static int askDialogYesNoRequestCode = 234789;
    static int askDialogYesNoResultCode = 12373489;
    static int NAME_REQUEST_CODE = 349058;
    static int ARE_YOU_SURE_SKIP = 38974;

    String creatureDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("CreatureDirectory" , String.valueOf(newCreatureNumber) + "Before_findViews");

        findViews();

        mp = MediaPlayer.create(this, R.raw.pokemon_music);
        mp.start();

        subtitles.setText("Oh!");

        calendar = Calendar.getInstance();
        currentMil = calendar.get(calendar.MILLISECOND);

        getParams();
        loadNewCreatureNumber();
        getCreatureDirectory();
        generateCreature();
        setVisibilities();

        File imgFile = new  File(Environment.getExternalStorageDirectory(), creatureDirectory + "eggCreature" + ".png");

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        customEgg.setImageBitmap(myBitmap);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeAwayBlackScreen();
            }
        }, 2000);
    }

    private void loadNewCreatureNumber() {
        File file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/" + "Creature" + String.valueOf(newCreatureNumber) + "/");
        if (file.exists()) {
            newCreatureNumber = newCreatureNumber + 1;
            loadNewCreatureNumber();
        }
    }

    private void getCreatureDirectory() {
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(newCreatureNumber) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void setVisibilities() {
        customEgg.setVisibility(View.INVISIBLE);
        tapMeText.setVisibility(View.INVISIBLE);
        thinkBubble.setVisibility(View.INVISIBLE);
        hatchStatus.setVisibility(View.INVISIBLE);
        pGif.setVisibility(View.INVISIBLE);
    }

    private void findViews() {
        eggHatcher = (RelativeLayout) findViewById(R.id.eggHatcher);

        blackScreen = (ImageView) findViewById(R.id.blackScreen);
        blackEgg = (ImageView) findViewById(R.id.blackEgg);
        customEgg = (ImageView) findViewById(R.id.customEgg);
        thinkBubble = (ImageView) findViewById(R.id.thinkBubble);
        hatchStatus = (ImageView) findViewById(R.id.hatchStatus);

        subtitles = (TextView) findViewById(R.id.subtitles);
        tapMeText = (TextView) findViewById(R.id.tapMeText);

        pGif = (ImageView) findViewById(R.id.explosionEffect);
    }

    public void fadeAwayBlackScreen () {
        blackScreen.animate().setDuration(2000).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                blackScreen.setVisibility(View.GONE);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeEgg();
                changeText();
            }
        }, 4000);

    }

    private void fadeEgg() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        customEgg.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                customEgg.setVisibility(View.VISIBLE);
                blackEgg.setVisibility(View.GONE);
                eggClickEnabled = true;
                fadeTapMe();

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void changeText() {
        subtitles.setText("What's this?");
    }

    private void fadeTapMe() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_short);
        tapMeText.startAnimation(fadeIn);
        thinkBubble.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tapMeText.setVisibility(View.VISIBLE);
                thinkBubble.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void getParams() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        layoutParamsBig = new RelativeLayout.LayoutParams(size.x /2 + 50, size.x /2 + size.x /3 + 50);
        layoutParamsBig.addRule(CENTER_HORIZONTAL);
        layoutParamsBig.addRule(CENTER_VERTICAL);

        layoutParamsSmall = new RelativeLayout.LayoutParams(size.x /2, size.x /2 + size.x /3);
        layoutParamsSmall.addRule(CENTER_HORIZONTAL);
        layoutParamsSmall.addRule(CENTER_VERTICAL);
    }


    public void eggClicked(View view) {
        Log.e("ClickCounter", String.valueOf(eggClickCounter));
        if (eggClickEnabled) {
            if (eggClickCounter >= 16) {
                startHatch();
            }else if (eggClickCounter >= 12) {
                hatchStatus.setImageDrawable(getResources().getDrawable(R.drawable.damage_egg4));
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.crack1);
                mp2.start();
            }else if (eggClickCounter >= 8) {
                hatchStatus.setImageDrawable(getResources().getDrawable(R.drawable.damage_egg3));
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.crack3);
                mp2.start();
            }else if (eggClickCounter >= 4) {
                hatchStatus.setImageDrawable(getResources().getDrawable(R.drawable.damage_egg2));
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.crack2);
                mp2.start();
            }else if (eggClickCounter < 4) {
                hatchStatus.setVisibility(View.VISIBLE);
                hatchStatus.setImageDrawable(getResources().getDrawable(R.drawable.damage_egg1));
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.crack1);
                mp2.start();
            }
            eggClickCounter ++;
            customEgg.setLayoutParams(layoutParamsBig);
            hatchStatus.setLayoutParams(layoutParamsBig);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setEggNormalSize();
                }
            }, 200);
        }
    }

    public void setEggNormalSize () {
        customEgg.setLayoutParams(layoutParamsSmall);
        hatchStatus.setLayoutParams(layoutParamsSmall);
    }

    @Override
    protected void onPause() {
        mp.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mp.start();
        super.onResume();
    }

    private void startHatch() {
        if (explosion1) {
            eggClickEnabled = false;
            mp.stop();
            mp = MediaPlayer.create(this, R.raw.crack4);
            mp.start();
            pGif.setVisibility(View.VISIBLE);
            customEgg.setVisibility(View.INVISIBLE);
            blackEgg.setVisibility(View.INVISIBLE);
            hatchStatus.setVisibility(View.INVISIBLE);
            tapMeText.setVisibility(View.INVISIBLE);
            thinkBubble.setVisibility(View.INVISIBLE);
            pGif.setBackground(getResources().getDrawable(R.drawable.explosion_egg1));
            explosion1 = false;
            explosion2 = true;
        }else if (explosion2) {
            pGif.setBackground(getResources().getDrawable(R.drawable.explosion_egg2));
            explosion2 = false;
            explosion3 = true;
        } else if (explosion3) {
            pGif.setBackground(getResources().getDrawable(R.drawable.explosion_egg3));
            explosion3 = false;
            explosion4 = true;
        } else if (explosion4) {
            pGif.setBackground(getResources().getDrawable(R.drawable.explosion_egg4));
            explosion4 = false;
            explosion5 = true;
        }else if (explosion5) {
            mp = MediaPlayer.create(this, R.raw.trading_music);
            mp.start();
            pGif.setBackground(getResources().getDrawable(android.R.color.black));
            explosion5 = false;
            stopHandler = true;
            customEgg.setVisibility(View.VISIBLE);
            File imgFile = new  File(Environment.getExternalStorageDirectory(), creatureDirectory + "CreatureImage" + ".png");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            customEgg.setImageBitmap(myBitmap);
            subtitles.setText("");
        }

        if (!stopHandler) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startHatch();
                }
            }, 50);
        }else {
            fadeAwayGif();
        }
    }

    private void fadeAwayGif() {
        subtitles.setText("You just got a " + creatureName + " (Level " + String.valueOf(currentLevel) + ")");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pGif.animate().setDuration(2000).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mp.stop();
                        MediaPlayer mp3 = MediaPlayer.create(HatchEgg.this, R.raw.evolve_finish);
                        mp3.start();
                        pGif.setVisibility(View.GONE);
                        fadeToBlackBeforeNameEnter();
                    }
                });
            }
        }, 2000);
    }

    private void fadeToBlackBeforeNameEnter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeToBlackBeforeNameEnter2();
            }
        }, 5000);
    }

    private void fadeToBlackBeforeNameEnter2() {
        blackScreen.setVisibility(View.VISIBLE);
        blackScreen.animate().setDuration(2000).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogYesNo.title = "Would you like to set a name?";
                        DialogYesNo.positiveButtonTitle = "Yes";
                        DialogYesNo.negativeButtonTitle = "No";
                        Intent intent = new Intent(HatchEgg.this, DialogYesNo.class);
                        startActivityForResult(intent, askDialogYesNoRequestCode);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == askDialogYesNoRequestCode) {
            String result = data.getStringExtra("result");
            if (result.equals("1")) {
                DialogInputSaveCancel.negativeButtonTitle = "Cancel";
                DialogInputSaveCancel.positiveButtonTitle = "Save";
                DialogInputSaveCancel.title = "Input name";
                Intent intent = new Intent(this, DialogInputSaveCancel.class);
                startActivityForResult(intent, NAME_REQUEST_CODE);
            }else {
                DialogYesNo.title = "Are you sure";
                DialogYesNo.positiveButtonTitle = "Yes";
                DialogYesNo.negativeButtonTitle = "No";
                Intent intent = new Intent(this, DialogYesNo.class);
                startActivityForResult(intent, ARE_YOU_SURE_SKIP);
            }
        }else if (requestCode == NAME_REQUEST_CODE) {
            String result = data.getStringExtra("result");
            if (!result.equals("NONAME")) {
                Saver.saveString("CreatureCustomName", newCreatureNumber, result);
            }else {
                Saver.saveString("CreatureCustomName", newCreatureNumber, creatureName);
            }
            Saver.saveString("InstallComplete", 0, "1");
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }else if (requestCode == ARE_YOU_SURE_SKIP) {
            String result = data.getStringExtra("result");
            if (result.equals("1")) {
                Saver.saveString("CreatureCustomName", newCreatureNumber, creatureName);
                Saver.saveString("InstallComplete", 0, "1");
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
                //// TODO: 26.02.2017 start HomeScreen
            }else {
                DialogYesNo.title = "Would you like to set a name?";
                DialogYesNo.positiveButtonTitle = "Yes";
                DialogYesNo.negativeButtonTitle = "No";
                Intent intent = new Intent(HatchEgg.this, DialogYesNo.class);
                startActivityForResult(intent, askDialogYesNoRequestCode);
            }

        }

    }

    private void generateCreature() {
        Log.e("CURRENTMIL", String.valueOf(currentMil + 1));

        CreatureGenerator.generateCreature(this);

        bm = CreatureGenerator.creatureEggImage.getBitmap();
        Saver.runSave("eggCreature", newCreatureNumber, bm);
        bm = CreatureGenerator.creatureImage.getBitmap();
        Saver.runSave("CreatureImage", newCreatureNumber, bm);
        creatureName = CreatureGenerator.creatureName;
        Saver.saveString("CreatureName", newCreatureNumber, creatureName);

        saveLevelCreature();
    }

    private void saveLevelCreature() {
        currentLevel = "";
        if (currentMil >= 950) {
            currentLevel = "10";
            Saver.saveString("CreatureXPNeeded", newCreatureNumber, "100");
        }else if (currentMil >= 500) {
            currentLevel = "6";
            Saver.saveString("CreatureXPNeeded", newCreatureNumber, "60");
        }else if (currentMil >= 300) {
            currentLevel = "7";
            Saver.saveString("CreatureXPNeeded", newCreatureNumber, "70");
        }else if (currentMil <= 300) {
            currentLevel = "4";
            Saver.saveString("CreatureXPNeeded", newCreatureNumber, "20");
        }else {
            currentLevel = "5";
            Saver.saveString("CreatureXPNeeded", newCreatureNumber, "25");
        }

        Saver.saveString("Health", newCreatureNumber, "100");;
        Saver.saveString("CreatureXP", newCreatureNumber, "0");
        Saver.saveString("CreatureLevel", newCreatureNumber, currentLevel);
        CreatureGenerator.creatureLevel = Integer.valueOf(currentLevel);
    }

}
