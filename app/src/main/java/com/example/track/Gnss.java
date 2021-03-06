package com.example.track;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.GnssStatusCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Gnss extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private GnssStatusCallback gnssStatusCallback;
    private SharedPreferences sharedPrefs;
    private TextView txtLatitude, txtLongitude, txtVelocidade, txtBearing;
    private cGnssView circuloGnss;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnss);
        circuloGnss=findViewById(R.id.circuloGnss);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
//        sharedPrefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        instaciaTxt();
        ativaGNSS();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStop() {
        super.onStop();
        desativaGNSS();
    }

    public void instaciaTxt(){
        txtLatitude = (TextView) findViewById(R.id.text_latitudeGnss);
        txtLongitude = (TextView) findViewById(R.id.text_longitudeGnss);
        txtVelocidade = (TextView) findViewById(R.id.text_VelocidadeGnss);
        txtBearing = (TextView) findViewById(R.id.text_BearingGnss);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void desativaGNSS() {
        // locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ativaGNSS(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Gnss.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(locationProvider.getName(),
                5*1000,
                0.1f,
                Gnss.this);
        gnssStatusCallback = new GnssStatusCallback();
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ativaGNSS();
                }
                else{
                    Toast.makeText(this, "Localiza????o desativada", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    public void escreverNaBarraDeStatusLocalizacao(double latitude, double longitude, double velocidade, double bearing){
        txtLatitude.setText(trataCoordenadas(latitude));
        txtLongitude.setText(trataCoordenadas(longitude));
        txtVelocidade.setText(trataVelocidade(velocidade));
        txtBearing.setText(String.valueOf(bearing));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        escreverNaBarraDeStatusLocalizacao(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing());
    }

    public String formatarFloat(double numero){
        String retorno = "";

            DecimalFormat formatter = new DecimalFormat("0.00");
            try{
                retorno = formatter.format(numero);
            }catch(Exception ex){
                System.err.println("Erro ao formatar numero: " + ex);
            }
        return retorno;
    }

    public String trataVelocidade(double velo){
        String result = "";
        double velocidade = velo;

            double Km = velocidade*3.60;
            result = formatarFloat(Km) + " Km/h";

        return result;
    }

    public String trataCoordenadas(double lat){
        double valGrau, valMin, valSeg = 0;
        String result = "";
        String latnormal = "";
        String retorno = "";
        double aux = lat;
        lat = Math.abs(lat);

            latnormal = Location.convert(lat, Location.FORMAT_DEGREES).replace(",", ".");
            result = latnormal + "??";

            //para grau, minutos e segundos
            valGrau = Math.floor(lat);
            result = formatarFloat(valGrau) + "??";

            valMin = Math.floor((lat - valGrau) * 60);
            result += formatarFloat(valMin) + "'";

            valSeg = Math.round((lat - valGrau - valMin / 60) * 3600 * 1000) / 1000;
            result += formatarFloat(valSeg) + "''";

        //testando se a coordenada original ?? positiva ao negativa
        if(aux <= 0){
            retorno = "-" + result;
        }
        else{
            retorno = result;
        }
        return retorno;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private class GnssStatusCallback extends GnssStatus.Callback{
        public GnssStatusCallback(){
            super();
        }
        @Override
        public void onStarted(){
        }
        @Override
        public void onStopped(){
        }
        @Override
        public void onFirstFix(int ttffMillis){
        }
        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            circuloGnss.onSatelliteStatusChanged(status);
            circuloGnss.invalidate();
        }
    }
}