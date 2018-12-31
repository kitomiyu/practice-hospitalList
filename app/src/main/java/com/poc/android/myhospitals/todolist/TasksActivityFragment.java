package com.poc.android.myhospitals.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment implements TaskItemAdapter.ListItemClickListener {

    // Firebase instance variables
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private List<TodoItem> mTargetItems;
    private String mUsername;
    private List<TodoItem> todoItems;
    private List<String> todoItemsKey;
    private TaskItemAdapter adapter;
    private Boolean authFlag = false;
    private Context mContext;

    private static final int UPDATE_KEY_REMOVE = 0;
    private static final int UPDATE_KEY_ADD = 1;
    public static final int RC_SIGN_IN = 1;
    private static final String ANONYMOUS = "anonymous";

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        mContext = getContext();

        Timber.v("------ onCreateView is called");

        // Initialize Firebase components
        // Firebase instance variables
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        //Initialize item ListView
        todoItems = new ArrayList<>();
        todoItemsKey = new ArrayList<>();

        // setup the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.tasksRecyclerView);
        adapter = new TaskItemAdapter(getContext(), todoItems, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (!authFlag) {
                        // User is signed in
                        onSignedInInitialize(user.getDisplayName());
                        Toast.makeText(mContext, R.string.sign_in, Toast.LENGTH_LONG).show();
                        onSignedInInitialize(user.getDisplayName());
                        authFlag = true;
                    }
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Collections.singletonList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(mContext, R.string.sign_in_initial, Toast.LENGTH_SHORT).show();
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext, R.string.sign_in_cancel, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.v("------ onResume is called");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.v("------ onPause is called");
    }

    public String returnUser() {
        return mUsername;
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

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
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
                    Toast.makeText(mContext, R.string.item_delete_success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}
