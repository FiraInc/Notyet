package com.unknown.notyet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import java.util.Random;

/**
 * Created by Johannett321 on 09.03.2017.
 */

public class CreatureGenerator {

    static Random random;
    static int randomInt;
    static Context mContext;
    static int levelMultiplier = 10;
    static int leveler;
    static int setLevel;

    static String creatureName;
    static String cardType;
    static String creatureType;
    static int creatureLevel;
    static BitmapDrawable creatureEggImage;
    static BitmapDrawable creatureImage;

    static public void generateCreature(Context context) {
        mContext = context;
        random = new Random();

        randomInt = random.nextInt(10000);
        setLevel = levelMultiplier + random.nextInt(10);

        if (setLevel <= 0) {
            setLevel = 7;
        }

        if (randomInt == 3783) {
            creatureName = "StormHawk";
            cardType = "Epic";
            creatureType = "Air";
            creatureLevel = setLevel;
        }else if (randomInt == 6879) {
            creatureName = "Zyrex";
            cardType = "Epic";
            creatureType = "";
            creatureLevel = setLevel;
        }else if (randomInt == 5775) {
            creatureName = "WoralWind";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 8193) {
            creatureName = "VanaRog";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 1188) {
            creatureName = "QuadRoff";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 1388) {
            creatureName = "Wolbra";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 6340) {
            creatureName = "WylperRid";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 3894) {
            creatureName = "Alaster";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 5346) {
            creatureName = "Phuron";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 1516) {
            creatureName = "Tryth";
            cardType = "Epic";
            creatureLevel = setLevel;
        }else if (randomInt == 2661) {
            creatureName = "Glacia";
            cardType = "Diamond";
            creatureLevel = setLevel;
        }else if (randomInt <= 200) {
            creatureName = "Peron";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 7500 && randomInt <= 7800) {
            creatureName = "AirBlub";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 4893 && randomInt <= 5000) {
            creatureName = "FlopRog";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 9324 && randomInt <= 9437) {
            creatureName = "DuffEra";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 4943 && randomInt <= 4500) {
            creatureName = "Vysray";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 1262 && randomInt <= 1456) {
            creatureName = "Strangis";
            cardType = "Shiny";
            creatureLevel = setLevel;
        }else if (randomInt >= 0 && randomInt <= 500) {
            creatureName = "StarBy";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 500 && randomInt <= 1000) {
            creatureName = "Cheerlent";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 1000 && randomInt <= 2000) {
            creatureName = "StankBud";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 2000 && randomInt <= 2500) {
            creatureName = "Rankward";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 2500 && randomInt <= 3500) {
            creatureName = "Triball";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 3500 && randomInt <= 4000) {
            creatureName = "Mushvil";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 4000 && randomInt <= 4500) {
            creatureName = "Burstray";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 4500 && randomInt <= 4850) {
            creatureName = "Dialphador";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 4850 && randomInt <= 5200) {
            creatureName = "Rambell";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 5200 && randomInt <= 6000) {
            creatureName = "Bellris";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 6500 && randomInt <= 7000) {
            creatureName = "Angild";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 7000 && randomInt <= 7500) {
            creatureName = "Rudnor";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 7500 && randomInt <= 8000) {
            creatureName = "Toblon";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 8000 && randomInt <= 8499) {
            creatureName = "LodVar";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt == 8500) {
            creatureName = "Puffy";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 8500 && randomInt <= 9500) {
            creatureName = "NexZyf";
            cardType = "Normal";
            creatureLevel = setLevel;
        }else if (randomInt >= 9500 && randomInt <= 10000) {
            creatureName = "Rockfall";
            cardType = "Normal";
            creatureLevel = setLevel;
        }

        if (creatureImage == null) {
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.egg_black);
            creatureImage = new BitmapDrawable(mContext.getResources(), bm);
        }

        if (creatureEggImage == null) {
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.egg_black);
            creatureEggImage = new BitmapDrawable(mContext.getResources(), bm);
        }
    }
}
