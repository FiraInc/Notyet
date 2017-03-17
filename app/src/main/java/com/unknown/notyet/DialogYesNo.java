package com.unknown.notyet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Johannett321 on 26.02.2017.
 */

public class DialogYesNo extends Activity {

    ImageView blackScreen;
    static String title = "";
    static String positiveButtonTitle = "";
    static String negativeButtonTitle = "";
    TextView Title;
    TextView PositiveButton;
    TextView NegativeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yes_no);
        findViews();

        Title.setText(title);
        PositiveButton.setText(positiveButtonTitle);
        NegativeButton.setText(negativeButtonTitle);
    }

    private void findViews() {
        blackScreen = (ImageView) findViewById(R.id.blackScreen);
        Title = (TextView) findViewById(R.id.dialogTitle);
        NegativeButton = (TextView) findViewById(R.id.negativeButtonText);
        PositiveButton = (TextView) findViewById(R.id.positiveButtonText);
    }

    public void negativeClicked(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "0");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void positiveClicked(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "1");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
