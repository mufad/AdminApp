package com.droiddigger.adminapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng dhk;
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    UiSettings us;
    View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




//        ?????????????????????????????????????????????????????????????????????????????????/

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
        dhk = new LatLng(23.8103, 90.4125);
        mMap.addMarker(new MarkerOptions().position(dhk).title("Amar Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhk));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dhk, 12.0f));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                AskPermission();
                mMap.clear();
                String addressString;
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getCountryName());
                    }
                    addressString = sb.toString();
                } catch (Exception e) {
                    addressString = "";
                    Toast.makeText(MapsActivity.this, "Error Detected!See LOGCAT for details!!", Toast.LENGTH_SHORT).show();
                    Log.d("LSN", e.toString());
                }
                mMap.addCircle(new CircleOptions().center(latLng).radius(1000.0).strokeColor(0xff00ff00)
                        .strokeWidth(10).visible(true));
                mMap.addMarker(new MarkerOptions().position(dhk).title("Home _ Dhaka"));
                Toast.makeText(MapsActivity.this, addressString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}