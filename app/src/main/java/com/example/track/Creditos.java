package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Creditos extends AppCompatActivity {

    TextView volta, versaoSDK;
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

        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;

        versaoSDK.setText("Versão do Android: " + release +"   "+ "SDK: "+ sdkVersion);
    }
    private void IniciaComponent(){
        volta = (TextView) findViewById(R.id.voltar);
        versaoSDK = (TextView) findViewById(R.id.versaoSDK);
    }
}