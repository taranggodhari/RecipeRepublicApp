package com.centennial.reciperepublic.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "RecipeRepublic.db";
    private static final int DB_VERSION = 1;

    //class constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //create the database
    public void createDatabase(Context context) {
        SQLiteDatabase db = getWritableDatabase();
        db = context.openOrCreateDatabase(
                DB_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null);

    }

    //Base onCreate method to initialize the sql Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        String sqlUser = "CREATE TABLE  tbl_user  (userId  INTEGER PRIMARY KEY AUTOINCREMENT, email  TEXT NOT NULL UNIQUE, fullName  TEXT NOT NULL UNIQUE, password  TEXT NOT NULL,phoneNumber  TEXT NOT NULL);";
        db.execSQL(sqlUser);
    }

    //Drop existing tables and call onCreate to upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlUser = "DROP TABLE IF EXISTS tbl_user";
        db.execSQL(sqlUser);

        onCreate(db);
    }

    /////////////////////////
    // Database operations
    /////////////////////////
    // Add a new record
    void addRecord(String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < record.length; i++)
            values.put(fields[i], record[i]);
        // Insert the row
        db.insert(tableName, null, values);
        db.close(); //close database connection
    }
    // Read all records
    public List getTable(String tableName) {
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM " + tableName;
        //get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //run the raw query and return the cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList row = new ArrayList(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst()) {
            do { // for each row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                }

                table.add(row); //add row to the list

            } while (cursor.moveToNext());
        }

        // return table as a list
        return table;
    }
    // Update a record
    public int updateRecord(String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < record.length; i++)

                values.put(fields[i], record[i]);

        // updating row with given id = record[0]
        return db.update(tableName, values, fields[0] + " = ?",
                new String[]{record[0]});
    }

    // Delete a record with a given id
    public void deleteRecord(String tableName, String idName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, idName + " = ?",
                new String[]{id});
        db.close();
    }

    // Read the userId using email
    public String getUserId(String email) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_user WHERE email = '" + email + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            cursor.close();
            db.close();
            return userId;
        }
        // return table as a list
        return "0";
    }

    //Get the row of user using the user
    public Cursor getUserByEmail(String email) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_user WHERE email = '" + email + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    //Get the row of user using the user
    public Cursor getUserById(int userId) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_user WHERE userId = " + userId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    //Get the row of user using the user
    public String getPhoneNumber(String email) {
        // Select all records
        String selectQuery = "SELECT phoneNumber FROM tbl_user WHERE email = '" + email + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String phoneNumber = cursor.getString(0);
            cursor.close();
            db.close();
            return phoneNumber;
        }
        else{
            return null;
        }
    }
    // Read all records
    public List getUsersList() {
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_user;";
        //get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //run the raw query and return the cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList row = new ArrayList(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst()) {
            do { // for each row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                    if(row.size() ==  9){
                        table.add(row); //add row to the list
                        row = new ArrayList();
                    }
                }

               // table.add(row); //add row to the list

            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        // return table as a list
        return table;
    }
    // This method to check user exist or not
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // query user table with condition
        Cursor cursor = db.rawQuery("SELECT userId FROM tbl_user WHERE email = '" + email + "';", null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    //This method to check user exist or not
    public boolean checkUser(String email, String password, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE email = '" + email + "' AND password = '" + password + "';", null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

}

