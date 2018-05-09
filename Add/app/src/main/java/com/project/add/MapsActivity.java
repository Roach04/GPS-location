package com.project.add;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.*;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private
    AdView mAdView;

    private GoogleMap mMap;


    TextView location,locality,latt,lonn,snippet;

    EditText loca;

    private Marker marker;

    private static final int ERROR_DIALOG_REQUEST = 90001;

    //global variables.
    GoogleApiClient mGoogleApiClient;

    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ensure google apis are available.
        if (servicesOk()) {

            setContentView(R.layout.activity_maps);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);

            //set the app button for navigating back to home ie splash activity.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (initMap()){

                buildGoogleApiClient();

                mGoogleApiClient.connect();
            }
        }
        else {

            //something is wrong.
            Toast.makeText(MapsActivity.this, " Something is wrong.", Toast.LENGTH_SHORT).show();
        }

        //ADS.
        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        //
        mAdView = (AdView) findViewById(R.id.ad_maps);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3BA21D3ACB5E7B3FA51CA692A6063E52")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
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

        location = (TextView) findViewById(R.id.textViewLocation);

        //create a type face have a variable to it and pass in the asset manager and its path.
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "fonts/SansationLight.ttf");

        //pass in the font to the appropriate text view.
        location.setTypeface(customFont);

        //ensure that google sdk is operational.
        if (servicesOk()){

            if (initMap()){

                buildGoogleApiClient();

                mGoogleApiClient.connect();
            }

            //ensures that i can view my current location by setting to true.
            mMap.setMyLocationEnabled(true);
        }
        else{

            //something is wrong.
            Toast.makeText(MapsActivity.this, " Something is wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean initMap(){

        if (mMap == null){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        return (mMap != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate with a menu with the appropriate xml file.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //get the items id and assign a variable to the same.
        int id = item.getItemId();

        //iterate through the menu items.
        switch (id) {

            case R.id.action_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.action_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.action_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.action_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.action_none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.action_rate:

                launchMarket();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Rate the app.
    private void launchMarket() {
        //instantiate uri and pass in link to the store.
        //concatenate with your package name.
        Uri uri = Uri.parse("market://details?id=" + getPackageName());

        //create an intent pass in the view and its uri.
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {

            //start the play store activity
            //to enable user rate the app
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

            //just in case the host ie user's phone doesn't have play store installed.
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    // Create an instance of GoogleAPIClient.
    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void search(View view)  {

        //hide the keyboard.
        hideSoftKeyboard(view);

        //get the text view.
        loca = (EditText) findViewById(R.id.editText);

        //convert input to a string.
        String location = loca.getText().toString();

        //instantiate geocoder.
        Geocoder geocoder = new Geocoder(this);

        //put the list of the address in a list.
        //assign the same to a variable list.
        //pass in the content the user typed in
        //the 1 indicates that you only want a single address.

        //check whether there is internet connectivity.
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //network information.
        NetworkInfo info = cm.getActiveNetworkInfo();

        //check whether the phone is connected to the network.
        if (info != null && info.isConnected()){

            List<Address> list = null;

            try {
                list = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();

                Toast.makeText(MapsActivity.this, "Try Again.", Toast.LENGTH_SHORT).show();
            }

            Address address;

            try{

                address = list.get(0);

                //get the locality within what the user has typed.
                String locale = address.getLocality();

                //display the locality with a toast.
                Toast.makeText(MapsActivity.this, "Found "+locale, Toast.LENGTH_SHORT).show();

                //get the latlon convert into double
                //assign a variable to the same.
                double lat = address.getLatitude();

                double lon = address.getLongitude();

                //create an instance of the latlng class
                //pass in the latitude and longitude.
                LatLng latLng = new LatLng(lat, lon);

                //ensure that only a single marker is displayed at a time.
                if (marker != null){

                    //remove the marker.
                    marker.remove();
                }

                //add a marker to where the search zone is.
                //define the marker properties
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(locale)
                        .position(new LatLng(lat,lon))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                String country = address.getCountryName();

                //ensure that there's a country to display.
                if (country.length() >0){

                    //display the country name in the info window.
                    markerOptions.snippet(country);
                }

                //ensure that the map is not empty.
                if (mMap != null){

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            //inflate with the xml holding the content.
                            View view = getLayoutInflater().inflate(R.layout.info, null);

                            //get the text views by id created.
                            locality = (TextView) view.findViewById(R.id.textViewFirst);
                            latt     = (TextView) view.findViewById(R.id.textViewSecond);
                            lonn     = (TextView) view.findViewById(R.id.textViewThird);
                            snippet  = (TextView) view.findViewById(R.id.textViewForth);

                            //get the position of the marker based on lat lng.
                            LatLng latLng = marker.getPosition();

                            //get the marker xtics to match with our text views.
                            //display the content.
                            locality.setText(marker.getTitle());
                            latt.setText("Latitude: " + latLng.latitude);
                            lonn.setText("Longitude: " + latLng.longitude);
                            snippet.setText(marker.getSnippet());

                            //return the view.
                            return view;
                        }
                    });
                }

                //add the marker and display it on the map.
                marker = mMap.addMarker(markerOptions);

                //adjust the camera accordingly.
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            catch (IndexOutOfBoundsException e){

                e.printStackTrace();

                //take in the user input and display an error message

                Toast.makeText(MapsActivity.this, location+" doesn't exist.", Toast.LENGTH_LONG).show();
            }
        }
        else{

            Toast.makeText(MapsActivity.this, "Network not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard(View v) {

        //gain access to the in build keyboard system service.
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //hide the keyboard.
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean servicesOk() {

        //in here we are checking to see whether google services api is available or not.
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {

            //IF THE CONNECTION IS A SUCCESS RETURN TRUE.
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {

            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);

            //show the dialog.
            dialog.show();
        } else {

            Toast.makeText(MapsActivity.this, "Can't connect to a mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    @Override
    public void onConnected(Bundle bundle) {

        //display a toast.
        Toast.makeText(MapsActivity.this, "Ready to Map.", Toast.LENGTH_SHORT).show();

        //get the last known location of the user/device.
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //an if statement to check whether we have a location populated or not.
        if (mCurrentLocation == null) {

            Toast.makeText(MapsActivity.this, "Couldn't not Connect.", Toast.LENGTH_SHORT).show();
        } else {

            LatLng latLng = new LatLng(

                    //pass in the current lat lng.
                    mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()
            );

            //adjust the camera accordingly.
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            /*//add a marker to where the search zone is.
            mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));*/
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        //something is wrong.
        Toast.makeText(MapsActivity.this, " Something is wrong.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        //something is wrong.
        Toast.makeText(MapsActivity.this, " Something is wrong.", Toast.LENGTH_SHORT).show();
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
