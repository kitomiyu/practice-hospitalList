package com.poc.android.myhospitals.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.poc.android.myhospitals.data.Item;

import java.util.List;

/**
 * This Model provides the interface between the UI and the data layer of the app,
 * represented by the repository
 */
public class ItemViewModel extends AndroidViewModel {

    private final ItemRepository mRepository;

    private final LiveData<List<Item>> mALlItems;

    public ItemViewModel(Application application) {
        super(application);
        mRepository = new ItemRepository(application);
        mALlItems = mRepository.getAllItems();
    }

    LiveData<List<Item>> getAllItems() {
        return mALlItems;
    }

    public void insert(Item item) {
        mRepository.insert(item);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteItem(Item item) {
        mRepository.deleteItem(item);
    }

    public void updateItem(Item item) {
        mRepository.updateItem(item);
    }

}
