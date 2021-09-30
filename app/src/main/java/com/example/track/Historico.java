package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;

public class Historico extends AppCompatActivity {

    TextView mList, volta;
    public static final String SHARED_PREFES = "sharedPrefes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        IniciaComponent();
        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Historico.this, Home.class);
                startActivity(in);
            }
        });

        //METODO QUE LISTA OS DADOS DO SHARED PREFES E ADD NA TELA
        getListaShared();

    }

    private void IniciaComponent() {
        volta = (TextView) findViewById(R.id.voltar);
        mList=(TextView)findViewById(R.id.tv_list);
    }


    public void getListaShared(){
        // INICIANDO MEU SHERED PREFERECES
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);

        //OBTER DATA ATUAL
        Date d = new Date();
        CharSequence data  = DateFormat.format("d, MMMM, yyyy ", d.getTime());

        //OBTENDO OS DADOS DO SHERED PREFERES
        String cordenada = sharedPreferences.getString("cordenadas", "unknown");
        String velocidade = sharedPreferences.getString("velocidade", "unknown");
        String orientacao = sharedPreferences.getString("orientacao ", "unknown");
        String trafego = sharedPreferences.getString("trafegon", "unknown");
        String tipo =sharedPreferences.getString("tipo", "unknown");

        //ADD OS DADOS EM UM ARRAY
        String[] list = {
                ""+data+"",
                "Cordenadas: "+cordenada,
                "Velocidade: "+velocidade,
                "Orientacao: "+orientacao,
                "Trafego: "+trafego,
                "Tipo: "+tipo
        };

        // PERCORRENDO O ARRAY E LISTANDO NA TELA
        for( String toys: list){
            mList.append(toys+"\n\n");
        }

    }
}