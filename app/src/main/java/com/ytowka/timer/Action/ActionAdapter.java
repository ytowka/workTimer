package com.ytowka.timer.Action;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.print.PrinterId;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.Action.touchHelper.ItemTouchHelperAdapter;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<Action> actions;
    editSetActivity main;
    private ItemTouchHelper touchHelper;
    private ArrayList<ActionViewHolder> viewHolders;
    public boolean isAllCollapsed = true;


    public ActionAdapter(editSetActivity main){
        this.main = main;
        actions = new ArrayList<>();
        viewHolders = new ArrayList<>();
    }
    public ActionAdapter(ArrayList<Action> actions, editSetActivity main){
        this.actions = actions;
        this.main = main;
        viewHolders = new ArrayList<>();
    }
    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_action_list,parent,false);
        ActionViewHolder viewHolder = new ActionViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.bind(actions.get(position));
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public void add(Action action){
        actions.add(action);
        notifyItemInserted(actions.size());
        updateIndexes();
    }
    public void remove(int indedx){
        actions.remove(indedx);
        notifyItemRemoved(indedx);
        updateIndexes();
    }

    @Override
    public void onItemMove(int fromPos, int toPos) {
        notifyItemMoved(fromPos,toPos);
        Action buffer = actions.get(fromPos);
        actions.remove(buffer);
        actions.add(toPos,buffer);
        updateIndexes();

    }

    @Override
    public void onItemSwiped(int pos) {
        remove(pos);
    }
    void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }
    public void updateIndexes(){
        for(ActionViewHolder i: viewHolders){
            i.updateIndex();
        }
    }
    public void collapseAll(){
        for(Action i:actions){
            i.setExpanded(false);
            notifyItemChanged(actions.indexOf(i));
        }
        isAllCollapsed = true;
        main.updateCollapseIcon(isAllCollapsed);
        //notifyDataSetChanged();
    }
    class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private Action action;
        private TextView label;
        private TextView time;
        private TextView index;
        private ImageView icon;
        //private ConstraintLayout expandable_layout;
        private CardView expandable_layout;
        private ConstraintLayout main_layout;
        GestureDetector gestureDetector;

        private EditText editName;



        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            time = itemView.findViewById(R.id.timeText);
            icon = itemView.findViewById(R.id.icon);
            index = itemView.findViewById(R.id.action_index);
            expandable_layout = itemView.findViewById(R.id.expandable_layout);
            main_layout = itemView.findViewById(R.id.item_main_Layout);
            gestureDetector = new GestureDetector(itemView.getContext(),this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.setExpanded(!action.isExpanded());
                    if(action.isExpanded()){
                        isAllCollapsed = false;
                        main.updateCollapseIcon(isAllCollapsed);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
        public void bind(Action action){
            this.action = action;
            label.setText(action.getName());
            time.setText(action.getTime());
            updateIndex();
            Drawable drawable = MainActivity.res.getDrawable(R.drawable.oval);
            drawable.setTint(action.getColor());
            icon.setBackground(drawable);
            main_layout.setVisibility(action.isExpanded() ? View.GONE : View.VISIBLE);
            expandable_layout.setVisibility(action.isExpanded() ? View.VISIBLE : View.GONE);
        }
        public void updateIndex(){
            index.setText(String.valueOf(actions.indexOf(action)+1));
        }
        public Action getAction(){
            return action;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
        @Override
        public void onShowPress(MotionEvent e) {

        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {
            touchHelper.startDrag(this);
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
