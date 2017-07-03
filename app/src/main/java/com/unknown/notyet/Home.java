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
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_VERTICAL;

/**
 * Created by Johannett321 on 26.02.2017.
 */

public class Home extends Activity {

    ImageView blackScreen;
    ImageView creatureImage;

    TextView CreatureNameTextView;
    TextView CreatureLevelTextView;
    TextView foodStatus;

    static int currentCreature = 1;

    RelativeLayout.LayoutParams params;

    StringBuilder text;
    File file;

    String creatureDirectory;

    static MediaPlayer music;
    MediaPlayer soundFX;

    static Boolean musicStartOver = false;
    static Boolean doNotStopMusic = false;
    static Boolean doNotStartMusic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        CreatureInventoryAdapter.creatureNumberArray = new ArrayList<>();

        findViews();
        creatureSetParams();
        removeBlackScreen();

        getCreatureDirectory();

        loadCreature();

        music = MediaPlayer.create(this, R.raw.home_music);
        music.setLooping(true);
        music.start();
    }

    private void getCreatureDirectory() {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(currentCreature) + "/CreatureAliveStatus.txt");
        text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (text.toString().equals("true")) {
            text.setLength(0);
            file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(currentCreature) + "/Health.txt");
            text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close() ;
            }catch (IOException e) {
                e.printStackTrace();
            }

            if (Integer.parseInt(text.toString()) < 0) {
                //todo handle Death
            }
        }


        if (currentCreature > 0) {
            setDirecoryFromInventory();
        }else {
            getDirectory();
        }
    }

    private void setDirecoryFromInventory() {
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(currentCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            getDirectory();
        }
    }

    private void getDirectory() {
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(currentCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            currentCreature = currentCreature +1;
            getDirectory();
        }
    }

    private void findViews() {
        blackScreen = (ImageView) findViewById(R.id.blackScreen);
        creatureImage = (ImageView) findViewById(R.id.creatureImage);
        CreatureNameTextView = (TextView) findViewById(R.id.CreatureName);
        CreatureLevelTextView = (TextView) findViewById(R.id.level);
        foodStatus = (TextView) findViewById(R.id.foodStatus);
    }

    private void creatureSetParams() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params = new RelativeLayout.LayoutParams(size.x /2, size.x /2 + size.x /3);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_VERTICAL);
        creatureImage.setLayoutParams(params);

    }

    private void loadCreature() {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/defaultCreature.txt");
        text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }

        if (!text.toString().isEmpty()) {
            currentCreature = Integer.parseInt(text.toString());
        }else {
            currentCreature = 1;
        }

        CurrentCreatureInfo.getAllCreatureInfo(this, currentCreature);

        creatureImage.setImageBitmap(CurrentCreatureInfo.creatureBitmap);

        CreatureNameTextView.setText(CurrentCreatureInfo.customName);

        CreatureLevelTextView.setText(String.valueOf(CurrentCreatureInfo.level));

        foodStatus.setText(String.valueOf(CurrentCreatureInfo.foodStatus));
    }

    private void removeBlackScreen() {
        if (Launcher.firstLaunchTime) {
            blackScreen.setVisibility(View.VISIBLE);

            blackScreen.animate().setDuration(2000).alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    blackScreen.setVisibility(View.INVISIBLE);
                    Launcher.firstLaunchTime = false;
                }
            });
        }else {
            blackScreen.setVisibility(View.INVISIBLE);
        }
    }

    public void levelUP(View view) {
        //Leveling.levelUp(this, currentCreature, 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCreature();
        if(musicStartOver) {
            music = MediaPlayer.create(this, R.raw.home_music);
            musicStartOver = false;
        }
        music.start();
    }

    public void startBattle(View view) {
        musicStartOver = true;
        Intent intent = new Intent(this, Battle.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!doNotStopMusic) {
            music.pause();
        }else {

        }

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        Saver.saveString("Creatures", "FoodHour", currentCreature, String.valueOf(hour));
        Saver.saveString("Creatures", "FoodDay", currentCreature, String.valueOf(day));
        Saver.saveString("Creatures", "FoodStatus", currentCreature, String.valueOf(CurrentCreatureInfo.foodStatus));
    }

    public void inventoryShopClicked(View view) {
        soundFX = MediaPlayer.create(this, R.raw.crack4);
        soundFX.start();
        doNotStopMusic = true;
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
    }

    public void openSearcher(View view) {
        doNotStopMusic = true;
        Intent intent = new Intent(this, Searcher.class);
        startActivity(intent);
        overridePendingTransition(R.animator.pull_in_top_slow, R.animator.push_out_bottom_slow);
    }

    public void creatureInventoryClicked(View view) {
        soundFX = MediaPlayer.create(this, R.raw.crack4);
        soundFX.start();
        doNotStopMusic = true;
        CreatureInventory.inBattle = false;
        Intent intent = new Intent(this, CreatureInventory.class);
        startActivity(intent);
    }
}
