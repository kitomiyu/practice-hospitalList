package com.poc.android.myhospitals.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poc.android.myhospitals.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksActivityFragment extends Fragment {

    public static final String ANONYMOUS = "anonymous";

    public TasksActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        // setup the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.tasksRecyclerView);
        final TaskItemAdapter adapter = new TaskItemAdapter(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
