package com.example.victoria.pearlhacks_1_2_3_4cast;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity_Locator extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity__locator);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GoogleMap map = mapFragment.getMap();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity_Locator.this, Temperature.class);
                startActivity(intent);
            }
        });

        View v = findViewById(R.id.map_touch_layer);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                Intent intent = new Intent(MapsActivity_Locator.this, Temperature.class);
                startActivity(intent);
                return false;
            }
        });

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

        // Add a marker in Sydney and move the camera
        LatLng chapelhill = new LatLng(35.9132, -79.0558);
        mMap.addMarker(new MarkerOptions().position(chapelhill).title("Marker in Chapel Hill"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chapelhill));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(MapsActivity_Locator.this, Temperature.class);
        startActivity(intent);
    }

    //public void temperature(View view){
      //  Intent intent = new Intent(Intent.)
    //}
}
