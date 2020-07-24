package com.ytowka.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.Action.ActionType.ActionType;
import com.ytowka.timer.Set.Set;
import com.ytowka.timer.Action.editSetActivity;
import com.ytowka.timer.Set.SetAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Resources res;
    public static ArrayList<ActionType> readyActions = new ArrayList<>();

    Toolbar toolbar;
    FloatingActionButton addFab;

    RecyclerView setList;
    public static SetAdapter adapter;

    public static final String SETID = "com.ytowka.timer.SETID";
    public static final int EDIT_SET = 0;
    public static final int ADD_SET = 1;

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

        adapter.getTouchHelper().attachToRecyclerView(setList);
        setList.setAdapter(adapter);

        adapter.add(new Set("test"));
        adapter.add(new Set("test2"));
        adapter.add(new Set("test3"));
        adapter.add(new Set("test4"));
        adapter.add(new Set("test5"));
        adapter.add(new Set("test6"));

        res = getResources();

        readyActions.add(new ActionType(res.getString(R.string.work),res.getColor(R.color.workColor)));
        readyActions.add(new ActionType(res.getString(R.string.rest),res.getColor(R.color.restColor)));
    }
    public void launchSet(Set launchedSet){
        launchedSet.launch();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                adapter.put(0,new Set());
                editSet(0,ADD_SET);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDIT_SET:
                if(resultCode == RESULT_CANCELED){

                }else{
                    adapter.notifyDataSetChanged();
                }
                break;
            case ADD_SET:
                if(resultCode == RESULT_CANCELED){
                    adapter.undo();
                }else{
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_item:
                adapter.remove(adapter.contextCallItem);
                break;
            case R.id.edit_item:
                editSet(adapter.contextCallItem,EDIT_SET);
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void editSet(int id,int point){
        Intent intent = new Intent(MainActivity.this, editSetActivity.class);
        intent.putExtra(SETID,id);
        intent.putExtra("requestCode", point);
        startActivityForResult(intent,point);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.set_menu,menu);
    }

    public SetAdapter getAdapter() {
        return adapter;
    }

}
