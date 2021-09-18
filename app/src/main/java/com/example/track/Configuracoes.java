package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Configuracoes extends AppCompatActivity {

    //SELECTS GROUP
    RadioGroup groupCordenada;
    RadioGroup groupVelocidade;
    RadioGroup groupOrientacao;
    RadioGroup groupTipo;
    RadioGroup groupTrafego;

    //RADIO BUTTON
    RadioButton radioCordenadas;
    RadioButton radioVelocidade;
    RadioButton radioOrientacao;
    RadioButton radioTipo;
    RadioButton radioTrafegon;

    // BUTOES LINKS
    View buttonEnviar;
    TextView voltaHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        // INICIANDO OS COMPONENTES
        IniciaComponent();

        //CLICK DO LINK VOLTAR A TELA ANTERIIOR
        voltaHome.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent in = new Intent(Configuracoes.this, Home.class);
                  startActivity(in);
              }
        });


        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> lista = new ArrayList();
                lista.add(radioCordenadas.getText().toString());
                lista.add(radioVelocidade.getText().toString());
                lista.add(radioOrientacao.getText().toString());
                lista.add(radioTipo.getText().toString());
                lista.add(radioTrafegon.getText().toString());

//                for (int i = 0; i < lista.size(); i++) {
//                lista.get(i);
//                   new Toast.makeText(this, "sua lista : " + lista.get(i), Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }
    private void IniciaComponent(){
        groupCordenada = findViewById(R.id.radio_cordenadas);
        groupVelocidade = findViewById(R.id.radio_Velocidade);
        groupOrientacao = findViewById(R.id.radio_orientacao);
        groupTipo = findViewById(R.id.radio_tipo);
        groupTrafego = findViewById(R.id.radio_trafego);

        buttonEnviar = (View)findViewById(R.id.enviar);
        voltaHome = (TextView) findViewById(R.id.voltar);
    }

    public void checkButton1(View v){
        int radioId1 = groupCordenada.getCheckedRadioButtonId();
        radioCordenadas = findViewById(radioId1);
        Toast.makeText(this, "Voce seleciocou : " + radioCordenadas.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkButton2(View v){
        int radioId2 = groupVelocidade.getCheckedRadioButtonId();
        radioVelocidade = findViewById(radioId2);
        Toast.makeText(this, "Voce seleciocou : " + radioVelocidade.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkButton3(View v){
        int radioId3 = groupOrientacao.getCheckedRadioButtonId();
        radioOrientacao = findViewById(radioId3);
        Toast.makeText(this, "Voce seleciocou : " + radioOrientacao.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkButton4(View v){
        int radioId4 = groupTipo.getCheckedRadioButtonId();
        radioTipo = findViewById(radioId4);
        Toast.makeText(this, "Voce seleciocou : " + radioTipo.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkButton5(View v){
        int radioId5 = groupTrafego.getCheckedRadioButtonId();
        radioTrafegon = findViewById(radioId5);
        Toast.makeText(this, "Voce seleciocou : " + radioTrafegon.getText(), Toast.LENGTH_SHORT).show();
    }
}