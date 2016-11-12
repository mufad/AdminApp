package com.droiddigger.adminapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener{

    LatLng dhk;
//    Geocoder geocoder ;//= new Geocoder(MapsActivity.this, Locale.getDefault());
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    UiSettings us;
    View mLayout;

    ArrayList<Post> postList=new ArrayList<Post>();

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        postList= (ArrayList<Post>) getIntent().getSerializableExtra("list");
        //Log.e("LIST_SIZE", postList.size()+"");
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(23.7100,90.4039)).title("Emmergency Problem").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(23.7100,90.4039)).title("Road Break Problem").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(new MarkerOptions().position(dhk).title("Amar Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhk));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dhk, 12.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title=marker.getTitle();
                LatLng location=marker.getPosition();

                String addressString=getAddressFromLatLng(location);

//                    Toast.makeText(MapsActivity.this,"Tapped",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MapsActivity.this,AdminSolution.class);
                intent.putExtra("title",title);
                intent.putExtra("addrs", addressString);
                return true;
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                AskPermission();
                mMap.clear();
                String addressString=getAddressFromLatLng(latLng);
                mMap.addCircle(new CircleOptions().center(latLng).radius(1000.0).strokeColor(0xff00ff00)
                        .strokeWidth(10).visible(true));
                mMap.addMarker(new MarkerOptions().position(dhk).title("Home _ Dhaka"));
                Toast.makeText(MapsActivity.this, addressString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        String addressString;
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
        return addressString;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                startActivity(new Intent(this, SIgninActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}