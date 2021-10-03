package com.example.track;
import com.example.track.databinding.ActivityMapsBinding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private double longitude;
    private double latitude;
    TextView volta, longLat;
    Button navegacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        IniciaComponent();

        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MapsActivity.this, Home.class);
                startActivity(in);
            }
        });

        pedirPermissoes();
        navegacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedirPermissoes();
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
    private void IniciaComponent(){
        volta = (TextView) findViewById(R.id.voltar);
        longLat = (TextView) findViewById(R.id.longLat);
        navegacao = (Button) findViewById(R.id.navegacao);
    }

    private void pedirPermissoes() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            configurarServico();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if(location.getAccuracy() < 25.0){
                        atualizar(location);
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.v("onStatusChanged: ", "Status changed: " + provider);
                    Log.v("onStatusChanged: ", "Status changed: " + status);
                    Log.v("onStatusChanged: ", "Status changed: " + extras);

                }

                public void onProviderEnabled(String provider) {
                    Log.v("onProviderEnabled: ", "Status changed: " + provider);
                }

                public void onProviderDisabled(String provider) {
                    Log.v("onProviderDisabled: ", "Status changed: " + provider);
                }
            };
            if (locationManager != null) {
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    locationManager.requestLocationUpdates(provider, 2l, 1f, locationListener);
                }
            }
            locationManager.removeUpdates(this);

//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2l, 1f, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(@NonNull Location location) {
         longitude = location.getLongitude();
         latitude = location.getLatitude();

        Log.d("long: "," "+longitude);
        Log.d("lat: "," "+latitude);

        if(longitude != 0.0  && latitude != 0.0) {
            // Get a handle to the fragment and register the callback.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        longLat.setText("LONGITUDE: " + longitude + "\n\n" + "LATITUDE: " + latitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set the map coordinates to Kyoto Japan.
        LatLng local = new LatLng(latitude, longitude);
        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // aqui e o tipo de mapa
        // Add a marker on the map coordinates.
        googleMap.addMarker(new MarkerOptions()
                .position(local)
                .title("Em algum Lugar do mundo !"));
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        googleMap.setTrafficEnabled(true);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }


}