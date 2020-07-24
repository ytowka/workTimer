package com.ytowka.timer.Action;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.print.PrinterId;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.Action.ActionType.ActionType;
import com.ytowka.timer.Action.ActionType.ActionTypeAdapter;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder>{
    ArrayList<Action> actions;
    editSetActivity main;
    private ArrayList<ActionViewHolder> viewHolders;
    private ArrayList<RecyclerView> actionTypesLists;
    private ArrayList<ActionTypeAdapter> actionTypeAdapters;

    public boolean isAllCollapsed = true;

    public void generalInit(){
        viewHolders = new ArrayList<>();
        actionTypesLists = new ArrayList<>();
        actionTypeAdapters = new ArrayList<>();
    }
    public ActionAdapter(editSetActivity main){
        this.main = main;
        actions = new ArrayList<>();
        generalInit();
    }
    public ActionAdapter(ArrayList<Action> actions, editSetActivity main){
        this.actions = actions;
        this.main = main;
        generalInit();
    }
    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_action_list,parent,false);

        RecyclerView actionTypesList = view.findViewById(R.id.actionTypesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(main, LinearLayoutManager.HORIZONTAL, false);
        actionTypesList.setLayoutManager(layoutManager);
        ActionTypeAdapter actionTypeAdapter = new ActionTypeAdapter(this,MainActivity.readyActions);
        actionTypesList.setAdapter(actionTypeAdapter);

        ActionViewHolder viewHolder = new ActionViewHolder(view);
        viewHolder.setRecycler(actionTypesList);

        viewHolders.add(viewHolder);
        actionTypesLists.add(actionTypesList);
        actionTypeAdapters.add(actionTypeAdapter);
        actionTypeAdapter.setInnerVH(viewHolder);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.bind(actions.get(position));
    }
    public ArrayList<Integer> getExpandedActionIndex(){
        ArrayList<Integer> expandedActions = new ArrayList<>();
        for(Action i : actions){
            if(i.isExpanded()){
                expandedActions.add(actions.indexOf(i));
            }
        }
        return expandedActions;
    }
    public ArrayList<Integer> getExpandedViewHolders(){
        ArrayList<Integer> expandedVH= new ArrayList<>();
        for(ActionViewHolder i:viewHolders){
            if(i.getAction().isExpanded()){
                expandedVH.add(viewHolders.indexOf(i));
            }
        }
        return expandedVH;
    }
    public void updateExpanded(){
        for(int i : getExpandedActionIndex()){
            notifyItemChanged(i);
        }
        updateExpandedVH();
    }
    public void updateExpandedVH(){
        for(int i : getExpandedViewHolders()){
            viewHolders.get(i).updateRV();
        }
    }
    public void bindActionType(ActionType actionType, ActionTypeAdapter adapter){
        int index = actionTypeAdapters.indexOf(adapter);
        viewHolders.get(index).bindActionTypeFields(actionType);
        Log.i("debug","try bind " + index);
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

    public void onItemMove(int fromPos, int toPos) {
        notifyItemMoved(fromPos,toPos);
        Action buffer = actions.get(fromPos);
        actions.remove(buffer);
        actions.add(toPos,buffer);
        updateIndexes();

    }
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
    public class ActionViewHolder extends RecyclerView.ViewHolder {
        private Action action;

        private TextView label;
        private TextView time;
        private TextView index;
        private ImageView icon;
        private ImageView repsIcon;
        private CardView expandable_layout;
        private ConstraintLayout main_layout;

        private boolean deletable = true;

        private EditText editLabel;
        private EditText editTime;
        private Button iconPick;
        private CheckBox reps;
        private Button addAT;

        private RecyclerView actioTypesList;
        private ActionTypeAdapter adapter;

        public ActionViewHolder(@NonNull final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            time = itemView.findViewById(R.id.timeText);
            icon = itemView.findViewById(R.id.icon);
            index = itemView.findViewById(R.id.action_index);
            repsIcon = itemView.findViewById(R.id.reps_icon);

            //itemView.findViewById()
            editLabel = itemView.findViewById(R.id.editActionName);
            editTime = itemView.findViewById(R.id.timeEditText);
            iconPick = itemView.findViewById(R.id.pickColorBtn);
            reps = itemView.findViewById(R.id.timedSwitcher);
            addAT = itemView.findViewById(R.id.add_actionType_btn); addAT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bindAction();
                    adapter.add(new ActionType(action.getName(),action.getColor()));
                }
            });
            expandable_layout = itemView.findViewById(R.id.expandable_layout);
            main_layout = itemView.findViewById(R.id.item_main_Layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick();
                }
            });
        }
        private void bindAction(){
            action.getActionType().setName(editLabel.getText().toString());
            //action.getActionType().setColor(color);
            action.setReps(reps.isChecked());
        }
        public void itemClick(){
            action.setExpanded(!action.isExpanded());
            if(action.isExpanded()){
                isAllCollapsed = false;
                main.updateCollapseIcon(isAllCollapsed);
            }else{
                bindAction();
            }
            notifyItemChanged(getAdapterPosition());
        }
        public void bind(Action action){
            this.action = action;
            updateIndex();

            main_layout.setVisibility(action.isExpanded() ? View.GONE : View.VISIBLE);
            expandable_layout.setVisibility(action.isExpanded() ? View.VISIBLE : View.GONE);

            if(action.isExpanded()){
                deletable = false;
                editLabel.setText(action.getName());
                editTime.setText(action.getTime());
                reps.setChecked(action.isReps());

                Drawable drawable = main.getDrawable(R.drawable.oval);
                drawable.setTint(action.getColor());
                iconPick.setBackground(drawable);

            }else {
                deletable = true;
                label.setText(action.getName());
                time.setText(action.getTime());
                repsIcon.setImageDrawable(action.isReps() ? main.getDrawable(R.drawable.ic_replay_black_24dp) : main.getDrawable(R.drawable.ic_timer_black_24dp));

                Drawable drawable = main.getDrawable(R.drawable.oval);
                drawable.setTint(action.getColor());
                icon.setBackground(drawable);
            }
        }
        public void bindActionTypeFields(ActionType actionType){
            //editLabel.setText(actionType.getName());

            //Drawable drawable = main.getDrawable(R.drawable.oval);
            //drawable.setTint(actionType.getColor());
            //iconPick.setBackground(drawable);

            action.getActionType().bind(actionType);

            notifyItemChanged(getIndex());
            Log.i("debug","try bind ");

        }
        public int getIndex(){
            return actions.indexOf(action);
        }
        public boolean isDeletable() {
            return deletable;
        }
        public void collapse(){
            bindAction();
            action.collapse();
        }
        public void updateIndex(){
            index.setText(String.valueOf(getIndex()+1));
        }
        public Action getAction(){
            return action;
        }
        public void setRecycler(RecyclerView rv){
            actioTypesList = rv;
            adapter = (ActionTypeAdapter) rv.getAdapter();
        }
        public void updateRV(){
            adapter.notifyDataSetChanged();
        }
    }
    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            remove(viewHolder.getAdapterPosition());
        }
    };
    private ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    public ItemTouchHelper.Callback getCallback() {
        return callback;
    }
    public ItemTouchHelper getTouchHelper() {
        return touchHelper;
    }
}
