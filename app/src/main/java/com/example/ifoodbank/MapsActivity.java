package com.example.ifoodbank;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private String location;
    private String foodbankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        location = getIntent().getStringExtra("location");
        foodbankName = getIntent().getStringExtra("foodbankName");

        System.out.println(location);
        System.out.println(foodbankName);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(location, 1);
        }
        catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: "+e.getMessage());
        }
        if(list.size()>0){
            Address address1 = list.get(0);
            Log.d(TAG, "getLocate: found a location: "+address1.toString());
            double latitude = address1.getLatitude();
            double longitude = address1.getLongitude();

            // Add a marker in Sydney and move the camera
            LatLng foodbank = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(foodbank).title(foodbankName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(foodbank, 15f));
        }
    }
}