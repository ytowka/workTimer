package com.ytowka.timer.action;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.set.MainActivity;
import com.ytowka.timer.R;
import com.ytowka.timer.set.Set;

import static com.ytowka.timer.set.MainActivity.ACTION_TYPES_PREFS;
import static com.ytowka.timer.set.MainActivity.APP_PREFERNCES;
import static com.ytowka.timer.set.MainActivity.gson;
import static com.ytowka.timer.set.MainActivity.readyActions;

public class EditSetActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    FloatingActionButton done;
    FloatingActionButton add;

    RecyclerView actionList;
    ActionAdapter adapter;

    private Set set;
    private static Set newSet;

    public void saveAT(){
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREFERNCES,MODE_PRIVATE).edit();
        editor.putString(ACTION_TYPES_PREFS,gson.toJson(readyActions));
        editor.apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);
        toolbar = findViewById(R.id.editLayoutToolBar);
        done = findViewById(R.id.fabActionListDone);
        add = findViewById(R.id.fabActionListAdd);
        done.setOnClickListener(this);
        add.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.createLayoutTitle);


        int requestCode = getIntent().getExtras().getInt("requestCode");

        actionList = findViewById(R.id.editSetRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        actionList.setLayoutManager(layoutManager);

        try{
            int id = getIntent().getExtras().getInt(MainActivity.SETID);
            set = MainActivity.adapter.getSets().get(id);
            newSet = new Set(set.getActions(),set.getName());
            adapter = new ActionAdapter(newSet.getActions(),this,(TextView)findViewById(R.id.empry_actions_massage));
        }catch (NullPointerException e){
            adapter = new ActionAdapter(this,(TextView)findViewById(R.id.empry_actions_massage));
            adapter.add(new Action(1, true, readyActions.get(0)));
        }
        if(requestCode == MainActivity.ADD_SET){
            getSupportActionBar().setTitle(R.string.newSet);
            getSupportActionBar().setSubtitle(R.string.createLayoutTitle);
        }else if(requestCode == MainActivity.EDIT_SET){
            getSupportActionBar().setTitle(set.getName());
            getSupportActionBar().setSubtitle(R.string.editToolBarTitle);
        }

        adapter.getTouchHelper().attachToRecyclerView(actionList);
        actionList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabActionListAdd:
                if (!adapter.add(new Action(10,false, readyActions.get(0)))) Toast.makeText(this,getResources().getText(R.string.max_actions_limit),Toast.LENGTH_SHORT).show();
                break;
            case R.id.fabActionListDone:
                NamingDialog dialog = new NamingDialog(newSet);
                dialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.ThemeDialogCustom);
                dialog.show(getSupportFragmentManager(),"naming dialog");

                break;
        }
    }
    public void finishEditing(){
        set.bind(newSet.getActions(),newSet.getName());
        adapter.collapseAll();
        Intent response = new Intent();
        setResult(RESULT_OK,response);
        finish();
    }
    public void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    public Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collapse_items,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.collapse_items_btn:
                adapter.collapseAll();
                break;
        }
        return true;
    }
    public void updateCollapseIcon(boolean isAllCollapsed){
        if(isAllCollapsed){
            menu.getItem(0).setIcon(R.drawable.ic_chevron_left_black_24dp);
        }else{
            menu.getItem(0).setIcon(R.drawable.ic_expand_less_black_24dp);
        }
    }
}
