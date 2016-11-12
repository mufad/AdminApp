package com.droiddigger.adminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class AdminSolution extends AppCompatActivity {

    TextView titleTV, addressTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_solution);
        String title = getIntent().getStringExtra("title");
        String address=getIntent().getStringExtra("addrs");

        titleTV= (TextView) findViewById(R.id.problemName);
        addressTV= (TextView) findViewById(R.id.address);
        titleTV.setText(title);
        addressTV.setText(address);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
