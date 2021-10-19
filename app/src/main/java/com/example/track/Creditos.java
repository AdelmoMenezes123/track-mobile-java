package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Creditos extends AppCompatActivity {

    TextView versaoSDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        IniciaComponent();

        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;

        versaoSDK.setText("Versão do Android: " + release +"   "+ "SDK: "+ sdkVersion);
    }
    private void IniciaComponent(){
        versaoSDK = (TextView) findViewById(R.id.versaoSDK);
    }
}