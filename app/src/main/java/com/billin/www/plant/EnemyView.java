package com.billin.www.plant;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Enemy view
 * <p/>
 * Created by Billin on 2016/12/10.
 */
public class EnemyView extends View {

    private final AnimationDrawable mAnimationDrawable;

    private float currentX;

    private float currentY;

    private boolean isShow = true;

    public static final int BOUND = 100;

    public EnemyView(Context context, float x, float y) {
        super(context);

        // init ordinal
        currentX = x;
        currentY = y;
        setX(currentX - BOUND / 2);
        setY(currentY);
        invalidate();

        // init background
        mAnimationDrawable = new AnimationDrawable() {
            private Handler finishHandler;

            @Override
            public void start() {
                super.start();
                finishHandler = new Handler();
                finishHandler.postDelayed(
                        new Runnable() {
                            public void run() {
                                stop();
                            }
                        }, 500);
            }
        };
        mAnimationDrawable.addFrame(ContextCompat.getDrawable(context, R.drawable.enemy1), 100);
        mAnimationDrawable.addFrame(ContextCompat.getDrawable(context, R.drawable.enemy1_down1), 100);
        mAnimationDrawable.addFrame(ContextCompat.getDrawable(context, R.drawable.enemy1_down2), 100);
        mAnimationDrawable.addFrame(ContextCompat.getDrawable(context, R.drawable.enemy1_down3), 100);
        mAnimationDrawable.addFrame(ContextCompat.getDrawable(context, R.drawable.enemy1_down4), 100);

        setBackground(mAnimationDrawable);
        setLayoutParams(new RelativeLayout.LayoutParams(BOUND, BOUND));
    }

    public void moveDown(int y) {
        if (isShow) {
            currentY = currentY + y;
            setY(currentY);
            invalidate();
        }
    }


    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public boolean isShow() {
        return isShow;
    }

    public void showDestroy() {
        isShow = false;

        mAnimationDrawable.setOneShot(true);
        mAnimationDrawable.start();
    }

}