package com.love.bts.ilovebts.adapter;

import com.love.bts.ilovebts.adapter.presenter.TwitterAdapterContract;
import com.love.bts.ilovebts.data.TimelineResult;
import com.love.bts.ilovebts.listener.ITimeLineClickListener;
import com.love.bts.ilovebts.listener.OnTimelineLoadListener;
import com.love.bts.ilovebts.presenter.presenter.TimeLinePresenter;
import com.love.bts.ilovebts.view.custom.BaseTwiterView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TwitterTimeLineAdapter<T> extends BaseAdapter
                                   implements TwitterAdapterContract.View, TwitterAdapterContract.Model {

    private final Context mContext;

    private TimeLinePresenter<T> mPresenter;

    /**
     * 큰 TimeLine 화면으로 이동하는 Listener
     */
    private ITimeLineClickListener mClickListener;

    public TwitterTimeLineAdapter(final Context context, final OnTimelineLoadListener<Timeline> listener) {
        super();
        this.mContext = context;
        this.mPresenter = (TimeLinePresenter<T>)new TimeLinePresenter<Timeline>(listener, this);

        // 데이터 List 초기 호출
        refresh(null);
    }

    /**
     * 첫 TimeLine API를 불러온다.
     */
    public void refresh(final Callback<TimelineResult<T>> listener) {
        mPresenter.refresh(listener);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPresenter.getCount();
    }

    @Override
    public T getItem(final int position) {
        return mPresenter.getItem(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rowView = convertView;
        final Tweet tweet = (Tweet)getItem(position);
        BaseTwiterView layout = null;
        if (rowView == null) {
            layout = new BaseTwiterView(mContext);
            layout.setOnItemClickListener(mClickListener);
            rowView = layout;
        } else {
            layout = (BaseTwiterView)rowView;
        }

        // 뷰에 비디오 또는 사진 렌더링 시작 ...
        layout.setTimelineData(tweet);

        return rowView;
    }

    /**
     * Click Listener 셋팅하기
     * 
     * @param listener : Click Listener
     */
    @Override
    public void setOnItemClickListener(final ITimeLineClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 데이터 갱신하기
     */
    @Override
    public void notifyData() {

    }

}

// 동영상 로드할때
// 1. 뷰 만들때 할 일
// 2. 화면에 attach 되었을때 할 일
// 3. 화면에 노출되었을때 할 일
// 이런 형태로 나눠서 기능을 구현해야 함
// 버퍼링없이라는 것은 인터넷이 빠른 상황이라는 가정이기 때문에
// 오히려 낮은 환경에서 어떻게 대응할지 고민해야 함
