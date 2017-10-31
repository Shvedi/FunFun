package com.androidkurs.micke.i_had_fun;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
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

public class GetPhotosAsync  {
    private GeoDataClient geoClient;
    private PlaceDetectionClient placeDetectionClient;
    private String placeId;
    private Controller controller;
    private String atts=null;
    private int index = 1;
    private Bitmap bitmap;
    private Place p;
    private PlaceInfo placeInfo;
    private boolean attsComplete = false;
    private boolean bitmapComplete = false;
    private HashMap<String, Bitmap> placeMap = new HashMap<>();




    public GetPhotosAsync(Controller controller, GeoDataClient geoClient, PlaceDetectionClient detectClient, String placeId){
        this.geoClient = geoClient;
        this.placeDetectionClient = detectClient;

        this.placeId = placeId;
        this.controller = controller;

        boolean attsComplete = false;
        boolean bitmapComplete = false;

    }



    public void fetchPlacePhoto() {
        Log.d("GETPHOTO","DOINBACKGROUND");
        Log.d("GETPHOTO", "PLACE ID: "+placeId);
        final Task<PlaceBufferResponse> placeDataResponse = geoClient.getPlaceById(placeId);
        placeDataResponse.addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                PlaceBufferResponse place = task.getResult();
                p =place.get(0);
                placeInfo = new PlaceInfo(p.getId(),p.getAddress(),p.getWebsiteUri(),p.getName(),p.getPhoneNumber());


                attsComplete = true;
                onPostExecute();
                place.release();

            }
        });

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = geoClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                Log.d("GETPHOTOASYnC", "METADATACOMPLETE");
                try {
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);


                // Get the attribution text.

                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = geoClient.getScaledPhoto(photoMetadata, 60, 60);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        bitmap = photo.getBitmap();
                        bitmapComplete = true;
                        onPostExecute();

                        Log.d("GETPHOTO", "onCOMPLETE");
                    }

                });
                    photoMetadataBuffer.release();
                }catch (IllegalStateException e){
                    Log.d("GETPHOTOASYNC","PHOTOMETADATABUFFER EMPTY");
                }

            }
        });


    }



    private void onPostExecute() {

        if(attsComplete && bitmapComplete){
            controller.PhotoFetched(bitmap,placeInfo, placeId);
        }


    }


}
