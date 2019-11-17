package com.love.bts.ilovebts.view.presenter;

import android.util.Log;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.adapter.presenter.BaseAdapterContract;
import com.love.bts.ilovebts.view.activity.GroundActivity;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * GroundActivity의 Presenter
 */
public class GroundPresenter implements GroundContract.Presenter {

    /**
     * Context 객체
     */
    private GroundActivity mContext;

    /**
     * Presenter를 사용할 뷰
     */
    private GroundContract.View mView;

    /**
     * Presenter를 사용할 View를 가져오기
     *
     * @param view : View 객체
     * @param activity : Context 객체
     */
    @Override
    public void attachView(final BaseContract.View view, final GroundActivity activity) {
        mView = (GroundContract.View)view;
        mContext = activity;
    }

    /**
     * View를 제거하기
     */
    @Override
    public void detatchView() {
        mView = null;
    }

    @Override
    public void setAdapterView(final BaseAdapterContract.View adapterView) {

    }

    @Override
    public void setAdapterModel(final BaseAdapterContract.Model adapterModel) {

    }

    /**
     * Twitter 인증 초기 셋팅하기
     */
    @Override
    public void initLoginTwitter() {
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(mContext.getString(R.string.api_key),
                                                                   mContext.getString(R.string.api_secret_key));
        final TwitterConfig config = new TwitterConfig.Builder(mContext).twitterAuthConfig(authConfig)
                                                                        .debug(true)
                                                                        .logger(new DefaultLogger(Log.DEBUG))
                                                                        .build();
        Twitter.initialize(config);
    }

    /**
     * Twitter 로그인 API 호출하기
     */
    @Override
    public void callTwitterLoginApi() {

    }
}
