package com.androidkurs.micke.i_had_fun;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hello on 2017-10-20.
 */

public class GetPhotosAsync extends AsyncTask<Void,Void,Void> {
    private GeoDataClient geoClient;
    private PlaceDetectionClient placeDetectionClient;
    private String placeId;
    private MainActivity activity;
    private ArrayList<mPlace> placeList;
    private int index = 1;
    private HashMap<String, Bitmap> placeMap = new HashMap<>();



    public GetPhotosAsync(MainActivity activity, GeoDataClient geoClient, PlaceDetectionClient detectClient, ArrayList<mPlace> placeList){
        this.geoClient = geoClient;
        this.placeDetectionClient = detectClient;
        this.placeList = placeList;
        this.activity = activity;


    }


    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("GETPHOTO","DOINBACKGROUND");


        Log.d("GETPHOTO","PlacelistSize: "+placeList.size());


        final mPlace place=placeList.get(index);

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = geoClient.getPlacePhotos(place.getId());
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                photoMetadataBuffer.release();
                // Get the attribution text.
                Log.d("GETPHOTOASYnC", "METADATACOMPLETE");
                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = geoClient.getScaledPhoto(photoMetadata, 40, 40);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();
                        placeMap.put(place.getId(), bitmap);
                        Log.d("GETPHOTO", "onCOMPLETE");
                    }
                });

            }
        });

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(index == placeList.size()-2){
           // activity.addToPlaceMap(placeMap);
        }else{
            index++;
           // activity.rerunTask();
        }

    }
}
