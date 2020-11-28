package br.mandioca.sesitue;

        import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HistoricoMapa extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mapaGoogle;
    private LocationManager locationManager;
    private DatabaseReference mDatabase;

    //Desenhar linha
    ArrayList<LatLng> listaPontos;
    Polyline linha;

    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_mapa);

        //checando permissões
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(), "Sem permissão!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(HistoricoMapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        //iniciando serviço de localização
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        //iniciando mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaHistorico);
        mapFragment.getMapAsync(this);

        //banco de dados
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Recupera pontos do banco de dados
        listaPontos = new ArrayList<LatLng>();
        recuperaListaPontosDB();



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap map) {
        mapaGoogle = map;

        //checando permissões
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(), "Sem permissão!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(HistoricoMapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);


    }
    private void recuperaListaPontosDB(){
        mDatabase.child("location").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                LatLng latlng = new LatLng((Double) ds.child("latitude").getValue(), (Double) ds.child("longitude").getValue());
                                listaPontos.add(latlng);
                            }
                            desenharLinha(listaPontos);
                        }else{
                            Toast.makeText(getApplication(), "Nao há dados de localização a exibir!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void desenharLinha(ArrayList<LatLng> lista){
        //apaga todos os marcadores e polilinhas
        mapaGoogle.clear();

        //inicializa a variável de ponto
        LatLng ponto = new LatLng(-12.9531,-38.4589);

        //define configurações da polilinha
        PolylineOptions options = new PolylineOptions().width(6).color(Color.BLACK).geodesic(true).pattern(PATTERN_POLYGON_ALPHA);;

        //Até o final da lista, vá adicionando os pontos a polilinha
        for (int i = 0; i < lista.size(); i++) {
            ponto = lista.get(i);
            options.add(ponto);
        }
        linha = mapaGoogle.addPolyline(options); //add Polyline

        //Marca última posição do log
        MarkerOptions marcarMeuLocal = new MarkerOptions().position(ponto).title("Minha última posição salva");
        mapaGoogle.addMarker(marcarMeuLocal);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ponto)      // Sets the center of the map to location user
                .zoom(18)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mapaGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Toast.makeText(getApplication(), "Desenhou!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapaGoogle != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mapaGoogle.clear();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
