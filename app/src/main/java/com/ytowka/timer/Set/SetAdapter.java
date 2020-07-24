package com.ytowka.timer.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.setViewHolder>{
    private ArrayList<Set> sets;
    MainActivity main;
    public int contextCallItem;

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
        int layoutId = R.layout.item_set_list;
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

    class setViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {
        private TextView name;
        private TextView time;
        Button drag;
        //private FloatingActionButton fab;
        private Set set;

        public setViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameLabel);
            time = itemView.findViewById(R.id.timeLabel);
            //fab = itemView.findViewById(R.id.goFab);
            //fab.setOnClickListener(this);
            drag = itemView.findViewById(R.id.dragButton);
            drag.setOnTouchListener(this);
            main.registerForContextMenu(itemView);
        }
        public void bind(final Set set){
            this.set = set;
            this.name.setText(set.getName());
            name.setHorizontalFadingEdgeEnabled(true);
            this.time.setText(MainActivity.res.getString(R.string.approximate_time)+": "+set.getTime());
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    contextCallItem = getAdapterPosition();
                    return false;
                }
            });
        }
        public Set getSet(){
            return set;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //case R.id.goFab:
                //    main.launchSet(set);
                //    break;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            touchHelper.startDrag(this);
            return false;
        }
    }
    public void add(Set set){
        sets.add(set);
        notifyDataSetChanged();
    }
    public void put(int index, Set set){
        sets.add(index,set);
        //notifyDataSetChanged();
    }
    public void undo(){
        sets.remove(0);
    }
    public void update(){
        notifyDataSetChanged();
    }
    public void remove(int index){
        sets.remove(index);
        notifyItemRemoved(index);
    }
    public void swapItems(int from, int to){
        Set buffer = sets.get(from);
        sets.remove(from);
        sets.add(to,buffer);
        notifyItemMoved(from,to);
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags,0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            swapItems(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    };
    private ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    public ItemTouchHelper.Callback getCallback(){
        return callback;
    }
    public ItemTouchHelper getTouchHelper(){
        return touchHelper;
    }
}
