package com.centennial.reciperepublic.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Intent intent;
    String tableName = "tbl_user";
    String userEmail, phoneNumber;
    EditText email, password;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //Initialize DBHelper
        db = new DatabaseHelper(this);

        //Get UserEmail SharedPreferences
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", 0);
        userEmail = mySharedPreferences.getString("userEmail", "");

        //Get Phone Number of logged in user
        phoneNumber  = db.getPhoneNumber(userEmail);

    }
}
