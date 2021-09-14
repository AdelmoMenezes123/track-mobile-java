package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Creditos extends AppCompatActivity {

    TextView volta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        IniciaComponent();
        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Creditos.this, Home.class);
                startActivity(in);
            }
        });
    }
    private void IniciaComponent(){
        volta = (TextView) findViewById(R.id.voltar);
    }
}