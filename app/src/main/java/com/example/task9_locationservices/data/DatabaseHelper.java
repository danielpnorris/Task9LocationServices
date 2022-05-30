package com.example.task9_locationservices.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.task9_locationservices.model.Item;
import com.example.task9_locationservices.utils.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.ITEMID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Util.NAME + " TEXT, "
                + Util.PHONE + " TEXT, "
                + Util.DESCRIPTION + " TEXT, "
                + Util.DATE + " DATE, "
                + Util.LOCATION + " TEXT, "
                + Util.FOUND + " INTEGER, "
                + Util.PLACE + " TEXT)";

        db.execSQL(CREATE_ITEM_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS";
        db.execSQL(DROP_ITEM_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(db);
    }

    public long insertItem(Item item){
        //Accepts an item object and uses it's properties to add to contentvalues
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.NAME, item.get_name());
        contentValues.put(Util.PHONE, item.get_phone());
        contentValues.put(Util.DESCRIPTION, item.get_description());
        contentValues.put(Util.DATE, item.get_date().toString());
        contentValues.put(Util.LOCATION, item.get_location());
        contentValues.put(Util.FOUND, item.is_found());
        contentValues.put(Util.PLACE, item.get_place());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Item> getItems(){
        List<Item> itemList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);
        LocalDate date;
        if(cursor.moveToFirst()){
            do{
                Item item = new Item(
                        //Method learnt from practical
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                //I manually set the item id based what is in the database
                item.set_itemId(cursor.getInt(0));
                itemList.add(item);
            }while(cursor.moveToNext());
        }
        return itemList;
    }

}
