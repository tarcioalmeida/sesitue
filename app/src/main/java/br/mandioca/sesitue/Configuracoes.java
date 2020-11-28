package br.mandioca.sesitue;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;
import android.view.View;

public class Configuracoes extends AppCompatActivity {
    /*criação dos objetos*/
    //radiogroup Coordenadas geográficas
    RadioGroup rgCoordGeo;
    RadioButton rbCoordGrauDecimal, rbCoordGrauMinDec, rbCoordGrauMinSegDec;

    //radiogroup Unidade de apresentação da velocidade
    RadioGroup rgUnidade;
    RadioButton rbUnidadeKmh, rbUnidadeMph;

    //radiogroup Orientação do Mapa
    RadioGroup rgOrientacao;
    RadioButton rbOriNenhuma, rbOriNorthpUp, rbOriCourseUp;

    //radiogroup Tipo do Mapa
    RadioGroup rgTipoMapa;
    RadioButton rbTipoVetorial, rbTipoSatelite;

    //switch Informações de tráfego
    Switch sTrafego;

    //botão salvar configurações
    Button btSalvarConfig;

    /*shared preferences*/
    public static final String nomePreferencias = "Preferencias" ;
    public static final String chaveCoordGeo= "coordgeo";
    public static final String chaveUnidade= "unidade";
    public static final String chaveOrientacao= "orientacao";
    public static final String chaveTipoMapa= "tipomapa";
    public static final String chaveTrafego= "trafego";
    SharedPreferences pref;

    /*variaveis*/
    String coordGeo, unidade, orientacao, tipoMapa;
    boolean trafego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        /*associando objetos*/
        //radiogroup Coordenadas geográficas
        rgCoordGeo = (RadioGroup) findViewById(R.id.rgCoordGeo);
        rbCoordGrauDecimal = (RadioButton) findViewById(R.id.rbCoordGrauDecimal);
        rbCoordGrauMinDec = (RadioButton) findViewById(R.id.rbCoordGrauMinDec);
        rbCoordGrauMinSegDec = (RadioButton) findViewById(R.id.rbCoordGrauMinSegDec);

        //radiogroup Unidade de apresentação da velocidade
        rgUnidade = (RadioGroup) findViewById(R.id.rgUnidade);
        rbUnidadeKmh = (RadioButton) findViewById(R.id.rbUnidadeKmh);
        rbUnidadeMph = (RadioButton) findViewById(R.id.rbUnidadeMph);

        //radiogroup Orientação do Mapa
        rgOrientacao = (RadioGroup) findViewById(R.id.rgOrientacao);
        rbOriNenhuma = (RadioButton) findViewById(R.id.rbOriNenhuma);
        rbOriNorthpUp = (RadioButton) findViewById(R.id.rbOriNorthpUp);
        rbOriCourseUp = (RadioButton) findViewById(R.id.rbOriCourseUp);

        //radiogroup Tipo do Mapa
        rgTipoMapa = (RadioGroup) findViewById(R.id.rgTipoMapa);
        rbTipoVetorial = (RadioButton) findViewById(R.id.rbTipoVetorial);
        rbTipoSatelite = (RadioButton) findViewById(R.id.rbTipoSatelite);

        //switch Informações de tráfego
        sTrafego = (Switch) findViewById(R.id.sTrafego);

        //botão salvar configurações
        btSalvarConfig = (Button) findViewById(R.id.btSalvarConfig);

        /*carregar preferencias default*/
        //Carregando preferencias
        pref = getSharedPreferences(nomePreferencias, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Carregando variáveis
        coordGeo = pref.getString(chaveCoordGeo, null);
        unidade = pref.getString(chaveUnidade, null);
        orientacao = pref.getString(chaveOrientacao, null);
        tipoMapa = pref.getString(chaveTipoMapa, null);
        trafego = pref.getBoolean(chaveTrafego, true);

        //Se não houver preferências salvas, definir configurações default
        if(TextUtils.isEmpty(coordGeo) && TextUtils.isEmpty(coordGeo) && TextUtils.isEmpty(coordGeo) && TextUtils.isEmpty(coordGeo)){
            Toast.makeText(getApplication(), "Definindo configurações iniciais", Toast.LENGTH_LONG).show();

            //radiogroup Coordenadas geográficas
            rbCoordGrauDecimal.setChecked(true);
            editor.putString(chaveCoordGeo, "CoordGrauDecimal");

            //radiogroup Unidade de apresentação da velocidade
            rbUnidadeKmh.setChecked(true);
            editor.putString(chaveUnidade, "UnidadeKmh");

            //radiogroup Orientação do Mapa
            rbOriNenhuma.setChecked(true);
            editor.putString(chaveOrientacao, "OriNenhuma");

            //radiogroup Tipo do Mapa
            rbTipoVetorial.setChecked(true);
            editor.putString(chaveTipoMapa, "TipoVetorial");

            //switch Informações de tráfego
            sTrafego.setChecked(false);
            editor.putBoolean(chaveTrafego, false);

            //commit
            editor.commit();

        } //fim-if

        //Se houver preferências salvas, carregá-las
        else{

            Toast.makeText(getApplication(), "Configurações carregadas!", Toast.LENGTH_LONG).show();
            //radiogroup Coordenadas geográficas
            switch(coordGeo) {
                case "CoordGrauDecimal":
                    rbCoordGrauDecimal.setChecked(true);
                    break;
                case "CoordGrauMinDec":
                    rbCoordGrauMinDec.setChecked(true);
                    break;
                case "CoordGrauMinSegDec":
                    rbCoordGrauMinSegDec.setChecked(true);
                    break;
            }

            //radiogroup Unidade de apresentação da velocidade
            switch(unidade) {
                case "UnidadeKmh":
                    rbUnidadeKmh.setChecked(true);
                    break;
                case "UnidadeMph":
                    rbUnidadeMph.setChecked(true);
                    break;
            }

            //radiogroup Orientação do Mapa
            switch(orientacao) {
                case "OriNenhuma":
                    rbOriNenhuma.setChecked(true);
                    break;
                case "OriNorthpUp":
                    rbOriNorthpUp.setChecked(true);
                    break;
                case "OriCourseUp":
                    rbOriCourseUp.setChecked(true);
                    break;
            }

            //radiogroup Tipo do Mapa
            switch(tipoMapa) {
                case "TipoVetorial":
                    rbTipoVetorial.setChecked(true);
                    break;
                case "TipoSatelite":
                    rbTipoSatelite.setChecked(true);
                    break;
            }

            //switch Informações de tráfego
            if(trafego == true){
                sTrafego.setChecked(true);
            }else if(trafego == false){
                sTrafego.setChecked(false);
            }

        }//fim-else


        //Programando o botão Salvar Configurações
        btSalvarConfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*editor de preferencias*/
                SharedPreferences.Editor editor = pref.edit();

                /*checar configurações escolhidas*/
                //radiogroup Coordenadas geográficas
                switch(rgCoordGeo.getCheckedRadioButtonId()) {
                    case R.id.rbCoordGrauDecimal:
                        editor.putString(chaveCoordGeo, "CoordGrauDecimal");
                        break;
                    case R.id.rbCoordGrauMinDec:
                        editor.putString(chaveCoordGeo, "CoordGrauMinDec");
                        break;
                    case R.id.rbCoordGrauMinSegDec:
                        editor.putString(chaveCoordGeo, "CoordGrauMinSegDec");
                        break;
                }

                //radiogroup Unidade de apresentação da velocidade
                switch(rgUnidade.getCheckedRadioButtonId()) {
                    case R.id.rbUnidadeKmh:
                        editor.putString(chaveUnidade, "UnidadeKmh");
                        break;
                    case R.id.rbUnidadeMph:
                        editor.putString(chaveUnidade, "UnidadeMph");
                        break;
                }

                //radiogroup Orientação do Mapa
                switch(rgOrientacao.getCheckedRadioButtonId()) {
                    case R.id.rbOriNenhuma:
                        editor.putString(chaveOrientacao, "OriNenhuma");
                        break;
                    case R.id.rbOriNorthpUp:
                        editor.putString(chaveOrientacao, "OriNorthpUp");
                        break;
                    case R.id.rbOriCourseUp:
                        editor.putString(chaveOrientacao, "OriCourseUp");
                        break;
                }

                //radiogroup Tipo do Mapa
                switch(rgTipoMapa.getCheckedRadioButtonId()) {
                    case R.id.rbTipoVetorial:
                        editor.putString(chaveTipoMapa, "TipoVetorial");
                        break;
                    case R.id.rbTipoSatelite:
                        editor.putString(chaveTipoMapa, "TipoSatelite");
                        break;
                }

                //switch Informações de tráfego
                if(sTrafego.isChecked()){
                    editor.putBoolean(chaveTrafego, true);
                }else{
                    editor.putBoolean(chaveTrafego, false);
                }

                Toast.makeText(getApplication(), "Alterações salvas!", Toast.LENGTH_LONG).show();
                editor.commit();

            }


        });

    }//fim-oncreate





}
