package com.unknown.notyet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Johannett321 on 20.03.2017.
 */

public class CurrentCreatureInfo {
    static int health = 100;
    static int foodStatus = 100;
    static int level = 10;

    static String customName = "Error";
    static String name = "Error";

    static Bitmap creatureBitmap;


    static File file;
    static StringBuilder text;

    static int CreatureFolderNumber;


    static void getAllCreatureInfo(Context context, int creatureFolderNumber) {
        CreatureFolderNumber = creatureFolderNumber;
        getFoodInfo(creatureFolderNumber);
        getHealthInfo(creatureFolderNumber);
        getLevel(creatureFolderNumber);
        getName(creatureFolderNumber);
        getCustomName(creatureFolderNumber);
        getCreatureImage(context, creatureFolderNumber);
    }

    static void saveValues(int creatureFolderNumber) {
        Saver.saveString("Creatures", "Health", creatureFolderNumber, String.valueOf(health));
        Saver.saveString("Creatures", "FoodStatus", creatureFolderNumber, String.valueOf(foodStatus));
    }

    static void fillFoodHealth(int creatureFolderNumber) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        foodStatus = 100;
        health = 100;

        Saver.saveString("Creatures", "FoodDay", creatureFolderNumber, String.valueOf(day));
        Saver.saveString("Creatures", "FoodHour", creatureFolderNumber, String.valueOf(hour));

        saveValues(CurrentCreatureInfo.CreatureFolderNumber);
    }

    static void getCreatureImage(Context context, int creatureFolderNumber) {
        try {
            creatureBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/UnknownYet/Creature" + String.valueOf(creatureFolderNumber) + "/CreatureImage" + ".png");
        }catch (Exception e) {
            creatureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg_black);
        }

    }

    static void getFoodInfo(int creatureFolderNumber) {
        String creatureDirectory = "/UnknownYet/Creature" + String.valueOf(creatureFolderNumber) + "/";
        file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "FoodHour" + ".txt");
        if (file.exists()) {
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
            int foodHour = Integer.parseInt(text.toString());
            text.setLength(0);


            file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "FoodDay" + ".txt");
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
            int foodDay = Integer.parseInt(text.toString());
            text.setLength(0);


            file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + "FoodStatus" + ".txt");
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
            int statusFood = Integer.parseInt(text.toString());
            text.setLength(0);


            Calendar c = Calendar.getInstance();
            int nowDayOfYear = c.get(Calendar.DAY_OF_YEAR);
            int nowHourOfDay = c.get(Calendar.HOUR_OF_DAY);

            int Y;
            int X;

            if (nowDayOfYear > foodDay) {
                X = nowDayOfYear - foodDay;
                X = 24*X;
                Y = (X-foodHour)+nowHourOfDay;
                statusFood = statusFood - (Y *2);
                foodStatus = statusFood;
            }else if (nowDayOfYear == foodDay) {
                if (nowHourOfDay < foodHour) {
                    foodStatus = statusFood;
                }else {
                    Y = nowHourOfDay - foodHour;
                    statusFood = statusFood - (Y *2);
                    foodStatus = statusFood;
                }
            }else if (nowDayOfYear < foodDay) {
                X = 365-foodDay;
                X = X + nowDayOfYear;
                X = 24*X;
                Y = (X-foodHour)+nowHourOfDay;
                statusFood = statusFood - (Y*2);
                foodStatus = statusFood;

            }

            if (CurrentCreatureInfo.foodStatus <= 0) {
                foodStatus = 0;
            }

            Log.e("Foodstatus:", String.valueOf(creatureFolderNumber) + ":" + String.valueOf(foodStatus));
        }else {
            foodStatus = 100;
            Log.e("Foodstatus:", String.valueOf(foodStatus));
        }
    }

    static void getHealthInfo(int creatureFolderNumber) {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(creatureFolderNumber) + "/Health.txt");
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
            health = Integer.parseInt(text.toString());
        }else {
            health = 0;
            Saver.saveString("Creatures", "Health", creatureFolderNumber, String.valueOf(health));
        }
    }

    static void getLevel(int creatureFolderNumber) {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + creatureFolderNumber + "/CreatureLevel" + ".txt");
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
            level = Integer.parseInt(text.toString());
        }else {
            level = 10;
        }
    }

    static void getCustomName(int creatureFolderNumber) {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + creatureFolderNumber + "/CreatureCustomName" + ".txt");
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
            customName = text.toString();
        }else {
            customName = name;
        }
    }

    static void getName(int creatureFolderNumber) {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + creatureFolderNumber + "/CreatureName" + ".txt");
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
            name = text.toString();
        }else {
            name = "Error";
        }
    }

}
