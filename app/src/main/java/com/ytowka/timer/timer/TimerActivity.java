package com.ytowka.timer.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ytowka.timer.R;
import com.ytowka.timer.set.MainActivity;
import com.ytowka.timer.set.Set;

public class TimerActivity extends AppCompatActivity implements TimerCallback {
    private Set set;
    private int currentIndex = 0;

    private SoundPool soundPool;
    private int bipShortSound, bipLongSound;

    private TextView label;
    private ProgressBar progress;
    private ViewPager2 viewPager;
    private TimerViewPagerAdapter pagerAdapter;
    private CheckBox soundCheckBox;

    private boolean startOnPageSelectFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        String jsonObject = (String)getIntent().getExtras().get(MainActivity.JSON_SET_INTENT_KEY);
        set = MainActivity.gson.fromJson(jsonObject,Set.class);

        getWindow().setStatusBarColor(getColor(R.color.bg));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attributes)
                .build();
        bipLongSound = soundPool.load(this,R.raw.bip_long,1);
        bipShortSound = soundPool.load(this,R.raw.bip_short,1);

        soundCheckBox = findViewById(R.id.timeractivity_soundcb);
        soundCheckBox.setChecked(true);
        label = findViewById(R.id.set_name_textview);
        progress = findViewById(R.id.progressBar);
        progress.setMax(set.getTotalTimeSeconds()*1000);
        label.setText(set.getName());
        viewPager = findViewById(R.id.actionViewPager);
        pagerAdapter = new TimerViewPagerAdapter(set.getActions(),this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //Log.i("debug","page scrolled "+position+", "+positionOffset+", "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getCurrentPage().restart();
                currentIndex = position;
                progress.setProgress(set.getTotalTimeSeconds(currentIndex)*1000);
                if(startOnPageSelectFlag){
                    getCurrentPage().start();
                    startOnPageSelectFlag = false;
                }
                Log.i("debug","select: "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }
    public TimerViewPagerAdapter.IntervalViewHolder getCurrentPage(){
        return pagerAdapter.getViewHolderAt(currentIndex);
    }
    private void pause(){
        getCurrentPage().pause();
    }
    private void resume(){
        getCurrentPage().start();
    }
    private void next(){
        if(currentIndex==pagerAdapter.getItemCount()-1){
            finishSet();
        }else{
            startOnPageSelectFlag = true;
            if(!getCurrentPage().getAction().isReps()) viewPager.setCurrentItem(currentIndex+1,true);
        }

    }
    private void finishSet(){

    }
    /*public void changeState(){
        getCurrentPage().isActive() = !active;
        if (active) resume(); else pause();
    }*/
    public void onBackPressed() {
        AlertDialog.Builder ad1=new AlertDialog.Builder(this);
        ad1.setMessage(getText(R.string.exit_timer));
        ad1.setCancelable(false);
        pause();
        ad1.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                resume();
            }
        });
        ad1.setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        AlertDialog alert=ad1.create();
        alert.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        alert.show();

    }
    @Override
    public void onFinishTimer() {
        getCurrentPage().pause();
        next();
    }
    @Override
    public void onUpdateTimer(int timePassed, int timeLeft) {
        getCurrentPage().setProgress(timePassed);
        progress.setProgress(set.getTotalTimeSeconds(currentIndex)*1000+timePassed);
    }

    @Override
        public void onUpdateSecondTimer(int secondsLeft) {
        if(soundCheckBox.isChecked()) {
            if (secondsLeft <= 4 && secondsLeft != 1) {
                if (getCurrentPage().getAction().getTimeSeconds() >= 5) {
                    soundPool.play(bipShortSound, 1, 1, 0, 0, 1);
                }
            } else if (secondsLeft == 1) {
                soundPool.play(bipLongSound, 1, 1, 0, 0, 1);
            }
        }
    }
}
