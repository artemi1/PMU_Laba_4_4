package com.example.pmu_laba_4_4;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

    ImageButton btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final PlayArena playArena = (PlayArena) findViewById(R.id.playArena);

        btnRestart = (ImageButton) findViewById(R.id.btnRestart);


    }


    public void onRestartBtnClick(View view) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
        animation.setInterpolator(interpolator);
        btnRestart.startAnimation(animation);
    }

}
