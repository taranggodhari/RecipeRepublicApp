package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


//Custom Info Window For Google Maps
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    //Initializing Context and LayoutInflater
    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    //Method to render the Text on Marker InfoWindow
    private void renderWindowText(Marker marker, View view) {

        //Get Title Text from marker
        String title = marker.getTitle();
        //Set it to TextView of Title
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if (!title.equals("")) {
            tvTitle.setText(title);
        }

        //Get Snippet Text from marker
        String snippet = marker.getSnippet();
        //Set it to TextView of Snippet
        TextView tvSnippet = (TextView) view.findViewById((R.id.snippet));
        if (!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }

        //Get Latitude from marker
        String latitude  = Double.toString(marker.getPosition().latitude );
        //Set it to TextView of Latitude
        TextView tvLatitude  = (TextView) view.findViewById((R.id.latitude ));
        if (!tvLatitude.equals("")) {
            tvLatitude.setText(latitude);
        }

        //Get Longitude from marker
        String longitude = Double.toString(marker.getPosition().longitude);
        //Set it to TextView of Longitude
        TextView tvLongitude = (TextView) view.findViewById((R.id.longitude));
        if (!tvLongitude.equals("")) {
            tvLongitude.setText(longitude);
        }
    }

    //Methods to render the InfoWindow Content
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
    //Methods to get the Content of InfoWindow
    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
