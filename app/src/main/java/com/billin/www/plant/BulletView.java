package com.billin.www.plant;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * BulletView for hero plant
 * <p/>
 * Created by Billin on 2016/12/10.
 */
public class BulletView extends View {

    private float currentX;

    private float currentY;

    public static final int BOUND = 20;

    public BulletView(Context context, float x, float y) {
        super(context);

        setLayoutParams(new RelativeLayout.LayoutParams(BOUND, BOUND));
        setBackgroundResource(R.drawable.bullet1);

        currentX = x;
        currentY = y;

        setX(x - BOUND / 2);
        setY(y);
        invalidate();
    }

    public void moveUp(int y) {
        currentY = currentY - y;
        setY(currentY);
        invalidate();
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }
}
