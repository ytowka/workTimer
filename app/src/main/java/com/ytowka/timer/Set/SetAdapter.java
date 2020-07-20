package com.ytowka.timer.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ytowka.timer.Action.Action;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.setViewHolder>{
    private ArrayList<Set> sets;
    MainActivity main;

    public SetAdapter(MainActivity main) {
        sets = new ArrayList<>();
        this.main = main;
    }
    public SetAdapter(ArrayList<Set> setList,MainActivity main){
        sets = setList;
        this.main = main;
    }
    @NonNull
    @Override
    public setViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.set_list_element;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,parent,false);
        setViewHolder viewHolder = new setViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull setViewHolder holder, int position) {
        holder.bind(sets.get(position));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    class setViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView time;
        private FloatingActionButton fab;
        private Set set;

        public setViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameLabel);
            time = itemView.findViewById(R.id.timeLabel);
            fab = itemView.findViewById(R.id.goFab);
            fab.setOnClickListener(this);
        }
        public void bind(Set set){
            this.set = set;
            this.name.setText(set.getName());
            this.time.setText(MainActivity.res.getString(R.string.approximate_time)+": "+set.getTime());
        }
        public Set getSet(){
            return set;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.goFab:
                    main.launchSet(set);
                    break;
            }
        }
    }
    public void add(Set set){
        sets.add(set);
        notifyDataSetChanged();
    }
}
