package com.poc.android.myhospitals.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poc.android.myhospitals.itemdetails.NewItemActivity;
import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.data.Item;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ItemListAdapter.ListItemClickListener {

    private ItemViewModel mItemViewModel;
    public static final int EDIT_ACTIVITY_REQUEST_CODE = 2;

    // Bundle Key
    public static final String ITEM_INFO = "3";

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
    public void onItemClick(Bundle bundle) {
//        Uri webPage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivity(intent);
//        }
        Intent intent = new Intent(getActivity(), NewItemActivity.class);
        intent.putExtra(ITEM_INFO, bundle);
        getActivity().startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
    }
}
