package com.poc.android.myhospitals.todolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.main.ItemListAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment {

    public static final String ANONYMOUS = "anonymous";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;

    private List<TodoItem> todoItems;

    private String mUsername;
    private CheckBox mCheckbox;

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        mUsername = ANONYMOUS;

        // Initialize references to views
        mCheckbox = rootView.findViewById(R.id.taskCheck);

        // setup the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.tasksRecyclerView);
        final TaskItemAdapter adapter = new TaskItemAdapter(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items");

        return rootView;
    }
}
