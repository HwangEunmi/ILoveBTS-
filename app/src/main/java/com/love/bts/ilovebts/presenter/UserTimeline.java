package com.love.bts.ilovebts.presenter;

import com.love.bts.ilovebts.listener.OnTimelineLoadListener;
import com.love.bts.ilovebts.data.TimelineResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import retrofit2.Call;

/**
 * Timeline API를 호출하여 사용자의 Timeline을 제공한다.
 */
public class UserTimeline extends BaseTimeline implements OnTimelineLoadListener<Tweet> {

    // 로그인 관련 정보를 제공
    final TwitterCore mTwitterCore;

    // 사용자 ID
    final Long mUserId;

    // 사용자 이름 (ex. @BTS_twt)
    final String mScreenName;

    // 반환할 트윗 수 (일반 트윗 + 리트윗 트윗 + 답장 트윗)
    final Integer mMaxCount;

    /**
     * 답장된 트윗 보여줄건지에 대한 유무
     * (Default : false)
     */
    final Boolean mIncludeReplies;

    /**
     * 리트윗된 트윗 보여줄건지에 대한 유무
     */
    final Boolean mIncludeRetweets;


    public UserTimeline(final TwitterCore twitterCore, final Long userId, final String screenName
            , final Integer maxCount, final Boolean includeReplies, final Boolean includeRetweets) {
        this.mTwitterCore = twitterCore;
        this.mUserId = userId;
        this.mScreenName = screenName;
        this.mMaxCount = maxCount;
        this.mIncludeReplies = includeReplies == null ? false : includeReplies;
        this.mIncludeRetweets = includeRetweets;
    }

    /**
     * minPosition보다 이후 Timeline을 불러온다.
     * 만약 minPosition이 NULL일 경우 최신 Timeline을 불러온다.
     */
    @Override
    public void onLoadNext(final Long minPosition, final Callback<TimelineResult<Tweet>> callback) {
        requestUserTimeline(minPosition, null).enqueue(new TimelineCallback(callback));
    }

    /**
     * maxPosition보다 이전 Timeline을 불러온다.
     */
    @Override
    public void onLoadPrevious(final Long maxPosition, final Callback<TimelineResult<Tweet>> callback) {
        // maxPosition 보정처리
        requestUserTimeline(null, maxPosition == null ? null : maxPosition - 1).enqueue(new TimelineCallback(callback));
    }

    public Call<List<Tweet>> requestUserTimeline(final Long minPosition, final Long maxPosition) {
        return mTwitterCore.getApiClient().getStatusesService().userTimeline(mUserId, mScreenName,
                mMaxCount, minPosition, maxPosition, false, mIncludeReplies, null, mIncludeRetweets);
    }

    /**
     * Builder
     */
    public static class Builder {
        private final TwitterCore twitterCore;
        private Long userId;
        private String screenName;
        private Integer maxCount = 30;
        private Boolean includeReplies;
        private Boolean includeRetweets;

        public Builder() {
            twitterCore = TwitterCore.getInstance();
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setScreenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public Builder setMaxCount(Integer maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setIncludeReplies(Boolean includeReplies) {
            this.includeReplies = includeReplies;
            return this;
        }

        public Builder setIncludeRetweets(Boolean includeRetweets) {
            this.includeRetweets = includeRetweets;
            return this;
        }

        public UserTimeline build() {
            return new UserTimeline(twitterCore, userId, screenName, maxCount, includeReplies, includeRetweets);
        }
    }
}
