package com.poc.android.myhospitals.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.tododetails.TodoItemActivity;

import timber.log.Timber;

public class TasksActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TasksActivityFragment tasksActivityFragment;
    String mUser;
    final String user_name = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupToolbar();

        setupNavigation();

        setupViewFragment();

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser = tasksActivityFragment.returnUser();
                Intent intent = new Intent(TasksActivity.this, TodoItemActivity.class);
                intent.putExtra(user_name, mUser);
                startActivity(intent);
            }
        });
    }

    private void setupViewFragment() {
        tasksActivityFragment = new TasksActivityFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, tasksActivityFragment)
                .commit();
    }

    // add toolbar as action bar
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    private void setupNavigation() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            //set item as selected to persist highlight
                            item.setChecked(true);
                            //close drawer when item is tapped
                            mDrawerLayout.closeDrawers();

                            int id = item.getItemId();
                            switch (id) {
                                case R.id.nav_list:
                                    NavUtils.navigateUpFromSameTask(TasksActivity.this);
                                    break;
                                case R.id.nav_medicine:
                                    //TODO
                                    Timber.v("----- under construction1 ------");
                                    return true;
                                case R.id.nav_todo:
                                    // Do nothing, we're already on that screen
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    }
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.sign_out:
                //Sign out
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.clear_item:
                tasksActivityFragment.clearSelectedData();
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
