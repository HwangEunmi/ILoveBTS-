package com.love.bts.ilovebts.presenter.holder;

import com.love.bts.ilovebts.presenter.cursor.TimeLineCursor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TimeLineAdapter로부터 TimeLineCursor의 현재 스크롤 위치를 다룬다.
 */
public class TimeLineScrollHolder {

    /**
     * 다음 TimeLine을 요청하기 위한 Cursor
     */
    private TimeLineCursor mNextCursor;

    /**
     * 이전 TimeLine을 요청하기 위한 Cursor
     */
    private TimeLineCursor mBeforeCursor;

    /**
     * API 콜을 해도 되는 상황인지 판단하는 플래그
     * (이전 API의 Response를 받은 상태인지)
     */
    private final AtomicBoolean mEnableRequestFlag = new AtomicBoolean(false);

    public TimeLineScrollHolder() {

    }

    public TimeLineScrollHolder(final TimeLineCursor nextCursor, final TimeLineCursor beforeCursor) {
        this.mNextCursor = nextCursor;
        this.mBeforeCursor = beforeCursor;
    }

    /**
     * Cursor 초기화
     */
    public void initCursor() {
        mNextCursor = null;
        mBeforeCursor = null;
    }

    /**
     * 다음 TimeLine을 요청할 Position값을 리턴한다.
     */
    public Long getNextPosition() {
        return mNextCursor == null ? null : mNextCursor.mMaxPosition;
    }

    /**
     * 이전 TimeLine을 요청할 Position값을 리턴한다.
     */
    public Long getBeforePosition() {
        return mBeforeCursor == null ? null : mBeforeCursor.mMinPosition;
    }

    /**
     * 다음 TimeLine을 요청할 Position값을 셋팅한다.
     *
     * @param nextCursor : 이후 TimeLine Position
     */
    public void setNextCursor(final TimeLineCursor nextCursor) {
        this.mNextCursor = nextCursor;
        // 만약 이전 TimeLine Position값이 없는 경우 셋팅
        if (mBeforeCursor == null) {
            mBeforeCursor = nextCursor;
        }
    }

    /**
     * 이전 TimeLine을 요청할 Position값을 셋팅한다.
     *
     * @param beforeCursor : 이전 TimeLine Position
     */
    public void setBeforeCursor(final TimeLineCursor beforeCursor) {
        this.mBeforeCursor = beforeCursor;
        // 만약 이후 TimeLine Position값이 없는 경우 셋팅
        if (mNextCursor == null) {
            mNextCursor = beforeCursor;
        }
    }

    /**
     * 현재 API를 콜할 수 있는 상황인지 체크
     * (이전 API Response를 정상적으로 받은 상태인지)
     * @return
     */
    public boolean isEnableCallApi() {
        return mEnableRequestFlag.compareAndSet(false, true);
    }

    /**
     * API를 성공적으로 마친 상태라면 셋팅
     */
    public void setFinishApiCall() {
        mEnableRequestFlag.set(false);
    }

}
