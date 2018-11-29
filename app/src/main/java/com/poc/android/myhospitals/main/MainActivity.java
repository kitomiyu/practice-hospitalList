package com.poc.android.myhospitals.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.data.Item;
import com.poc.android.myhospitals.itemdetails.NewItemActivity;
import com.poc.android.myhospitals.todolist.TasksActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static ItemViewModel viewModel;
    private static final int NEW_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_ACTIVITY_REQUEST_CODE = 2;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());

        setupToolbar();

        setupNavigation();

        setupViewFragment();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE);
            }
        });
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
                                    // Do nothing, we're already on that screen
                                    break;
                                case R.id.nav_medicine:
                                    //TODO
                                    Timber.v("----- under construction1 ------");
                                    return true;
                                case R.id.nav_todo:
                                    Intent intent =
                                            new Intent(MainActivity.this, TasksActivity.class);
                                    startActivity(intent);
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

    // add toolbar as action bar
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze);
    }

    // handle navigation click events
    private void setupViewFragment() {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, mainActivityFragment)
                .commit();
    }

    public static ItemViewModel obtainViewModel(FragmentActivity activity) {
        viewModel =
                ViewModelProviders.of(activity).get(ItemViewModel.class);
        return viewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.clear_data:
                // Add a toast just for confirmation
                Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();
                // Delete existing data
                viewModel.deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = new Item(
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_URL),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_ACCOUNT),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_PASSWORD));
            viewModel.insert(item);
        } else if (requestCode == EDIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = new Item(
                    data.getIntExtra(NewItemActivity.EXTRA_REPLY_ID, 0),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_URL),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_ACCOUNT),
                    data.getStringExtra(NewItemActivity.EXTRA_REPLY_PASSWORD));
            viewModel.updateItem(item);
            Toast.makeText(this, R.string.item_edited, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }
}