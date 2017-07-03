package com.unknown.notyet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 05.03.2017.
 */

public class CreatureLoader {

    static Drawable creatureDrawable;
    static String Attack1;
    static String Attack2;
    static String Attack3;
    static String Attack4;

    static int AttackDamage1;
    static int AttackDamage2;
    static int AttackDamage3;
    static int AttackDamage4;

    static String AttackOpponent1;
    static String AttackOpponent2;
    static String AttackOpponent3;
    static String AttackOpponent4;

    static Drawable AttackEffect1;
    static Drawable AttackEffect2;
    static Drawable AttackEffect3;
    static Drawable AttackEffect4;
    static MediaPlayer soundEffect;

    static String opponentName;
    static String yourName;

    static int yourHealth;
    static int opponentHealth;

    static int yourLevel;
    static int opponentLevel;

    static File file;
    static StringBuilder text;

    static int attackTimeInMillis;

    static int damage;

    public static void getOpponent(Context context) {
        CreatureGenerator.generateCreature(context);
        opponentName = CreatureGenerator.creatureName;
        creatureDrawable = CreatureGenerator.creatureImage;
        opponentLevel = CreatureGenerator.creatureLevel;
        opponentHealth = 100;

        //// TODO: 10.03.2017 Create attack generator
        AttackOpponent1 = "Recover";
        AttackOpponent2 = "Explosion";
        AttackOpponent3 = "Explosion";
        AttackOpponent4 = "Explosion";

    }

    public static void saveHealth (int creatureNumber) {
        Saver.saveString("Creatures", "Health", creatureNumber, String.valueOf(yourHealth));
    }

    public static void loadAttacks (int creatureNumber) {
        String CreatureLocation = "/UnknownYet/Creature" + String.valueOf(creatureNumber) + "/";

        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "Attack1.txt");

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

        if (file.exists()) {
            Attack1 = text.toString();
        }else {
            Attack1 = null;
        }
        text.setLength(0);



        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "Attack2.txt");

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

        if (file.exists()) {
            Attack2 = text.toString();
        }else {
            Attack2 = null;
        }
        text.setLength(0);



        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "Attack3.txt");

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

        if (file.exists()) {
            Attack3 = text.toString();
        }else {
            Attack3 = null;
        }

        text.setLength(0);



        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "Attack4.txt");

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

        if (file.exists()) {
            Attack4 = text.toString();
        }else {
            Attack4 = null;
        }

        text.setLength(0);



        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "Health.txt");

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

        if (file.exists()) {
            yourHealth = Integer.parseInt(text.toString());
        }else {
            yourHealth = 100;
        }
        text.setLength(0);





        file = new File (Environment.getExternalStorageDirectory(), CreatureLocation + "CreatureLevel.txt");

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

        yourLevel = Integer.parseInt(text.toString());
        text.setLength(0);
    }

    static void loadAttackEffect(Context context, String attackName) {
        Log.e("AttackName:",attackName.toString());
        if (attackName.equals("Explosion")) {
            AttackEffect1 = context.getResources().getDrawable(R.drawable.explosion_egg1);
            AttackEffect2 = context.getResources().getDrawable(R.drawable.explosion_egg2);
            AttackEffect3 = context.getResources().getDrawable(R.drawable.explosion_egg3);
            AttackEffect4 = context.getResources().getDrawable(R.drawable.explosion_egg4);
            soundEffect = MediaPlayer.create(context, R.raw.crack4);
            attackTimeInMillis = 400;
            damage = 50;
        }else if (attackName.equals("Recover")) {
            AttackEffect1 = context.getResources().getDrawable(R.drawable.egg_black);
            AttackEffect2 = context.getResources().getDrawable(R.drawable.egg_blue);
            AttackEffect3 = context.getResources().getDrawable(R.drawable.egg_green);
            AttackEffect4 = context.getResources().getDrawable(R.drawable.egg_orange);
            soundEffect = MediaPlayer.create(context, R.raw.evolve_finish);
            attackTimeInMillis = 1000;
            damage = -100;
        }
    }

    static void calculateAttackDamage (int suspect) {
        int afterHealth;
        int DamageAttack;
        int attackersLevel;
        int writableHealth;
        int suspectLevel;
        int beforeHealth;

        if (damage < 0) {
            if (suspect == 1) {
                beforeHealth = yourHealth * yourLevel * 10;
                attackersLevel = yourLevel;
                suspectLevel = yourLevel;
            }else {
                beforeHealth = opponentHealth * opponentLevel * 10;
                attackersLevel = opponentLevel;
                suspectLevel = opponentLevel;
            }
        }else {
            if (suspect == 1) {
                beforeHealth = yourHealth * yourLevel * 10;
                attackersLevel = opponentLevel;
                suspectLevel = yourLevel;
            }else {
                beforeHealth = opponentHealth * opponentLevel * 10;
                attackersLevel = yourLevel;
                suspectLevel = opponentLevel;
            }
        }

        DamageAttack = damage * attackersLevel;
        afterHealth = beforeHealth - DamageAttack;
        writableHealth = afterHealth / suspectLevel / 10;

        if (suspect == 1) {
            if (writableHealth > 100) {
                writableHealth = 100;
                yourHealth = 100;
            }else if (writableHealth < 0){
                writableHealth = 0;
                yourHealth = 0;

            }else {
                yourHealth = writableHealth;
            }
        }else {
            if (writableHealth > 100) {
                writableHealth = 100;
                opponentHealth = 100;
            }else if (writableHealth < 0){
                writableHealth = 0;
                opponentHealth = 0;
            }else {
                opponentHealth = writableHealth;
            }
        }
    }

    public static int calculateXP() {
        int neededXP;
        int calculator;
        int calculator2;
        int differenceBetween;
        int xpToAdd;
        if (opponentLevel > yourLevel) {
            neededXP = yourLevel*(yourLevel/4)*(yourLevel/10)*10;
            calculator = neededXP / (yourLevel / 2);
            differenceBetween = opponentLevel - yourLevel;
            calculator2 = calculator * (2 * differenceBetween);
            xpToAdd = calculator2 * 5;
        }else if (opponentLevel < yourLevel) {
            neededXP = yourLevel*(yourLevel/4)*(yourLevel/10)*10;
            calculator = neededXP / (yourLevel / 2);
            differenceBetween = yourLevel - opponentLevel;
            calculator2 = calculator / (2 * differenceBetween);
            xpToAdd = calculator2 * 10;
        }else {
            neededXP = yourLevel*(yourLevel/4)*(yourLevel/10)*10;
            calculator = neededXP / (yourLevel / 2);
            xpToAdd = calculator * 15;
        }
        return xpToAdd;
    }
}
