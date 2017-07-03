package com.unknown.notyet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class Store extends Activity {

    ArrayList<StoreItems> arrayOfItems;
    static StoreAdapter adapter;
    ListView listView;

    BitmapDrawable imageToAdd;

    String ItemName;
    String ItemDesc;
    String ItemCat;
    String ItemAmount;
    String Price;
    String currentAmount;

    static String category = "Food";
    TextView categoryText;

    ProgressBar progressBar;

    File file;
    StringBuilder text;

    TextView diamondsText;
    TextView coinsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);
        createOn();
    }

    private void createOn() {
        findViews();

        progressBar.setVisibility(View.VISIBLE);

        arrayOfItems = new ArrayList<StoreItems>();

        adapter = new StoreAdapter(this, arrayOfItems, coinsText, diamondsText);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        diamondsText.setText(String.valueOf(Bank.getCurrentDiamonds(this)));
        coinsText.setText(String.valueOf(Bank.getCurrentCoins(this)));
        categoryText.setText(category);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadItems();
            }
        }, 200);
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        diamondsText = (TextView) findViewById(R.id.diamondsText);
        coinsText = (TextView) findViewById(R.id.coinsText);
        categoryText = (TextView) findViewById(R.id.categoryText);
    }

    private void loadItems() {
        String item;

        if (category.equals("Food")) {
            item = "Bread";
            Items.getItem(this, item);
            ItemName = Items.itemNameToBeAdded;
            ItemDesc = Items.itemDescription;
            ItemCat = Items.itemCategory;
            ItemAmount = String.valueOf(Items.itemAmountBuy);
            Price = String.valueOf(Items.itemPrice);
            imageToAdd = Items.bitmapToBeAdded;
            currentAmount = String.valueOf(Items.loadAmount(item));
            addItem();
        }else if (category.equals("Special")) {
            item = "Egg";
            Items.getItem(this, item);
            ItemName = Items.itemNameToBeAdded;
            ItemDesc = Items.itemDescription;
            ItemCat = Items.itemCategory;
            ItemAmount = String.valueOf(Items.itemAmountBuy);
            Price = String.valueOf(Items.itemPrice);
            imageToAdd = Items.bitmapToBeAdded;
            currentAmount = String.valueOf(Items.loadAmount(item));
            addItem();
        }


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

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("my-integer"));

        diamondsText.setText(String.valueOf(Bank.getCurrentDiamonds(this)));
        coinsText.setText(String.valueOf(Bank.getCurrentCoins(this)));
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void addItem() {
        try {
            StoreItems newItem = new StoreItems(ItemName, ItemDesc, ItemCat, ItemAmount, currentAmount,Price, imageToAdd);
            adapter.add(newItem);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InventoryClicked(View view) {
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void buyMoneyClicked(View view) {
        buyMoney();
    }

    private void buyMoney() {
        Intent intent = new Intent(this, BuyMoney.class);
        startActivity(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            int yourMessage = intent.getIntExtra("message",-1/*default value*/);
            int yourInteger = intent.getIntExtra("positioner",-1/*default value*/);
            if (yourMessage == 1) {
                StoreShowNotEnoughDialog(yourInteger);
            }

        }
    };

    public void StoreShowNotEnoughDialog(int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Store.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_yes_no, null);
        TextView title = (TextView) mView.findViewById(R.id.dialogTitle);
        TextView positiveText = (TextView) mView.findViewById(R.id.positiveButtonText);
        TextView negativeText = (TextView) mView.findViewById(R.id.negativeButtonText);
        ImageView positive = (ImageView) mView.findViewById(R.id.positiveButton);
        ImageView negative = (ImageView) mView.findViewById(R.id.negativeButton);

        int price = Integer.parseInt(adapter.getItem(position).Price);
        final int coinsNeeded = price - Bank.getCurrentCoins(this);

        final int diamondsNeededToBuyCoins;
        diamondsNeededToBuyCoins = coinsNeeded / 75;

        title.setText("Do you want to buy " + String.valueOf(coinsNeeded) + " coins, with " + (diamondsNeededToBuyCoins + 1) + " diamonds?");
        positiveText.setText("Yes");
        negativeText.setText("No");

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diamondsNeededToBuyCoins < Bank.getCurrentDiamonds(Store.this)) {
                    Bank.addDiamonds(Store.this, -diamondsNeededToBuyCoins);
                    Bank.addCoins(Store.this, coinsNeeded);
                    diamondsText.setText(String.valueOf(Bank.getCurrentDiamonds(Store.this)));
                    coinsText.setText(String.valueOf(Bank.getCurrentCoins(Store.this)));
                    dialog.dismiss();
                }else {
                    buyMoney();
                }
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diamondsText.setText(String.valueOf(Bank.getCurrentDiamonds(Store.this)));
                coinsText.setText(String.valueOf(Bank.getCurrentCoins(Store.this)));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void changeCategory(View view) {
        setContentView(R.layout.category_browser);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);
    }

    public void closeChangeCategory() {
        setContentView(R.layout.store);
        categoryText = (TextView) findViewById(R.id.categoryText);
        categoryText.setText(category);
        createOn();
    }

    public void changeCategoryToFood(View view) {
        category = "Food";
        closeChangeCategory();
    }

    public void changeCategoryToSpecial(View view) {
        category = "Special";
        closeChangeCategory();
    }
}
