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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    CircleOptions mCircle;
    MarkerOptions maker;
    LatLng latLng;
    CameraPosition cameraPosition;

    private ActivityMapsBinding binding;
    public static final String SHARED_PREFES = "sharedPrefes";
    private String cordenada, velocidade, orientacao, trafego, tipo;

    //    private static final REQUEST_LAST_LOCATION = 1;
    //    private static final REQUEST_LOCATION_UPDATE = 2;
    private double longitude;
    private double latitude;
    private float speed;

    TextView longLat;
    TextView campVelocidade;

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
        switch (requestCode) {
            case 10: {
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
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            //Ultima Localizacaao
//            Location loc =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER, 500, 5 , locationListener);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                        atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Toast.makeText(MapsActivity.this, "O status do Provider", Toast.LENGTH_SHORT).show();
                }

                public void onProviderEnabled(String provider) {
                    Toast.makeText(MapsActivity.this, "Provider Habilitado", Toast.LENGTH_SHORT).show();
                }

                public void onProviderDisabled(String provider) {
                    Toast.makeText(MapsActivity.this, "Provider Desabilitado", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
                }
            };
            if (locationManager != null) {
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    locationManager.requestLocationUpdates(provider, 500, 1, locationListener);
                    // Location loc =  locationManager.getLastKnownLocation(provider);
                }
            }else{
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_UPDATE);
//                lastLocation();
            }

        }catch(SecurityException ex){

            Toast.makeText(this,"ERROR: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location) {

            if(location!=null)
            {

                getShared();

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
                mCircle = new CircleOptions();
                maker = new MarkerOptions();

                  mMap.clear();
                maker.position(latLng);
                maker.title("Minha Localizacao");
                maker.icon(BitmapDescriptorFactory.fromResource(R.drawable.voltar));
                maker.anchor(0.5f, 0.5f);
                maker.flat(true);

                mCircle.center(latLng);
                mCircle.radius(16);
                mCircle.strokeColor(R.color.red);
                mCircle.fillColor(0x1D0000F);
                mCircle.strokeWidth(location.getAccuracy());

                mMap.addMarker(maker).setAnchor(0.5f, 0.5f);
                mMap.addCircle(mCircle);

                mMap.getUiSettings().setScrollGesturesEnabled(false);

                String latSN = (int)latitude >= 0 ? "N" : "S";
                String lonEW = (int)longitude >= 0 ? "E" : "W";
                switch (cordenada) {
                    case "Grau-Minuto decimal":
                        String strLatitude = Location.convert(location.getLatitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + latSN;
                        String strLongitude = Location.convert(location.getLongitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + lonEW;
                        longLat.setText("LATITUDE: " + strLatitude + "\n\n" + "LONGITUDE: " + strLongitude + "\n");
                        break;
                    case "Grau-Minuto-Segundo decimal":
                        String strLatitudeSeconds = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + latSN;
                        String strLongitudeSeconds = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + lonEW;
                        longLat.setText("LATITUDE: " + strLatitudeSeconds + "\n\n" + "LONGITUDE: " + strLongitudeSeconds + "\n");
                        break;
                    case "Grau decimal":
                        longLat.setText("LATITUDE: " + latitude + "\n\n" + "LONGITUDE: " + longitude + "\n");
                        break;
                    default:
                        Toast.makeText(this,"Cordenadas Falhou", Toast.LENGTH_LONG).show();
                }

                switch (velocidade) {
                    case "Km/h (quilometro por hora)":
                        location.setSpeed(location.getSpeed()*3.6f);
                        speed = location.getSpeed();
                        campVelocidade.setText("velocidade(Km/h) " + speed + "\n");
                        break;
                    case "Mph (milhas por hora)":
                        location.setSpeed(location.getSpeed()*2.23694f);
                        speed = location.getSpeed();
                        campVelocidade.setText("velocidade(Mph) " + speed + "\n");
                        break;
                    default:
                        Toast.makeText(this,"Velocidade Falhou", Toast.LENGTH_LONG).show();
                }

                switch (orientacao) {
                    case "North Up (Norte do Mapa)":
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        if (mMap.getCameraPosition().zoom <= 6.5f) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.5f));
                        } else if (mMap.getCameraPosition().zoom > 6.5f) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        break;
                    case "Course Up (Topo do Mapa)":
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        if(mMap.getCameraPosition().zoom<=6.5f){

                            cameraPosition = new CameraPosition.Builder()
                                    .target(latLng)
                                    .bearing(location.getBearing())
                                    .zoom(18.0f)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                        }
                        else if(mMap.getCameraPosition().zoom>6.5f){
                            cameraPosition = new CameraPosition.Builder()
                                    .target(latLng)
                                    .bearing(location.getBearing())
                                    .zoom(18.0f)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                        }
                         break;
                    case "Nenhuma":
                        mMap.getUiSettings().setRotateGesturesEnabled(true);
                        mMap.getUiSettings().setScrollGesturesEnabled(false);
                        if(mMap.getCameraPosition().zoom<=6.5f){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
                        }
                        else if(mMap.getCameraPosition().zoom>6.5f){
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        break;
                    default:
                        Toast.makeText(this,"Orientacao Falhou", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Incapaz de buscar a localização atual", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();

//        mMap.addMarker(new MarkerOptions().position(new LatLng(-12.62987332, -38.67299200)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(-12.62987332, -38.67299200),18));
    }

    public void setUpMap(){
        try {
            getShared();

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

            mMap.setMyLocationEnabled(true);

        }catch (SecurityException e){
            Toast.makeText(this, "ERROR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
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