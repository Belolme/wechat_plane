package com.billin.www.plant;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int mScreenX;

    private int mScreenY;

    private TextView mScoreTextView;

    private Button mStartAndStopButton;

    private boolean isStart;

    private RelativeLayout mContainer;

    private HeroView mHero;

    private List<BulletView> mBulletViews;

    private List<EnemyView> mEnemyViews;

    private int mScore;

    private Timer mCreateEnemyThread;

    private Timer mMoveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScore = 0;

        isStart = false;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenX = dm.widthPixels;
        mScreenY = dm.heightPixels - 50;

        mContainer = (RelativeLayout) findViewById(R.id.activity_main);

        mScoreTextView = (TextView) findViewById(R.id.score);

        mStartAndStopButton = (Button) findViewById(R.id.start_and_stop);
        mStartAndStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStart) {
                    pauseGame();
                } else {
                    startGame();
                }
            }
        });

        mHero = new HeroView(this);
        mContainer.addView(mHero);

        mBulletViews = new ArrayList<>();

        mEnemyViews = new ArrayList<>();
    }

    private void increaseScore() {
        mScore++;
        mScoreTextView.setText(String.valueOf(mScore));
    }

    private void stopUpdateView() {
        mCreateEnemyThread.cancel();
        mMoveThread.cancel();
    }

    private void pauseGame() {
        isStart = false;
        stopUpdateView();
        mStartAndStopButton.setText("Start");
    }

    private void resetGame() {
        isStart = false;
        mStartAndStopButton.setText("Start");

        // resetGame score number
        mScore = 0;
        mScoreTextView.setText(String.valueOf(mScore));

        stopUpdateView();

        // remove all view and resetGame hero plant
        for (EnemyView enemyView : mEnemyViews) {
            mContainer.removeView(enemyView);
        }
        mEnemyViews = new ArrayList<>();

        for (BulletView bulletView : mBulletViews) {
            mContainer.removeView(bulletView);
        }
        mBulletViews = new ArrayList<>();

        mContainer.removeView(mHero);
        mHero = new HeroView(this);
        mContainer.addView(mHero);
    }

    private void startGame() {

        isStart = true;
        mStartAndStopButton.setText("Pause");

        mCreateEnemyThread = new Timer();
        mCreateEnemyThread.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // create new bullet
                        BulletView bulletView = new BulletView(MainActivity.this,
                                mHero.getCurrentX(),
                                mHero.getCurrentY());

                        mContainer.addView(bulletView);
                        mBulletViews.add(bulletView);

                        // move bullets
                        for (int i = 0; i < mBulletViews.size(); i++) {
                            BulletView bullet = mBulletViews.get(i);

                            // if bullet's ordinal is equal enemy's ordinal
                            // show enemy destroyed
                            for (int j = 0; j < mEnemyViews.size(); j++) {
                                EnemyView enemy = mEnemyViews.get(j);
                                if (bullet.getCurrentY() > enemy.getCurrentY() - EnemyView.BOUND / 2
                                        && bullet.getCurrentY() < enemy.getCurrentY() + EnemyView.BOUND / 2
                                        && bullet.getCurrentX() > enemy.getCurrentX() - EnemyView.BOUND / 2
                                        && bullet.getCurrentX() < enemy.getCurrentX() + EnemyView.BOUND / 2
                                        && enemy.isShow()) {

                                    enemy.showDestroy();
//                                    mContainer.removeView(enemy);
//                                    mEnemyViews.remove(enemy);

                                    mContainer.removeView(bullet);
                                    mBulletViews.remove(bullet);

                                    increaseScore();
                                    break;
                                }
                            }

                            bullet.moveUp(80);

                            if (bullet.getCurrentY() < 0) {
                                mContainer.removeView(bullet);
                                mBulletViews.remove(bullet);
                                i--;
                            }
                        }

                        // move enemy
                        for (int i = 0; i < mEnemyViews.size(); i++) {
                            EnemyView enemy = mEnemyViews.get(i);
                            enemy.moveDown(20);

                            if (enemy.getCurrentX() > mHero.getCurrentX() - HeroView.BOUND / 2
                                    && enemy.getCurrentX() < mHero.getCurrentX() + HeroView.BOUND / 2
                                    && enemy.getCurrentY() > mHero.getCurrentY() - HeroView.BOUND / 2
                                    && enemy.getCurrentY() < mHero.getCurrentY() + HeroView.BOUND / 2
                                    && enemy.isShow()) {

                                // restart game
                                resetGame();
                            }

                            if (enemy.getCurrentY() > mScreenY
                                    || (!enemy.isShow() && !((AnimationDrawable) enemy.getBackground()).isRunning())) {
                                mContainer.removeView(enemy);
                                mEnemyViews.remove(enemy);
                                i--;
                            }
                        }

                    }
                });

            }
        }, 0, 100);

        final Random random = new Random();

        mMoveThread = new Timer();
        mMoveThread.schedule(new TimerTask() {
            @Override
            public void run() {

                // create enemy randomly
                final EnemyView enemyView = new EnemyView(MainActivity.this, random.nextInt(mScreenX), 50f);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContainer.addView(enemyView);
                        mEnemyViews.add(enemyView);
                    }
                });
            }
        }, 0, 1000);
    }
}
