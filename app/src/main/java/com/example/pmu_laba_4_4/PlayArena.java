package com.example.pmu_laba_4_4;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PlayArena extends View implements View.OnTouchListener {

    float sizeX, sizeY;


    public PlayArena(Context context, AttributeSet attrs) {
        super(context, attrs);

        //polygons = new ArrayList<Polygon>();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.sizeX = canvas.getWidth();
        this.sizeY = canvas.getHeight();

        canvas.drawRGB(255,255,255);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //x = event.getX();
        //y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                //sDown = "Down: " + x + "," + y;
                //sMove = ""; sUp = "";
                break;
            case MotionEvent.ACTION_MOVE: // движение
                //sMove = "Move: " + x + "," + y;
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                //sMove = "";
                //sUp = "Up: " + x + "," + y;
                break;
        }
        //tv.setText(sDown + "\n" + sMove + "\n" + sUp);
        return true;
    }

}
