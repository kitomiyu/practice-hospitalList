package com.poc.android.myhospitals;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * This Model provides the interface between the UI and the data layer of the app,
 * represented by the repository
 */
public class ItemViewModel extends AndroidViewModel{

    private ItemRepository mRepository;

    private LiveData<List<Item>> mALlItems;

    public ItemViewModel(Application application) {
        super(application);
        mRepository = new ItemRepository(application);
        mALlItems = mRepository.getAllItems();
    }

    LiveData<List<Item>> getAllItems() { return mALlItems; }

    public void insert(Item item) { mRepository.insert(item); }

    public void deleteAll() { mRepository.deleteAll(); }

    public void deleteItem(Item item) { mRepository.deleteItem(item); }
}
