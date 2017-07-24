package com.example.administrator.javatest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View mView;
    private View mViewRoot;
    private int mHeight;
    String TAG = "MainActivity";
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.tv_text);
        mViewRoot = findViewById(R.id.text_view);
        findViewById(R.id.btn_sure).setOnClickListener(this);

        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        //需要先隐藏高度
        mViewRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = mView.getHeight();
                int width = mView.getWidth();
                mHeight = mViewRoot.getMeasuredHeight();
                Log.e(TAG, "onGlobalLayout: height : " + height);
                Log.e(TAG, "onGlobalLayout: measuredHeight: " + mHeight);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mView.getLayoutParams();
                layoutParams.setMargins(0,-mHeight,0,0);
//                layoutParams.height = mHeight;
//                layoutParams.width = width;
                mView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sure:
                doAnimation();
                break;
        }
    }

    boolean isOpen;
    ValueAnimator mSelectTimeAnimator;
    //做动画
    private void doAnimation() {
        if(mSelectTimeAnimator != null && mSelectTimeAnimator.isRunning()){return;}
        mView.setVisibility(View.VISIBLE);
        mSelectTimeAnimator = isOpen ? ValueAnimator.ofFloat(0 , -1.0f ) : ValueAnimator.ofFloat(-1.0f , 0 );
        mSelectTimeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mView.setTranslationY(value * mHeight );
                mView.setAlpha((1+value));
            }
        });
        mSelectTimeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isOpen = !isOpen;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mSelectTimeAnimator.setDuration(2500L);
        mSelectTimeAnimator.start();
    }
}
