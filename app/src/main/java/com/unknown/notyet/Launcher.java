package com.unknown.notyet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 26.02.2017.
 */

public class Launcher extends Activity {

    File file;
    StringBuilder text;
    String creatureDirectory;
    int currentCreature = 1;

    static boolean firstLaunchTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInstallation();
            }
        }, 400);
    }

    private void checkInstallation() {
        if (Environment.getExternalStorageDirectory().canWrite()) {
            file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/InstallComplete.txt");
            if (!file.exists()) {
                askForPermissions();
            }else {
                startLaunchProcess();
            }
        }else {
            askForPermissions();
        }

    }

    private void askForPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void runInstall() {
        File file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/");
        if (!file.exists()) {
            file.mkdirs();
        }

        startHatching();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runInstall();
                } else {
                    askForPermissions();
                }
                return;
            }
        }
    }

    private void startHatching() {
        Intent intent = new Intent(this, HatchEgg.class);
        startActivity(intent);
        finish();
    }

    private void startLaunchProcess() {
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
            CurrentCreatureInfo.getAllCreatureInfo(this, currentCreature);
            if (CurrentCreatureInfo.foodStatus > 0 && CurrentCreatureInfo.health > 0) {
                startHome();
            }else {
                startDeadScreen();
            }
        }else {
            currentCreature = 1;
            getCreatureDirectory();
        }
    }

    private void startDeadScreen() {
        Intent intent = new Intent(this, AllDead.class);
        startActivity(intent);
        finish();
    }

    public void getCreatureDirectory() {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(currentCreature) + "/");
        if (file.exists()) {
            CurrentCreatureInfo.getAllCreatureInfo(this, currentCreature);
            Saver.saveStringWithLocation(this, "/UnknownYet/", "defaultCreature.txt", String.valueOf(currentCreature));
            if (CurrentCreatureInfo.health > 0 && CurrentCreatureInfo.foodStatus > 0) {
                startHome();
            }else {
                startDeadScreen();
            }
        }else {
            startHatching();
        }


    }

    private void startHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
