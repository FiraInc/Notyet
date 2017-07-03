package com.unknown.notyet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Johannett321 on 19.03.2017.
 */

public class BuyMoney extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_money);
    }

    public void addCoinsClicked(View view) {
        Bank.addCoins(this, 500);
    }

    public void addDiamondsClicked(View view) {
        Bank.addDiamonds(this, 2);
    }
}
