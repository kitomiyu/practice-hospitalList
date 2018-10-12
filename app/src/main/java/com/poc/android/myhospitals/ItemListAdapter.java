package com.poc.android.myhospitals;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poc.android.myhospitals.data.Item;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    final Context mContext;
    private final LayoutInflater mInflater;
    List<Item> mItems;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_main_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mItems != null) {
            Item current = mItems.get(position);
            holder.listItemView.setText(current.getItem());
        } else {
            holder.listItemView.setText(R.string.no_item);
        }
    }

    void setItems(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    public ItemListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView listItemView;
        private final ImageView imageItemView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            listItemView = itemView.findViewById(R.id.itemName);
            imageItemView = itemView.findViewById(R.id.imageView);
        }
    }
}
