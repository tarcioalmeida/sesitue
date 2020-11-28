package br.mandioca.sesitue;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HistoricoLog extends AppCompatActivity {
    //Banco de Dados
    private DatabaseReference mDatabase;
    ArrayList<DataPosicao> listaDataPosicao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_historico_log);

        //Recupera pontos do banco de dados
        mDatabase= FirebaseDatabase.getInstance().getReference();;
        listaDataPosicao = new ArrayList<DataPosicao>();
        recuperaListaPontosDB();

    }

    private void recuperaListaPontosDB(){
        mDatabase.child("location").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                LatLng posicao = new LatLng((Double) ds.child("latitude").getValue(), (Double) ds.child("longitude").getValue());
                                Date data = new Date(Long.valueOf(ds.getKey()));

                                DataPosicao dataPosicao = new DataPosicao(data, posicao);
                                listaDataPosicao.add(dataPosicao);
                            }
                            desenharTabela(listaDataPosicao);
                        }else{
                            Toast.makeText(getApplication(), "Nao há dados a exibir!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void desenharTabela(ArrayList<DataPosicao> lista) {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT));

        TableLayout tl = new TableLayout(this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tl.setLayoutParams(lp);

        //cabeçalho
        TableRow trCabecalho = new TableRow(this);
        tl.addView(trCabecalho);

        TextView tvCabecalho1 = new TextView(this);
        tvCabecalho1.setText(" Data");
        tvCabecalho1.setTypeface(null, Typeface.BOLD);

        TextView tvCabecalho2 = new TextView(this);
        tvCabecalho2.setTypeface(null, Typeface.BOLD);
        tvCabecalho2.setText(" Latitude");

        TextView tvCabecalho3 = new TextView(this);
        tvCabecalho3.setTypeface(null, Typeface.BOLD);
        tvCabecalho3.setText(" Longitude");

        trCabecalho.addView(tvCabecalho1);
        trCabecalho.addView(tvCabecalho2);
        trCabecalho.addView(tvCabecalho3);

        for( int i = 0; i<lista.size(); i++){
            TableRow tr = new TableRow(this);
            tl.addView(tr);

            TextView tv1 = new TextView(this);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            tv1.setText(dateFormat.format(lista.get(i).getData()));

            TextView tv2 = new TextView(this);
            tv2.setText(" "+lista.get(i).getLatLng().latitude);

            TextView tv3 = new TextView(this);
            tv3.setText(" "+lista.get(i).getLatLng().longitude);

            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(tv3);
        }

        scroll.addView(tl);
        setContentView(scroll);
    }

}