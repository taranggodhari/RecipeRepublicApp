package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Intent intent;
    String userEmail, recipeJson;
    EdamamModel.Recipe recipe;
    ImageView locationOnMap,sendMessage;
    TextView textViewRecipeName, textViewOwner, textViewCalories, textViewDailyValue, textViewServings;
    Button buttonPrepStep;
    LinearLayout layoutIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize Gson
        Gson gson = new Gson();

        //Get all View Items
        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewOwner = findViewById(R.id.textViewOwner);
        textViewCalories = findViewById(R.id.textViewCalories);
        textViewDailyValue = findViewById(R.id.textViewDailyValue);
        textViewServings = findViewById(R.id.textViewServings);
        layoutIngredients = findViewById(R.id.layoutIngredients);
        locationOnMap = findViewById(R.id.locationOnMap);
        sendMessage = findViewById(R.id.sendMessage);
        buttonPrepStep = findViewById(R.id.buttonPrepStep);
        //Get UserEmail SharedPreferences
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", 0);
        userEmail = mySharedPreferences.getString("userEmail", "");
        recipeJson = mySharedPreferences.getString("recipe", "");
        recipe = gson.fromJson(recipeJson, EdamamModel.Recipe.class);

        //Set Recipe image
        new DownloadImageTask((ImageView) findViewById(R.id.imgRecipe)).execute(recipe.image);
        //Set Recipe Label
        textViewRecipeName.setText(recipe.getLabel());
        textViewOwner.setText("By " + recipe.getSource());
        textViewCalories.setText(String.format("%.0f", recipe.getCalories()));
        textViewDailyValue.setText(String.format("%.0f", recipe.getTotalWeight()) + "%");
        textViewServings.setText(String.format("%.0f", recipe.getYield()));
        for (String line : recipe.getIngredientLines()) {
            TextView textViewIngredientsLine = new TextView(this);
            textViewIngredientsLine.setTextSize(16);
            textViewIngredientsLine.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            textViewIngredientsLine.setTextColor(Color.BLACK);
            textViewIngredientsLine.setTypeface(null, Typeface.ITALIC);
            textViewIngredientsLine.setText("➤ " + line);
            layoutIngredients.addView(textViewIngredientsLine);
        }
        buttonPrepStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recipe.getUrl()));
                startActivity(intent);
            }
        });
        locationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RecipeDetailsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RecipeDetailsActivity.this, MessagingActivity.class);
                startActivity(intent);
            }
        });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
