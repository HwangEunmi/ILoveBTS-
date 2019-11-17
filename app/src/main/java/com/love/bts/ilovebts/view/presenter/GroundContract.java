package com.love.bts.ilovebts.view.presenter;

import com.love.bts.ilovebts.listener.ITwitterLoginApiListener;

/**
 * GroundPresenter의 인터페이스
 */
public interface GroundContract {

    interface View extends BaseContract.View {

        void setITwitterLoginApiListener(final ITwitterLoginApiListener listener); // Twitter Login API Listener 셋팅하기
    }

    interface Presenter extends BaseContract.Presenter {

        void initLoginTwitter(); // Twitter 인증 초기 셋팅하기

        void callTwitterLoginApi(); // Twitter 로그인 API 호출하기
    }
}
