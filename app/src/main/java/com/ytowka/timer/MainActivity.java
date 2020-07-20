package com.ytowka.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.Set.Set;
import com.ytowka.timer.Action.editSetActivity;
import com.ytowka.timer.Set.SetAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Resources res;

    Toolbar toolbar;
    FloatingActionButton addFab;

    RecyclerView setList;
    private SetAdapter adapter;
    public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFab = findViewById(R.id.fab);
        addFab.setOnClickListener(this);

        setList = findViewById(R.id.setList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        setList.setLayoutManager(layoutManager);
        adapter = new SetAdapter(this);
        setList.setAdapter(adapter);

        adapter.add(new Set("test"));
        adapter.add(new Set("test2"));

        res = getResources();
        main = this;
    }
    public void launchSet(Set launchedSet){
        Toast.makeText(this,launchedSet.getName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, editSetActivity.class);
                startActivity(intent);
                break;
        }
    }
}
