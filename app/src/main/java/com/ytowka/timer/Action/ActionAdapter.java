package com.ytowka.timer.Action;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ytowka.timer.Action.touchHelper.ItemTouchHelperAdapter;
import com.ytowka.timer.MainActivity;
import com.ytowka.timer.R;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<Action> actions;
    Activity main;
    private ItemTouchHelper touchHelper;

    public ActionAdapter(Activity main){
        this.main = main;
        actions = new ArrayList<>();
    }
    public ActionAdapter(ArrayList<Action> actions,Activity main){
        this.actions = actions;
        this.main = main;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.action_list_element,parent,false);
        ActionViewHolder viewHolder = new ActionViewHolder(view);
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
    }
    public void remove(int indedx){
        actions.remove(indedx);
        notifyItemRemoved(indedx);
    }

    @Override
    public void onItemMove(int fromPos, int toPos) {
        Action buffer = actions.get(fromPos);
        actions.remove(buffer);
        actions.add(toPos,buffer);
        notifyItemMoved(fromPos,toPos);
    }

    @Override
    public void onItemSwiped(int pos) {
        remove(pos);
    }
    void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }

    class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private Action action;
        private TextView label;
        private TextView time;
        private ImageView icon;
        GestureDetector gestureDetector;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
            gestureDetector = new GestureDetector(itemView.getContext(),this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(main,"edit "+label.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void bind(Action action){
            this.action = action;
            label.setText(action.getName());
            time.setText(action.getTime());
            Drawable drawable = MainActivity.res.getDrawable(R.drawable.oval);
            drawable.setTint(action.getColor());
            icon.setBackground(drawable);
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
