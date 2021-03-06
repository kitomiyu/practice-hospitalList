package com.poc.android.myhospitals.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.poc.android.myhospitals.R;
import com.poc.android.myhospitals.data.Item;
import com.poc.android.myhospitals.itemdetails.NewItemActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static ItemViewModel viewModel;
    private static final int NEW_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
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