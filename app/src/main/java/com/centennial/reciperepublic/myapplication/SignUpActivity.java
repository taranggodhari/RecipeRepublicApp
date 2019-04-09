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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText fullName, emailAddress, password, phoneNumber;
    Button signUpBtn;
    private Intent intent;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = new DatabaseHelper(this);
        final String fields[] = {"userId", "email", "fullName", "password", "phoneNumber"};
        final String record[] = new String[5];

        // Handle signup button
        signUpBtn = findViewById(R.id.signUpBtn);
        //Get all the Edit texts form items from View
        emailAddress = findViewById(R.id.userEmailId);
        fullName = findViewById(R.id.fullName);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        //Pass entered values to database
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkEnteredData()) {
                    record[1] = emailAddress.getText().toString();
                    record[2] = fullName.getText().toString();
                    record[3] = password.getText().toString();
                    record[4] = phoneNumber.getText().toString();
                    //populate the row with some values
                    db.addRecord("tbl_user", fields, record);

                    //If the user is registered then redirect user to Welcome Activity
                    if (db.checkUser(emailAddress.getText().toString())) {
                        SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                        SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                        prefEditor.putString("userEmail", emailAddress.getText().toString());
                        prefEditor.commit();

                        intent = new Intent(SignUpActivity.this, SearchActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong! Please Restart the App", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill up required details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Login(View view) {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //Validation for each and every editTexts
    public boolean checkEnteredData() {
        boolean isValid = true;
        //First Name isEmpty Validation
        if (TextUtils.isEmpty(fullName.getText().toString())) {
            fullName.setError(getString(R.string.error_message_fullName));
            isValid = false;
        }
        //Email Validation by REGEX
        if (!emailAddress.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailAddress.setError(getString(R.string.error_message_email));
            isValid = false;
        }
        //Check password Length
        if (password.getText().toString().trim().length() < 8) {
            password.setError(getString(R.string.error_message_password));
            isValid = false;
        }
        //Phone Number isEmpty Validation
        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError(getString(R.string.error_message_phone_number));
            isValid = false;
        }
        //if all values are valid return true else false
        return isValid;
    }
}
