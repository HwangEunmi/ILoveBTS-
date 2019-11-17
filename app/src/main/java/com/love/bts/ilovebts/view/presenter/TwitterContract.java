package com.love.bts.ilovebts.view.presenter;

import com.twitter.sdk.android.core.TwitterException;

import android.content.Context;

/**
 * TwitterPresenter의 인터페이스
 */
public interface TwitterContract {

    interface View extends BaseContract.View {

        void onLoginSuccess(); // Twitter Login에 성공한 경우 호출하기

        void onLoginFail(final TwitterException t_exception); // Twitter Login에 실패한 경우 호출하기

        void startFullActivity(final boolean isImageView); // 해당 화면으로 이동하기
    }

    interface Presenter extends BaseContract.Presenter {

        void loadRemoteData(final Context context, final Long minPosition, final Long maxPosition); // 해당하는 Request의 Response를 요청하기 (서버 데이터를 요청한다.)
    }
}
