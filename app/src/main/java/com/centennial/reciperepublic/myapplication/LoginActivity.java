package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void Login (View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void SignUp (View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
