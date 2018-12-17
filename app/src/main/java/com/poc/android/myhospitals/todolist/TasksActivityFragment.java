package com.poc.android.myhospitals.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.poc.android.myhospitals.R;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment implements TaskItemAdapter.ListItemClickListener {

    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Initialize Firebase components
        // Firebase instance variables
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        //Initialize item ListView
        final List<TodoItem> todoItems = new ArrayList<>();
        final List<String> todoItemsKey = new ArrayList<>();

        // setup the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.tasksRecyclerView);
        final TaskItemAdapter adapter = new TaskItemAdapter(getContext(), todoItems, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TodoItem todoItem = dataSnapshot.getValue(TodoItem.class);
                todoItems.add(todoItem);
                todoItemsKey.add(dataSnapshot.getKey());
                adapter.setTodoItems(todoItems);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = todoItemsKey.indexOf(dataSnapshot.getKey());
                todoItems.remove(index);
                todoItemsKey.remove(index);
                adapter.setTodoItems(todoItems);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

        return rootView;
    }

    @Override
    public void onItemClick(TodoItem current) {
        Query itemQuery = mMessagesDatabaseReference.orderByChild("text").equalTo(current.getText());
        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.v("onCancelled" + databaseError.toException());
            }
        });
    }
}
