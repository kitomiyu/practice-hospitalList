package com.poc.android.myhospitals.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.poc.android.myhospitals.data.Item;
import com.poc.android.myhospitals.data.ItemDao;
import com.poc.android.myhospitals.data.ItemRoomDatabase;

import java.util.List;

class ItemRepository {

    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;

    ItemRepository(Application application) {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        mItemDao = db.itemDao();
        mAllItems = mItemDao.getAllItems();
    }

    LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    public void insert(Item item) {
        new insertAsyncTask(mItemDao).execute(item);
    }

    public void deleteAll() {
        new deleteAllItemsAsyncTask(mItemDao).execute();
    }

    public void deleteItem(Item item) {
        new deleteItemAsyncTask(mItemDao).execute(item);
    }

    public void updateItem(Item item) {
        new updateItemAsyncTask(mItemDao).execute(item);
    }

    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        public insertAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            mAsyncTaskDao.insert(items[0]);
            return null;
        }
    }

    /**
     * Delete all words from the database (does not delete the table)
     */
    private class deleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ItemDao mAsyncTaskDao;

        public deleteAllItemsAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     * Delete a single item from the database.
     */
    private class deleteItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        public deleteItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            mAsyncTaskDao.deleteItem(items[0]);
            return null;
        }
    }

    /**
     * Update a single item from the database.
     */
    private class updateItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        public updateItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            mAsyncTaskDao.update(items[0]);
            return null;
        }
    }

}
