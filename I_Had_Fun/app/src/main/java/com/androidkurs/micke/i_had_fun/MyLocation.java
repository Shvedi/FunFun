package com.androidkurs.micke.i_had_fun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sonaj on 2017-10-17.
 */

public class MyLocation implements LocationListener {
    private MainActivity main;
    private LocationManager locM;
    public MyLocation(MainActivity main){
        this.main = main;
        locM =(LocationManager) main.getSystemService(main.LOCATION_SERVICE);


    }

    public void startLocation(){
        if (ContextCompat.checkSelfPermission(main, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(main, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            Log.d("MyLocation","PERMISSION GRANTED!!");
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(locM.getBestProvider(criteria, true));

            Location loc = locM.getLastKnownLocation(bestProvider);


            if(loc!=null) {
                main.initPosition(loc.getLatitude(), loc.getLongitude());
                //Toast.makeText(main,"Location via " + bestProvider, Toast.LENGTH_SHORT).show();
                locM.requestLocationUpdates(bestProvider,30000,0,this);
            }
            else{
                switch (bestProvider) {
                    case "gps":
                        Toast.makeText(main, "gps location is null, trying network", Toast.LENGTH_SHORT).show();
                        loc = locM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(loc!=null){
                            main.initPosition(loc.getLatitude(),loc.getLongitude());
                            locM.requestLocationUpdates("network",30000,0,this);
                        }
                        else{
                            Toast.makeText(main, "network and gps location is null", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "network":
                        Toast.makeText(main, "network location is null, trying gps", Toast.LENGTH_SHORT).show();
                        loc = locM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(loc!=null){
                            main.initPosition(loc.getLatitude(),loc.getLongitude());
                            locM.requestLocationUpdates("gps",30000,0,this);
                        }
                        else{
                            Toast.makeText(main, "network and gps location is null", Toast.LENGTH_SHORT).show();
                        }
                }

            }
        } else {
            Log.d("MyLocation","PERMISSION NOT GRANTED");
            Toast.makeText(main,"permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(main, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 10);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        main.initPosition(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
