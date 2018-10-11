package com.poc.android.myhospitals;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Entity class that represents a item in the database
 */

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name= "item")
    private String mItem;

    public Item(@NonNull String item) {this.mItem = item;}

    public String getItem() {return this.mItem;}
}
