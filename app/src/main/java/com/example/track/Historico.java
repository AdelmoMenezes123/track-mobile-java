package com.example.track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Historico extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private MarkerOptions origem;
    private MarkerOptions destino;
    private  CircleOptions mCircle;
    Button historicoBtn;
    ArrayList<Cordenadas_ll> latitudeLatitude = new ArrayList<Cordenadas_ll>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        iniciaComponent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        historicoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Historico.this, Log.class);
                startActivity(in);
            }
        });

        query();
    }

    public void iniciaComponent(){
        historicoBtn = (Button) findViewById(R.id.historicoBtn);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        origem =  new MarkerOptions();
        destino =  new MarkerOptions();
        mCircle = new CircleOptions();

        int height = 100;
        int width = 100;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gnss);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        origem.position(new LatLng(latitudeLatitude.get(0).getLatitude(),latitudeLatitude.get(0).getLongitude()));
        origem.icon(smallMarkerIcon);
        origem.rotation(0.5f);
        origem.title("Ponto inicial");


        destino.position(new LatLng(
                latitudeLatitude.get(latitudeLatitude.size() - 1).getLatitude(),
                latitudeLatitude.get(latitudeLatitude.size() - 1).getLongitude() ));
        destino.title("Ponto Final");

            mMap.addMarker(origem);
            mMap.addMarker(destino);

        PolylineOptions recOption = new PolylineOptions()
                .width(10)
                .color(R.color.purple);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if(latitudeLatitude.size() >0) {


            for (int i = 0; i < latitudeLatitude.toArray().length; i++) {

                LatLng ponto = new LatLng(latitudeLatitude.get(i).getLatitude(), latitudeLatitude.get(i).getLongitude());


                recOption.add(ponto);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ponto,18));
            }

            Polyline polyline = mMap.addPolyline(recOption);

            polyline.isClickable();

            mMap.addPolyline(recOption);

        }else{


        }

    }

    public void query(){

        CordenadaDao cordenadaDao = new CordenadaDao(this);
        Cursor dados = cordenadaDao.find();

        int indiceLatitude = dados.getColumnIndex("latitude");
        int indiceLongitude = dados.getColumnIndex("longitude");
        int indiceData = dados.getColumnIndex("data");
        while (dados.moveToNext()) {
            Cordenadas_ll cordenadas_ll = new Cordenadas_ll(
                    dados.getDouble(indiceLatitude),
                    dados.getString(indiceData),
                    dados.getDouble(indiceLongitude)
            );
            latitudeLatitude.add(cordenadas_ll);

        }

    }
}