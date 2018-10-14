package com.poc.android.myhospitals;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poc.android.myhospitals.data.Item;

import java.util.List;

import timber.log.Timber;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    final Context mContext;
    final private ListItemClickListener mClickListener;
    private final LayoutInflater mInflater;
    List<Item> mItems;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onItemClick(String url);
    }

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
            holder.listItemUrlView.setText(current.getUrl());
        } else {
            holder.listItemView.setText(R.string.no_item);
            holder.listItemUrlView.setText(R.string.no_item);
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

    public ItemListAdapter(Context context, ListItemClickListener clickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView listItemView;
        private final TextView listItemUrlView;

        private ItemViewHolder(final View itemView) {
            super(itemView);
            listItemView = itemView.findViewById(R.id.itemName);
            listItemUrlView = itemView.findViewById(R.id.itemUrl);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Item current = mItems.get(adapterPosition);
            mClickListener.onItemClick(current.getUrl());
        }
    }

    public Item getItemAtPosition(int position) {
        return mItems.get(position);
    }
}
