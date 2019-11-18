package com.example.pmu_laba_4_4;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity{


    private ImageButton btnRestart;
    private TextView scoreField;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private GameObject mBarn;
    private Random random;
    private int maxX, maxY;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRestart = (ImageButton) findViewById(R.id.btnRestart);
        scoreField = (TextView) findViewById(R.id.txtViewScore);
        scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

/*
        // узнаем размер основного layout'а
        final ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.playArena);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //int availableHeight = mainLayout.getMeasuredHeight();
                //if (availableHeight > 0) {
                    maxY = mainLayout.getHeight();
                    maxX = mainLayout.getWidth();
                    mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //}
            }
        });

 */


        maxX=1440;
        maxY=2560;


        // инициализируем рандомайзер
        long millisecondsFromEpoch = System.currentTimeMillis();
        random = new Random(millisecondsFromEpoch);


        // создаем игровые объекты...
        // ...сарай
        mBarn = placeNewGameObject();
        mBarn.initPicture(GlobalConstants.OBJ_CODE_BARN);
        gameObjects.add(mBarn);

        // ...волки и овцы
        for (int i = 0; i < GlobalConstants.NUMBER_OF_GAME_OBJECTS; i++) {
            GameObject mGameObject = placeNewGameObject();
            mGameObject.initPicture(random.nextInt(2)+1); // 1 (=wolf) or 2 (=sheep)
            gameObjects.add(mGameObject);
        }
    }






    // обработчик нажатия клавиши Reload
    private void onRestartBtnClick(View view) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
        animation.setInterpolator(interpolator);
        btnRestart.startAnimation(animation);
    }

    // создаем экземпляр класса GameObject и размещаем его на layout'е
    private GameObject placeNewGameObject() {

        GameObject mGameObject = new GameObject(this);

        int X = random.nextInt(maxX-100);
        int Y = random.nextInt(maxY-400);
        mGameObject.initCoords(X,Y, maxX, maxY);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(200, 200);
        mGameObject.setLayoutParams(lp);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.playArena);
        layout.addView(mGameObject);

        int gameObjectID = View.generateViewId();
        mGameObject.setId(gameObjectID);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(gameObjectID, ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, Y);
        constraintSet.connect(gameObjectID, ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, X);
        constraintSet.applyTo(layout);

        return mGameObject;
    }

    // при отпускании игрового объекта
    public void onGameObjectDrop(GameObject droppedObject){

        int objX = droppedObject.getCurrX();
        int objY = droppedObject.getCurrY();

        switch(droppedObject.getObjCode()){
            case GlobalConstants.OBJ_CODE_SHEEP:
                if (isDroppedOnBarn(droppedObject)){
                    score++;
                    scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

                    removeGameObject(droppedObject);
                }

                break;

            case GlobalConstants.OBJ_CODE_WOLF:
                if (isDroppedOnBarn(droppedObject)){
                    score--;
                    scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
                    removeGameObject(droppedObject);
                }

                break;
        }

    }

    private boolean isDroppedOnBarn (GameObject mObj1){
        if (mObj1.getCurrX() > mBarn.getCurrX()-50 &&
            mObj1.getCurrX() < mBarn.getCurrX()+50 &&
            mObj1.getCurrY() > mBarn.getCurrY()-50 &&
            mObj1.getCurrY() < mBarn.getCurrY()+50){

            return true;
        }else{
            return false;
        }
    }

    private void removeGameObject(GameObject mObj){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.playArena);
        layout.removeView(mObj);
        //mObj = null;
    }

}
