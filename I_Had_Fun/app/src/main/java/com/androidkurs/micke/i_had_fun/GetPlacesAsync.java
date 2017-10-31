package com.androidkurs.micke.i_had_fun;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * Created by hello on 2017-10-22.
 */

public class GetPlacesAsync  {
    private ArrayList<mPlace> placeList = new ArrayList<>();
    private Controller controller;
    private PlaceDetectionClient placeDetectionClient;


    public GetPlacesAsync(Controller controller, PlaceDetectionClient placeDetectionClient){
        this.controller = controller;
        this.placeDetectionClient = placeDetectionClient;
    }



    public void fetchPlaces(){
        Log.d("GETPLACES ASYNC","fetchplaces");
        @SuppressWarnings("MissingPermission")
        Task<PlaceLikelihoodBufferResponse> result = placeDetectionClient
                .getCurrentPlace(new PlaceFilter());
        result.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                PlaceLikelihoodBufferResponse likelyPlaces = null;
                try {
                    likelyPlaces = task.getResult();
                    for(PlaceLikelihood p: likelyPlaces){
                        placeList.add(new mPlace(p.getPlace().getName().toString(),p.getPlace().getId(), p.getPlace().getLatLng()));


                    }
                }catch (RuntimeExecutionException e){
                    e.printStackTrace();
                    Log.d("GETPLACE ASYNC","ERROR: "+e.getMessage());
                }

                if(placeList.size()>1){

                    placeList.remove(placeList.size()-1);
                }

                if(likelyPlaces != null){
                    likelyPlaces.release();
                }
                controller.placeFetchComplete(placeList);

                // getPhotoTask.execute();
            }

        });
    }



}
