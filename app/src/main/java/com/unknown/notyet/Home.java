package com.unknown.notyet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    static int currentCreature = 1;
    int creatureLevel;

    String creatureName;

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

        InventoryAdapter.creatureNumberArray = new ArrayList<>();

        findViews();
        creatureSetParams();
        //blackScreen.setVisibility(View.VISIBLE);
        //removeBlackScreen();

        getCreatureDirectory();

        loadCreature();

        music = MediaPlayer.create(this, R.raw.home_music);
        music.setLooping(true);
        music.start();
    }

    private void getCreatureDirectory() {
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
        try {
            String externalStorage = Environment.getExternalStorageDirectory().getPath();
            Bitmap creatureBitmap = BitmapFactory.decodeFile(externalStorage + creatureDirectory + "CreatureImage" + ".png");
            creatureImage.setImageBitmap(creatureBitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }


        //setName

        file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "CreatureCustomName" + ".txt");
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

        creatureName = text.toString();
        CreatureNameTextView.setText(creatureName);

        text.setLength(0);



        //setLevel
        file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "CreatureLevel" + ".txt");
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

        creatureLevel = Integer.parseInt(text.toString());
        CreatureLevelTextView.setText(String.valueOf(creatureLevel));
        text.setLength(0);
    }

    private void removeBlackScreen() {
        blackScreen.animate().setDuration(2000).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blackScreen.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void levelUP(View view) {
        Leveling.levelUp(this, currentCreature, 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDirectory();
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
}
