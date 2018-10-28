package com.poc.android.myhospitals.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.poc.android.myhospitals.Util.ResourceUtil;
import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.data.Item;
import com.poc.android.myhospitals.itemdetails.NewItemActivity;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ItemListAdapter.ListItemClickListener {

    private ItemViewModel mItemViewModel;
    private static final int EDIT_ACTIVITY_REQUEST_CODE = 2;
    Bundle mBundle;

    // Bundle Key
    private static final String ITEM_NAME = "1";
    public static final String ITEM_URL = "2";
    public static final String ITEM_INFO = "3";

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
                        }
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        float mdX = dX;
                        final Bitmap bitmap;

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Get RecyclerView item from the ViewHolder
                            final View itemView = viewHolder.itemView;

                            // Get item data
                            final int fromPos = viewHolder.getAdapterPosition();
                            Item myItem = adapter.getItemAtPosition(fromPos);

                            final Bundle bundle = new Bundle();

                            bundle.putString(ITEM_NAME, myItem.getItem());
                            bundle.putString(ITEM_URL, myItem.getUrl());

                            Paint p = new Paint();
                            final float width = itemView.getHeight() / 5;

                            // LEFT to RIGHT :: edit item
                            if (dX > 0) {
                                mdX = dX / 5;
                                /* Set color for positive displacement */
                                int color = ContextCompat.getColor(getContext(), R.color.colorDarKGreen);
                                p.setColor(color);

                                // Draw Rect with varying right side, equal to displacement dX
                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), mdX,
                                        (float) itemView.getBottom(), p);

                                bitmap = ResourceUtil.getBitmap(getContext(), R.drawable.ic_vector_edit);
                                final float height = (itemView.getHeight() / 2) - (bitmap.getHeight() / 2);

                                c.drawBitmap(bitmap, (float) itemView.getLeft() + width, (float) itemView.getTop() + height, p);

                                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                            // when user tap edit image, show NewItemActivity
                                            Intent intent = new Intent(getActivity(), NewItemActivity.class);
                                            intent.putExtra(ITEM_INFO, bundle);
                                            getActivity().startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
                                        }
                                        return false;
                                    }
                                });

                            } else { // RIGHT to LEFT :: delete item
                                /* Set color for negative displacement */
                                int color = ContextCompat.getColor(getContext(), R.color.colorDarkRed);
                                p.setColor(color);

                                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

                                bitmap = ResourceUtil.getBitmap(getContext(), R.drawable.ic_vector_delete);
                                float height = (itemView.getHeight() / 2) - (bitmap.getHeight() / 2);

                                c.drawBitmap(bitmap, (float) itemView.getRight() - (bitmap.getWidth() + width), (float) itemView.getTop() + height, p);
                            }
                            super.onChildDraw(c, recyclerView, viewHolder, mdX, dY, actionState, isCurrentlyActive);
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