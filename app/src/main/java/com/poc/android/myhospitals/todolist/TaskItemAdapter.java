package com.poc.android.myhospitals.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poc.android.myhospitals.R;

import java.util.ArrayList;
import java.util.List;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder> {

    private List<TodoItem> mItems;
    private final LayoutInflater mInflater;
    final private TaskItemAdapter.ListItemClickListener mClickListener;
    private final List<TodoItem> mDeleteItems = new ArrayList<>();
    Context mContext;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    private static final int UPDATE_KEY_REMOVE = 0;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onItemClick(List<TodoItem> targetItems);
    }

    public TaskItemAdapter(Context context, List<TodoItem> item, ListItemClickListener clickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
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
            holder.bind(position);
            holder.itemName.setText(current.getText());
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

        void bind(int position) {

            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                itemCheckBox.setChecked(false);
            }
            else{
                itemCheckBox.setChecked(true);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TodoItem current = mItems.get(adapterPosition);

            if (!itemStateArray.get(adapterPosition, false)) {
                // when item is checked, add the item as delete target
                mDeleteItems.add(current);
                // add in arraylist as target item to remove
                itemStateArray.put(adapterPosition, true);
                mClickListener.onItemClick(mDeleteItems);
            } else {
                // when item is unchecked, add the item as delete target
                mDeleteItems.remove(current);
                itemStateArray.put(adapterPosition, false);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    void resetValue() {
        itemStateArray.clear();
        mDeleteItems.clear();
    }

    void setTodoItems(List<TodoItem> items, int action) {
        if (action == UPDATE_KEY_REMOVE) {
            resetValue();
        }
        mItems = items;
        notifyDataSetChanged();
    }
}
