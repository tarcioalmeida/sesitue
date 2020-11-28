package br.mandioca.sesitue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    //criação dos objetos e declarações globais
    Button btConfiguracoes, btMapa, btHistorico, btSobre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vincunlando os objetos aos seus IDS
        btConfiguracoes = (Button) findViewById(R.id.botaoConfiguracoes);
        btMapa = (Button) findViewById(R.id.botaoMapa);
        btHistorico = (Button) findViewById(R.id.botaoHistorico);
        btSobre = (Button) findViewById(R.id.botaoSobre);


        //Programando o botão Configurar
        btConfiguracoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent configuracao = new Intent(MainActivity.this, Configuracoes.class);
                startActivity(configuracao);
            }

        });

        //Programando o botão Mapa
        btMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent mapa = new Intent(MainActivity.this, Mapa.class);
                startActivity(mapa);
            }

        });

        //Programando o botão Histórico
        btHistorico.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent historico = new Intent(MainActivity.this, Historico.class);
                startActivity(historico);
            }

        });


        //Programando o botão Sobre
        btSobre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent sobre = new Intent(MainActivity.this, Sobre.class);
                startActivity(sobre);
            }

        });

    }
}
