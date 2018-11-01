package com.poc.android.myhospitals.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Entity class that represents a item in the database
 */

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "item")
    private final String mItem;

    @ColumnInfo(name = "url")
    private final String mUrl;

    @ColumnInfo(name = "account")
    private final String mAccount;

    @ColumnInfo(name = "password")
    private final String mPassword;

    @Ignore
    public Item(int id, @NonNull String item, String mUrl, String mAccount, String mPassword) {
        this.id = id;
        this.mItem = item;
        this.mUrl = mUrl;
        this.mAccount = mAccount;
        this.mPassword = mPassword;
    }

    public Item(@NonNull String item, String mUrl, String mAccount, String mPassword) {
        this.mItem = item;
        this.mUrl = mUrl;
        this.mAccount = mAccount;
        this.mPassword = mPassword;
    }

    public String getItem() {
        return this.mItem;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getAccount() { return this.mAccount; }

    public String getPassword() { return this.mPassword; }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
