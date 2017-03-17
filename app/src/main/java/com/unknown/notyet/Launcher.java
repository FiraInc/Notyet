package com.unknown.notyet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import java.io.File;

/**
 * Created by Johannett321 on 26.02.2017.
 */

public class Launcher extends Activity {

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
            File file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/InstallComplete.txt");
            if (!file.exists()) {
                askForPermissions();
            }else {
                startHome();
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
                    startHatching();
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

    private void startHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
