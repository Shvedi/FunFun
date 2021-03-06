package com.androidkurs.micke.i_had_fun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.util.HashMap;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Controller controller;
    private FloatingActionButton fab;
    private TextView fabText;
    private DrawerLayout drawerLayout;
    private Button navLogOut;
    private TextView navHeaderName,navHeaderEmail;
    private Toolbar toolbar;
    private NavigationView navView;
    private ImageView headerImage;
    private ActionBarDrawerToggle drawerToggle;
    private TwitterAuthClient tac;
    private float currentX,currentY;
    private int lastAction;
    private DialogFragment dialogFrag;
    private TwitterActivity twitterActivity;
    private MyLocation myLoc;
    private int happy;
    private TextView tvHappy1;
    private TextView tvHappy2;
    private TextView tvHappy3;
    private TextView tvHappy4;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions();

        initDrawer();
        initComponents();
        initListeners();
        initController();
        myLoc = new MyLocation(this);


    }

    private void initDrawer() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
        navView = (NavigationView)findViewById(R.id.navView);

        View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        headerImage = headerLayout.findViewById(R.id.headerImage);
        //setupDrawerContent(navView);
        drawerToggle.syncState();
        navHeaderName = (TextView)headerLayout.findViewById(R.id.nav_HeaderText);
        navHeaderEmail = (TextView)headerLayout.findViewById(R.id.nav_HeaderEmail);
        navLogOut = (Button) findViewById(R.id.nav_logout);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        controller.saveInstanceState();
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        controller.onRestoreInstatnce();
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }else{
            Log.d("MAINACTIVITY","PLACEFRAGSHOWING FALSE");

            super.onBackPressed();
            /*
            TODO!!
            FIX LOGOUT FUNCTIONS?
             */
            // controller.logout();
        }
    }


    private void initController() {
        this.controller = new Controller(this);
    }

    private void initComponents() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvHappy1 = (TextView) findViewById(R.id.tvAmm1);
        tvHappy2 = (TextView) findViewById(R.id.tvAmm2);
        tvHappy3 = (TextView) findViewById(R.id.tvAmm3);
        tvHappy4 = (TextView) findViewById(R.id.tvAmm4);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabText = (TextView) findViewById(R.id.fabText);
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (lastAction== MotionEvent.ACTION_BUTTON_RELEASE){
                    enableFab(false);
                    Log.d("MAIN","FAB PRESSED");
                    controller.actionBtnPressed();
               // }
            }
        });
/*
        fab.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) { Log.d("MAIN","FAB TOUCHED");
                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        currentX = view.getX() - event.getRawX();
                        currentY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.setX(currentX + event.getRawX());
                        view.setY(currentY + event.getRawY());
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN){
                            lastAction = MotionEvent.ACTION_BUTTON_RELEASE;
                        }
                        break;
                    default:
                        return false;
                }
                return false;

            }
        });*/

        ButtonListener listener = new ButtonListener();
        navLogOut.setOnClickListener(listener);

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
        myLoc.startLocation();
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        controller.markerInfoClicked(marker);

        Log.d("MAINACTIVITY","MARKER ID: "+marker.getId());
    }


    public Controller getController() {
        return this.controller;
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {

                    controller.setLoactionPermission(true);
                    myLoc.startLocation();

                }
            }
        }

    }

    public void showHappyAmmount(int[] happyArr) {
        tvHappy1.setText("x" + happyArr[0]);
        tvHappy2.setText("x" + happyArr[1]);
        tvHappy3.setText("x" + happyArr[2]);
        tvHappy4.setText("x" + happyArr[3]);
    }

    public void setAllMarkers(HashMap<String, mPlace> mPlace, BitmapDescriptor bitmapDescriptor) {
        mMap.clear();
        for (HashMap.Entry<String, mPlace> entry : mPlace.entrySet()) {
            LatLng myLocation = new LatLng(entry.getValue().getLatitude(), entry.getValue().getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLocation).title(entry.getValue().getName() + "\n" + entry.getValue().getDate()).icon(twitterActivity.getTweetHandler().translateHappiness(entry.getValue().getText(),false)).snippet(entry.getValue().getId()));
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MainActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(MainActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());


                TextView snippet = new TextView(MainActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                info.removeViewAt(info.getChildCount()-1);
                return info;
            }

        });
    }


    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.nav_logout:
                    twitterActivity.logOut();
                    //removeFragment(controller.getDataFrag());
                    controller.resetHappyArr();
                    controller.resetDialog();
                    drawerLayout.closeDrawers();
                    mMap.clear();
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        if(isFinishing()){
            removeFragment(controller.getDataFrag());
        }

        super.onPause();
    }

    public void removeFragment(Fragment dataFrag){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(dataFrag);
        ft.commit();
    }

    public void setUserProfile(User user){
        navHeaderName.setText(user.screenName.replace("_"," "));
        navHeaderEmail.setText(user.email);
        String profileImage = user.profileImageUrl.replace("_normal", "");
        Glide.with(getApplicationContext()).load(profileImage).apply(RequestOptions.circleCropTransform()).into(headerImage);
    }
    public void enableFab(boolean b) {
        fab.setEnabled(b);
    }

    public void twitterOnActivity(TwitterAuthClient tac){
        this.tac = tac;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ONACTIVITY","SENDING RESULT");
        // Pass the activity result to the login button.
        try{
            tac.onActivityResult(requestCode, resultCode, data);
        } catch (TwitterException TwitterAuthException){
            Log.v("onActivity","Authorize failed");
        } catch (Exception RuntimeException){
            Log.v("onActivity","Failed delivering result");
        }

    }

    private void permissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE}, 10);
    }

    public void initPosition(double latitude, double longitude) {
        LatLng myLocation = new LatLng(latitude,longitude);
        if(mMap!= null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0f));
        }

    }

    public void setDialogFrag(DialogFragment dialogFrag){
        this.dialogFrag = dialogFrag;
    }
    public void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        dialogFrag.show(fm, "DialogFragment");
        //Log.v("showDialog","Showing dialog");
    }


   public void setMarker(Double latitude, Double longitude, String placename, String date, BitmapDescriptor bit,String placeId) {
        LatLng myLocation = new LatLng(latitude,longitude);


        mMap.addMarker( new MarkerOptions().position(myLocation).title(placename + "\n" + date).icon(bit).snippet(placeId));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

           @Override
           public View getInfoWindow(Marker arg0) {

               return null;
           }

           @Override
           public View getInfoContents(Marker marker) {

               LinearLayout info = new LinearLayout(MainActivity.this);
               info.setOrientation(LinearLayout.VERTICAL);

               TextView title = new TextView(MainActivity.this);
               title.setTextColor(Color.BLACK);
               title.setGravity(Gravity.CENTER);
               title.setTypeface(null, Typeface.BOLD);
               title.setText(marker.getTitle());


               TextView snippet = new TextView(MainActivity.this);
               snippet.setTextColor(Color.GRAY);
               snippet.setText(marker.getSnippet());

               info.addView(title);
               info.addView(snippet);

               info.removeViewAt(info.getChildCount()-1);
               return info;
           }

       });}

/**
    public void setMarker(Double latitude, Double longitude, String placename) {
        LatLng myLocation = new LatLng(latitude,longitude);
        if(happy==1){
            mMap.addMarker(new MarkerOptions().position(myLocation).title(placename).icon(BitmapDescriptorFactory.fromResource(R.drawable.happymarker1)));
        }
        else if(happy==2){
            mMap.addMarker(new MarkerOptions().position(myLocation).title(placename).icon(BitmapDescriptorFactory.fromResource(R.drawable.happymarker2)));
        }
        else if(happy==3){
            mMap.addMarker(new MarkerOptions().position(myLocation).title(placename).icon(BitmapDescriptorFactory.fromResource(R.drawable.happymarker3)));
        }
        else if(happy==4){
            mMap.addMarker(new MarkerOptions().position(myLocation).title(placename).icon(BitmapDescriptorFactory.fromResource(R.drawable.happymarker4)));
        }


        //mMap.addMarker(new MarkerOptions().position(myLocation).title(placename));

    }
**/
    public void setTwitterActivity(TwitterActivity twitterActivity) {
        this.twitterActivity = twitterActivity;
    }
    public void setHappy(int happy){
        this.happy = happy;
    }

    public void fabVisible(boolean b) {
        if(b){
            fab.setVisibility(View.VISIBLE);
            fabText.setVisibility(View.VISIBLE);

        }else{
            fab.setVisibility(View.INVISIBLE);
            fabText.setVisibility(View.INVISIBLE);
        }

    }

}
