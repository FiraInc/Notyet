package com.unknown.notyet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class Store extends Activity {

    ArrayList<StoreItems> arrayOfItems;
    static StoreAdapter adapter;
    ListView listView;

    Bitmap imageToAdd;

    String ItemName;
    String ItemDesc;
    String Price;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        arrayOfItems = new ArrayList<StoreItems>();

        adapter = new StoreAdapter(this, arrayOfItems);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadItems();
            }
        }, 500);

    }

    private void loadItems() {

        ItemName = "Trond";
        ItemDesc = "Trond er en hyggelig kar :)";
        Price = "3578,50kr";
        imageToAdd = BitmapFactory.decodeResource(getResources(), R.drawable.damage1);
        addItem();

        ItemName = "Kveld med Mats";
        ItemDesc = "Mats er en deilig type :)";
        Price = "4,50 kr pr kg";
        imageToAdd = BitmapFactory.decodeResource(getResources(), R.drawable.think_bubble);
        addItem();




        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Home.doNotStopMusic) {
            Home.doNotStopMusic = false;
        }else {
            Home.music.start();
        }
    }

    private void addItem() {
        try {
            StoreItems newItem = new StoreItems(ItemName, ItemDesc, Price, imageToAdd);
            adapter.add(newItem);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InventoryClicked(View view) {
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
        finish();
    }
}
