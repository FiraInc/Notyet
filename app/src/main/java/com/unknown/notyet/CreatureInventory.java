package com.unknown.notyet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Johannett321 on 03.03.2017.
 */

public class CreatureInventory extends Activity {

    ArrayList<CreatureInventoryItems> arrayOfItems;
    static CreatureInventoryAdapter adapter;
    GridView grid;

    static int currentItem = 1;

    String titleToBeAdded;
    Bitmap bitmapToBeAdded;
    int healthStatusToBeAdded;
    int foodStatusToBeAdded;

    File file;
    StringBuilder text;

    ProgressBar progressBar;
    String StatusAlive;

    static Boolean inBattle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creature_inventory);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        arrayOfItems = new ArrayList<CreatureInventoryItems>();

        adapter = new CreatureInventoryAdapter(this, arrayOfItems);

        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inBattle) {
                    if (adapter.getItem(position).AliveStatus.equals("1")) {
                        backToBattle(adapter.getItem(position).ItemFolderNumber);
                    }else {
                        CreatureInfo.currentCreature = adapter.getItem(position).ItemFolderNumber;
                        showInfo();
                    }
                }else {
                    if (adapter.getItem(position).AliveStatus.equals("1")) {
                        setDefaultCreature(adapter.getItem(position).ItemFolderNumber);
                    }else {
                        CreatureInfo.currentCreature = adapter.getItem(position).ItemFolderNumber;
                        showInfo();
                    }
                }
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (inBattle) {
                    CreatureInfo.inBattle = true;
                }
                CreatureInfo.currentCreature = adapter.getItem(position).ItemFolderNumber;
                showInfo();

                return true;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadItems();
            }
        }, 500);
    }

    private void setDefaultCreature(int folderNumber) {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(folderNumber) + "/Health.txt");
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

        if (Integer.parseInt(text.toString()) > 0) {
            Log.e("SUCCESS", "CONGRATS");
            Saver.saveString("Default", "defaultCreature", 0,String.valueOf(folderNumber));
            homeGo();
        }else {
            Saver.saveString("Default", "defaultCreature", 0,String.valueOf(folderNumber));
            Intent intent = new Intent(this, AllDead.class);
            startActivity(intent);
            finish();
        }
            text.setLength(0);
    }

    private void backToBattle(int Position) {
        currentItem = 1;
        Battle.currentCreature = Position;
        super.onBackPressed();
    }

    private void showInfo() {
        Home.doNotStopMusic = true;
        Intent intent = new Intent(this, CreatureInfo.class);
        startActivity(intent);
    }

    private void loadItemsTest() {
        titleToBeAdded = "TURID";
        bitmapToBeAdded = BitmapFactory.decodeResource(this.getResources(), R.drawable.damage1);
        addItem();
    }

    private void loadItems() {
        file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(currentItem));
        if (file.exists()) {
            CurrentCreatureInfo.getAllCreatureInfo(this, currentItem);
            titleToBeAdded = CurrentCreatureInfo.customName;

            file = new File(Environment.getExternalStorageDirectory(), "/UnknownYet/Creature" + String.valueOf(currentItem) + "/CreatureAliveStatus" + ".txt");
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
                StatusAlive = "1";
            }else if (currentItem == 1) {
                StatusAlive = "1";
            }else {
                StatusAlive = "0";
            }

            if (StatusAlive.equals("1")) {
                bitmapToBeAdded = CurrentCreatureInfo.creatureBitmap;
                foodStatusToBeAdded = CurrentCreatureInfo.foodStatus;
                healthStatusToBeAdded = CurrentCreatureInfo.health;
                addItem();
            }



            currentItem  = currentItem + 1;
            loadItems();
        }else {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void addItem() {
        try {
            CreatureInventoryItems newItem = new CreatureInventoryItems(titleToBeAdded, bitmapToBeAdded, currentItem, StatusAlive, healthStatusToBeAdded, foodStatusToBeAdded);
            adapter.add(newItem);
        }catch (Exception e) {
            e.printStackTrace();
        }
        adapter.getCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Home.doNotStopMusic) {
            Home.music.pause();
        }else {
            Home.doNotStopMusic = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("HEI", "RESUMED");
        if (Home.doNotStopMusic) {
            Home.doNotStopMusic = false;
        }else {
            Home.music.start();
        }

        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentItem = 1;
                adapter.clear();
                loadItems();
            }
        },500);
    }

    @Override
    public void onBackPressed() {
        if (inBattle) {
            backToBattle(Battle.currentCreature);
        }else {
            homeGo();
        }
    }

    public void GoHome(View view) {
        homeGo();
    }

    private void homeGo() {
        currentItem = 1;
        Home.doNotStartMusic = true;
        super.onBackPressed();
    }

    public void startHatch(View view) {
        Home.music.stop();
        Intent intent = new Intent(this, HatchEgg.class);
        startActivity(intent);
        finish();
    }

    public void storeClicked(View view) {
        Home.doNotStartMusic = true;
        currentItem = 1;
        Intent intent = new Intent(this, Store.class);
        startActivity(intent);
        finish();
    }
}
