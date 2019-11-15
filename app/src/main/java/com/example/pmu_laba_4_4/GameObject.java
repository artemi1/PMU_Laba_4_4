package com.example.pmu_laba_4_4;

import android.content.Context;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.MotionEventCompat;

public class GameObject extends AppCompatImageView {
    //private float prevX, currX, prevY, currY;
    private int X, Y;
    private int prevEventX, prevEventY;
    //ConstraintLayout parentLayout;
    ConstraintSet constraintSet;

    public GameObject(Context context){
        super(context);

        constraintSet = new ConstraintSet();
    }

    public void initXY (int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int eventX = (int)event.getRawX();
        int eventY = (int)event.getRawY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // нажатие
                prevEventX = eventX;
                prevEventY = eventY;

                break;
            case MotionEvent.ACTION_MOVE: // движение
                int deltaX = eventX - prevEventX;
                int deltaY = eventY - prevEventY;

                prevEventX = eventX;
                prevEventY = eventY;

                X += deltaX;
                Y += deltaY;

                ConstraintLayout parentLayout = (ConstraintLayout)getParent();
                int mID = this.getId();
                constraintSet.clone(parentLayout);
                constraintSet.setMargin(mID, ConstraintSet.START, X);
                constraintSet.setMargin(mID, ConstraintSet.TOP, Y);
                constraintSet.applyTo(parentLayout);

                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }


}
