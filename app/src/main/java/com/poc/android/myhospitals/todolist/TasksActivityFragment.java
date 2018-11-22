package com.poc.android.myhospitals.todolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.poc.android.myhospitals.R;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private List<TodoItem> todoItems;

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items");

        return rootView;
    }
}
