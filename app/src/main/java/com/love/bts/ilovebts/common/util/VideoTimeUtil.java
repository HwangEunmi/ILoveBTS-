package com.love.bts.ilovebts.common.util;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 비디오 시간 관련 유틸 클래스
 */
public class VideoTimeUtil {
    // 긴 시간 포맷
    private static final String TIME_FORMAT_LONG = "%1$d:%2$02d:%3$02d"; // x:00:00 형식

    // 짧은 시간 포맷
    private static final String TIME_FORMAT_SHORT = "%1$d:%2$02d"; // x:00 형식

    /**
     * 포맷에 맞춰서 재생시간 리턴하기
     *
     * @param timeMillis : 재생시간 (초단위)
     * @return
     */
    public static String getPlayTimeFormat(final int timeMillis) {

        final int totalSeconds = (int) (timeMillis / 1000);
        final int seconds = totalSeconds % 60;
        final int minutes = (totalSeconds / 60) % 60;

        final int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), TIME_FORMAT_LONG, hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), TIME_FORMAT_SHORT, minutes, seconds);
        }
    }

}


