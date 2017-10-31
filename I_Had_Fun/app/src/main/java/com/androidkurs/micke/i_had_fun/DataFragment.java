package com.androidkurs.micke.i_had_fun;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hello on 2017-10-20.
 */

public class DataFragment extends Fragment {
    private ArrayList<mPlace> placeList = new ArrayList<>();
    private boolean locationPermissionGranted=false;
    private boolean placeFragShowing = false;
    private HashMap<String, mPlace> tweetsMap = new HashMap<>();
    private HashMap<String, mPlace> placeMap = new HashMap<>();
    private mPlace placeToDisplay;
    private int[] happyArr = {0,0,0,0};


    public void addToTweetsMap(String key, mPlace value){
        tweetsMap.put(key,value);
    }

    public mPlace getFromTweetsMap(String key){
        return tweetsMap.get(key);
    }
    public HashMap<String,mPlace> getPlaceMap(){
        return placeMap;
    }

    public HashMap<String, mPlace> getTweetsMap() {
        return tweetsMap;
    }

    public void setTweetsMap(HashMap<String, mPlace> tweetsMap) {
        this.tweetsMap = tweetsMap;
    }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public boolean locationPermissionGranted() {
        return this.locationPermissionGranted;
    }

    public void placeFragShowing(boolean b) {
        this.placeFragShowing = b;
    }

    public boolean getplaceFragShowing() {
        return placeFragShowing;
    }

    public void setPlaceToDisplay(String placeID) {
        this.placeToDisplay = placeMap.get(placeID);
    }

    public mPlace getPlaceToDisplay() {
        return this.placeToDisplay;
    }

    public void setHappyArr(int[] happyArr) {
        this.happyArr = happyArr;
    }
    public int[] getHappyArr() {
        return this.happyArr;
    }
    public void addToPlaceMap(String id, mPlace mPlace) {
        placeMap.put(id,mPlace);
    }

    public void setNewInfo(String id, mPlace place) {
        placeMap.put(id,place);
    }

    public void addBitmapToPlace(String placeid, Bitmap bitmap,PlaceInfo placeInfo) {
        if(this.placeMap.containsKey(placeid)){
            this.placeMap.get(placeid).setBitmap(bitmap);
            this.placeMap.get(placeid).setPlaceInfo(placeInfo);
        }
    }
}
