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
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
                animation.setInterpolator(interpolator);
                btnRestart.startAnimation(animation);

                // сарай не трогаем!

                // удаляем оставшихся волков и овец
                while(!gameObjects.isEmpty()){
                    removeGameObject(gameObjects.get(0));
                }


                // создаем новых волков и овец
                for (int i = 0; i < GlobalConstants.NUMBER_OF_GAME_OBJECTS; i++) {
                    int objCode = random.nextInt(2)+1;  // 1 (=wolf) or 2 (=sheeр)
                    GameObject mGameObject = placeNewGameObject(objCode);
                    gameObjects.add(mGameObject);
                }


                score=0;
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
            }
        });



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
        mBarn = placeNewGameObject(GlobalConstants.OBJ_CODE_BARN);

        // ...волки и овцы
        for (int i = 0; i < GlobalConstants.NUMBER_OF_GAME_OBJECTS; i++) {
            int objCode = random.nextInt(2)+1;  // 1 (=wolf) or 2 (=sheep)
            GameObject mGameObject = placeNewGameObject(objCode);
            gameObjects.add(mGameObject);
        }
    }






    // -----------------------------------------------------------------------------
    // создаем экземпляр класса GameObject и размещаем его на layout'е
    // -----------------------------------------------------------------------------
    private GameObject placeNewGameObject(int objCode) {
        int X, Y;
        ConstraintLayout.LayoutParams layoutParams;

        GameObject mGameObject = new GameObject(this);

        if (objCode == GlobalConstants.OBJ_CODE_BARN){
            X = maxX/2-GlobalConstants.BARN_SIZE/2;
            Y = maxY/2-GlobalConstants.BARN_SIZE/2;
            layoutParams = new ConstraintLayout.LayoutParams(GlobalConstants.BARN_SIZE,
                    GlobalConstants.BARN_SIZE);
        }else {
            X = random.nextInt(maxX - 100);
            Y = random.nextInt(maxY - 500);
            layoutParams = new ConstraintLayout.LayoutParams(GlobalConstants.WOLF_SHEEP_SIZE,
                    GlobalConstants.WOLF_SHEEP_SIZE);
        }

        mGameObject.initCoords(X, Y, maxX, maxY);
        mGameObject.initPicture(objCode);

        mGameObject.setLayoutParams(layoutParams);

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




    // -----------------------------------------------------------------------------
    // при отпускании игрового объекта
    // -----------------------------------------------------------------------------
    public void onGameObjectDrop(GameObject droppedObject){

        switch(droppedObject.getObjCode()){
        case GlobalConstants.OBJ_CODE_SHEEP:
            if (isDroppedOnBarn(droppedObject)) {
                score++;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
                break;
            }

            if (isSwiped(droppedObject)){
                score--;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
                break;
            }

        case GlobalConstants.OBJ_CODE_WOLF:
            if (isDroppedOnBarn(droppedObject)) {
                score--;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
                break;
            }

            if (isSwiped(droppedObject)) {
                score++;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));
                break;
            }

        }
    }

    private boolean isDroppedOnBarn (GameObject droppedObj){
        if (droppedObj.getCurrX() > mBarn.getCurrX()-50 &&
                droppedObj.getCurrX()+GlobalConstants.WOLF_SHEEP_SIZE < mBarn.getCurrX()+GlobalConstants.BARN_SIZE+50 &&
                droppedObj.getCurrY() > mBarn.getCurrY()-50 &&
                droppedObj.getCurrY()+GlobalConstants.WOLF_SHEEP_SIZE < mBarn.getCurrY()+GlobalConstants.BARN_SIZE+50){

            return true;
        }else{
            return false;
        }
    }

    private boolean isSwiped (GameObject droppedObj){
        if (droppedObj.getCurrX() < 50 ||
                droppedObj.getCurrX() > maxX-100 ||
                droppedObj.getCurrY() < 50 ||
                droppedObj.getCurrY() > maxY-100){

            return true;
        }else{
            return false;
        }
    }

    private void removeGameObject(GameObject mObj){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.playArena);
        layout.removeView(mObj);
        gameObjects.remove(mObj);
        //Log.d("QWERTY", "Object removed!" + mObj);
    }

}
