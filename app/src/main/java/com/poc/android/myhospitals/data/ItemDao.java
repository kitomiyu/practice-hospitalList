package com.poc.android.myhospitals.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.poc.android.myhospitals.data.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * from item_table LIMIT 1")
    Item[] getAnyItem();

    //LiveData, which is a lifecycle library class for data observation, can help app respond to data changes.
    @Query("SELECT * from item_table ORDER BY item ASC")
    LiveData<List<Item>> getAllItems();
}
