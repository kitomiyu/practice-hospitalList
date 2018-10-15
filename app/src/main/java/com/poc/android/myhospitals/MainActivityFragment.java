package com.poc.android.myhospitals;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.poc.android.myhospitals.data.Item;

import java.util.List;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ItemListAdapter.ListItemClickListener {

    private ItemViewModel mItemViewModel;
    public static final int NEW_ACTIVITY_REQUEST_CODE = 1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // setup the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.mainRecyclerView);
        final ItemListAdapter adapter = new ItemListAdapter(getContext(), this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItemViewModel = MainActivity.obtainViewModel(getActivity());
        // Get all item from the database and associate them to the adapter
        mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                adapter.setItems(items);
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                        final int fromPos = viewHolder.getAdapterPosition();
//                        final int toPos = target.getAdapterPosition();
//                        adapter.notifyItemMoved(fromPos, toPos);
//
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        Item myItem = adapter.getItemAtPosition(fromPos);

                        // delete the item
                        mItemViewModel.deleteItem(myItem);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
        return rootView;
    }

    @Override
    public void onItemClick(String url) {
        Timber.v("item is clicked " + url);
//        Uri webPage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivity(intent);
//        }
        Intent intent = new Intent(getActivity(), NewItemActivity.class);
        startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE);
    }
}
