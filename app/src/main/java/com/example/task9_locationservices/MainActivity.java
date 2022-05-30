package com.example.task9_locationservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.example.task9_locationservices.data.DatabaseHelper;
import com.example.task9_locationservices.utils.Util;

public class MainActivity extends AppCompatActivity {

    Button createButton;
    Button viewItems;
    Button showOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButton = findViewById(R.id.button);
        viewItems = findViewById(R.id.button2);
        showOnMap = findViewById(R.id.button5);
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);


        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent createAdvert = new Intent(MainActivity.this, CreateAdvert.class);
                startActivity(createAdvert);
            }
        });

        viewItems.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent displayItems = new Intent(MainActivity.this, DisplayItems.class);
                startActivity(displayItems);
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(maps);
            }
        });
    }
}