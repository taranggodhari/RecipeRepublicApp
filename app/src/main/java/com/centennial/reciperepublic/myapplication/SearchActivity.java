package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Intent intent;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "SearchActivity";

    //Edamam search vars
    private String uri = "https://api.edamam.com/search?q=";

    private String searchQuery = "fresh summer picks";
    private static String app_id = BuildConfig.RecipeRepublic_AppId;
    private static String app_key = BuildConfig.RecipeRepublic_AppKey;
    private static String search_range = "0&to=7";
    private String urlAfter = "&app_id=" + app_id + "&app_key=" + app_key + "&from=" + search_range + "";
    private EdamamModel.Rootobject edamamModel;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RefreshSearchList(searchQuery);
        //Search Bar Design
        searchView = findViewById(R.id.search);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.ColorWhiteGreyish));
        searchEditText.setHintTextColor(getResources().getColor(R.color.ColorWhiteGreyish));

        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchMagIcon.setImageResource(R.drawable.ic_search_white);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RefreshSearchList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
//              }
                return true;
            }
        });
    }

    public void RefreshSearchList(String searchQuery) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                uri + searchQuery + urlAfter,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        edamamModel = new Gson().fromJson(response.toString(), EdamamModel.Rootobject.class);
                        Toast.makeText(SearchActivity.this, "Yay! We Found your Recipe", Toast.LENGTH_SHORT).show();
                        mAdapter = new MyRecyclerViewAdapter(getRecipeList());
                        mRecyclerView.setAdapter(mAdapter);
                        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                Gson gson = new Gson();
                                EdamamModel.Recipe recipe = edamamModel.hits[position].recipe;
                                String recipeJson = gson.toJson(recipe);
                                SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                                SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                                prefEditor.putString("recipe", recipeJson);
                                prefEditor.commit();
                                intent = new Intent(SearchActivity.this, RecipeDetailsActivity.class);
                                startActivity(intent);
                                Log.i(LOG_TAG, " Clicked on Item " + position);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this, "Error Occurred While Searching", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    //Indexing the recipe list for onclick listeners
    private ArrayList<EdamamModel.Recipe> getRecipeList() {
        ArrayList results = new ArrayList<EdamamModel.Recipe>();
        if (edamamModel != null) {
            for (int index = 0; index < edamamModel.hits.length; index++) {
                EdamamModel.Recipe recipe = edamamModel.hits[index].getRecipe();
                results.add(index, recipe);
            }
        }
        return results;
    }
}