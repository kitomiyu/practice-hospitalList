package com.poc.android.myhospitals.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Entity class that represents a item in the database
 */

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name= "item")
    private String mItem;

    @ColumnInfo(name = "url")
    private String mUrl;

    public Item(@NonNull String item, String mUrl) {
        this.mItem = item;
        this.mUrl = mUrl;
    }

    public String getItem() {return this.mItem; }

    public String getUrl() {return  this.mUrl; }
}
