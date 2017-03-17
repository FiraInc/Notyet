package com.unknown.notyet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Johannett321 on 26.02.2017.
 */

public class DialogInputSaveCancel extends Activity {

    EditText textInput;
    int REQUEST_SAVE_NAME = 234897;
    int REQUEST_CANCEL_NAME = 234897;

    static String title = "";
    static String positiveButtonTitle = "";
    static String negativeButtonTitle = "";

    TextView Title;
    TextView PositiveButtonTitle;
    TextView NegativeButtonTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_save_cancel);
        findViews();
        Title.setText(title);
        PositiveButtonTitle.setText(positiveButtonTitle);
        NegativeButtonTitle.setText(negativeButtonTitle);
    }

    private void findViews() {
        textInput = (EditText) findViewById(R.id.textInput);
        Title = (TextView) findViewById(R.id.dialogTitle);
        PositiveButtonTitle = (TextView) findViewById(R.id.positiveButtonText);
        NegativeButtonTitle = (TextView) findViewById(R.id.negativeButtonText);
    }

    public void negativeClicked(View view) {
        DialogYesNo.title = "Are you sure?";
        DialogYesNo.negativeButtonTitle = "No";
        DialogYesNo.positiveButtonTitle = "Yes";
        Intent intent1 = new Intent(this, DialogYesNo.class);
        startActivityForResult(intent1, REQUEST_CANCEL_NAME);
    }

    public void positiveClicked(View view) {
        if (!textInput.getText().toString().isEmpty()) {
            DialogYesNo.title = "You entered (" + textInput.getText().toString() + ") Correct?";
            DialogYesNo.negativeButtonTitle = "No";
            DialogYesNo.positiveButtonTitle = "Yes";
            Intent intent1 = new Intent(this, DialogYesNo.class);
            startActivityForResult(intent1, REQUEST_SAVE_NAME);
        }else {
            DialogYesNo.title = "Are you sure?";
            DialogYesNo.negativeButtonTitle = "No";
            DialogYesNo.positiveButtonTitle = "Yes";
            Intent intent1 = new Intent(this, DialogYesNo.class);
            startActivityForResult(intent1, REQUEST_CANCEL_NAME);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = data.getStringExtra("result");
        if (requestCode == REQUEST_SAVE_NAME) {
            if (result.equals("1")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", textInput.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }else if (requestCode == REQUEST_CANCEL_NAME) {
            if (result.equals("1")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "NONAME");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}
