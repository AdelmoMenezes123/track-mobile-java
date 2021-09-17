package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    View entrarConfig;
    View entrarCredito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        IniciaComponent();
        entrarConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Home.this, Configuracoes.class);
                startActivity(in);
            }
        });

        entrarCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Home.this, Creditos.class);
                startActivity(in);
            }
        });
    }
    private void IniciaComponent(){
        entrarConfig = (View) findViewById(R.id.configuracoes);
        entrarCredito = (View) findViewById(R.id.creditos);
    }
}