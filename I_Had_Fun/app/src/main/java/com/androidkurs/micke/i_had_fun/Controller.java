package com.androidkurs.micke.i_had_fun;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/**
 * Created by hello on 2017-10-20.
 */

public class Controller {
    private MainActivity main;
    private TwitterActivity twitterActivity;
    private TweetHandler tweetHandler;
    private DataFragment dataFrag;
    private PlacesFragment placeFrag;
    private DialogFragment dialogFrag;
    private PlaceDetectionClient placeDetectionClient;
    private String fun;


    public Controller(MainActivity mainActivity) {
        this.main = mainActivity;
        initPlaceDetectionClient();
        initDataFrag();
        getLocationPermission();
        initTwitter();
        initFragments();
    }

    private void initTwitter() {
        twitterActivity = new TwitterActivity(main);
        tweetHandler = twitterActivity.getTweetHandler();
        twitterActivity.setController(this);
        main.setTwitterActivity(twitterActivity);
    }

    public void startLogin(){
            String res = String.valueOf(twitterActivity.initLogin());
            Log.d("RES INIT", res);
    }

    private void initPlaceDetectionClient() {
        this.placeDetectionClient = Places.getPlaceDetectionClient(main,null);
    }
    public void test(){
        String str = "str";
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
        resetDialog();
    }

    public void resetDialog(){
        this.dialogFrag = new DialogFragment();
        dialogFrag.setController(this);
        main.setDialogFrag(dialogFrag);
        showDialogFrag();
    }

    private void showDialogFrag(){
        if(twitterActivity.isLoggedIn()){
            String res = String.valueOf(twitterActivity.fetchSession());
            Log.d("RES INIT", res);
            Log.d("DialogFragment","Logged in, no need for button");
        }
        else{
            main.showDialog();
        }
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
            main.setMarker(place.getLatitude(),place.getLongitude(),placename,"Date");
            tweetHandler.tweet(fun + placename + "!", place.getLatitude(), place.getLongitude(), place.getId());
        }
        placeFrag.dismiss();
        main.enableFab(true);
    }

    public void placeFragDismissed() {
        main.enableFab(true);
    }

    public DialogFragment getNewDialogFrag() {
        DialogFragment dialogFrag = new DialogFragment();
        dialogFrag.setController(this);
        return dialogFrag;
    }
    public void setFunString(String fun){
        this.fun = fun;
    }

}
