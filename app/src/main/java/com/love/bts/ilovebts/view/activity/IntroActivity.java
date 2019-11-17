package com.love.bts.ilovebts.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

    private ActivityIntroBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        mBinding.setIntro(this);
        mContext = IntroActivity.this;

        final Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        // 보라색 하트 애니메이션
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animation animMoveHeart = AnimationUtils.loadAnimation(mContext, R.anim.move_heart);
                mBinding.ivAnimHeart.startAnimation(animMoveHeart);
                animMoveHeart.setFillAfter(true);
                animMoveHeart.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mIntent = new Intent(mContext, GroundActivity.class);
                        startActivity(mIntent);
                        finish();
                        // overridePendingTransition(R.anim.move_left_screen, R.anim.move_right_screen);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, 500);
    }
}
