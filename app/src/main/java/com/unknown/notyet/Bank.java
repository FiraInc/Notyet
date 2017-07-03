package com.unknown.notyet;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 19.03.2017.
 */

public class Bank {

    static File file;
    static StringBuilder text;

    private static void createBankFolder(Context context) {
        file = new File(context.getFilesDir(), "Bank");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static int getCurrentCoins(Context context) {
        int currentCoins;

        createBankFolder(context);

        file = new File(context.getFilesDir(), "Bank/" + "currentCoins.txt");
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

        if (text.toString().isEmpty()) {
            currentCoins = 0;
        }else {
            currentCoins = Integer.parseInt(text.toString());
        }
        text.setLength(0);
        return currentCoins;
    }

    public static int getCurrentDiamonds(Context context) {
        int currentDiamonds;

        createBankFolder(context);

        file = new File(context.getFilesDir(), "Bank/" + "currentDiamonds.txt");
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

        if (text.toString().isEmpty()) {
            currentDiamonds = 0;
        }else {
            currentDiamonds = Integer.parseInt(text.toString());
        }
        text.setLength(0);
        return currentDiamonds;
    }

    public static void addCoins(Context context, int coinsToAdd) {
        int TotalMoney;
        createBankFolder(context);


        if (getCurrentCoins(context) == 0) {
            TotalMoney = coinsToAdd;
        }else {
            TotalMoney = getCurrentCoins(context) + coinsToAdd;
        }


        Saver.saveStringWithLocation(context, "Bank/", "currentCoins.txt", String.valueOf(TotalMoney));

        Toast.makeText(context,"Current is: " + String.valueOf(TotalMoney), Toast.LENGTH_LONG).show();
        text.setLength(0);
    }


    public static void addDiamonds(Context context, int diamondsToAdd) {
        int TotalDiamonds;
        createBankFolder(context);

        if (getCurrentDiamonds(context) == 0) {
            TotalDiamonds = diamondsToAdd;
        }else {
            TotalDiamonds = getCurrentDiamonds(context) + diamondsToAdd;
        }


        Saver.saveStringWithLocation(context, "Bank/", "currentDiamonds.txt", String.valueOf(TotalDiamonds));

        Toast.makeText(context,"Current is: " + String.valueOf(TotalDiamonds), Toast.LENGTH_LONG).show();
        text.setLength(0);
    }


    public static void diamondsToCoinsConverter(Context context, int diamonds) {
        int outputCoins;
        if (diamonds <= getCurrentDiamonds(context)) {
            outputCoins = diamonds* 75;
            addDiamonds(context, -diamonds);
            addCoins(context, outputCoins);
        }else {
            //todo play error sound
        }
    }

}
