package com.poc.android.myhospitals.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poc.android.myhospitals.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder> {

    private List<TodoItem> mItems;
    private final LayoutInflater mInflater;
    final private TaskItemAdapter.ListItemClickListener mClickListener;
    List<TodoItem> mDeleteItems = new ArrayList<>();

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onItemClick(List<TodoItem> targetItems);
    }

    public TaskItemAdapter(Context context, List<TodoItem> item, ListItemClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        mItems = item;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_tasks_item, parent, false);

        return new TaskItemViewHolder(itemView);
    }

    // replace the contents of a view
    @Override
    public void onBindViewHolder(@NonNull TaskItemViewHolder holder, int position) {
        if (mItems != null) {
            TodoItem current = mItems.get(position);
            holder.itemName.setText(current.getText());
            holder.itemCheckBox.setChecked(false);
        }
    }

    // provide a reference to the views for each data
    class TaskItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itemName;
        private final CheckBox itemCheckBox;

        TaskItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.taskName);
            itemCheckBox = itemView.findViewById(R.id.taskCheck);

            itemCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TodoItem current = mItems.get(adapterPosition);

            if (itemCheckBox.isChecked()){
                // when item is checked, add the item as delete target
                mDeleteItems.add(current);
                mClickListener.onItemClick(mDeleteItems);
            } else {
                mDeleteItems.remove(current);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    void setTodoItems(List<TodoItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}
