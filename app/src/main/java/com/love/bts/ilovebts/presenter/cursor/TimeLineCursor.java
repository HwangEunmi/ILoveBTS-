package com.love.bts.ilovebts.presenter.cursor;

import com.twitter.sdk.android.core.models.Identifiable;

import java.util.List;

/**
 * Timeline의 마지막 호출했던 위치를 가진다.
 * (+ NULL 일 수도 있다.)
 */
public class TimeLineCursor {
    // null을 나타내기 위해 long대신 Long 사용
    public final Long mMinPosition;
    public final Long mMaxPosition;

    /**
     * 생성자 1
     *
     * @param minPosition : Response의 최소 위치, 또는 NULL
     * @param maxPosition : Response의 최대 위치, 또는 NULL
     */
    public TimeLineCursor(final Long minPosition, final Long maxPosition) {
        this.mMinPosition = minPosition;
        this.mMaxPosition = maxPosition;
    }

    /**
     * 생성자 2
     *
     * @param list : Response으로부터 최소 위치, 최대 위치를 구한다.
     *             최소 위치는 List의 Size
     *             최대 위치는 0
     */
    public TimeLineCursor(final List<? extends Identifiable> list) {
        this.mMinPosition = list.size() > 0 ? list.get(list.size() - 1).getId() : null;
        this.mMaxPosition = list.size() > 0 ? list.get(0).getId() : null;
    }
}
