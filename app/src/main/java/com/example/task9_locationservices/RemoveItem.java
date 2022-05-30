package com.example.task9_locationservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task9_locationservices.data.DatabaseHelper;
import com.example.task9_locationservices.model.Item;
import com.example.task9_locationservices.utils.Util;
import com.google.android.gms.maps.model.LatLng;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class RemoveItem extends AppCompatActivity {
    TextView title;
    TextView date;
    TextView location;
    Button remove;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        title = findViewById(R.id.itemTitle);
        date = findViewById(R.id.itemDate);
        location = findViewById(R.id.itemLocation);
        remove = findViewById(R.id.button4);

        DatabaseHelper db = new DatabaseHelper(RemoveItem.this);

        String itemId = getIntent().getStringExtra("itemId");
        int itemIdInt = Integer.parseInt(itemId);

        List<Item> itemList = db.getItems();
        String locDate = itemList.get(itemIdInt).get_date();

        //Date calculation based on https://stackoverflow.com/questions/13037654/subtract-two-dates-in-java
        LocalDate dateNow = LocalDate.now();
        LocalDate localDate = LocalDate.parse(itemList.get(itemIdInt).get_date());
        Duration diff = Duration.between(localDate.atStartOfDay(), dateNow.atStartOfDay());
        long diffDays = diff.toDays();

        //Determine if sentence should use day or days
        String days = (diffDays > 1) ? "days" : "day";

        title.setText(itemList.get(itemIdInt).is_found() + " " + itemList.get(itemIdInt).get_name());
        //Using place for the name instead
        location.setText(("At " + itemList.get(itemIdInt).get_place()));
        date.setText(diffDays + " " + days + " ago");



        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Deletion od row based on https://www.codegrepper.com/code-examples/sql/delete+query+in+sqlite+android
                db.getWritableDatabase().delete(Util.TABLE_NAME, Util.ITEMID + "=" + itemList.get(itemIdInt).get_itemId(), null);
                Toast.makeText(RemoveItem.this, "Item deleted", Toast.LENGTH_SHORT).show();

                //Create new intent and return after deleting item
                Intent displayItem= new Intent(RemoveItem.this, DisplayItems.class);
                startActivity(displayItem);
            }
        });
    }

}