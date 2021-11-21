package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Log extends AppCompatActivity {
    TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        IniciaComponent();

        query();

    }
    private void IniciaComponent(){
        log = (TextView) findViewById(R.id.log);
    }

    public void query(){

        try{
            CordenadaDao cordenadaDao = new CordenadaDao(this);
            Cursor dados = cordenadaDao.find();

            int indiceLatitude = dados.getColumnIndex("latitude");
            int indiceLongitude = dados.getColumnIndex("longitude");
            int indiceData = dados.getColumnIndex("data");
            while (dados.moveToNext()) {

                log.append(
                        "Data: " + dados.getString(indiceData)+"\n"+
                                "Latitude: " +dados.getDouble(indiceLatitude)+"\n"+
                                "Longitude: "+ dados.getDouble(indiceLongitude)+"\n\n\n"
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}