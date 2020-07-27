package com.ytowka.timer.Action;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ytowka.timer.R;
import com.ytowka.timer.Set.Set;

public class NamingDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText nameET;
    private Button apply;
    private Set set;
    private ViewGroup parent;

    public NamingDialog(Set set){
        this.set = set;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_naming_set,container,false);
        nameET = v.findViewById(R.id.edit_set_name);
        apply = v.findViewById(R.id.apply_edit_set_name); apply.setOnClickListener(this);
        nameET.setText(set.getName());
        parent = container;
        //container.setBackground(MainActivity.res.getDrawable(R.drawable.rounded_dialog));
        return v;
    }

    @Override
    public void onClick(View v) {
        String text = nameET.getText().toString();
        if(text.length()>0){
            set.setName(text);
            getDialog().dismiss();

            Activity activity = getActivity();
            if(activity instanceof EditSetActivity){
                EditSetActivity editSetActivity = (EditSetActivity)activity;
                editSetActivity.finishEditing();
            }
        }else{
            nameET.setError(getResources().getString(R.string.min1char));
        }
    }

}
