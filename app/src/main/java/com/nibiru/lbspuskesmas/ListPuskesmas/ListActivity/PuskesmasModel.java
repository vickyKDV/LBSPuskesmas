package com.nibiru.lbspuskesmas.ListPuskesmas.ListActivity;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PuskesmasModel {

    private String nama_puskesmas;
    private LatLng latLng;
    private String aimage;

    public PuskesmasModel(String nama_puskesmas, LatLng latLng, String aimage) {
        this.nama_puskesmas = nama_puskesmas;
        this.latLng = latLng;
        this.aimage = aimage;
    }

    public String getNama_puskesmas() {
        return nama_puskesmas;
    }

    public void setNama_puskesmas(String nama_puskesmas) {
        this.nama_puskesmas = nama_puskesmas;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAimage() {
        return aimage;
    }

    public void setAimage(String aimage) {
        this.aimage = aimage;
    }
}
