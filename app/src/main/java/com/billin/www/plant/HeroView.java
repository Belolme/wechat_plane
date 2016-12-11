package com.billin.www.plant;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * hero plant
 * <p/>
 * Created by Billin on 2016/12/10.
 */
public class HeroView extends View {

    private int mScreenWidth;

    private int mScreenHeight;

    private float currentX;

    private float currentY;

    public static final int BOUND = 200;

    public HeroView(Context context) {
        super(context);

        setBackgroundResource(R.drawable.hero_blowup_n0);
        setLayoutParams(new RelativeLayout.LayoutParams(BOUND, BOUND));

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - 50;

        reset();
    }

    /**
     * reset bullet view and plant view
     */
    public void reset() {
        // reset plant view
        currentX = mScreenWidth / 2;
        currentY = mScreenHeight - BOUND;
        update(currentX, currentY);
    }


    /**
     * update plant's position
     *
     * @param x rawX
     * @param y rawY
     */
    private void update(float x, float y) {
        // update plant's position
        setX(x - BOUND / 2);
        setY(y - BOUND / 2);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下
                return true;

            case MotionEvent.ACTION_MOVE:
                // 手指移动

                if (event.getRawY() > mScreenHeight) {
                    return true;
                }

                currentX = event.getRawX();
                currentY = event.getRawY();
                update(currentX, currentY);
                return true;

            case MotionEvent.ACTION_UP:
                // 手指抬起
                break;

            case MotionEvent.ACTION_CANCEL:
                // 事件被拦截
                break;

            case MotionEvent.ACTION_OUTSIDE:
                // 超出区域
                break;
        }

        return super.onTouchEvent(event);
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }
}
