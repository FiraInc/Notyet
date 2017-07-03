package com.unknown.notyet;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import static com.unknown.notyet.Saver.creatureDirectory;

/**
 * Created by Johannett321 on 02.03.2017.
 */

public class Leveling {

    static File file;
    static StringBuilder text;
    static int currentLevel;
    static int currentXP;
    static int XPnew;
    static int XPNeeded = 100;
    static int currentNumerCreature;
    static int startedLevel;
    static Context mContext;
    static Boolean didLevelUp;

    public static void levelUp(Context context, int currentCreatureNumber, int newXP) {

        mContext = context;
        currentNumerCreature = currentCreatureNumber;
        getCreatureDirectory();

        //get level

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

        currentLevel = Integer.parseInt(text.toString());
        startedLevel = currentLevel;
        text.setLength(0);

        //get XP

        file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "CreatureXP" + ".txt");
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

        currentXP = Integer.parseInt(text.toString());
        text.setLength(0);


        XPNeeded = currentLevel*(currentLevel/6)*(currentLevel/10)*10;
        text.setLength(0);

        XPnew = newXP;
        currentXP = currentXP + XPnew;
        runLeveler();
    }

    public static void runLeveler() {
        if (currentXP >= XPNeeded) {
            currentXP = currentXP - XPNeeded;
            currentLevel = currentLevel + 1;
            XPNeeded = currentLevel*(currentLevel/4)*(currentLevel/10)*10;
            runLeveler();
        }else {
            saveNewXPAndLevel();
            checkIfLeveled();
        }
    }

    private static void checkIfLeveled() {
        if (startedLevel < currentLevel) {
            didLevelUp = true;
            Intent intent = new Intent(mContext, LeveledUpScreen.class);
            mContext.startActivity(intent);
        }else {
            didLevelUp = false;
        }
    }

    private static void saveNewXPAndLevel() {
        Saver.saveString("Creatures", "CreatureLevel", currentNumerCreature, String.valueOf(currentLevel));
        Saver.saveString("Creatures", "CreatureXP", currentNumerCreature, String.valueOf(currentXP));
        Saver.saveString("Creatures", "CreatureXPNeeded", currentNumerCreature, String.valueOf(XPNeeded));
    }

    private static void getCreatureDirectory() {
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(currentNumerCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    static void feed(int creatureFolderNumber, String foodName) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DAY_OF_YEAR);

        int value = 0;


        if (foodName.equals("Bread")) {
            value = 20;
        }
        CurrentCreatureInfo.foodStatus = CurrentCreatureInfo.foodStatus + value;
        if (CurrentCreatureInfo.foodStatus > 100) {
            CurrentCreatureInfo.foodStatus = 100;
        }
        Saver.saveString("Creatures", "FoodHour", creatureFolderNumber, String.valueOf(hour));
        Saver.saveString("Creatures", "FoodDay", creatureFolderNumber, String.valueOf(day));
        CurrentCreatureInfo.saveValues(creatureFolderNumber);
    }
}
