package com.ytowka.timer.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.ytowka.timer.action.ActionType.ActionType;
import com.ytowka.timer.action.ActionType.ActionTypeAdapter;
import com.ytowka.timer.set.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder>{
    public static final int MAX_ACTIONS = 360;

    ArrayList<Action> actions;
    public EditSetActivity main;
    private ArrayList<ActionViewHolder> viewHolders;
    private ArrayList<RecyclerView> actionTypesLists;
    private ArrayList<ActionTypeAdapter> actionTypeAdapters;
    public TextView emptyMassage;
    private boolean isEmpty = true; public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isAllCollapsed = true;
    public int expanded = 0;

    private void generalInit(){
        viewHolders = new ArrayList<>();
        actionTypesLists = new ArrayList<>();
        actionTypeAdapters = new ArrayList<>();
    }
    public ActionAdapter(EditSetActivity main, TextView emptyMassage){
        this.main = main;
        actions = new ArrayList<>();
        this.emptyMassage = emptyMassage;
        generalInit();
    }
    public ActionAdapter(ArrayList<Action> actions, EditSetActivity main, TextView emptyMassage){
        this.actions = actions;
        this.main = main;
        this.emptyMassage = emptyMassage;
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
    @Override
    public int getItemCount() {
        int count = actions.size();
        isEmpty = count == 0;
        emptyMassage.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        return count;
    }
    public boolean add(Action action){
        if(actions.size() < MAX_ACTIONS){
            actions.add(action);
            notifyItemInserted(actions.size());
            updateIndexes();
            return true;
        }
        return false;
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
        expanded = 0;
        isAllCollapsed = true;
        main.updateCollapseIcon(isAllCollapsed);
    }
    public class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Action action;

        private TextView label;
        private TextView time;
        private TextView index;
        private ImageView icon;
        private ImageView repsIcon;
        private CardView expandable_layout;
        private ConstraintLayout main_layout;

        private boolean deletable = true;

        View v;

        private TextInputEditText editLabel;
        private TextInputEditText editTime;
        private Button iconPick;
        private CheckBox reps;

        private Button addAT;
        private Button apply;

        private RecyclerView actioTypesList;
        private ActionTypeAdapter adapter;

        public ActionViewHolder(@NonNull final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            time = itemView.findViewById(R.id.timeText);
            icon = itemView.findViewById(R.id.icon);
            index = itemView.findViewById(R.id.action_index);
            repsIcon = itemView.findViewById(R.id.reps_icon);

            v = itemView;

            //itemView.findViewById()
            editLabel = itemView.findViewById(R.id.editActionName);editLabel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    action.getActionType().setName(s.toString());
                }
            });
            editTime = itemView.findViewById(R.id.timeEditText); editTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    action.setTimeSeconds(getTimeSeconds(s.toString()));
                }
            });
            iconPick = itemView.findViewById(R.id.pickColorBtn); iconPick.setOnClickListener(this);
            reps = itemView.findViewById(R.id.timedSwitcher); reps.setOnClickListener(this);
            apply = itemView.findViewById(R.id.apply_edit_action_btn); apply.setOnClickListener(this);
            addAT = itemView.findViewById(R.id.add_actionType_btn); addAT.setOnClickListener(this);

            expandable_layout = itemView.findViewById(R.id.expandable_layout);
            main_layout = itemView.findViewById(R.id.item_main_Layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick();
                }
            });
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.timedSwitcher:
                    action.setReps(reps.isChecked());
                    break;
                case R.id.apply_edit_action_btn:
                    itemClick();
                    break;
                case R.id.add_actionType_btn:
                    bindAction();
                    adapter.add(new ActionType(action.getName(),action.getColor()));
                    main.saveAT();
                    break;
                case R.id.pickColorBtn:
                    AmbilWarnaDialog dialog = new AmbilWarnaDialog(main, action.getColor(), false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            Drawable a = iconPick.getBackground();
                            a.setTint(color);
                            iconPick.setBackground(a);
                            action.getActionType().setColor(color);
                        }
                    });
                    dialog.show();
                    break;
            }
        }
        private void bindAction(){
            action.getActionType().setName(editLabel.getText().toString());
            //action.getActionType().setColor(color);
            action.setTimeSeconds(getTimeSeconds(editTime.getText().toString()));
            action.setReps(reps.isChecked());
        }
        public int getTimeSeconds(String text){
            if(text.length()>=1){
                String[] time = text.split(":");
                int timeSeconds = 0;
                if(time.length == 1){
                    timeSeconds += Integer.parseInt(time[0]);
                }else if(time.length == 2){
                    timeSeconds += Integer.parseInt(time[1]);
                    timeSeconds += Integer.parseInt(time[0])*60;
                }else if(time.length != 0){
                    timeSeconds += Integer.parseInt(time[time.length-1]);
                    timeSeconds += Integer.parseInt(time[time.length-2])*60;
                }else{
                    timeSeconds = 5;
                }
                if(timeSeconds > 5999) timeSeconds = 5999;
                return timeSeconds;
            }
            return 5;
        }
        public void itemClick(){
            action.setExpanded(!action.isExpanded());
            if(action.isExpanded()){
                isAllCollapsed = false;
                main.updateCollapseIcon(isAllCollapsed);
                expanded++;
            }else{
                expanded--;
                if(expanded == 0){
                    isAllCollapsed = true;
                    main.updateCollapseIcon(true);
                }
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
                Log.i("debug",action.getTime()+" getTimeSbind");
                time.setText(action.getTime());
                repsIcon.setImageDrawable(action.isReps() ? main.getDrawable(R.drawable.ic_replay_black_24dp) : main.getDrawable(R.drawable.ic_timer_black_24dp));

                Drawable drawable = main.getDrawable(R.drawable.oval);
                drawable.setTint(action.getColor());
                icon.setBackground(drawable);
            }
        }
        public void bindActionTypeFields(ActionType actionType){
            //editLabel.setText(actionType.getName());
            action.getActionType().bind(actionType);
            notifyItemChanged(getIndex());
        }
        public int getIndex(){
            return actions.indexOf(action);
        }
        public boolean isDeletable() {
            return deletable;
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
