package com.ytowka.timer.timer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.R;
import com.ytowka.timer.action.Action;

import java.util.ArrayList;
import java.util.List;

public class TimerViewPagerAdapter extends RecyclerView.Adapter<TimerViewPagerAdapter.IntervalViewHolder> {
    private List<Action> actions;
    private Context context;
    private TimerCallback callback;

    private ArrayList<IntervalViewHolder> viewHolders;
    public IntervalViewHolder getViewHolderAt(int index){
        return viewHolders.get(index);
    }

    public TimerViewPagerAdapter(List<Action>actions, TimerCallback callback){
        this.actions = actions;
        this.callback = callback;
        this.actions = new ArrayList<>();
        this.actions.addAll(actions);
        this.actions.add(Action.finishAction);
        viewHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public IntervalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.page_action,parent,false);
        this.context = context;
        IntervalViewHolder vh = new IntervalViewHolder(view);
        viewHolders.add(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IntervalViewHolder holder, int position) {
        holder.bind(actions.get(position));
        viewHolders.add(position,holder);

    }
    @Override
    public int getItemCount() {
        return actions.size();
    }

    class IntervalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar progress;
        private TextView name;
        private TextView index;
        private ImageView ATTypeIcon;
        private ImageView stateIcon;
        private ConstraintLayout bg;

        private TextView timerText;
        private Timer timer;

        private Action action;

        public boolean isActive(){
            return timer.isActive();
        }
        public void pause(){
            timer.pause();
            stateIcon.setImageDrawable(context.getDrawable(timer.isActive() ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24));
        }
        public void start(){
            timer.start();
            stateIcon.setImageDrawable(context.getDrawable(timer.isActive() ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24));
        }
        public IntervalViewHolder(@NonNull View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.timeraction_progress);
            name = itemView.findViewById(R.id.timeraction_actionname);
            ATTypeIcon = itemView.findViewById(R.id.timeraction_typeicon);
            stateIcon = itemView.findViewById(R.id.timeraction_stateicon);
            timerText = itemView.findViewById(R.id.timeraction_timerview);
            bg = itemView.findViewById(R.id.timeraction_bg);
            index = itemView.findViewById(R.id.timeraction_actionindex);

            stateIcon.setOnClickListener(this);
            bg.setOnClickListener(this);
        }
        public void bind(Action action){
            this.action = action;

            Log.i("debug","bind: "+TimerViewPagerAdapter.this.actions.indexOf(action));

            timer = new Timer(timerText,TimerViewPagerAdapter.this.callback,action.getTimeSeconds());

            timer.setTextColor(action.getActionType().textColor());
            bg.setBackgroundColor(action.getColor());
            progress.setProgressDrawable(action.getActionType().isBgLight() ? context.getDrawable(R.drawable.circular_progress_bar_dark) : context.getDrawable(R.drawable.circular_progress_bar_light));
            progress.setMax(action.getTimeSeconds()*1000);
            progress.setProgress(0);
            name.setTextColor(action.getActionType().textColor());
            name.setText(action.getName());
            ATTypeIcon.setColorFilter(action.getActionType().textColor());
            stateIcon.setColorFilter(action.getActionType().textColor());
            index.setText(actions.indexOf(action)+1+"");
        }
        void changeState(){
            if(timer.isActive()){
                timer.pause();
            }else{
                timer.start();
            }
            stateIcon.setImageDrawable(context.getDrawable(timer.isActive() ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24));
        }
        public void restart(){
            timer.restart();
            if(action != null){
                bind(action);
            }
            stateIcon.setImageDrawable(context.getDrawable(timer.isActive() ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24));
        }
        public void setProgress(int progress){
            this.progress.setProgress(progress);
        }
        public Action getAction(){
            return action;
        }
        @Override
        public void onClick(View v) {
            changeState();
        }
    }
}
