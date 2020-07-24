package com.ytowka.timer.Action.ActionType;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionTypeAdapter extends RecyclerView.Adapter<ActionTypeAdapter.ActionTypeViewHolder>{
    public static  ArrayList<ActionType> readyActions;
    static{
        readyActions = new ArrayList<>();
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.work),MainActivity.res.getColor(R.color.workColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
        readyActions.add(new ActionType("+",MainActivity.res.getColor(R.color.colorPrimary)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
        readyActions.add(new ActionType(MainActivity.res.getString(R.string.rest),MainActivity.res.getColor(R.color.restColor)));
    }

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
    }
    @Override
    public void onBindViewHolder(@NonNull ActionTypeViewHolder holder, int position) {
        if(position == readyActions.size()-1){
            holder.bind(readyActions.get(position),true);
        }else{
            holder.bind(readyActions.get(position),false);
        }
        if(position <= 2){
            holder.setDeletable(false);
        }
    }
    @Override
    public int getItemCount() {
        return readyActions.size();
    }


    class ActionTypeViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private boolean isAddButton = false;
        private boolean deletable = true;
        private Button icon;
        private ActionType actionType;

        public ActionTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.action_type_icon);
        }
        public void bind(ActionType actionType,boolean isAddButton){
            this.isAddButton = isAddButton;
            this.actionType = actionType;
            icon.setText(actionType.getName());
            if(!isAddButton){
                Drawable dr = icon.getBackground();
                dr.setTint(actionType.getColor());
                icon.setBackground(dr);
            }
        }
        public void setDeletable(boolean deletable){
            this.deletable = deletable;
        }

        @Override
        public boolean onLongClick(View v) {
            if(deletable){
                remove(readyActions.indexOf(actionType));
            }
            return true;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
