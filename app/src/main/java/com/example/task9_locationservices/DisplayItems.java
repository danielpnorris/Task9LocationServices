package com.example.task9_locationservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.task9_locationservices.data.DatabaseHelper;
import com.example.task9_locationservices.model.Item;

import java.util.ArrayList;
import java.util.List;

public class DisplayItems extends AppCompatActivity {

    ListView itemsListView;
    ArrayList<String> itemArrayList;
    ArrayAdapter<String> adapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        //Code based on week 7 lecture/practical
        itemsListView = findViewById(R.id.lostFoundDisplay);
        itemArrayList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(DisplayItems.this);

        List<Item> itemList = db.getItems();
        for(Item item : itemList){
            itemArrayList.add(item.is_found() + " " + item.get_name());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemArrayList);
        itemsListView.setAdapter(adapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Intent removeItem= new Intent(DisplayItems.this, RemoveItem.class);
                removeItem.putExtra("itemId", new Integer(position).toString());
                startActivity(removeItem);
            }
        });
    }
}