package com.example.track;
import com.example.track.databinding.ActivityMapsBinding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location lastLocation;
    private LocationListener locationListener;
    private Circle mCircle = null;
    private Marker maker = null;
    private CameraPosition cameraPosition  = null;
    private LatLng latLng;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefsEditor;
    List<Location> local = new ArrayList<Location>();

    private ActivityMapsBinding binding;
    public static final String SHARED_PREFES = "sharedPrefes";
    private String cordenada, velocidade, orientacao, trafego, tipo;

    TextView longLat;
    TextView campVelocidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);

        IniciaComponent();
        getShared();
        pedirPermissoes();

        // Obtenha um identificador para o fragmento e registre o retorno de chamada.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    private void IniciaComponent(){
        longLat = (TextView) findViewById(R.id.longLat);
        campVelocidade = (TextView) findViewById(R.id.velocidade);
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
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    recreate();
                }
                else{
                    Toast.makeText(this, "Negado!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    public void configurarLastServico(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            Toast.makeText(this,  "-> "+lastLocation, Toast.LENGTH_LONG).show();

            //atualizar(lastLocation);


        }catch(SecurityException ex){
            Toast.makeText(this,"ERROR: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void configurarServico(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if(mMap != null){
                        mMap.clear();
                        atualizar(location);
                        local.add(location);
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //status do provedor
                }
                public void onProviderEnabled(String provider) {
                    //provedor habilitado
                }
                public void onProviderDisabled(String provider) {
                    //provider desabilitado
                }
            };
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.0f, locationListener);
                }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0.0f, locationListener);
                }
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

        }catch(SecurityException ex){

            Toast.makeText(this,"ERROR: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location) {
        if(location != null){
            float rotacao = location.getBearing();
            float acuracia = location.getAccuracy();
            LatLng origem = new LatLng(location.getLatitude(), location.getLongitude());
            float aprox = mMap.getCameraPosition().zoom;

            addMarcador(origem, rotacao);
            addCirculo(origem,acuracia);
            mudaOrientacao(rotacao, origem, aprox);
            mudaCordenada(location.getLatitude(), location.getLongitude());
            mudaVelocidade(location);

//            Toast.makeText(this, ""+rotacao, Toast.LENGTH_LONG).show();

        }

    }

    public void addMarcador(LatLng origem, float rotacao){
        this.maker = mMap.addMarker(new MarkerOptions()
                .position(origem)
                .flat(true)
                .rotation(rotacao)
                .title("Estou aqui")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.voltar))
                .anchor(0.5f, 0.5f)
        );
    }

    public void addCirculo(LatLng origem, float acuracia){
       mCircle = mMap.addCircle(new CircleOptions()
                        .center(origem)
                        .radius(acuracia)
                        .strokeColor(R.color.red)
                        .fillColor(0x00000000)
                      .strokeWidth(acuracia)
        );
    }

    public  void mudaCordenada(Double latitude, Double longitude){
        String latSN = latitude >= 0.f ? "N" : "S";
        String lonEW = longitude >= 0.f ? "E" : "W";
        switch (cordenada) {
            case "Grau-Minuto decimal":
                String strLatitude = Location.convert(latitude, Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + latSN;
                String strLongitude = Location.convert(longitude, Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + lonEW;
                longLat.setText("LATITUDE: " + strLatitude + "\n\n" + "LONGITUDE: " + strLongitude + "\n");
                break;
            case "Grau-Minuto-Segundo decimal":
                String strLatitudeSeconds = Location.convert(latitude, Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + latSN;
                String strLongitudeSeconds = Location.convert(longitude, Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + lonEW;
                longLat.setText("LATITUDE: " + strLatitudeSeconds + "\n\n" + "LONGITUDE: " + strLongitudeSeconds + "\n");
                break;
            case "Grau decimal":
                longLat.setText("LATITUDE: " + latitude + "\n\n" + "LONGITUDE: " + longitude + "\n");
                break;
            default:
                Toast.makeText(this,"Cordenadas Falhou", Toast.LENGTH_LONG).show();
        }
    }

    public void mudaOrientacao(float rotacao, LatLng origem, float aprox){
        switch (orientacao) {
            case "North Up (Norte do Mapa)":
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                if (mMap.getCameraPosition().zoom <= 6.5f) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origem, 18.5f));
                } else if (mMap.getCameraPosition().zoom > 6.5f) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(origem));
                }
                break;
            case "Course Up (Topo do Mapa)":
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                if(mMap.getCameraPosition().zoom<=6.5f){

                    cameraPosition = new CameraPosition.Builder()
                            .target(origem)
                            .bearing(rotacao)
                            .zoom(18.0f)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                }
                else if(mMap.getCameraPosition().zoom>6.5f){
                    cameraPosition = new CameraPosition.Builder()
                            .target(origem)
                            .bearing(rotacao)
                            .zoom(aprox)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                }
                break;
            case "Nenhuma":
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                if(mMap.getCameraPosition().zoom<=6.5f){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origem, 18.0f));
                }
                else if(mMap.getCameraPosition().zoom>6.5f){
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(origem));
                }
                break;
            default:
                Toast.makeText(this,"Orientacao Falhou", Toast.LENGTH_LONG).show();
        }
    }

    public void mudaVelocidade(Location location){
        switch (velocidade) {
            case "Km/h (quilometro por hora)":
                location.setSpeed(location.getSpeed()*3.6f);
                campVelocidade.setText("velocidade(Km/h) " + location.getSpeed() + "\n");
                break;
            case "Mph (milhas por hora)":
                location.setSpeed(location.getSpeed()*2.23694f);
                campVelocidade.setText("velocidade(Mph) " + location.getSpeed() + "\n");
                break;
            default:
                Toast.makeText(this,"Velocidade Falhou", Toast.LENGTH_LONG).show();
        }
    }

    public void mudaMapa(){
        if (tipo.equals("Vetorial")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        if (trafego.equals("Ligado")) {
            mMap.setTrafficEnabled(true);
        } else {
            mMap.setTrafficEnabled(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    public void setUpMap(){
        try {
            mMap.setMinZoomPreference(6.0f);
            mMap.setMaxZoomPreference(20.0f);

            mudaMapa();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(false);

            double latitude1 = -12.6208881;
            double longitude1 = -38.6745817;
            float rotacao = 90.0f;
            float acuracia = 18.0f;
            LatLng origem = new LatLng(-12.6208881, -38.6745817);

            addMarcador(origem, rotacao);
            addCirculo(origem,acuracia);
            longLat.setText("LATITUDE: " + latitude1 + "\n\n" + "LONGITUDE: " + longitude1 + "\n");
            campVelocidade.setText("VELOCIDADE:  0.0 " +"\n");
            addMarcador(origem, 0);

            cameraPosition = new CameraPosition.Builder()
                    .target(origem)
                    .bearing(rotacao)
                    .zoom(18.0f)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);

        }catch (SecurityException e){
            Toast.makeText(this, "ERROR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onPause(){
        super.onPause();
        mMap.clear();
        sharedPrefsEditor = sharedPreferences.edit();
        if (sharedPrefsEditor != null) {
            int i = 1;

            for (Location l : local) {
//                mMap.clear();
                float longitude = (float) l.getLongitude();
                float latitude = (float) l.getLatitude();
                sharedPrefsEditor.putFloat("Latitude" + i, latitude);
                sharedPrefsEditor.putFloat("Longitude" + i, longitude);
                i++;
                sharedPrefsEditor.putInt("Contador", i);
                sharedPrefsEditor.commit();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastLocal();
    }

    public void lastLocal() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            configurarLastServico();
        }
    }

    public void getShared(){
        cordenada = sharedPreferences.getString("cordenadas", "unknown");
        velocidade = sharedPreferences.getString("velocidade", "unknown");
        orientacao = sharedPreferences.getString("orientacao ", "unknown");
        trafego = sharedPreferences.getString("trafegon", "unknown");
        tipo = sharedPreferences.getString("tipo", "unknown");
    }
}