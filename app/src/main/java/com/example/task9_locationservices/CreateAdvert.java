package com.example.task9_locationservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task9_locationservices.data.DatabaseHelper;
import com.example.task9_locationservices.model.Item;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvert extends AppCompatActivity {
    TextView name;
    TextView phone;
    TextView description;
    TextView date;
    TextView location;
    Button save;
    Button currentLocation;
    RadioButton lost;
    RadioButton found;
    DatePickerDialog dpDialog;
    LocationManager locationManager;
    LocationListener locationListener;
    String locationString;
    double lat;
    double lng;
    LatLng latLng;
    String placeString;
    AutocompleteSupportFragment autocompleteFragment;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        name = findViewById(R.id.nameEntry);
        phone = findViewById(R.id.phoneEntry);
        description = findViewById(R.id.descriptionEntry);
        date = findViewById(R.id.dateEntry);
        save = findViewById(R.id.button3);
        lost = findViewById(R.id.radioButton);
        found = findViewById(R.id.radioButton2);
        currentLocation = findViewById(R.id.button6);
        Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        latLng = new LatLng(0, 0);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location1) {
                //variables set for current location
                lat = location1.getLatitude();
                lng = location1.getLongitude();
            }
        };
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        Places.initialize(getApplicationContext(), getString(R.string.API_KEY));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        DatabaseHelper db = new DatabaseHelper(CreateAdvert.this);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                latLng = place.getLatLng();
                lat = latLng.latitude;
                lng = latLng.longitude;

               //Geo locator code based on https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
                List<Address> addresses = null;
                try {
                    addresses = geo.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.isEmpty()) {

                }
                else {
                    placeString = addresses.get(0).getLocality();;
                }
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(CreateAdvert.this, "An error occurred: " + status, Toast.LENGTH_LONG).show();
            }
        });
        //Maniually set the background for the places lookup fragment
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);


        lost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(found.isChecked()){
                    found.setChecked(false);
                }
            }
        });

        found.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (lost.isChecked()) {
                    lost.setChecked(false);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String lostFound = (lost.isChecked()) ? "Lost" : "Found";
                Item item = new Item(name.getText().toString(), phone.getText().toString(), description.getText().toString(), date.getText().toString(), latLng.toString(), lostFound, placeString);
                db.insertItem(item);
                Toast.makeText(CreateAdvert.this, "Item submitted", Toast.LENGTH_SHORT).show();
            }
        });
        currentLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Set a LatLng for current location
                latLng = new LatLng(lat, lng);

                List<Address> addresses = null;
                try {
                    addresses = geo.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.isEmpty()) {

                }
                else {
                    //Set place name as suburb
                    placeString = addresses.get(0).getLocality();
                }
            }
        });
    }
}