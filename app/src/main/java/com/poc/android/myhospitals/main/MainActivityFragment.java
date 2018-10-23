package com.poc.android.myhospitals.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    Bundle mBundle;

    // Bundle Key
    public static final String ITEM_INFO = "3";
    public static final String ITEM_URL = "2";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);

                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                        // RIGHT to LEFT :: delete item
                        if (direction == ItemTouchHelper.LEFT) {
                            final int fromPos = viewHolder.getAdapterPosition();
                            Item myItem = adapter.getItemAtPosition(fromPos);

                            // delete the item
                            mItemViewModel.deleteItem(myItem);
                        } else { // LEFT to RIGHT :: edit item
                            Intent intent = new Intent(getActivity(), NewItemActivity.class);
                            intent.putExtra(ITEM_INFO, mBundle);
                            getActivity().startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Get RecyclerView item from the ViewHolder
                            View itemView = viewHolder.itemView;
                            Paint p = new Paint();
                            if (dX > 0) {
                                /* Set color for positive displacement */
                                p.setColor(Color.GREEN);

                                // Draw Rect with varying right side, equal to displacement dX
                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                        (float) itemView.getBottom(), p);
                            } else {
                                /* Set color for negative displacement */
                                int color = ContextCompat.getColor(getContext(), R.color.colorDarkRed);
                                p.setColor(color);
                                
                                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
                            }
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }
                });
        helper.attachToRecyclerView(recyclerView);
        return rootView;
    }

    @Override
    public void onItemClick(Bundle bundle) {
        mBundle = bundle;
        Uri webPage = Uri.parse(bundle.getString(ITEM_URL));
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
