package com.androidkurs.micke.i_had_fun;


import android.net.Uri;

/**
 * Created by hello on 2017-10-31.
 */

public class PlaceInfo {
    private String placeID;
    private CharSequence address;
    private Uri website;
    private CharSequence name;
    private CharSequence phonenbr;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public CharSequence getAddress() {
        return address;
    }

    public void setAddress(CharSequence address) {
        this.address = address;
    }

    public Uri getWebsite() {
        return website;
    }

    public void setWebsite(Uri website) {
        this.website = website;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public CharSequence getPhonenbr() {
        return phonenbr;
    }

    public void setPhonenbr(CharSequence phonenbr) {
        this.phonenbr = phonenbr;
    }

    public PlaceInfo(String placeID, CharSequence address, Uri website, CharSequence name, CharSequence phonenbr) {
        this.placeID = placeID;
        this.address = address;
        this.website = website;
        this.name = name;
        this.phonenbr = phonenbr;
    }
}
