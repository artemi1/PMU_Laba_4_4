package com.example.pmu_laba_4_4;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class PlayArena extends View {

    float sizeX, sizeY;
    //BtnReload mBtnReload;

    public PlayArena(Context context, AttributeSet attrs) {
        super(context, attrs);

        //polygons = new ArrayList<Polygon>();
       // mBtnReload = new BtnReload(context);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.sizeX = canvas.getWidth();
        this.sizeY = canvas.getHeight();

        canvas.drawRGB(255,255,255);

    }

}
