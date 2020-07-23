package com.ytowka.timer.Action.ActionType;

import android.view.View;
import android.view.ViewGroup;

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
    }

    @NonNull
    @Override
    public ActionTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull ActionTypeViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 0;
    }
    class ActionTypeViewHolder extends RecyclerView.ViewHolder{
        public ActionTypeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
