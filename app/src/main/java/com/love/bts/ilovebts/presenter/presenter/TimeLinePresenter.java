package com.love.bts.ilovebts.presenter.presenter;

import com.love.bts.ilovebts.adapter.TwitterTimeLineAdapter;
import com.love.bts.ilovebts.listener.OnTimelineLoadListener;
import com.love.bts.ilovebts.data.TimelineResult;
import com.love.bts.ilovebts.presenter.holder.TimeLineScrollHolder;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeLine API로부터 받은 Response를 다루는 Presenter
 */
public class TimeLinePresenter<T> {

    /**
     * 최대용량인 200을 넘으면 더이상 데이터를 로드할 수 없다.
     */
    private static final int CAPACITY = 200;

    /**
     * TimeLine 어댑터
     */
    private TwitterTimeLineAdapter mAdapter;

    /**
     * 이전 또는 이후 데이터를 불러오는 리스너
     */
    private OnTimelineLoadListener<T> mListener;

    /**
     * TimeLineCursor의 현재 스크롤 위치를 다룬다.
     */
    private TimeLineScrollHolder mHolder;

    /**
     * Response 리스트
     */
    private List<T> mList;

    public TimeLinePresenter(final OnTimelineLoadListener<T> listener, final TwitterTimeLineAdapter adapter) {
        this(listener, null, adapter);
    }

    TimeLinePresenter(final OnTimelineLoadListener<T> listener, List<T> list, final TwitterTimeLineAdapter adapter) {
        if (listener == null) {
            throw new IllegalArgumentException("TimeLine must not be null!!");
        }
        this.mListener = listener;
        this.mHolder = new TimeLineScrollHolder();
        this.mAdapter = adapter;
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
    }

    /**
     * 첫 TimeLine API를 불러온다.
     */
    public void refresh(final Callback<TimelineResult<T>> listener) {
        mHolder.initCursor();
        loadNext(mHolder.getNextPosition(), new RefreshCallback(listener, mHolder));
    }

    /**
     * 다음 TimeLine을 불러온다.
     */
    public void showNext(final Callback<TimelineResult<T>> listener) {
        loadNext(mHolder.getNextPosition(), new NextCallback(listener, mHolder));
    }

    /**
     * 현재 position이 마지막 아이템인 경우, 이전 TimeLine을 불러온다.
     */
    public void showPrevious(final Callback<TimelineResult<T>> listener) {
        loadPrevious(mHolder.getBeforePosition(), new PreviousCallback(listener, mHolder));
    }

    /**
     * 다음 TimeLine의 Response를 가져온다.
     *
     * @param minPosition : 이전 API의 최대 position (현재 API의 최소 position)
     * @param listener    : 통신 콜백 리스너
     */
    private void loadNext(final Long minPosition, final Callback<TimelineResult<T>> listener) {
        if (mList.size() < CAPACITY) {
            if (mHolder.isEnableCallApi()) {
                mListener.onLoadNext(minPosition, listener);
            } else {
                listener.failure(new TwitterException("이전 API 콜이 끝나지 않았습니다."));
            }
        } else { // 요청할 수 있는 데이터를 초과한 경우
            listener.failure(new TwitterException("요청할 수 있는 한도에 도달했습니다."));
        }
    }

    /**
     * 이전 TimeLine의 Response를 가져온다.
     *
     * @param maxPosition : 이전 API의 최대 position (현재 API의 최대 position)
     * @param listener    : 통신 콜백 리스너
     */
    private void loadPrevious(final Long maxPosition, final Callback<TimelineResult<T>> listener) {
        if (mList.size() < CAPACITY) {
            if (mHolder.isEnableCallApi()) {
                mListener.onLoadPrevious(maxPosition, listener);
            } else {
                listener.failure(new TwitterException("이전 API 콜이 끝나지 않았습니다."));
            }
        } else { // 요청할 수 있는 데이터를 초과한 경우
            listener.failure(new TwitterException("요청할 수 있는 한도에 도달했습니다."));
        }
    }

    /**
     * 현재 아이템의 갯수를 리턴한다.
     */
    public int getCount() {
        return mList.size();
    }

    /**
     * 현재 Position이 마지막 아이템일 경우 이전 TimeLine을 모두 불러오기 (그래서 계속 안끊기고 자동으로 call)
     * 아닌 경우, 해당 아이템 불러오기
     *
     * @param position : 위치
     * @return
     */
    public T getItem(final int position) {
        if (isLastItem(position)) {
            showPrevious(null);
        }
        return mList.get(position);
    }

    /**
     * 현재 Position이 마지막 아이템인지 체크
     *
     * @return
     */
    public boolean isLastItem(final int position) {
        return position == mList.size() - 1;
    }

    /**
     * 초기화 (Refresh) 시키고 다시 API를 호출하는 콜백 클래스
     */
    private class RefreshCallback extends NextCallback {

        private RefreshCallback(final Callback<TimelineResult<T>> listener,
                                final TimeLineScrollHolder holder) {
            super(listener, holder);
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            if (result.data.mList.size() > 0) {
                mList.clear();
            }
            super.success(result);
        }
    }

    /**
     * 이후 TimeLine의 Response를 받는 콜백 클래스
     */
    private class NextCallback extends BaseCallback {

        private NextCallback(final Callback<TimelineResult<T>> listener, final TimeLineScrollHolder holder) {
            super(listener, holder);
        }

        @Override
        public void success(final Result<TimelineResult<T>> result) {
            mHolder.setFinishApiCall();
            if (result.data.mList.size() > 0) {
                // 해당 Response이 제일 위로 올라와야 하니까
                final List<T> list = new ArrayList<>(result.data.mList);
                list.addAll(mList);
                mList = list;
                mAdapter.notifyDataSetChanged();
                mHolder.setNextCursor(result.data.mCursor);
            }
            super.success(result);
        }

        @Override
        public void failure(TwitterException exception) {
            mHolder.setFinishApiCall();
        }
    }

    /**
     * 이전 TimeLine의 Response를 받는 콜백 클래스
     */
    private class PreviousCallback extends BaseCallback {

        private PreviousCallback(final Callback<TimelineResult<T>> listener, final TimeLineScrollHolder holder) {
            super(listener, holder);
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            mHolder.setFinishApiCall();
            if (result.data.mList.size() > 0) {
                mList.addAll(result.data.mList);
                mAdapter.notifyDataSetChanged();
                mHolder.setBeforeCursor(result.data.mCursor);
            }
            super.success(result);
        }

        @Override
        public void failure(TwitterException exception) {
            mHolder.setFinishApiCall();
        }
    }

    /**
     * TimeLine의 Response를 받는 기본 콜백 클래스
     */
    private class BaseCallback extends Callback<TimelineResult<T>> {

        private Callback<TimelineResult<T>> mListener;

        private TimeLineScrollHolder mHolder;

        public BaseCallback(final Callback<TimelineResult<T>> listener, final TimeLineScrollHolder holder) {
            this.mListener = listener;
            this.mHolder = holder;
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            mHolder.setFinishApiCall();
            if (mListener != null) {
                mListener.success(result);
            }
        }

        @Override
        public void failure(TwitterException exception) {
            mHolder.setFinishApiCall();
            if (mListener != null) {
                mListener.failure(exception);
            }
        }
    }

}
