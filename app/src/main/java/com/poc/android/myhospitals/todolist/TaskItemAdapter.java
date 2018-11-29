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

import java.util.List;


public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder> {

    private List<TodoItem> mItems;
    private LayoutInflater mInflater;

    public TaskItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
        if (mItems!= null) {
            TodoItem current = mItems.get(position);
            holder.itemName.setText(current.getText());
        }
    }

    // provide a reference to the views for each data
    class TaskItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;

        public TaskItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.taskName);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    public void setTodoItems(List<TodoItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}
