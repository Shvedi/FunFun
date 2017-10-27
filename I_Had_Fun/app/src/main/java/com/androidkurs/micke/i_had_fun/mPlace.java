package com.androidkurs.micke.i_had_fun;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hello on 2017-10-19.
 */

public class mPlace {
    private String text;
    private String date;
    private String name;
    private String id;
    private Bitmap bitmap = null;
    private Boolean selected = false;
    private LatLng latLong;
    private double Latitude = 0.0;
    private double Longitude = 0.0;

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public mPlace(String name, String id, LatLng latLong) {
        this.name = name;
        this.id = id;
        this.latLong = latLong;
        this.Latitude = latLong.latitude;
        this.Longitude = latLong.longitude;
    }

    public mPlace(String text,String date, Double latitude,Double longitude){
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.text = text;
        this.date = date;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean hasImage() {
        return false;
    }

    public Boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }


}
