package com.example.track;
import com.example.track.databinding.ActivityMapsBinding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    public static final String SHARED_PREFES = "sharedPrefes";
    private String cordenada, velocidade, orientacao, trafego, tipo;

    //    private static final REQUEST_LAST_LOCATION = 1;
    //    private static final REQUEST_LOCATION_UPDATE = 2;
    private double longitude;
    private double latitude;
    TextView longLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        IniciaComponent();
        pedirPermissoes();


        // Obtenha um identificador para o fragmento e registre o retorno de chamada.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void IniciaComponent(){
        longLat = (TextView) findViewById(R.id.longLat);
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
                    finish();
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
                    locationManager.requestLocationUpdates(provider, 1l, 1f, locationListener);
                }
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1l, 1f, locationListener);

            }
//            locationManager.removeUpdates(this);

        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location) {

        if(location!=null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                    .title("Minha Localizacao").icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15F));

            Log.d("long: ", " " + longitude);
            Log.d("lat: ", " " + latitude);
            Log.d("tipo: ", " " + tipo);
            Log.d("trafego: ", " " + trafego);

            longLat.setText("LONGITUDE: " + longitude +
                    "\n\n" + "LATITUDE: " + latitude +
                    "\n\n" + "velocidade(m/s) " + location.getSpeed() +
                    "\n\n" + "Rumo(graus) " + location.getBearing() +
                    "\n\n" + "Acuracia(metros) " + location.getAccuracy());

        }
        else
        {
            Toast.makeText(this, "Incapaz de buscar a localização atual", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    public void setUpMap(){
        getShared();

        if(tipo.equals("Vetorial")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }


        if(trafego.equals("Ligado")){
            mMap.setTrafficEnabled(true);
            mMap.isTrafficEnabled();
        }else{
            mMap.setTrafficEnabled(false);
            mMap.isTrafficEnabled();
        }

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onPause() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(false);
            mMap.setTrafficEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
        }
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getShared(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);

        cordenada = sharedPreferences.getString("cordenadas", "unknown");
        velocidade = sharedPreferences.getString("velocidade", "unknown");
        orientacao = sharedPreferences.getString("orientacao ", "unknown");
        trafego = sharedPreferences.getString("trafegon", "unknown");
        tipo = sharedPreferences.getString("tipo", "unknown");
    }

}