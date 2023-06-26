package com.example.sfcm;

import android.os.Bundle;

import com.example.sfcm.src.ContactPreferenceAdapter;
import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sfcm.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private List<ContactPreference> dataList = new ArrayList<>();
    private ContactPreferenceDB database;
    private ContactPreferenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = ContactPreferenceDB.getInstance(this);
        dataList = database.mainDao().getAll();

        setSupportActionBar(binding.toolbar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactPreferenceAdapter(this, dataList);
        binding.recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactPreference data = new ContactPreference();
                data.name = "asdf";
                data.phone = "asdf2";
                database.mainDao().insert(data);
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO thep : Add settings
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //TODO thep : Add setting activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}