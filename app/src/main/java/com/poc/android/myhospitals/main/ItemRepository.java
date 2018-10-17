package com.poc.android.myhospitals.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.poc.android.myhospitals.data.Item;
import com.poc.android.myhospitals.data.ItemDao;
import com.poc.android.myhospitals.data.ItemRoomDatabase;

import java.util.List;

public class ItemRepository {

    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;

    ItemRepository(Application application) {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        mItemDao = db.itemDao();
        mAllItems = mItemDao.getAllItems();
    }

    LiveData<List<Item>> getAllItems() { return mAllItems; }

    public void insert(Item item) { new insertAsyncTask(mItemDao).execute(item); }

    public void deleteAll() { new deleteAllItemsAsyncTask(mItemDao).execute(); }

    public void deleteItem(Item item) { new delteItemAsyncTask(mItemDao).execute(item); }


    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Item, Void, Void>{

        private ItemDao mAsyncTaskDao;

        public insertAsyncTask(ItemDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Item... items) {
            mAsyncTaskDao.insert(items[0]);
            return null;
        }
    }

    /**
     * Delete all words from the database (does not delete the table)
     */
    private class deleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void>{

        private ItemDao mAsyncTaskDao;

        public deleteAllItemsAsyncTask(ItemDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Delete a single word from the database.
     */
    private class delteItemAsyncTask extends AsyncTask<Item, Void, Void>{

        private ItemDao mAsyncTaskDao;

        public delteItemAsyncTask(ItemDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Item... items) {
            mAsyncTaskDao.deleteItem(items[0]);
            return null;
        }
    }
}
