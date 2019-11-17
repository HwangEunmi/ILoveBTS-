package com.love.bts.ilovebts.view.activity;

import android.os.Bundle;
import android.view.View;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.presenter.controller.PlayerController;

public class TwitterVideoActivity extends BaseActivity {

    /**
     * 비디오 컨트롤러
     */
    private PlayerController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_video);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.onResume();
    }

    @Override
    protected void onPause() {
        mController.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mController.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO : 사라지는 애니메이션 추가
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        // setContentView()에 셋팅한 뷰 가져오기
        final View rootView = this.findViewById(android.R.id.content);

        mController = new PlayerController(rootView);
        mController.initController();
    }

}

