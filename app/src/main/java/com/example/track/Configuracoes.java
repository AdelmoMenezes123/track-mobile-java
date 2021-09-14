package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Configuracoes extends AppCompatActivity {

    TextView voltaHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        IniciaComponent();
        voltaHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Configuracoes.this, Home.class);
                startActivity(in);
            }
        });
    }
    private void IniciaComponent(){
        voltaHome = (TextView) findViewById(R.id.voltar);
    }
}