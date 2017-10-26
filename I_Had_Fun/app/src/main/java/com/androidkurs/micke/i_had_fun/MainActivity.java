package com.androidkurs.micke.i_had_fun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Controller controller;
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private TextView navHeader;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions();
        initController();
        initDrawer();
        initComponents();
        initListeners();
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
        setupDrawerContent(navView);
        drawerToggle.syncState();
        navHeader = (TextView)headerLayout.findViewById(R.id.nav_HeaderText);
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
        } else {
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
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastAction== MotionEvent.ACTION_BUTTON_RELEASE){
                    enableFab(false);
                    controller.actionBtnPressed();
                }
            }
        });

        fab.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
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
        });

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
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {

                    controller.setLoactionPermission(true);
                }
            }
        }

    }


    private void setupDrawerContent(final NavigationView navView){

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int drawerItemSelected = 0;
                int id = item.getItemId();

                switch (id){
                    case R.id.nav_logout:
                        drawerItemSelected = 4;
                        twitterActivity.logOut();
                        controller.resetDialog();
                        break;
                }
                    /*
                    TODO!!!
                    LOGOUTFUNCTIONS + OTHER OPTIONS?
                     */

                //controller.setFragment(drawerItemSelected);
                drawerLayout.closeDrawers();

                return true;}
        });
    }
    public void setUserProfile(User user){
        navHeader.setText(user.name);
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

        // Pass the activity result to the login button.
        tac.onActivityResult(requestCode, resultCode, data);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.0f));
    }
    public void setDialogFrag(DialogFragment dialogFrag){
        this.dialogFrag = dialogFrag;
    }
    public void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        dialogFrag.show(fm, "DialogFragment");
        //Log.v("showDialog","Showing dialog");
    }


   public void setMarker(Double latitude, Double longitude, String placename, String date) {
        LatLng myLocation = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(placename + "\n" + date));
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

    /*
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
    */
    public void setTwitterActivity(TwitterActivity twitterActivity) {
        this.twitterActivity = twitterActivity;
    }
    public void setHappy(int happy){
        this.happy = happy;
    }
}
