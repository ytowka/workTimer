package com.ytowka.timer.action.ActionType;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.action.ActionAdapter;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionTypeAdapter extends RecyclerView.Adapter<ActionTypeAdapter.ActionTypeViewHolder>{
    private ActionAdapter parentAdapter;

    public ActionTypeAdapter(ActionAdapter actionAdapter, ArrayList<ActionType> actionTypes) {
        this.readyActions = actionTypes;
        this.parentAdapter = actionAdapter;
    }
    private ArrayList<ActionType> readyActions;
    private int inVHindex;
    private ActionAdapter.ActionViewHolder innerVH;

    @NonNull
    @Override
    public ActionTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_action_type,parent,false);
        ActionTypeViewHolder viewHolder = new ActionTypeViewHolder(view);
        return viewHolder;
    }
    public void remove(int index){
        readyActions.remove(index);
        notifyItemRemoved(index);
        parentAdapter.updateExpanded();
    }
    public void add(ActionType actionType){
        readyActions.add(actionType);
        notifyItemInserted(readyActions.size()-1);
        parentAdapter.updateExpanded();
    }
    @Override
    public void onBindViewHolder(@NonNull ActionTypeViewHolder holder, int position) {
        holder.bind(readyActions.get(position),position);
    }
    @Override
    public int getItemCount() {
        return readyActions.size();
    }

    public int getInVHindex(){
        return inVHindex;
    }

    public ActionAdapter.ActionViewHolder getInnerVH() {
        return innerVH;
    }

    public void setInnerVH(ActionAdapter.ActionViewHolder innerVH) {
        this.innerVH = innerVH;
    }

    class ActionTypeViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public static final int UNDELETABLE = 3;

        private Button icon;
        private ActionType actionType;
        private int index = 0;

        public ActionTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.action_type_icon);
            icon.setOnClickListener(this);
        }
        public void bind(ActionType actionType,int id){
            this.actionType = actionType;
            index = id;
            icon.setText(actionType.getName());
            icon.setTextColor(actionType.textColor());

            index = readyActions.indexOf(actionType);

            Drawable drawable = icon.getBackground();
            drawable.setTint(actionType.getColor());
            icon.setBackground(drawable);

            icon.setOnLongClickListener(this);
        }
        public boolean deletable(){
            return index > UNDELETABLE - 1;
        }

        @Override
        public boolean onLongClick(View v) {
            if(deletable()){
                remove(index);
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            //parentAdapter.bindActionType(this.actionType,ActionTypeAdapter.this);
            innerVH.bindActionTypeFields(actionType);

        }
    }
}
