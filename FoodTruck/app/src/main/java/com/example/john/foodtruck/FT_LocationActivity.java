package com.example.john.foodtruck;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FT_LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    double latitude;
    double longitude;
    double FTlatitude;
    double FTlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__location);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.FTMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        FTlatitude = Double.parseDouble(intent.getStringExtra("FTlat"));
        FTlongitude = Double.parseDouble(intent.getStringExtra("FTlon"));
        latitude=intent.getDoubleExtra("lat",100);
        longitude=intent.getDoubleExtra("lon",100);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Marker marker1,marker2;
        mMap=map;

        LatLng location = new LatLng(latitude,longitude );
        LatLng FTlocation = new LatLng(FTlatitude,FTlongitude);



        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        marker1=mMap.addMarker(markerOptions);
        marker1.showInfoWindow();

        final MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(FTlocation);
        markerOptions2.title("푸드트럭 위치");
        marker2=mMap.addMarker(markerOptions2);
        marker2.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(FTlocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
