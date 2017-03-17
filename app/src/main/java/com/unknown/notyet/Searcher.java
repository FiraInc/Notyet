package com.unknown.notyet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by johannett321 on 08.03.2017.
 */

public class Searcher extends Activity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    Button startBattleButton;
    Button cancelBattleButton;
    TextView walkText;
    TextView creatureTextName;
    ImageView searcherBackground;
    TextView gpsUnavailable;

    Random random;



    int randomInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searcher);

        findViews();
        startBattleButton.setVisibility(View.GONE);
        cancelBattleButton.setVisibility(View.GONE);
        makeInvNet();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //gpsUnavailable.setVisibility(View.GONE);
                if (!Constants.stopBattleCheck) {
                    walkText.setText("Halla Trude");
                    Log.e("Location", "Cordinates: " +location.getLatitude() + " " + location.getLongitude());
                    Toast.makeText(Searcher.this, "Cordinates: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    checkBattle();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("Steg", "StatusChanged");
                Log.e("Steg Error", provider + String.valueOf(status));

                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        makeVisNet();
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        makeVisNet();
                        break;
                    case LocationProvider.AVAILABLE:
                        makeInvNet();
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("Steg", "ProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("Steg", "Provider Disabled");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };


        configureButton();
    }

    private void makeInvNet() {
        gpsUnavailable.setVisibility(View.INVISIBLE);
    }
    private void makeVisNet() {
        gpsUnavailable.setVisibility(View.VISIBLE);
    }

    private void checkBattle() {
        random = new Random();
        randomInt = random.nextInt(1000);
        if (randomInt <= 750) {
            Constants.stopBattleCheck = true;
            generateBattleOpponent();
        }
    }

    private void generateBattleOpponent() {
        CreatureLoader.getOpponent(this);
        if (CreatureGenerator.cardType.equals("Shiny")){
            searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_purple));
        }else if (CreatureGenerator.cardType.equals("Epic")){
            searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_red_dark));
        }else if (CreatureGenerator.cardType.equals("Diamond")){
            searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_blue_dark));
        }else {
            searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_green_light));
        }


        startBattleButton.setVisibility(View.VISIBLE);
        cancelBattleButton.setVisibility(View.VISIBLE);
        walkText.setText("Creature found!");
        creatureTextName.setText(CreatureGenerator.creatureName);
    }

    private void findViews() {
        startBattleButton = (Button) findViewById(R.id.startBattleButton);
        cancelBattleButton = (Button) findViewById(R.id.cancelBattleButton);
        searcherBackground = (ImageView) findViewById(R.id.searcherBackground);
        walkText = (TextView) findViewById(R.id.walkText);
        gpsUnavailable = (TextView) findViewById(R.id.gpsUnavailable);
        creatureTextName = (TextView) findViewById(R.id.creatureTextName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void configureButton() {
        Log.e("Steg", "1 fullført");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("Steg", "2 fullført");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                Log.e("Steg", "3 fullført, permi");
                return;
            }else {
                Log.e("Steg", "3 fullført, startGPS");

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                    Log.e("Provider", "Network");
                }else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }


            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        battleCanceler();
        Constants.stopBattleCheck = true;
        if (Home.doNotStopMusic) {
            Home.doNotStopMusic = false;
        }else {
            Home.music.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Home.doNotStopMusic) {
            Home.doNotStopMusic = false;
        }else {
            Home.music.start();
        }
        Constants.stopBattleCheck = false;
    }

    @Override
    public void onBackPressed() {
        Home.doNotStopMusic = true;
        battleCanceler();
        Constants.stopBattleCheck = true;
        super.onBackPressed();
        overridePendingTransition(R.animator.pull_in_bottom_slow, R.animator.push_out_top_slow);
        finish();
    }

    public void StartBattle(View view) {
        searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_blue_bright));
        startBattleButton.setVisibility(View.GONE);
        cancelBattleButton.setVisibility(View.GONE);
        walkText.setText("Try to walk around...");
        Intent intent = new Intent(this, Battle.class);
        startActivity(intent);
    }

    public void CancelBattle(View view) {
        battleCanceler();
    }

    private void battleCanceler() {
        searcherBackground.setBackground(getResources().getDrawable(android.R.color.holo_blue_bright));
        startBattleButton.setVisibility(View.GONE);
        cancelBattleButton.setVisibility(View.GONE);
        walkText.setText("Try to walk around...");
        Constants.stopBattleCheck = false;
    }
}
