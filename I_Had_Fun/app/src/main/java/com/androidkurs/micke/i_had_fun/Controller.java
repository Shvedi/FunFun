package com.androidkurs.micke.i_had_fun;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private GeoDataClient geoClient;
    private String fun;
    private String[] months = {"Jan","Feb","Mar","Apr","Maj","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private int happy;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private HashMap<String,mPlace> places;
    private String id;



    public Controller(MainActivity mainActivity) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference().child("places");

        mDatabaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        retreivePlaces((Map<String,Object>) dataSnapshot.getValue());
                        dataFrag.setTweetsMap(places);
                        twitterActivity.getData();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        this.main = mainActivity;
        initPlaceDetectionClient();
        initDataFrag();
        getLocationPermission();
        initTwitter();
        initFragments();
    }

    private void retreivePlaces(Map<String, Object> object) {
        places = new HashMap<>();
        String name;
        String id;
        double latitude;
        double longitude;
        LatLng latlng;
        for (Map.Entry<String, Object> entry : object.entrySet()){

            Map singelPlace = (Map) entry.getValue();
            name = singelPlace.get("name").toString();
            id = singelPlace.get("id").toString();
            latitude = Double.parseDouble(singelPlace.get("latitude").toString());
            longitude = Double.parseDouble(singelPlace.get("longitude").toString());
            latlng = new LatLng(latitude,longitude);

            places.put(name, new mPlace(name,id,latlng));
        }

    }

    private void initTwitter() {
        twitterActivity = new TwitterActivity(main);
        tweetHandler = twitterActivity.getTweetHandler();
        twitterActivity.setController(this);
        main.setTwitterActivity(twitterActivity);
    }

    public void startLogin(){
            twitterActivity.initLogin();
    }

    private void initPlaceDetectionClient() {
        this.placeDetectionClient = Places.getPlaceDetectionClient(main,null);
        this.geoClient = Places.getGeoDataClient(main,null);
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
        }else{
            Log.d("MAINCONTROLLER","DATAFRAG = NOT NULL");
        }
    }

    private void initFragments() {
        this.placeFrag = new PlacesFragment();
        resetDialog();
    }


    public void onRestoreInstatnce() {
        Log.d("CONTROLLER","ONRESTORE");
        initDataFrag();
        initFragments();
        if(dataFrag.getplaceFragShowing()){
            Log.d("CONTROLLER","PLACEFRAG SHOWING");
            main.fabVisible(false);
            main.enableFab(false);
            placeFrag.show(main.getFragmentManager(),"Choose Loacation");
        }
    }

    public void resetDialog(){
        if(dialogFrag!=null) {
            this.dialogFrag.dismiss();
            this.dialogFrag.onDestroyView();
        }
        if(twitterActivity.getSession()==null) {
            this.dialogFrag = new DialogFragment();
            dialogFrag.setController(this);
            main.setDialogFrag(dialogFrag);
        }
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
        dataFrag.placeFragShowing(true);
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
        main.fabVisible(false);

        placeFrag.show(main.getFragmentManager(),"Choose Loacation");
    }

    public void tweetBtnPressed(mPlace place) {
        if(!(place == null)) {
            createPlace(place);
            String placename = place.getName();
            main.setMarker(place.getLatitude(),place.getLongitude(),placename,newDate() , tweetHandler.translateHappiness(fun),place.getId());
            tweetHandler.tweet(fun + placename + "!", place.getLatitude(), place.getLongitude(), place.getId());
        }
        dataFrag.placeFragShowing(false);
        placeFrag.dismiss();
        main.enableFab(true);
        main.fabVisible(true);
    }

    public void placeFragDismissed() {
        dataFrag.placeFragShowing(false);
        placeFrag.dismiss();
        main.enableFab(true);
        main.fabVisible(true);
    }

    public void setFunString(String fun){
        this.fun = fun;
    }
    public void setHappy(int happy){
        main.setHappy(happy);
    }

    private String newDate() {
        String DATE_FORMAT_NOW = "dd-MM-yyyy";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date );
        try {
            Date date2 = sdf.parse(stringDate);
        } catch(ParseException e){
            //Exception handling
        } catch(Exception e){
            //handle exception
        }
        stringDate = translateMonth(stringDate);
        return stringDate;
    }

    public String translateMonth(String date){
        int month = Integer.parseInt(date.substring(3,5));
        return date.substring(0,3) + months[month-1] + date.substring(5,date.length());

    }

    public boolean getPermissions() {
        return dataFrag.isLocationPermissionGranted();
    }


    public void saveInstanceState() {
        if(dataFrag.getplaceFragShowing()){
            placeFrag.dismiss();
        }
    }

    public boolean placeFragShowing() {
        return dataFrag.getplaceFragShowing();
    }

    public void markerInfoClicked(Marker marker) {
        GetPhotosAsync getPhoto = new GetPhotosAsync(this,geoClient,placeDetectionClient,dataFrag.getFromTweetsMap(marker.getSnippet()));
        getPhoto.fetchPlacePhoto();
        
    }

    public void PhotoFetched(mPlace place) {
        dataFrag.setPlaceToDisplay(place);

        new PlaceInformationSheet().show(main.getSupportFragmentManager(),"bottom");

    }

    public void updateSheetContent(PlaceInformationSheet placeInformationSheet) {
        placeInformationSheet.setContent(dataFrag.getPlaceToDisplay());

    }

    public DialogFragment getDialogFrag() {
        return dialogFrag;
    }

    private void createPlace(mPlace mPlace) {

        if (TextUtils.isEmpty(id)) {
            id = mDatabaseReference.push().getKey();
        }
        mDatabaseReference.child(id).setValue(mPlace);
        id="";
    }


    public HashMap<String, mPlace> getPlaces() {
        return places;
    }
}
