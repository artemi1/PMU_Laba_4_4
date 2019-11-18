package com.example.pmu_laba_4_4;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.MotionEventCompat;

public class GameObject extends AppCompatImageView {
    private int X, Y;
    private int maxX, maxY;
    private int prevEventX, prevEventY;
    private int objCode;
    private MainActivity mainActivity;

    //ConstraintLayout parentLayout;
    //ConstraintSet constraintSet;

    public GameObject(Context context){
        super(context);
        mainActivity = (MainActivity) context;
    }

    public int getCurrX(){return X;}
    public int getCurrY(){return Y;}
    public int getObjCode(){return objCode;}


    public void initCoords (int X, int Y, int maxX, int maxY){
        this.X = X;
        this.Y = Y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void initPicture (int objCode){
        this.objCode = objCode;

        switch (objCode){
            case GlobalConstants.OBJ_CODE_BARN:
                this.setImageResource(R.drawable.barn);
                break;

            case GlobalConstants.OBJ_CODE_WOLF:
                this.setImageResource(R.drawable.wolf);
                break;

            case GlobalConstants.OBJ_CODE_SHEEP:
                this.setImageResource(R.drawable.sheep);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // сарай не двигается!
        if (objCode != GlobalConstants.OBJ_CODE_BARN) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();

            switch (event.getAction()) {
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

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.getLayoutParams();
                    layoutParams.leftMargin = X;
                    layoutParams.topMargin = Y;
                    this.setLayoutParams(layoutParams);


            /*
            ConstraintLayout parentLayout = (ConstraintLayout)getParent();
            int mID = this.getId();
            constraintSet.clone(parentLayout);
            constraintSet.setMargin(mID, ConstraintSet.START, X);
            constraintSet.setMargin(mID, ConstraintSet.TOP, Y);
            constraintSet.applyTo(parentLayout);

            */

                    break;

                case MotionEvent.ACTION_UP: // отпускание
                case MotionEvent.ACTION_CANCEL:
                    mainActivity.onGameObjectDrop(this);

                    break;

                default:
                    break;
            }
        }
        return true;
    }


}
