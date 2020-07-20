package com.ytowka.timer.Action;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.Action.ActionType.ActionTypeAdapter;
import com.ytowka.timer.Action.touchHelper.actionItemTouchHelper;
import com.ytowka.timer.R;

public class editSetActivity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton done;
    FloatingActionButton add;

    RecyclerView actionList;
    ActionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_edit_activity);
        toolbar = findViewById(R.id.editLayoutToolBar);
        done = findViewById(R.id.fabActionListDone);
        add = findViewById(R.id.fabActionListAdd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.editLayoutTitle);

        actionList = findViewById(R.id.editSetRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        actionList.setLayoutManager(layoutManager);
        adapter = new ActionAdapter(this);
        ItemTouchHelper.Callback callback = new actionItemTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(actionList);
        actionList.setAdapter(adapter);

        adapter.add(new Action(1,true,ActionTypeAdapter.readyActions.get(0)));
        adapter.add(new Action(2,true,ActionTypeAdapter.readyActions.get(1)));
        adapter.add(new Action(3,true,ActionTypeAdapter.readyActions.get(0)));
        adapter.add(new Action(4,true,ActionTypeAdapter.readyActions.get(0)));
        adapter.add(new Action(5,true,ActionTypeAdapter.readyActions.get(1)));
        adapter.add(new Action(6,true,ActionTypeAdapter.readyActions.get(0)));
    }
}
