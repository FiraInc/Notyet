package com.unknown.notyet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class CreatureInfo extends Activity {

    static int currentCreature;

    String CreatureFolder;

    ImageView CreatureImage;
    TextView CreatureName;
    TextView AliveButton;
    ImageView AliveButtonImage;

    String creatureName;
    String aliveButtonText;
    Boolean isItAlive;

    File file;
    StringBuilder text;

    ProgressBar progressBar;

    static boolean inBattle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creature_info);

        findViews();
        progressBar.setVisibility(View.VISIBLE);
        if (inBattle) {
            AliveButton.setVisibility(View.GONE);
            AliveButtonImage.setVisibility(View.GONE);
        }
        loadFolder();


    }

    private void loadFolder() {
        CreatureFolder = "/UnknownYet/Creature" + String.valueOf(currentCreature) + "/";
    }

    private void loadInfo() {
        file = new File(Environment.getExternalStorageDirectory(), CreatureFolder + "CreatureCustomName.txt");
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
        CreatureName.setText(creatureName);
        text.setLength(0);


        file = new File(Environment.getExternalStorageDirectory(), CreatureFolder + "CreatureAliveStatus.txt");
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
            isItAlive = true;
            aliveButtonText = "Transfer to card";
            AliveButton.setText(aliveButtonText);
        }else {
            isItAlive = false;
            aliveButtonText = "Transfer from card";
            AliveButton.setText(aliveButtonText);
        }
        text.setLength(0);

        try {
            String externalStorage = Environment.getExternalStorageDirectory().getPath();
            Bitmap creatureBitmap = BitmapFactory.decodeFile(externalStorage + CreatureFolder + "CreatureImage.png");
            CreatureImage.setImageBitmap(creatureBitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void findViews() {
        CreatureImage = (ImageView) findViewById(R.id.creatureImage);
        CreatureName = (TextView) findViewById(R.id.CreatureName);
        AliveButton = (TextView) findViewById(R.id.AliveText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        AliveButtonImage = (ImageView) findViewById(R.id.AliveButton);
    }

    public void AliveSwitch (View view) {
        if (currentCreature == 1) {
            Toast.makeText(this, "You cannot transfer you main creature to a card", Toast.LENGTH_LONG).show();
        }else {
            if (isItAlive) {
                Saver.saveString("CreatureAliveStatus", currentCreature, "false");
                AliveButton.setText("Transfer from card");
                isItAlive = false;
                Inventory.currentItem = 1;
                Inventory.adapter.clear();
            }else {
                Saver.saveString("CreatureAliveStatus", currentCreature, "true");
                AliveButton.setText("Transfer to card");
                isItAlive = true;
                Inventory.currentItem = 1;
                Inventory.adapter.clear();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadInfo();
            }
        }, 500);
    }
}
