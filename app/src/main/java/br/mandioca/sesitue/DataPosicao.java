package br.mandioca.sesitue;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class DataPosicao {

    private Date data;
    private LatLng latLng;

    public DataPosicao(Date data, LatLng latLng) {
        this.data = data;
        this.latLng = latLng;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
