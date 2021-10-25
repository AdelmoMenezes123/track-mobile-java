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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    private ActivityMapsBinding binding;
    public static final String SHARED_PREFES = "sharedPrefes";
    private String cordenada, velocidade, orientacao, trafego, tipo;

    //    private static final REQUEST_LAST_LOCATION = 1;
    //    private static final REQUEST_LOCATION_UPDATE = 2;
    private double longitude;
    private double latitude;
    float speed;
    private CircleOptions mCircle = new CircleOptions();
    private MarkerOptions maker = new MarkerOptions();
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
                    Toast.makeText(MapsActivity.this, "O status do Provider 1 ", Toast.LENGTH_SHORT).show();
                }

                public void onProviderEnabled(String provider) {
                    Toast.makeText(MapsActivity.this, "Provider Habilitado", Toast.LENGTH_SHORT).show();
                }

                public void onProviderDisabled(String provider) {
                    Toast.makeText(MapsActivity.this, "Provider Desabilitado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            };
            if (locationManager != null) {
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    locationManager.requestLocationUpdates(provider, 5000, 0, locationListener);
                    // Location loc =  locationManager.getLastKnownLocation(provider);
                }
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
                LatLng latLng = new LatLng(latitude, longitude);

                try {

                    mMap.clear();

                    maker.position(latLng).title("Minha Localizacao");
                    mMap.addMarker(maker).setAnchor(0.5f, 0.5f);

                    mCircle.center(latLng);
                    mCircle.radius(25);
                    mCircle.strokeColor(R.color.red);
                    mCircle.fillColor(0x1D0000F);
                    mCircle.strokeWidth(location.getAccuracy());

                    mMap.addCircle(mCircle);
                }catch (Exception e){
                    e.printStackTrace();
                }

                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setScrollGesturesEnabled(false);

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

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
                        longLat.setText("Falhou");
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

            if(orientacao.equals("North Up (Norte do Mapa)")){mMap.getUiSettings().setRotateGesturesEnabled(false);}
            if(orientacao.equals("Course Up (Topo do Mapa)")){mMap.getUiSettings().setRotateGesturesEnabled(false);}
            if(orientacao.equals("Nenhuma")){}

            mMap.setMyLocationEnabled(true);

        }catch (SecurityException e){
            Toast.makeText(this, "ERROR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
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


    public static String getLatitudeAsDMS(Location location, int decimalPlace){
        String strLatitude = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS);
        strLatitude = replaceDelimiters(strLatitude, decimalPlace);
        strLatitude = strLatitude + " N";
        return strLatitude;
    }

    public static String getLongitudeAsDMS(Location location, int decimalPlace){
        String strLongitude = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS);
        strLongitude = replaceDelimiters(strLongitude, decimalPlace);
        strLongitude = strLongitude + " W";
        return strLongitude;
    }

    @NonNull
    private static String replaceDelimiters(String str, int decimalPlace) {
        str = str.replaceFirst(":", "°");
        str = str.replaceFirst(":", "'");
        int pointIndex = str.indexOf(".");
        int endIndex = pointIndex + 1 + decimalPlace;
        if(endIndex < str.length()) {
            str = str.substring(0, endIndex);
        }
        str = str + "\"";
        return str;
    }

}