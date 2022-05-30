package com.example.task9_locationservices;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.task9_locationservices.data.DatabaseHelper;
import com.example.task9_locationservices.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.task9_locationservices.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Passing multiple locations based on prior learning and https://www.geeksforgeeks.org/how-to-add-multiple-markers-on-google-maps-in-android/
    private GoogleMap mMap;
    ListView itemsListView;
    ArrayList<String> itemArrayList;

    // creating array list for adding all our locations.
    private ArrayList<LatLng> latlngList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Code based on week 7 lecture/practical
        itemsListView = findViewById(R.id.lostFoundDisplay);
        itemArrayList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(MapsActivity.this);

        List<Item> itemList = db.getItems();
        for (Item item : itemList) {
            itemArrayList.add(item.get_location().toString());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        latlngList = new ArrayList<>();
        for (int i = 0; i < itemArrayList.size(); i++) {
            latlngList.add(makeLatLng(itemArrayList.get(i)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < latlngList.size(); i++) {
            //Based on practical from week 9
            mMap.addMarker(new MarkerOptions().position(latlngList.get(i)).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(i), 8));
        }
    }

    //Created a method to remove unnecessary characters from LatLng objects to pass to Map
    public LatLng makeLatLng(String latlong) {
        double nlat = 0;
        double nlng = 0;
        LatLng result;
        String[] splitArray = latlong.split(",");
        for (int i = 0; i < splitArray.length; i++) {
            StringBuilder s = new StringBuilder(splitArray[i]);
            if (i == 0) {
                //Cleans up the latlng object that is saved in the db
                s.delete(0, 10);
                nlat = Double.parseDouble(s.toString());
            } else {
                s.deleteCharAt(s.length() - 1);
                nlng = Double.parseDouble(s.toString());
            }
        }
        result = new LatLng(nlat, nlng);
        return result;
    };
}
