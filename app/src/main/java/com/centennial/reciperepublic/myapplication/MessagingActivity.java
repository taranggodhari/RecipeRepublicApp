package com.centennial.reciperepublic.myapplication;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity {
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    String userEmail, recipeJson, myPhoneNumber, receiverPhoneNumber, smsTemplate;
    Button btnSendSMS;
    EditText smsEditText, phoneNumberEditText;
    EdamamModel.Recipe recipe;
    DatabaseHelper db;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        Gson gson = new Gson();
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", 0);
        userEmail = mySharedPreferences.getString("userEmail", "");
        recipeJson = mySharedPreferences.getString("recipe", "");
        recipe = gson.fromJson(recipeJson, EdamamModel.Recipe.class);
        myPhoneNumber = db.getPhoneNumber(userEmail);

        smsEditText = findViewById(R.id.smsEditText);

        smsTemplate = "Hey There! I found this awesome recipe for " + recipe.getLabel() + ". Check it out at " + recipe.getUrl() + "";

        smsEditText.setText(smsTemplate);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        sendBtn = findViewById(R.id.btnSendSMS);

        sendBtn.setEnabled(false);
        if (!checkPermission(Manifest.permission.SEND_SMS))  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            sendBtn.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void onSend(View v) {
        receiverPhoneNumber = "+1"+phoneNumberEditText.getText().toString();
        smsTemplate = smsEditText.getText().toString();

        if (receiverPhoneNumber == null || phoneNumberEditText.length() == 0 || smsTemplate == null || smsTemplate.length() == 0) {
            Toast.makeText(this, "Please enter Reciever's Phone Number ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> msgArray = smsManager.divideMessage(smsTemplate);
                smsManager.sendMultipartTextMessage(receiverPhoneNumber, null,msgArray, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                this.finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"SMS failed, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    //Method to handle the back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
