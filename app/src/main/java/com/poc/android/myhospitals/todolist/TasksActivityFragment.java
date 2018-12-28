package com.poc.android.myhospitals.todolist;

import android.content.Context;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.poc.android.myhospitals.R;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment implements TaskItemAdapter.ListItemClickListener {

    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    List<TodoItem> mTargetItems;

    private static final int UPDATE_KEY_REMOVE = 0;
    private static final int UPDATE_KEY_ADD = 1;
    public static final int RC_SIGN_IN = 1;

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Initialize Firebase components
        // Firebase instance variables
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

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
                adapter.setTodoItems(todoItems, UPDATE_KEY_ADD);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = todoItemsKey.indexOf(dataSnapshot.getKey());
                todoItems.remove(index);
                todoItemsKey.remove(index);
                adapter.setTodoItems(todoItems, UPDATE_KEY_REMOVE);
                Toast.makeText(getContext(), R.string.item_delete_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Timber.v(String.valueOf(R.string.sign_in));
                } else {
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.v("onResume is called");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.v("onPause is called");
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onItemClick(List<TodoItem> targetItems) {
        mTargetItems = targetItems;
    }

    public void clearSelectedData() {
        if (mTargetItems != null) {
            for (TodoItem mCurrentData : mTargetItems) {
                Query itemQuery = mMessagesDatabaseReference.orderByChild("text").equalTo(mCurrentData.getText());
                itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
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
    }

}
