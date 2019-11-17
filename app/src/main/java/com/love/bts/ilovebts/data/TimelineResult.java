package com.love.bts.ilovebts.data;

import com.love.bts.ilovebts.presenter.cursor.TimeLineCursor;

import java.util.List;

/**
 * Timeline API Response와 Cursor를 가지고 있는 클래스
 */
public class TimelineResult<T> {

    public final TimeLineCursor mCursor;
    public final List<T> mList;

    public TimelineResult(final TimeLineCursor cursor, final List<T> list) {
        this.mCursor = cursor;
        this.mList = list;
    }

}
