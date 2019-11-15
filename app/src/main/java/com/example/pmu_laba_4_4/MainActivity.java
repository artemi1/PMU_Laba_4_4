package com.example.pmu_laba_4_4;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;


public class MainActivity extends Activity{

    ImageButton btnRestart;
    ArrayList<GameObject> gameObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRestart = (ImageButton) findViewById(R.id.btnRestart);

        int x=200;
        int y=300;

        GameObject mGameObject = new GameObject(this);
        mGameObject.setImageResource(R.drawable.wolf);
        mGameObject.setId(View.generateViewId());
        mGameObject.initXY(x,y);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(200, 200);
        mGameObject.setLayoutParams(lp);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.playArena);
        layout.addView(mGameObject);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(mGameObject.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, y);
        constraintSet.connect(mGameObject.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, x);
        constraintSet.applyTo(layout);



        //gameObjects.add(mGameObject);
    }



    public void onRestartBtnClick(View view) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
        animation.setInterpolator(interpolator);
        btnRestart.startAnimation(animation);
    }



}
