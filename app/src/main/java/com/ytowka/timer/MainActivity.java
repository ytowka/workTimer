package com.ytowka.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ytowka.timer.Action.ActionType.ActionType;
import com.ytowka.timer.Set.Set;
import com.ytowka.timer.Action.EditSetActivity;
import com.ytowka.timer.Set.SetAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Resources res;
    public static ArrayList<ActionType> readyActions;

    Toolbar toolbar;
    FloatingActionButton addFab;

    RecyclerView setList;
    public static SetAdapter adapter;

    public static final String SETID = "com.ytowka.timer.SETID";
    public static final int EDIT_SET = 0;
    public static final int ADD_SET = 1;

    public static final String APP_PREFERNCES = "work timer saves";
    public static final String SET_PREFS = "work_timer_sets";
    public static final String ACTION_TYPES_PREFS = "work_timer_action_types";
    public static final String ADS_PREFS = "work_timer_ad_data";

    private boolean isSetsLoaded;
    private boolean isActionTypesLoaded;

    private Gson gson;

    private SharedPreferences preferences;

    @Override
    protected void onDestroy() {
       save();
        super.onDestroy();
    }
    public void save(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SET_PREFS,gson.toJson(adapter.getSets()));
        editor.putString(ACTION_TYPES_PREFS,gson.toJson(readyActions));
        editor.commit();
    }

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

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();

        Type itemsListType = new TypeToken<List<Set>>() {}.getType();
        preferences = getSharedPreferences(APP_PREFERNCES,MODE_PRIVATE);
        if(preferences.contains(SET_PREFS)){
            List<Set> sets = gson.fromJson(preferences.getString(SET_PREFS,""),itemsListType);
            adapter = new SetAdapter(sets,this,(TextView) findViewById(R.id.empty_set_massage));
        }else{
            adapter = new SetAdapter(this,(TextView) findViewById(R.id.empty_set_massage));
        }
        adapter.getTouchHelper().attachToRecyclerView(setList);
        setList.setAdapter(adapter);

        itemsListType = new TypeToken<List<ActionType>>() {}.getType();
        res = getResources();
        if(preferences.contains(ACTION_TYPES_PREFS)){
            readyActions = gson.fromJson(preferences.getString(ACTION_TYPES_PREFS,""),itemsListType);
        }else{
            readyActions = new ArrayList<>();
            readyActions.add(new ActionType(res.getString(R.string.work),res.getColor(R.color.workColor)));
            readyActions.add(new ActionType(res.getString(R.string.rest),res.getColor(R.color.restColor)));
            readyActions.add(new ActionType(res.getString(R.string.prepare),res.getColor(R.color.prepareColor)));
        }
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
                save();
                break;
            case ADD_SET:
                if(resultCode == RESULT_CANCELED){
                    adapter.undo();
                }else{
                    adapter.notifyDataSetChanged();
                }
                save();
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
        Intent intent = new Intent(MainActivity.this, EditSetActivity.class);
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
