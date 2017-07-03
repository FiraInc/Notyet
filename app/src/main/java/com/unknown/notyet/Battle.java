package com.unknown.notyet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Johannett321 on 03.03.2017.
 */

public class Battle extends Activity {

    ImageView blackScreen;

    ImageView yourImage;
    ImageView opponentImage;

    ImageView backgroundAttack;

    //hei

    MediaPlayer music;

    static int level;
    static String name = "Ikke gjør snapchat komplisert Maybritt!";

    String creatureDirectory;
    static int currentCreature = 0;

    RelativeLayout attackView;
    Boolean attackWindowOpen = false;

    TextView attackName1;
    TextView attackName2;
    TextView attackName3;
    TextView attackName4;

    ImageView attackEffectOpponent;

    TextView opponentHealth;
    TextView yourHealth;

    TextView opponentLevel;
    TextView yourLevel;

    Boolean yourTurn = true;

    int AnimationCounter = 0;

    ImageView attackEffectYou;

    Calendar calendar;
    long currentMil;

    int healthHardness;

    static Boolean runPossible = true;
    static Boolean attackPossible = true;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);
        findViews();

        if (music == null) {
            music = MediaPlayer.create(this, R.raw.battle_music2);
            music.setLooping(true);
            music.start();
        }

        blackScreen.setVisibility(View.VISIBLE);
        attackView.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeBlackScreen();
            }
        }, 3000);

        try {
            getCreatureDirectory();
            loadYourCreature();
            loadOpponentCreature();
            loadAttacks();
            updateDamage();
            setLevels();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setLevels() {
        yourLevel.setText(String.valueOf(CreatureLoader.yourLevel));
        opponentLevel.setText(String.valueOf(CreatureLoader.opponentLevel));
    }

    private void findViews() {
        blackScreen = (ImageView) findViewById(R.id.blackScreen);
        opponentImage = (ImageView) findViewById(R.id.opponentImage);
        yourImage = (ImageView) findViewById(R.id.yourImage);
        attackView = (RelativeLayout) findViewById(R.id.attackView);
        backgroundAttack = (ImageView) findViewById(R.id.backgroundAttack);

        attackName1 = (TextView) findViewById(R.id.attackName1);
        attackName2 = (TextView) findViewById(R.id.attackName2);
        attackName3 = (TextView) findViewById(R.id.attackName3);
        attackName4 = (TextView) findViewById(R.id.attackName4);

        attackEffectOpponent = (ImageView) findViewById(R.id.attackEffectOpponent);
        opponentHealth = (TextView) findViewById(R.id.opponentHealth);
        yourHealth = (TextView) findViewById(R.id.yourHealth);

        opponentLevel = (TextView) findViewById(R.id.opponentLevel);
        yourLevel = (TextView) findViewById(R.id.yourLevel);

        attackEffectYou = (ImageView) findViewById(R.id.attackEffectYou);
    }

    private void getCreatureDirectory() {
        if (currentCreature > 0) {
            setDirecoryFromInventory();
        }else {
            getDirectory();
        }
    }
    private void setDirecoryFromInventory() {
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(currentCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            getDirectory();
        }
    }
    private void getDirectory() {
        currentCreature = currentCreature +1;
        creatureDirectory = ("/UnknownYet/Creature" + String.valueOf(currentCreature) + "/");
        File file = new File(Environment.getExternalStorageDirectory(), creatureDirectory);
        if (!file.exists()) {
            getDirectory();
        }
    }

    private void loadYourCreature() {
        try {
            String externalStorage = Environment.getExternalStorageDirectory().getPath();
            Bitmap creatureBitmap = BitmapFactory.decodeFile(externalStorage + creatureDirectory + "CreatureImage" + ".png");
            yourImage.setImageBitmap(creatureBitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOpponentCreature() {
        try {
            opponentImage.setImageDrawable(CreatureLoader.creatureDrawable);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadAttacks () {
        CreatureLoader.loadAttacks(currentCreature);
        if (CreatureLoader.Attack1 == null) {
            attackName1.setText("");
        }else {
            attackName1.setText(CreatureLoader.Attack1);
        }

        if (CreatureLoader.Attack2 == null) {
            attackName2.setText("");
        }else {
            attackName2.setText(CreatureLoader.Attack2);
        }

        if (CreatureLoader.Attack3 == null) {
            attackName3.setText("");
        }else {
            attackName3.setText(CreatureLoader.Attack3);
        }

        if (CreatureLoader.Attack4 == null) {
            attackName4.setText("");
        }else {
            attackName4.setText(CreatureLoader.Attack4);
        }
    }

    private void removeBlackScreen() {
        blackScreen.animate().setDuration(2000).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blackScreen.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        CreatureLoader.saveHealth(currentCreature);
        music.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCreatureDirectory();
        loadYourCreature();
        loadAttacks();
        updateDamage();
        setLevels();
        Log.e("CURRENT", String.valueOf(currentCreature));
        music.start();
        if (CreatureLoader.yourHealth <= 0) {
            backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.darker_gray));
            attackPossible = false;
            lowHealth();
        }else if (yourTurn){
            attackPossible = true;
        }

        if (attackPossible) {
            backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
        }
    }

    @Override
    public void onBackPressed() {
        if (runPossible) {
            runFromBattle();
        }
    }

    private void runFromBattle() {
        CreatureInventory.inBattle = false;
        blackScreen.setVisibility(View.VISIBLE);
        blackScreen.animate().setDuration(2000).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goBack();
            }
        });
    }

    private void goBack() {
        Constants.stopBattleCheck = false;
        super.onBackPressed();
    }

    public void attackCLicked(View view) {
        if (attackPossible) {
            if (attackWindowOpen) {
                attackView.setVisibility(View.GONE);
                attackWindowOpen = false;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
            }else {
                attackView.setVisibility(View.VISIBLE);
                attackWindowOpen = true;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.holo_orange_dark));
            }
        }
    }

    public void RunClicked(View view) {
        if (runPossible) {
            runFromBattle();
        }
    }

    public void InventoryClicked(View view) {
        clickInventory();
    }

    private void clickInventory() {
        Intent intent = new Intent(this, CreatureInventory.class);
        startActivity(intent);
        CreatureInventory.inBattle = true;
    }

    public void attack1(View view) {
        if (CreatureLoader.Attack1 != null) {
            if (yourTurn) {
                yourTurn = false;
                attackPossible = false;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.darker_gray));
                attackWindowOpen = false;
                attackView.setVisibility(View.GONE);
                CreatureLoader.loadAttackEffect(this, CreatureLoader.Attack1);
                CreatureLoader.AttackDamage1 = CreatureLoader.damage;
                animateAttack(attackEffectYou, attackEffectOpponent, 2);
            }
        }
    }

    public void attack2(View view) {
        if (CreatureLoader.Attack2 != null) {
            if (yourTurn) {
                yourTurn = false;
                attackPossible = false;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.darker_gray));
                attackWindowOpen = false;
                attackView.setVisibility(View.GONE);
                CreatureLoader.loadAttackEffect(this, CreatureLoader.Attack2);
                CreatureLoader.AttackDamage2 = CreatureLoader.damage;
                animateAttack(attackEffectYou, attackEffectOpponent, 2);
            }
        }
    }

    public void attack3(View view) {
        if (CreatureLoader.Attack3 != null) {
            if (yourTurn) {
                yourTurn = false;
                attackPossible = false;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.darker_gray));
                attackWindowOpen = false;
                attackView.setVisibility(View.GONE);
                CreatureLoader.loadAttackEffect(this, CreatureLoader.Attack3);
                CreatureLoader.AttackDamage3 = CreatureLoader.damage;
                animateAttack(attackEffectYou, attackEffectOpponent, 2);
            }
        }
    }

    public void attack4(View view) {
        if (CreatureLoader.Attack4 != null) {
            if (yourTurn) {
                yourTurn = false;
                attackPossible = false;
                backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.darker_gray));
                attackWindowOpen = false;
                attackView.setVisibility(View.GONE);
                CreatureLoader.loadAttackEffect(this, CreatureLoader.Attack4);
                CreatureLoader.AttackDamage4 = CreatureLoader.damage;
                animateAttack(attackEffectYou, attackEffectOpponent, 2);
            }
        }
    }

    private void updateDamage() {
        yourHealth.setText(String.valueOf(CreatureLoader.yourHealth));
        opponentHealth.setText(String.valueOf(CreatureLoader.opponentHealth));
    }

    public void animateAttack(final ImageView attacker, final ImageView suspect, final int suspectNumber) {

        if (AnimationCounter == 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CreatureLoader.damage < 0) {
                        attacker.setImageDrawable(CreatureLoader.AttackEffect1);
                    }else {
                        suspect.setImageDrawable(CreatureLoader.AttackEffect1);
                    }

                    AnimationCounter = 1;
                    animateAttack(attacker, suspect, suspectNumber);
                    CreatureLoader.soundEffect.start();
                }
            }, CreatureLoader.attackTimeInMillis / 4);
        }else if (AnimationCounter == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CreatureLoader.damage < 0) {
                        attacker.setImageDrawable(CreatureLoader.AttackEffect2);
                    }else {
                        suspect.setImageDrawable(CreatureLoader.AttackEffect2);
                    }
                    AnimationCounter = 2;
                    animateAttack(attacker, suspect, suspectNumber);
                }
            }, CreatureLoader.attackTimeInMillis / 4);
        }else if (AnimationCounter == 2) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CreatureLoader.damage < 0) {
                        attacker.setImageDrawable(CreatureLoader.AttackEffect3);
                    }else {
                        suspect.setImageDrawable(CreatureLoader.AttackEffect3);
                    }
                    AnimationCounter = 3;
                    animateAttack(attacker, suspect, suspectNumber);
                }
            }, CreatureLoader.attackTimeInMillis / 4);
        }else if (AnimationCounter == 3) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CreatureLoader.damage < 0) {
                        attacker.setImageDrawable(CreatureLoader.AttackEffect4);
                    }else {
                        suspect.setImageDrawable(CreatureLoader.AttackEffect4);
                    }
                    AnimationCounter = 4;
                    animateAttack(attacker, suspect, suspectNumber);
                }
            }, CreatureLoader.attackTimeInMillis / 4);
        }else {
            AnimationCounter = 0;
            if (CreatureLoader.damage < 0) {
                attacker.setImageDrawable(null);
                if (suspectNumber == 2) {
                    CreatureLoader.calculateAttackDamage(1);
                }else {
                    CreatureLoader.calculateAttackDamage(2);
                }
            }else {
                suspect.setImageDrawable(null);
                CreatureLoader.calculateAttackDamage(suspectNumber);
            }
            updateDamage();

            if (CreatureLoader.opponentHealth <= 0) {
                CreatureLoader.opponentHealth = 0;
                music.pause();
                music = MediaPlayer.create(this, R.raw.evolve_finish);
                Leveling.levelUp(this, currentCreature, CreatureLoader.calculateXP());
                if (Leveling.didLevelUp) {
                    finish();
                }else {
                    runFromBattle();
                }
            }else if (CreatureLoader.yourHealth <= 0) {
                lowHealth();
            }else {
                if (suspectNumber == 2) {
                    startOpponent();
                }else {
                    CreatureLoader.saveHealth(currentCreature);
                    yourTurn = true;
                    attackPossible = true;
                    backgroundAttack.setImageDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
                }
            }
        }
    }

    private void lowHealth() {
        //// TODO: 07.03.2017 Creature Died
        CreatureLoader.yourHealth = 0;
        CreatureLoader.saveHealth(currentCreature);

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_yes_no, null);
        TextView text = (TextView) mView.findViewById(R.id.dialogTitle);
        TextView positiveButton = (TextView) mView.findViewById(R.id.positiveButtonText);
        TextView negativeButton = (TextView) mView.findViewById(R.id.negativeButtonText);

        text.setText("This creature cannot continue the battle. Do you want to continue with another one?");
        positiveButton.setText("Yes");
        negativeButton.setText("No");

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTheDialog();
                clickInventory();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTheDialog();
                runPossible = true;
                runFromBattle();
            }
        });
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissTheDialog();
                runPossible = true;
                runFromBattle();
            }
        });
    }

    private void dismissTheDialog() {
        dialog.dismiss();
    }


    private void startOpponent() {
        Random randm = new Random();
        int randmNumber = randm.nextInt(1000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                opponentChooseAttack();
            }
        }, 2000 - randmNumber);
    }

    private void opponentChooseAttack() {
        int healthHardness = 300;
        Random random = new Random();
        int randomNumber = random.nextInt(1000)+1;
        Log.e("TIME", String.valueOf(randomNumber +1));
        if (CreatureLoader.opponentHealth < 10 & randomNumber < healthHardness) {
            Log.e("UHHHHHHHMMMMM", String.valueOf(currentMil));
            if (CreatureLoader.AttackOpponent1.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent1);
                CreatureLoader.AttackDamage1 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            }else if (CreatureLoader.AttackOpponent2.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent2);
                CreatureLoader.AttackDamage2 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            }else if (CreatureLoader.AttackOpponent3.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent3);
                CreatureLoader.AttackDamage3 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            }else if (CreatureLoader.AttackOpponent4.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent4);
                CreatureLoader.AttackDamage4 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            }
        }else {
            if (currentMil < 300 && !CreatureLoader.AttackOpponent1.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent1);
                CreatureLoader.AttackDamage1 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else if (currentMil < 600 && !CreatureLoader.AttackOpponent2.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent2);
                CreatureLoader.AttackDamage2 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else if (currentMil < 900 && !CreatureLoader.AttackOpponent3.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent3);
                CreatureLoader.AttackDamage3 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else if (currentMil >= 900 && !CreatureLoader.AttackOpponent4.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent4);
                CreatureLoader.AttackDamage4 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);

            } else if (currentMil < 600 && !CreatureLoader.AttackOpponent1.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent1);
                CreatureLoader.AttackDamage1 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else if (currentMil >= 900 && !CreatureLoader.AttackOpponent2.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent2);
                CreatureLoader.AttackDamage2 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else if (currentMil < 300 && !CreatureLoader.AttackOpponent3.equals("Recover")) {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent3);
                CreatureLoader.AttackDamage3 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            } else {
                CreatureLoader.loadAttackEffect(getApplicationContext(), CreatureLoader.AttackOpponent1);
                CreatureLoader.AttackDamage1 = CreatureLoader.damage;
                animateAttack(attackEffectOpponent, attackEffectYou, 1);
            }
        }
    }
}
