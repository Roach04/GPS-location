package com.project.add;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.project.frag.SplashFrag;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Splash extends AppCompatActivity {

    private AdView mAdView;

    TextView welcome;

    int counter = 0;

    //global variables.
    CustomAdapter adapter;

    //location manager variable
    LocationManager manager;

    //location listener.
    LocationListener locaListener;

    //get the location.
    Location lastLoca;

    //to ensure data is displayed on a list.
    ArrayList<Address> data;

    //handle listview.
    ListView list;

    //progress Dialog.
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get the text view to apply the font.
        welcome = (TextView) findViewById(R.id.textView);

        //create a type face have a variable to it and pass in the asset manager and its path.
        Typeface customFont = Typeface.createFromAsset(this.getAssets(),"fonts/SansationLight.ttf");

        //pass in the font to the appropriate text view.
        welcome.setTypeface(customFont);

        //ADS.
        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        //
        mAdView = (AdView) findViewById(R.id.ad_address);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        //blue stacks emulator id "5171181AB398FB071B0DB0D47F7F0EE8"
        //phone samsung id "3BA21D3ACB5E7B3FA51CA692A6063E52"
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3BA21D3ACB5E7B3FA51CA692A6063E52")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        //instantiate our array list.
        data = new ArrayList<Address>();

        //find the list view by id.
        list = (ListView) findViewById(R.id.listViewAddress);

        //get the addresses.
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        progressAddress();


        //initialize the location listener.
        locaListener = new LocationListener() {

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

            }

            @Override
            public void onProviderEnabled(String prov) {

                //provide a toast message indicating to the user that the app requires
                //location services.
                Toast.makeText(getBaseContext(), "Location Services Operational.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String arg0) {

                showSettingsAlert();

            }

            @Override
            public void onLocationChanged(Location loc) {

                //check whether the current location is better that the last known location.
                //or if its empty
                //if so, display the current location.
                if (loc != null) {

                    //when the location changes, do the following.
                    //i e when the user moves.
                    Double latt = loc.getLatitude();

                    Double lonn = loc.getLongitude();

                    //get the locations addresses based on the latitude and longitude.
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    StringBuilder builder = new StringBuilder();

                    try {
                        List<android.location.Address> addressList = geocoder.getFromLocation(latt, lonn, 1);

                        android.location.Address address = addressList.get(0);
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {

                            builder.append(address.getAddressLine(i)).append(" ");

                        }

                        String [] street = {
                                address.getAddressLine(0)
                        };

                        String[] locale = {
                                address.getSubLocality()
                        };

                        String[] city = {
                                address.getAdminArea()
                        };

                        String[] country = {
                                address.getCountryName()
                        };

                        String[] latitude = {
                                //This will display the final address.
                                String.valueOf(latt)
                        };

                        String[] longitude = {
                                String.valueOf(lonn)
                        };

                        //instantiate the custom adapter.
                        adapter = new CustomAdapter(Splash.this, data);

                        progressDialog.dismiss();

                        // Save the ListView state (= includes scroll position) as a Parceble
                        Parcelable state = list.onSaveInstanceState();

                        //set the adapter.
                        list.setAdapter(adapter);

                        // Restore previous state (including selected item index and scroll position)
                        list.onRestoreInstanceState(state);

                        //create a for loop to loop thru the locales.
                        //create a single locale and loop thru.
                        for (int i = 0; i < street.length; i++) {

                            //clear before displaying new locales.
                            data.clear();

                            //get the new locales.
                            Address a = new Address(street[i], locale[i], city[i], country[i],longitude[i], latitude[i]);

                            //add the data to our list.
                            data.add(a);
                        }

                        // Save the ListView state (= includes scroll position) as a Parceble
                        Parcelable stat = list.onSaveInstanceState();

                        //notify the list that the adapter has changed.
                        adapter.notifyDataSetChanged();

                        // Restore previous state (including selected item index and scroll position)
                        list.onRestoreInstanceState(stat);

                    } catch (IOException e) {
                    } catch (NullPointerException e) {
                    }

                } else {

                    showSettingsAlert();
                }
            }
        };

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 90000, 0, locaListener);
    }

    @Override
    protected void onResume() {

        //counter++;

        /** Called when returning to the activity */

        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("counter", counter);

        //log msg.
        Log.d("SAVE", counter + "was saved.");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        counter = savedInstanceState.getInt("counter");

        //log msg.
        Log.d("SAVE", counter + "was restored.");
    }

    //ensure that menu actions from the user are handled.
    public void showSettingsAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);

        alertDialog.setTitle("Location Settings.");

        alertDialog.setMessage("Access Location Settings from the Settings Menu.");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                Splash.this.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                progressDialog.dismiss();

                //dismiss
                finish();

                //Toast.makeText(Splash.this, "Start Location Services.", Toast.LENGTH_LONG).show();
            }
        });
        try {
            alertDialog.show();
        }catch (WindowManager.BadTokenException e)
        {

        }
    }

    //progress dialog when addresses are being fetched.
    public void progressAddress(){

        progressDialog = new ProgressDialog(this);

        progressDialog.onSaveInstanceState();

        progressDialog.setTitle("Current Address");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Just a Moment...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                finish();
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
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
