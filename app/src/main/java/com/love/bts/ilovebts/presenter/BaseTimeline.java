package com.love.bts.ilovebts.presenter;

import com.love.bts.ilovebts.data.TimelineResult;
import com.love.bts.ilovebts.presenter.cursor.TimeLineCursor;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Timeline API 부모 Presenter
 */
public class BaseTimeline {

    /**
     * Timeline의 Response 콜백
     */
    static class TimelineCallback extends Callback<List<Tweet>> {
        // Timeline의 Response 콜백 원본
        final Callback<TimelineResult<Tweet>> rCallback;

        public TimelineCallback(final Callback<TimelineResult<Tweet>> rCallback) {
            this.rCallback = rCallback;
        }

        @Override
        public void success(final Result<List<Tweet>> result) {
            final List<Tweet> list = result.data;
            final TimelineResult<Tweet> rsTimeline = new TimelineResult<Tweet>(new TimeLineCursor(list), list);
            if (rCallback != null) {
                rCallback.success(new Result<>(rsTimeline, result.response));
            }
        }

        @Override
        public void failure(TwitterException exception) {
            if (rCallback != null) {
                rCallback.failure(exception);
            }
        }
    }
}
