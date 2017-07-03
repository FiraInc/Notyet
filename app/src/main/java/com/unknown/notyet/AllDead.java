package com.unknown.notyet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Johannett321 on 25.03.2017.
 */

public class AllDead extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_dead);
    }

    public void refillAll(View view) {
        CurrentCreatureInfo.fillFoodHealth(CurrentCreatureInfo.CreatureFolderNumber);
        startHome();
    }

    private void startHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
