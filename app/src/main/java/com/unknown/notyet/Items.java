package com.unknown.notyet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 18.03.2017.
 */

public class Items {
    static String itemNameToBeAdded;
    static int itemPrice;
    static int itemAmountToBeAdded;
    static int itemAmountBuy;
    static BitmapDrawable bitmapToBeAdded;
    static String itemDescription;
    static String itemCategory;


    public static void getItem (Context mContext, String item) {
        if (item.equals("Bread")) {
            itemNameToBeAdded = item;
            itemPrice = 350;
            itemAmountToBeAdded = loadAmount(item);
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.egg_black);
            bitmapToBeAdded = new BitmapDrawable(mContext.getResources(), bm);
            itemCategory = "Food";
            itemDescription = "Well, it's not much";
            itemAmountBuy = 1;

            Log.e("YOUJSB", itemNameToBeAdded);
        }else if (item.equals("Egg")) {
            itemNameToBeAdded = item;
            itemPrice = 7500;
            itemAmountToBeAdded = loadAmount(item);
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.egg_black);
            bitmapToBeAdded = new BitmapDrawable(mContext.getResources(), bm);
            itemCategory = "Food";
            itemDescription = "Hatch a new egg";
            itemAmountBuy = 1;
        }
    }

    public static int loadAmount(String item) {
        File file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Items/" + String.valueOf(item) + "/ItemAmount" + ".txt");
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!text.toString().isEmpty()) {
                return Integer.parseInt(text.toString());
            }else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    public static void itemSpecial(Context context, String itemName) {
        if (itemName.equals("Egg")) {
            Intent intent = new Intent(context, HatchEgg.class);
            context.startActivity(intent);
        }

    }
}
