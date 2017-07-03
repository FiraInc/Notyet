package com.unknown.notyet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Johannett321 on 25.02.2017.
 */

public class Saver {

    static int numberCreature;
    static String creatureDirectory;

    public static void runSave(String fileName, int fileNumber, Bitmap bm) {
        numberCreature = fileNumber;
        getCreatureDirectory();
        Log.e("CreatureDirectory is", creatureDirectory);
        File dir = new File(Environment.getExternalStorageDirectory(), creatureDirectory);

        boolean doSave = true;
        if (!dir.exists()) {
            doSave = dir.mkdirs();
        }

        if (doSave) {
            if (fileNumber > 0) {
                saveBitmapToFile(dir,fileName + ".png",bm, Bitmap.CompressFormat.PNG,100);
            }

        }
        else {
            Log.e("app","Couldn't create target directory.");
        }
    }

    static public Bitmap drawableToBitmap(PictureDrawable pd) {
        Bitmap bm = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawPicture(pd.getPicture());
        return bm;
    }

    public static boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
                                           Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format,quality,fos);

            fos.close();

            return true;
        }
        catch (IOException e) {
            Log.e("app",e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }


    public static void saveString(String folderLocation, String fileName, int creatureNumber, String data) {
        numberCreature = creatureNumber;
        if (folderLocation.equals("Creatures")) {
            getCreatureDirectory();
        }else if (folderLocation.equals("Items")) {
            getItemsDirectory();
        }else {
            getDefaultDirectory();
        }
        Log.e("CreatureDirectory is", creatureDirectory);
        File file;
        if (creatureNumber > 0) {
            file = new File(Environment.getExternalStorageDirectory(), creatureDirectory + fileName + ".txt");
        }else {
            file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/" + fileName + ".txt");
        }

        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void storeSave(String item, int newAmount) {
        int amount;
        File file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Items/" + item);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Items/" + item + "/ItemAmount.txt");
        StringBuilder text = new StringBuilder();
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
            if (Integer.parseInt(text.toString()) <= 0) {
                amount = 0;
            }else {
                amount = Integer.parseInt(text.toString());
            }
        }else {
            amount = 0;
        }

        amount = amount + newAmount;

        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(String.valueOf(amount));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStringWithLocation(Context context, String fileLocation, String fileNameWithExtension, String data) {
        File file = new File(context.getFilesDir(), fileLocation);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(context.getFilesDir(), fileLocation + fileNameWithExtension);

        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(String.valueOf(data));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getDefaultDirectory() {
        Log.e("GettingDirectory is", String.valueOf(numberCreature));
        creatureDirectory = ("/UnknownYet/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void getItemsDirectory() {
        Log.e("GettingDirectory is", String.valueOf(numberCreature));
        creatureDirectory = ("/UnknownYet/Items/Item" + String.valueOf(numberCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void getCreatureDirectory() {
        Log.e("GettingDirectory is", String.valueOf(numberCreature));
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(numberCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
