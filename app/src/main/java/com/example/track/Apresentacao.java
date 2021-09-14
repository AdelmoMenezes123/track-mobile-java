package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Apresentacao extends AppCompatActivity {

    Button entrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);

        IniciaComponent();

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Apresentacao.this, Home.class);
                startActivity(in);
            }
        });
    }

    private void IniciaComponent(){
        entrar = (Button) findViewById(R.id.buttonApresentacao);
    }
}