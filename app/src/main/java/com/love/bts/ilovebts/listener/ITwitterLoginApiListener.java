package com.love.bts.ilovebts.listener;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * 트위터 로그인 API 결과값 리스너
 */
public interface ITwitterLoginApiListener {
    void onLoginApiSuccess(final Result<TwitterSession> result);

    void onLoginApiFail(final TwitterException exception);
}
