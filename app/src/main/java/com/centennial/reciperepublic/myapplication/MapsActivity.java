package com.centennial.reciperepublic.myapplication;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Create a private object of map
    private GoogleMap mMap;
    //Declare city locInfo and addressLine Strings.
    String locInfo, addressLine, recipeJson;
    private EdamamModel.Recipe recipe;
    //Dummy LatLongs
    double latitude = 43.6426f;
    double longitude = -79.3871f;
    //Strings for Permissions;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //Variables for getting current locations
    private Boolean mapLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;

    //    ImageView Devlaration
    ImageView changeType, clearType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Setting up the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Generate bundle to get the extras stored in the intent.
        Gson gson = new Gson();
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", 0);
        recipeJson = mySharedPreferences.getString("recipe", "");
        recipe = gson.fromJson(recipeJson, EdamamModel.Recipe.class);

        //Get Views from the layout with id.
        clearType = (ImageView) findViewById(R.id.clear_type);
        clearType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearMapStyle();
            }
        });
        changeType = (ImageView) findViewById(R.id.change_type);
        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeMapType(v);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocationPermission();
    }

    //Method to initialize the map
    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Method to obtain the location of device
    private void getDeviceLocation() {
        //Initializing the Fused Location Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mapLocationPermissionGranted) {
                //Task to get the last known location
                Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //If location is obtained then get current location
                            Location currentLocation = (Location) task.getResult();
                            //Call animate camera method
                            animateCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f, 500);
                        } else {
                            //If no location is obtained set a toast message
                            Toast.makeText(MapsActivity.this, "Unable to get current loaction", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Permissions", "Unexpected error Occurred", e);
            Toast.makeText(MapsActivity.this, "Unexpected error Occurred" + e, Toast.LENGTH_SHORT).show();
        }
    }

    //Method to get Loaction permission from user
    public void getLocationPermission() {
        //String array of required permissions.
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        //Check if FINE_LOCATION permission is granted by user
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check if COURSE_LOCATION permission is granted by user
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //If both are true then set the boolean to true then initialize the map
                mapLocationPermissionGranted = true;
                initMap();
            } else {
                //If not Request for permission with request code
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            //If not Request for permission with request code
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Once user's permission is obtained either initialize the map or get permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mapLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mapLocationPermissionGranted = true;
                    //initializeMap
                    initMap();
                }
            }
        }
    }

    // Function to perform once the map is initialized and is ready.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initialize the mMap Object
        mMap = googleMap;

        //Check if location permission is granted
        if (mapLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //Enable Get current location
            mMap.setMyLocationEnabled(true);
            //Enable Compass
            mMap.getUiSettings().setCompassEnabled(true);
            //Enable Zoom Controls
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //Enable All the Gestures
            mMap.getUiSettings().setAllGesturesEnabled(true);
            //Enable Scroll Gestures
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            //Enable Map toolbar
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }
        //On location button click event Listener
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mapLocationPermissionGranted) {
                    //if Device location permission is granted the call getDeviceLocation Methos
                    getDeviceLocation();
                    return true;
                } else {
                    //else Return false
                    return false;
                }

            }
        });

        //Initialize a new object of type Geocoder
        final Geocoder coder = new Geocoder(getApplicationContext());

        //string concatenation of Search keyword
        String placeName = recipe.getLabel() + " Toronto";
        try {
            //using geocoder object getting maximum 3 addresses for given place name / address
            List<Address> geocodeResults = coder.getFromLocationName(placeName, 5);
            Iterator<Address> locations = geocodeResults.iterator();

            // Perform iterations over the location retrieved form geoCoder Result
            while (locations.hasNext()) {
                //Get the line of location
                Address loc = locations.next();

                //using an address object getting latitude and longitute values for the given address
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();

                // using an address object, getting address information
                String pName = loc.getLocality();
                String featureName = loc.getFeatureName();
                String country = loc.getCountryName();
                String road = loc.getThoroughfare();
                addressLine = loc.getAddressLine(0);

                locInfo = placeName.toUpperCase();

                //Draw a bitmap for custom MapMarker
                BitmapDrawable bitmapMapMarker = (BitmapDrawable) getResources().getDrawable(R.drawable.mapmarker);
                Bitmap b = bitmapMapMarker.getBitmap();
                //Set Scale of marker
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 85, false);

                //Initialize a mapMarker object of type Marker options
                MarkerOptions mapMarker = new MarkerOptions();
                //Set position of marker
                mapMarker.position(new LatLng(latitude, longitude));
                //Set title of marker
                mapMarker.title(locInfo);
                //Set snippet of marker
                mapMarker.snippet(addressLine);
                //Set icon of marker
                mapMarker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                //Add the markers on map
                mMap.addMarker(mapMarker);

                //Set the default type for map to be NORMAL
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //Move Camera to the marker's position
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));

                //Method for on marker click event
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //  animate camera to marker position
                        animateCamera(marker.getPosition(), 15f, 500);
                        //Set the custom info window for marker
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                        //Show the InfoWindow
                        marker.showInfoWindow();
                        return true;
                    }
                });
            }
        } catch (IOException e) {
            Log.e("GeoAddress", "Failed to get location info", e);
        }
    }

    //Private Method to animate Camera
    private void animateCamera(LatLng latLng, float zoom, int time) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), time, null);


    }

    //Method to change the MapType on imageView Click
    public void ChangeMapType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            clearType.setVisibility(View.VISIBLE);
        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            clearType.setVisibility(View.VISIBLE);
        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            clearType.setVisibility(View.INVISIBLE);
        }
    }

    //Method to clear the map style and set it to Normal
    public void ClearMapStyle() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        clearType.setVisibility(View.INVISIBLE);
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
