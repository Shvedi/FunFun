package com.androidkurs.micke.i_had_fun;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/**
 * Created by hello on 2017-10-20.
 */

public class Controller {
    private MainActivity main;
    TweetComposer tweetC;
    private DataFragment dataFrag;
    private PlacesFragment placeFrag;
    private PlaceDetectionClient placeDetectionClient;


    public Controller(MainActivity mainActivity) {
        this.main = mainActivity;
        initPlaceDetectionClient();

        initDataFrag();
        getLocationPermission();
        initFragments();
        initTwitter();
    }

    private void initTwitter() {
        tweetC = new TweetComposer(main);
        tweetC.setController(this);
    }

    private void initPlaceDetectionClient() {
        this.placeDetectionClient = Places.getPlaceDetectionClient(main,null);
    }


    private void initDataFrag(){
        FragmentManager fm = main.getSupportFragmentManager();
        dataFrag =(DataFragment)fm.findFragmentByTag("dataFrag");
        if(dataFrag==null){
            Log.d("MAINCONTROLLER","DATAFRAG = NULL");
            dataFrag=new DataFragment();
            fm.beginTransaction().add(dataFrag,"dataFrag").commit();
        }
    }

    private void initFragments() {
        this.placeFrag = new PlacesFragment();

    }

    public DataFragment getDataFrag() {
        return this.dataFrag;
    }

    public void actionBtnPressed() {
        if (!dataFrag.locationPermissionGranted()) {
            getLocationPermission();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("MainAcitivty","PERMISSION FAIL");
        }
        GetPlacesAsync getPlaces = new GetPlacesAsync(this, placeDetectionClient);
        getPlaces.fetchPlaces();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(main.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            dataFrag.setLocationPermissionGranted(true);
        } else {
            ActivityCompat.requestPermissions(main,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }


    public void setLoactionPermission(boolean b) {
        dataFrag.setLocationPermissionGranted(b);
    }

    public void placeFetchComplete(ArrayList<mPlace> placeList) {
        dataFrag.setPlaceList(placeList);
        Log.d("CONTROLLER","PlaceListSize: "+placeList.size());
        placeFrag.show(main.getFragmentManager(),"Choose Loacation");
    }

    public void tweetBtnPressed(mPlace place) {
        if(!(place == null)) {
            String placename = place.getName();

            tweetC.tweet("I had fun at " + placename + "!");
        }
        placeFrag.dismiss();
        main.enableFab(true);
    }

    public void placeFragDismissed() {
        main.enableFab(true);
    }
}
