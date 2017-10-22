package com.androidkurs.micke.i_had_fun;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hello on 2017-10-20.
 */

public class DataFragment extends Fragment {
    private ArrayList<mPlace> placeList = new ArrayList<>();
    private HashMap<String, Bitmap> placeMap = new HashMap<>();
    private boolean locationPermissionGranted=false;


    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }



    public ArrayList<mPlace> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(ArrayList<mPlace> placeList) {
        this.placeList = placeList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public boolean locationPermissionGranted() {
        return this.locationPermissionGranted;
    }
}
