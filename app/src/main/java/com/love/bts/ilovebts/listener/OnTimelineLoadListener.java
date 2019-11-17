package com.love.bts.ilovebts.listener;

import com.love.bts.ilovebts.data.TimelineResult;
import com.twitter.sdk.android.core.Callback;

/**
 * position을 중심으로 이전/이후 Timeline을 불러오는 리스너
 * == Timeline
 */
public interface OnTimelineLoadListener<T> {

    /**
     * minPosition보다 이후 Timeline을 불러온다.
     * 만약 minPosition이 NULL일 경우 최신 Timeline을 불러온다.
     */
    void onLoadNext(final Long minPosition, final Callback<TimelineResult<T>> callback);

    /**
     * maxPosition보다 이전 Timeline을 불러온다.
     */
    void onLoadPrevious(final Long maxPosition, final Callback<TimelineResult<T>> callback);
}
