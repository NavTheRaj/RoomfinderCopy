package com.example.roomfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    SearchView searchView;
    private CharSequence searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_room);

        setActionAndToolBar();

        setUpRecyclerView(); // Sets up recycler view and its data
    }

    public void setActionAndToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showroom_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_action);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); //So that there wont be any search icon in the users enter keyboard

        //Search view text listenner
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {@Override
        public boolean onQueryTextSubmit(String s) {

            return false;
        }

            //We are creating real time search thats why results will be shown as soon as the text is changed
            @Override
            public boolean onQueryTextChange(String s) {
                rvAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void setUpRecyclerView() {

        SqliteDBHelper dbHelper;

        recyclerView = (RecyclerView) findViewById(R.id.imagesRV);

        dbHelper = new SqliteDBHelper(this);

        final ArrayList < Room > rooms = dbHelper.getRoomsList();

        rvAdapter = new RVAdapter(rooms, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rvAdapter);
    }

}