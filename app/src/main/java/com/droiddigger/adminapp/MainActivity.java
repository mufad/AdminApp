package com.droiddigger.adminapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static String lng;
    public static String lat;
    private View mLayout;
    private int REQUEST_LOCATION_FINE = 1;

    private StorageReference mStorage;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private GoogleApiClient mGoogleApiClient;
    private Button mSignInButton;
    Uri file;
    String fileString;
    ImageView img;
    CoordinatorLayout coordinatorLayout;
    String productString = "posts";
    String strDate = "";
    CheckBox checkBox;

    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_INTENT = 3;
    private ProgressDialog mProgressDialog;
    EditText problem, solution;
    String problemString, solutionString, latString, longString, urgencyString, timeStamp, solveDate, etDay;
    private Location mLastLocation;
    LocationManager locationManager;
    private String provider;
    private LocationManager mLocationManager;
     Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.activity_main);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        problem = (EditText) findViewById(R.id.problemTitle);
        solution = (EditText) findViewById(R.id.solution);
        mSignInButton = (Button) findViewById(R.id.button3);
        mSignInButton.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build();
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Hi " + mFirebaseUser.getDisplayName(), Snackbar.LENGTH_LONG);
        snackbar.show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        strDate = mdformat.format(calendar.getTime());
        Log.e("TAG", strDate);

        mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
        location = find_Location(MainActivity.this);
        latString=location.getLatitude()+"";
        longString=location.getLongitude()+"";
        lat=latString;
        lng=longString;
        Toast.makeText(MainActivity.this,latString+" "+longString,Toast.LENGTH_SHORT).show();

        //Register Device With FireBase Cloud Messaging For Push Notification
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
    }
    public Location find_Location(Context con) {
        Log.d("Find Location", "in find_location");

        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) con.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {}

                        public void onProviderDisabled(String provider) {}

                        public void onProviderEnabled(String provider) {}

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {}
                    });
            location = locationManager.getLastKnownLocation(provider);

        }
        return location;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(mFirebaseUser.getDisplayName());
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void select(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions


        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            img.setImageURI(uri);
            final StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());
            showProgressDialog();
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    file = taskSnapshot.getDownloadUrl();
                    fileString = file.toString();
                    hideProgressDialog();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Product photo uploaded", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }
            });
        } else if (requestCode == CAMERA_INTENT && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
            Uri uri = getImageUri(MainActivity.this, bitmap);
            Log.e("URI", uri.toString());
            final StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());
            showProgressDialog();
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    file = taskSnapshot.getDownloadUrl();
                    fileString = file.toString();
                    hideProgressDialog();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Product photo uploaded", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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

    //Add lat and longs before this one
    @Override
    public void onClick(View view) {
        problemString = problem.getText().toString();
        solutionString = solution.getText().toString();


        String key = mFirebaseDatabaseReference.child(productString).push().getKey();
        //Product product = new Product(name,price,tag, key, fileString);
        if (checkBox.isChecked()) {
            urgencyString = "urgent";
        }
        Post_Item post = new Post_Item(problemString, fileString, mFirebaseUser.getDisplayName(), "5days", "status", latString, longString, strDate, "thursday", key, solutionString, urgencyString);
        Map<String, Object> values = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(productString + "/" + key, values);

        Log.d("PRODUCT", productString);
        mFirebaseDatabaseReference.updateChildren(childUpdates);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Product Added", Snackbar.LENGTH_SHORT);
        snackbar.show();

    }

    public void openCamera(View view) {
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, CAMERA_INTENT);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}

