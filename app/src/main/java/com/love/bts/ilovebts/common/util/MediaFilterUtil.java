package com.love.bts.ilovebts.common.util;

import android.os.Build;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 해당 타임라인글이 어떤 형식인지 (이미지/비디오) 구분하는 유틸 클래스
 */
public class MediaFilterUtil {

    // 사진일 경우
    private static final String PHOTO_TYPE = "photo";

    // 비디오인 경우
    private static final String VIDEO_TYPE = "video";

    /**
     * 비디오 형식
     */
    private static final String CONTENT_TYPE_MP4 = "video/mp4";
    private static final String CONTENT_TYPE_HLS = "application/x-mpegURL";


    /**
     * 해당 타임라인글이 사진을 포함하는지 확인
     *
     * @param tweet : 타임라인 데이터
     * @return
     */
    public static boolean hasPoto(final Tweet tweet) {
        return getMediaData(tweet, PHOTO_TYPE) != null;
    }

    /**
     * 해당 타임라인글이 비디오를 포함하는지 확인
     *
     * @param tweet : 타임라인 데이터
     * @return
     */
    public static boolean hasSupportedVideo(final Tweet tweet) {
        final MediaEntity data = getMediaData(tweet, VIDEO_TYPE);
        return data != null && getSupportedVariant(data) != null;
    }

    /**
     * 비디오/사진 타임라인 데이터를 리턴
     *
     * @param tweet : 타임라인 데이터
     * @param type: 미디어 타입
     * @return
     */
    private static MediaEntity getMediaData(final Tweet tweet, final String type) {
        final List<MediaEntity> list = getMediaList(tweet);
        for (MediaEntity data : list) {
            if (data.type != null && type.equals(data.type)) { // 해당 타임라인글이 사진인 경우
                return data;
            }
        }
        return null;
    }

    /**
     * 비디오/사진 타임라인 데이터 리스트를 리턴
     *
     * @param tweet : 타임라인 데이터
     * @return
     */
    private static List<MediaEntity> getMediaList(final Tweet tweet) {
        final List<MediaEntity> list = new ArrayList<>();
        // 데이터가 있는 경우
        if (tweet.entities != null && tweet.entities.media != null) {
            list.addAll(tweet.entities.media);
        }
        if (tweet.extendedEntities != null && tweet.extendedEntities.media != null) {
            list.addAll(tweet.extendedEntities.media);
        }

        return list;
    }


    /**
     * 비디오 형식에 맞는지 체크하기
     *
     * @param mediaEntity : 데이터
     * @return
     */
    public static VideoInfo.Variant getSupportedVariant(MediaEntity mediaEntity) {
        for (VideoInfo.Variant variant : mediaEntity.videoInfo.variants) {
            if (isVariantSupported(variant)) {
                return variant;
            }
        }
        return null;
    }

    /**
     * 비디오 형식에 맞는지 체크하기
     *
     * @param variant
     * @return
     */
    private static boolean isVariantSupported(VideoInfo.Variant variant) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                CONTENT_TYPE_HLS.equals(variant.contentType)) {
            return true;
        } else if (CONTENT_TYPE_MP4.equals(variant.contentType)) {
            return true;
        }
        return false;
    }
}
