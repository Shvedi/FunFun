package se.af8143mah.assignment3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mapReady;
    private boolean addMarker = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    static final int REQUEST_TAKE_THUMBNAIL = 2;
    private BroadcastReceiver providerListener;
    private Button markerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_maps);
        initialisemapstuff();
        showDialog();
    }

    public void initialisemapstuff(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationListener = new LocList(this);

        markerbtn = (Button) findViewById(R.id.markerbutton);
        View.OnClickListener choiceListener = new ChoiceButtonListener();
        markerbtn.setOnClickListener(choiceListener);

        providerListener = new ProviderListener();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void showDialog(){
        FragmentManager fm = getSupportFragmentManager();
        DialogueFragment df = new DialogueFragment();
        df.show(fm, "HEJHEJ");

    }

    private class ChoiceButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (addMarker == false && mapReady == true) {
                addMarker = true;
            } else {
                Log.d("setOnClickL", "mapReady=false");
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION :
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(providerListener, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        providers();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(locationListener);
        } catch(SecurityException e) {
            Log.d("onPause","removeUpdates");
        }
        this.unregisterReceiver(providerListener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;


    }

    public void placeSmiley(LatLng latLng){
        addMarker = false;
        MarkerOptions mo = new MarkerOptions().position(latLng).title("My position");
        mMap.addMarker(mo);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3));
        Toast.makeText(this, "Place smiley", Toast.LENGTH_SHORT).show();
        Log.d("setOnClickL", "mapReady=false");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_TAKE_THUMBNAIL && resultCode== Activity.RESULT_OK) {

        }
    }

    private class LocList implements LocationListener {
        Activity act;

        public LocList(Activity a){
            this.act = a;
        }

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if (addMarker) {
                addMarker = false;
                placeSmiley(new LatLng(latitude, longitude));
            }

            Log.d("onLocChanged", "Lng=" + longitude + ",Lat=" + latitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if(provider.equals(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(act, "GPS provider aktiv", Toast.LENGTH_SHORT).show();
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER)){
                Toast.makeText(act, "Network provider aktiv", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if(provider.equals(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(act, "GPS provider inaktiv", Toast.LENGTH_SHORT).show();
            }
            if(provider.equals(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(act, "Network provider inaktiv", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void providers() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS provider aktiv", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "GPS provider inaktiv", Toast.LENGTH_SHORT).show();
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "Network provider aktiv", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Network provider inaktiv", Toast.LENGTH_SHORT).show();
        }
    }

    public class ProviderListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                providers();
            }
        }
    }
}
