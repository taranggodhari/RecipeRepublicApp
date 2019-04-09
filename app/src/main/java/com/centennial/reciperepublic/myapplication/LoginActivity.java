package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Intent intent;
    String tableName = "tbl_user";
    EditText email, password;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Create Database RecipeRepublic.db
        db = new DatabaseHelper(this);
        db.createDatabase(getApplicationContext());
        //Get email and password Fields
        email = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);

    }
    public void Login (View view){
        //Verify id user is in database or not
        if (db.checkUser(email.getText().toString(), password.getText().toString(), tableName)) {
            //Create SharedPreferences to store userEmail
            SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
            SharedPreferences.Editor prefEditor = mySharedPreference.edit();
            //userEmail SharedPreference
            prefEditor.putString("userEmail", email.getText().toString());
            prefEditor.commit();

            //Send to next Activity
            intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Email or Password is Invalid", Toast.LENGTH_LONG).show();
        }

    }
    public void SignUp (View view){
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
