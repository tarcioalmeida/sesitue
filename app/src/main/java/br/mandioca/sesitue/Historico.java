package br.mandioca.sesitue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Historico extends AppCompatActivity {
    //criação dos objetos e declarações globais
    Button btHistoricoLog, btHistoricoMapa, btApagarHistorico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        //Vincunlando os objetos aos seus IDS
        btHistoricoLog = (Button) findViewById(R.id.botaoHistoricoLog);
        btHistoricoMapa = (Button) findViewById(R.id.botaoHistoricoMapa);
        btApagarHistorico = (Button) findViewById(R.id.botaoApagarHistorico);

        //Programando o botão Log
        btHistoricoLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent historicoLog = new Intent(Historico.this, HistoricoLog.class);
                startActivity(historicoLog);
            }

        });

        //Programando o botão Mapa
        btHistoricoMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Mudar de tela
                Intent historicoMapa = new Intent(Historico.this, HistoricoMapa.class);
                startActivity(historicoMapa);
            }

        });

        //Programando o botão Apagar Histórico
        btApagarHistorico.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                //Banco de Dados
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;

                mDatabase.child("location").addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ds.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplication(), "Histórico apagado!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplication(), "Nao há dados de localização a serem apagados!", Toast.LENGTH_LONG).show();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });


            }

        });


    }
}
