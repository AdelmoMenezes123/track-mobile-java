package com.example.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

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

    public static final String SHARED_PREFES = "sharedPrefes";

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

        getListaShared();

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // INICIANDO OS COMPONENTES DO BUTON RADIO
                initComponentesToLista();

                //INICIANDO A LISTA OS VALORES SELECIONADO NA CONFIG
                String[] lista = {
                        radioCordenadas.getText().toString(),
                        radioVelocidade.getText().toString(),
                        radioOrientacao.getText().toString(),
                        radioTrafegon.getText().toString(),
                        radioTipo.getText().toString()
                };

                //PERCORRENDO A LISTA E MOSTRANDO OS VALORES NA TELA USANDO TOAST
                for (String itens: lista) {
                    Toast.makeText(getApplicationContext(), itens, Toast.LENGTH_SHORT).show();
                }

                //SALVANDOS OS DADOS
                saveArrayList(lista);

                // VOLTANDO A PAGINA HOME
                Intent in = new Intent(Configuracoes.this, Home.class);
                startActivity(in);
            }
        });

    }

    //PEGANDO TODOS IDs  QUE TEM NO 'RADIO-GRUOP' E PESQUISANDO OS VALORES NO 'RADIO-BUTTON'
    public void initComponentesToLista(){
        int radioId;
        radioId = groupCordenada.getCheckedRadioButtonId();
        radioCordenadas = findViewById(radioId);

         radioId = groupVelocidade.getCheckedRadioButtonId();
        radioVelocidade = findViewById(radioId);

        radioId = groupOrientacao.getCheckedRadioButtonId();
        radioOrientacao = findViewById(radioId);

        radioId = groupTipo.getCheckedRadioButtonId();
        radioTipo = findViewById(radioId);

        radioId = groupTrafego.getCheckedRadioButtonId();
        radioTrafegon = findViewById(radioId);

    }

   //INICIANDO OS COMPONENTES PRINCIPAIS ... VIEW, TEXT, GROUP
    private void IniciaComponent(){
        groupCordenada = findViewById(R.id.radio_cordenadas);
        groupVelocidade = findViewById(R.id.radio_Velocidade);
        groupOrientacao = findViewById(R.id.radio_orientacao);
        groupTipo = findViewById(R.id.radio_tipo);
        groupTrafego = findViewById(R.id.radio_trafego);

        buttonEnviar = (View)findViewById(R.id.enviar);
        voltaHome = (TextView) findViewById(R.id.voltar);
    }

    // METODOS PARA CHECAR BOTOES E MOSTRAR QUANDO  ESCOLHER
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
    //FIM METODO CHECA BOTOES

    // METODO PARA SALVAR MINHA LISTA COMO UM JSON
    public void saveArrayList(String[] l){
        //CRIANDO O PREFERE SHERED
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //INSTANCIA O "JSON"
        Gson gson = new Gson();

        //SERIALIZA OS VALORES E ARMAZENA NAS DEVIDAS CHAVES ( KEY, VALUE )
        editor.putString("cordenadas", gson.toJson(l[0]));
        editor.putString("velocidade", gson.toJson(l[1]));
        editor.putString("orientacao ", gson.toJson(l[2]));
        editor.putString("trafegon", gson.toJson(l[3]));
        editor.putString("tipo", gson.toJson(l[4]));

        // id dos group-radio
        editor.putInt("cordenadas_id", groupCordenada.getCheckedRadioButtonId());
        editor.putInt("velocidade_id", groupVelocidade.getCheckedRadioButtonId());
        editor.putInt("orientacao_id", groupOrientacao.getCheckedRadioButtonId());
        editor.putInt("trafegon_id", groupTrafego.getCheckedRadioButtonId());
        editor.putInt("tipo_id", groupTipo.getCheckedRadioButtonId());

        editor.apply();
    }

    public void getListaShared(){
        // INICIANDO MEU SHERED PREFERECES
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);

        int cordenada = sharedPreferences.getInt("cordenadas_id", 0);
        int velocidade = sharedPreferences.getInt("velocidade_id", 0);
        int orientacao = sharedPreferences.getInt("orientacao_id", 0);
        int trafego = sharedPreferences.getInt("trafegon_id", 0);
        int tipo =sharedPreferences.getInt("tipo_id", 0);

        groupCordenada.check(cordenada);
        groupVelocidade.check(velocidade);
        groupOrientacao.check(orientacao);
        groupTipo.check(tipo);
        groupTrafego.check(trafego);
        }

    }
