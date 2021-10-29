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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener  {
    private GoogleMap mMap;
    private Circle mCircle = null;
    private Marker maker = null;
    private CameraPosition cameraPosition  = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefsEditor;
    private SupportMapFragment mapFragment;
    List<Location> local = new ArrayList<Location>();

    private ActivityMapsBinding binding;
    public static final String SHARED_PREFES = "sharedPrefes";
    private String cordenada, velocidade, orientacao, trafego, tipo;

    /* Request code for location permission request.*/
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_LOCATION_UPDATES = 2;
    private static final int REQUEST_LAST_LOCATION = 1;

    // Classe central na API Fused Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    TextView longLat;
    TextView campVelocidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);
        IniciaComponent();
        getShared();

        // Obtenha um identificador para o fragmento e registre o retorno de chamada.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // atualiza informações na tela
        atualizar(null);

    }

    private void IniciaComponent(){
        longLat = (TextView) findViewById(R.id.longLat);
        campVelocidade = (TextView) findViewById(R.id.velocidade);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                habilitaMyLocation();
                finish();
            }
            else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this,"Sem permissão para mostrar sua localização",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_LOCATION_UPDATES) {
            if(grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                lastLocation();
                Toast.makeText(this,"ultima localizacao localização",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this,"Sem permissão para mostrar sua localização",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Clicou no botão de localização",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Localização Atual (Lat,Lon,Alt):\n"+
                "("+location.getLatitude()+","+location.getLongitude()+","
                +location.getAltitude()+")", Toast.LENGTH_LONG).show();
    }

    private void startLocationUpdates() {
        // Se a app já possui a permissão, ativa a calamada de localização
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            // Cria o cliente FusedLocation
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            // Configura solicitações de localização
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5*500);
            mLocationRequest.setFastestInterval(1*500);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location=locationResult.getLastLocation();


                    atualizar(location);

                    // usar na tela de historico
                    local.add(location);
                }
            };
            //
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES);
        }
    }

    private void stopLocationUpdates() {
        if (mFusedLocationProviderClient!=null)
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        atualizar(null);
    }

    private void lastLocation() {
        // Se a app já possui a permissão, ativa a calamada de localização
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    atualizar(location);
                }
            });
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LAST_LOCATION);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();

        habilitaMyLocation();
        startLocationUpdates();
    }

    @Override
    public void onPause(){
        super.onPause();
        mMap.clear();

        //usar na tela de historico
        sharedPrefsEditor = sharedPreferences.edit();
        if (sharedPrefsEditor != null) {
            int i = 1;

            for (Location l : local) {
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

    private void habilitaMyLocation() {
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            mMap.setMyLocationEnabled(true);
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
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

            double latitude1 = -12.6208881;
            double longitude1 = -38.6745817;
            float rotacao = 90.0f;
            float acuracia = 18.0f;
            LatLng origem = new LatLng(-12.6208881, -38.6745817);

            addMarcador(origem, rotacao);
            addCirculo(origem,acuracia);
            longLat.setText("LATITUDE: " + latitude1 + "\n\n" + "LONGITUDE: " + longitude1 + "\n");
            campVelocidade.setText("VELOCIDADE:  0.0 " +"\n");

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

    public void atualizar(Location location) {
//                    Toast.makeText(this, ""+location, Toast.LENGTH_LONG).show();
        if(location != null){
            float rotacao = location.getBearing();
            float acuracia = location.getAccuracy();
            LatLng origem = new LatLng(location.getLatitude(), location.getLongitude());
            float aprox = mMap.getCameraPosition().zoom;

            mCircle.setCenter(origem);
            mCircle.setRadius(acuracia);

            maker.setPosition(origem);
            maker.setRotation(rotacao);

            mudaOrientacao(rotacao, origem, aprox);
            mudaCordenada(location.getLatitude(), location.getLongitude());
            mudaVelocidade(location);

//            Toast.makeText(this, " "+ location.getBearing(), Toast.LENGTH_LONG).show();

        }

    }

    //----------------------- Configuracoes com shared preferes e atualizacao do mapa--------------
    public void getShared(){
        cordenada = sharedPreferences.getString("cordenadas", "unknown");
        velocidade = sharedPreferences.getString("velocidade", "unknown");
        orientacao = sharedPreferences.getString("orientacao ", "unknown");
        trafego = sharedPreferences.getString("trafegon", "unknown");
        tipo = sharedPreferences.getString("tipo", "unknown");
    }

    public void addMarcador(LatLng origem, float rotacao){
        maker = mMap.addMarker(new MarkerOptions()
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
                mMap.getUiSettings().setAllGesturesEnabled(false);;
                if (mMap.getCameraPosition().zoom <= 6.5f) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origem, 18.5f));
                } else if (mMap.getCameraPosition().zoom > 6.5f) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(origem));
                }
                break;
            case "Course Up (Topo do Mapa)":
                mMap.getUiSettings().setAllGesturesEnabled(false);
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
                mMap.getUiSettings().setAllGesturesEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
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

    //----------------------------------------------------------------------------------------------

}