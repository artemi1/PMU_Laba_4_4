package com.example.pmu_laba_4_4;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity{


    private ImageButton btnRestart, btnBanner;
    private TextView scoreField;
    private int gdlnTopID, gdlnBottomID;
    private int arenaMinX, arenaMaxX, arenaMinY, arenaMaxY;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private GameObject mBarn;
    private Random random;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициализируем рандомайзер
        long millisecondsFromEpoch = System.currentTimeMillis();
        random = new Random(millisecondsFromEpoch);

        scoreField = (TextView) findViewById(R.id.txtViewScore);
        scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

        btnBanner = (ImageButton) findViewById(R.id.btnBanner);

        // обработчик кнопки Restart
        btnRestart = (ImageButton) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // на тот случай, если рестарт нажали пр видимом финальном баннере
                btnBanner.setVisibility(View.GONE);
                mBarn.setVisibility(View.VISIBLE);

                // запускаем анимацию
                Animation animZoomBounced = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
                animZoomBounced.setInterpolator(new BounceInterpolator());
                btnRestart.startAnimation(animZoomBounced);

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



        // когда основной layout будет инициализирован - узнАем его размер и создадим игровые объекты
        final ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.playArena);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                populateArena(mainLayout.getWidth(), mainLayout.getHeight());
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    // -----------------------------------------------------------------------------
    // вычисляем и сохраняем размер игрового поля, создаем игровые объекты
    // -----------------------------------------------------------------------------
    private void populateArena(int layoutXSize, int layoutYSize){
        // сохраняем IDшки верхней и нижней линии привязки
        // для дальнейшей привязки к ним игровых объектов
        Guideline gdlnTop = (Guideline) findViewById(R.id.gdlnTop);
        this.gdlnTopID = gdlnTop.getId();
        Guideline gdlnBottom = (Guideline) findViewById(R.id.gdlnBottom);
        this.gdlnBottomID = gdlnBottom.getId();

        // сохраняем размер игрового поля по Х
        this.arenaMinX = 0;
        this.arenaMaxX = layoutXSize;

        // вычисляем и сохраняем размер игрового поля по Y
        ConstraintLayout.LayoutParams gdlnTopParams = (ConstraintLayout.LayoutParams) gdlnTop.getLayoutParams();
        ConstraintLayout.LayoutParams gdlnBottomParams = (ConstraintLayout.LayoutParams) gdlnBottom.getLayoutParams();
        this.arenaMinY = Math.round(layoutYSize * gdlnTopParams.guidePercent);
        this.arenaMaxY = Math.round(layoutYSize * gdlnBottomParams.guidePercent);

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
        float X=0.0f, Y=0.0f;
        ConstraintLayout.LayoutParams layoutParams;

        GameObject mGameObject = new GameObject(this);
        mGameObject.initGameObject(arenaMinX, arenaMaxX, arenaMinY, arenaMaxY, objCode);

        int gameObjectID = View.generateViewId();
        mGameObject.setId(gameObjectID);

        // сарай размкщаем в середине игровгого поля
        if (objCode == GlobalConstants.OBJ_CODE_BARN){
            X=0.5f;
            Y=0.5f;
            layoutParams = new ConstraintLayout.LayoutParams(GlobalConstants.BARN_SIZE,
                    GlobalConstants.BARN_SIZE);
        // генерируем смещения для волков и овец, чтобы он не налезали на уже созданные объекты
        }else {
            boolean exitFlag = false;
            while (!exitFlag) {
                exitFlag = true;

                X = random.nextFloat();
                Y = random.nextFloat();

                if (X > mBarn.getHorBias() - 0.15f && X < mBarn.getHorBias() + 0.15f &&
                        Y > mBarn.getVertBias() - 0.15f && Y < mBarn.getVertBias() + 0.15f){
                    exitFlag = false;
                    continue;
                }

                for (GameObject gameObject : gameObjects){
                    if (X > gameObject.getHorBias() - 0.15f && X < gameObject.getHorBias() + 0.15f &&
                            Y > gameObject.getVertBias() - 0.15f && Y < gameObject.getVertBias() + 0.15f){
                        exitFlag = false;
                    }
                }
            }

            layoutParams = new ConstraintLayout.LayoutParams(GlobalConstants.WOLF_SHEEP_SIZE,
                    GlobalConstants.WOLF_SHEEP_SIZE);
        }

        mGameObject.setLayoutParams(layoutParams);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.playArena);
        layout.addView(mGameObject);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        // привязываем игровой объект по горизонтали
        constraintSet.connect(gameObjectID, ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(gameObjectID, ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        constraintSet.setHorizontalBias(gameObjectID, X);

        // привязываем игровой обънект по вертикали
        constraintSet.connect(gameObjectID, ConstraintSet.TOP, gdlnTopID, ConstraintSet.BOTTOM);
        constraintSet.connect(gameObjectID, ConstraintSet.BOTTOM, gdlnBottomID, ConstraintSet.TOP);
        constraintSet.setVerticalBias(gameObjectID, Y);

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

                if (gameObjects.isEmpty()){
                    onLevelCompletion();
                }
                break;
            }

            if (isSwiped(droppedObject)){
                score--;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

                if (gameObjects.isEmpty()){
                    onLevelCompletion();
                }
                break;
            }


        case GlobalConstants.OBJ_CODE_WOLF:
            if (isDroppedOnBarn(droppedObject)) {
                score--;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

                if (gameObjects.isEmpty()){
                    onLevelCompletion();
                }
                break;
            }

            if (isSwiped(droppedObject)) {
                score++;
                removeGameObject(droppedObject);
                scoreField.setText(GlobalConstants.SCORE_MESSAGE + String.valueOf(score));

                if (gameObjects.isEmpty()){
                    onLevelCompletion();
                }
                break;
            }

        }
    }



    // -----------------------------------------------------------------------------
    // был ли объект дропнут на сарай?
    // -----------------------------------------------------------------------------
    private boolean isDroppedOnBarn (GameObject droppedObj){
        if (droppedObj.getHorBias() > mBarn.getHorBias() - 0.1f &&
                droppedObj.getHorBias() < mBarn.getHorBias() + 0.1f &&
                droppedObj.getVertBias() > mBarn.getVertBias() - 0.1f &&
                droppedObj.getVertBias() < mBarn.getVertBias() + 0.1f){
            return true;
        }else{
            return false;
        }
    }

    // -----------------------------------------------------------------------------
    // был ли объект дропнут на правый/левый край поля (=свайпнут)?
    // -----------------------------------------------------------------------------
    private boolean isSwiped (GameObject droppedObj){
        if (droppedObj.getHorBias() < 0.1f ||
                droppedObj.getHorBias() > 0.9f){

            return true;
        }else{
            return false;
        }
    }

    // -----------------------------------------------------------------------------
    // удаляем объект с layout'а и из списка активных объектов
    // -----------------------------------------------------------------------------
    private void removeGameObject(GameObject mObj){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.playArena);
        layout.removeView(mObj);
        gameObjects.remove(mObj);
    }

    // -----------------------------------------------------------------------------
    // отображение баннера завершения уровня
    // -----------------------------------------------------------------------------
    private void onLevelCompletion(){
        mBarn.setVisibility(View.GONE);

        Animation animZoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        btnBanner.setVisibility(View.VISIBLE);
        btnBanner.startAnimation(animZoom);

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBanner.setVisibility(View.GONE);
                mBarn.setVisibility(View.VISIBLE);

                // удаляем оставшихся волков и овец
                // (по идее, их быть уже не должно, но на всякий случай)
                while(!gameObjects.isEmpty()){
                    removeGameObject(gameObjects.get(0));
                }

                // создаем новых волков и овец
                for (int i = 0; i < GlobalConstants.NUMBER_OF_GAME_OBJECTS; i++) {
                    int objCode = random.nextInt(2)+1;  // 1 (=wolf) or 2 (=sheeр)
                    GameObject mGameObject = placeNewGameObject(objCode);
                    gameObjects.add(mGameObject);
                }
            }
        });
    }

}
