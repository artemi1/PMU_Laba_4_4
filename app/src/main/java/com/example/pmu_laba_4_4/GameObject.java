package com.example.pmu_laba_4_4;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameObject extends AppCompatImageView {
    private int arenaMinX, arenaMaxX, arenaMinY, arenaMaxY;
    private int prevEventX, prevEventY;
    private int objCode;
    private MainActivity mainActivity;


    public GameObject(Context context){
        super(context);
        mainActivity = (MainActivity) context;
    }

    public int getObjCode(){return objCode;}

    public float getVertBias(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.getLayoutParams();
        return layoutParams.verticalBias;
    }

    public float getHorBias(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.getLayoutParams();
        return layoutParams.horizontalBias;
    }

    public void initGameObject (int arenaMinX, int arenaMaxX, int arenaMinY, int arenaMaxY, int objCode){
        this.arenaMinX = arenaMinX;
        this.arenaMaxX = arenaMaxX;
        this.arenaMinY = arenaMinY;
        this.arenaMaxY = arenaMaxY;
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
                    if (eventX > arenaMinX && eventY > arenaMinY &&
                            eventX < arenaMaxX && eventY < arenaMaxY) {

                        int deltaX = eventX - prevEventX;
                        int deltaY = eventY - prevEventY;

                        prevEventX = eventX;
                        prevEventY = eventY;

                        float deltaHorizontalBias = (float) deltaX / (arenaMaxX - arenaMinX);
                        float deltaVerticalBias = (float) deltaY / (arenaMaxY - arenaMinY);

                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.getLayoutParams();
                        layoutParams.horizontalBias += deltaHorizontalBias;
                        layoutParams.verticalBias += deltaVerticalBias;
                        this.setLayoutParams(layoutParams);
                    }

                    break;

                case MotionEvent.ACTION_UP: // отпускание
                //case MotionEvent.ACTION_CANCEL:
                    mainActivity.onGameObjectDrop(this);

                    break;

                default:
                    break;
            }
        }
        return true;
    }


}
