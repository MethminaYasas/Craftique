package com.s23010903.craftique;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final LatLng shopLocation = new LatLng(6.9271, 79.8612); // Craftique Store
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add shop marker
        Marker shopMarker = mMap.addMarker(new MarkerOptions()
                .position(shopLocation)
                .title("Craftique Store")
                .snippet("We’re here! Come visit us.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        );

        // Move camera to shop
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 14));

        // Marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.equals(shopMarker)) {
                Toast.makeText(this, "Craftique Store: We’re here! 📍", Toast.LENGTH_SHORT).show();
            }
            return false; // show default info window
        });

        // Enable user location
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Get current location and optionally show it
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Optional: Zoom to show both user and shop
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(shopLocation);
                builder.include(userLatLng);
                LatLngBounds bounds = builder.build();

                int padding = 120; // padding in pixels
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                Toast.makeText(this, "Your current location is shown with a blue dot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            Toast.makeText(this, "Location permission required to show your position", Toast.LENGTH_SHORT).show();
        }
    }
}
