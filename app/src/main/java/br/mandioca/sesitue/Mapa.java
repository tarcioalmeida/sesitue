package br.mandioca.sesitue;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class Mapa extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mapaGoogle;
    private LocationManager locationManager;
    private DatabaseReference mDatabase;

    //texto com latitude e longitude
    private TextView textLatLong, textVelocidade;
    private String stringLatLong = "Latitude e Longitude", stringVelocidade = "Velocidade";

    /*shared preferences*/
    public static final String nomePreferencias = "Preferencias" ;
    public static final String chaveCoordGeo= "coordgeo";
    public static final String chaveUnidade= "unidade";
    public static final String chaveOrientacao= "orientacao";
    public static final String chaveTipoMapa= "tipomapa";
    public static final String chaveTrafego= "trafego";
    SharedPreferences pref;

    /*variaveis sp*/
    String coordGeo, unidade, orientacao, tipoMapa;
    boolean trafego;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //iniciando texto com latitude e longitude
        textLatLong=(TextView)findViewById(R.id.LatLong);
        textVelocidade=(TextView)findViewById(R.id.Velocidade);

        //checando permissões
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(), "Sem permissão!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //return;
        }

        boolean gps_enabled = false;
        boolean network_enabled = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        if(!gps_enabled && !network_enabled) {
            // notify user

            ImageView imgNotFound = (ImageView) findViewById(R.id.imgNotFound);
            imgNotFound.setVisibility(View.VISIBLE);
            Toast.makeText(getApplication(), "O GPS está desligado", Toast.LENGTH_LONG).show();

        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

            //iniciando mapa
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
            mapFragment.getMapAsync(this);

            //banco de dados
            mDatabase = FirebaseDatabase.getInstance().getReference();

            //pegando as configuranções do shared preferences
            pref = getSharedPreferences(nomePreferencias, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            //Carregando variáveis
            coordGeo = pref.getString(chaveCoordGeo, null);
            unidade = pref.getString(chaveUnidade, null);
            orientacao = pref.getString(chaveOrientacao, null);
            tipoMapa = pref.getString(chaveTipoMapa, null);
            trafego = pref.getBoolean(chaveTrafego, true);
        }




    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap map) {
        mapaGoogle = map;

        //checando permissões
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(), "Sem permissão!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);


        //alteração tipo do mapa de acordo com configuração
        if(tipoMapa.equals("TipoVetorial")){
            mapaGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }else if(tipoMapa.equals("TipoSatelite")){
            mapaGoogle.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        //alterando exibição de informações de tráfego
        if(trafego == true){
            mapaGoogle.setTrafficEnabled(true);
        }else{
            mapaGoogle.setTrafficEnabled(false);
        }

    }



    @Override
    public void onLocationChanged(Location location) {

        mapaGoogle.clear();

        Toast.makeText(getApplication(), "Localização mudou", Toast.LENGTH_LONG).show();

        //colocar location numa latlng
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        //desenhar
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.user_icon);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false);


        //adicionar marcador
        MarkerOptions marcarMeuLocal = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .position(latLng)
                .title("Minha Posição Atual");
        mapaGoogle.addMarker(marcarMeuLocal);

        //mudar orientação de aconrdo com configuração
        float pegaOrientacao = 0;
        if(orientacao.equals("OriNorthpUp")){
            pegaOrientacao = 0;
        }else if(orientacao.equals("OriCourseUp")){
            pegaOrientacao = location.getBearing();
        }

        //fazer camera seguir marcador
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(25)
                .bearing(pegaOrientacao)
                .build();
        mapaGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //salvar localização no banco de dados
        mDatabase.child("location").child(String.valueOf(new Date().getTime())).setValue(latLng);

        //atualizar texto de acordo com as configurações
        switch(coordGeo) {
            case "CoordGrauDecimal":
                stringLatLong="Grau decimal"+"\n"+
                        "Latitude:"+Location.convert(latitude,Location.FORMAT_DEGREES)+"\n"
                        +"Longitude:"+Location.convert(longitude,Location.FORMAT_DEGREES);
                break;
            case "CoordGrauMinDec":
                stringLatLong="Grau-minuto decimal"+"\n"+
                        "Latitude:"+Location.convert(latitude,Location.FORMAT_MINUTES)+"\n"
                        +"Longitude:"+Location.convert(longitude,Location.FORMAT_MINUTES);
                break;
            case "CoordGrauMinSegDec":
                stringLatLong="Grau-minuto-segundo decimal"+"\n"+
                        "Latitude:"+Location.convert(latitude,Location.FORMAT_SECONDS)+"\n"
                        +"Longitude:"+Location.convert(longitude,Location.FORMAT_SECONDS);
                break;
            default:
                stringLatLong="Grau-minuto-segundo decimal"+"\n"+
                        "Latitude:"+Location.convert(latitude,Location.FORMAT_SECONDS)+"\n"
                        +"Longitude:"+Location.convert(longitude,Location.FORMAT_SECONDS);
                break;
        }
        textLatLong.setText(stringLatLong);

        //atualizar velocidade
        if(unidade.equals("UnidadeMph")){
            stringVelocidade = "Velocidade: "+location.getSpeed()+" Mph";
        }
        else if(unidade.equals("UnidadeKmh")){
            stringVelocidade = "Velocidade: "+(location.getSpeed()*1.609)+" Km/h";
        }
        textVelocidade.setText(stringVelocidade);



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
        if(locationManager !=null) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(locationManager !=null) {
            locationManager.removeUpdates(this);
        }

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
