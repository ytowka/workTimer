package com.ytowka.timer.Action;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.Action.ActionType.ActionTypeAdapter;
import com.ytowka.timer.Action.touchHelper.actionItemTouchHelper;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;
import com.ytowka.timer.Set.Set;

public class editSetActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    FloatingActionButton done;
    FloatingActionButton add;

    RecyclerView actionList;
    ActionAdapter adapter;

    private Set set;
    private Set newSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_edit_activity);
        toolbar = findViewById(R.id.editLayoutToolBar);
        done = findViewById(R.id.fabActionListDone);
        add = findViewById(R.id.fabActionListAdd);
        done.setOnClickListener(this);
        add.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.createLayoutTitle);

        int requestCode = getIntent().getExtras().getInt("requestCode");
        if(requestCode == MainActivity.ADD_SET){
            getSupportActionBar().setSubtitle(R.string.createLayoutTitle);
        }else if(requestCode == MainActivity.EDIT_SET){
            getSupportActionBar().setSubtitle(R.string.editToolBarTitle);
        }

        actionList = findViewById(R.id.editSetRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        actionList.setLayoutManager(layoutManager);

        try{
            int id = getIntent().getExtras().getInt(MainActivity.SETID);
            set = MainActivity.adapter.getSets().get(id);
            newSet = new Set(set.getActions(),set.getName());
            adapter = new ActionAdapter(newSet.getActions(),this);
        }catch (NullPointerException e){
            adapter = new ActionAdapter(this);
            adapter.add(new Action(1, true, ActionTypeAdapter.readyActions.get(0)));
        }
        ItemTouchHelper.Callback callback = new actionItemTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        adapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(actionList);
        actionList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabActionListAdd:

                break;
            case R.id.fabActionListDone:
                set.bind(newSet.getActions(),newSet.getName());
                Intent response = new Intent();
                set.setName("additional");
                setResult(RESULT_OK,response);
                finish();
                break;
        }
    }
}
