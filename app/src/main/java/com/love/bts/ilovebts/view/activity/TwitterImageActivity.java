package com.love.bts.ilovebts.view.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.love.bts.ilovebts.R;

public class TwitterImageActivity extends BaseActivity {

    /**
     * Full ImageView 객체
     */
    private ImageView mIvFullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_image);
        initView();
    }

    /**
     * 뷰 초기화하기
     */
    private void initView() {
        mIvFullImage = findViewById(R.id.iv_full_image);
    }

}
