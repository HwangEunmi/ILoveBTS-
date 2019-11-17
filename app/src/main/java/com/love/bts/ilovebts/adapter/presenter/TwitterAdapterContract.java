package com.love.bts.ilovebts.adapter.presenter;

import com.love.bts.ilovebts.listener.ITimeLineClickListener;

/**
 * Twitter Adapter의 인터페이스
 */
public interface TwitterAdapterContract {

    // View에서 사용되는 메소드
    interface View extends BaseAdapterContract.View {
        void setOnItemClickListener(final ITimeLineClickListener listener); // 해당 TimeLine을 큰 화면으로 보기
    }

    // Model에서 사용되는 메소드
    interface Model extends BaseAdapterContract.Model {

    }
}
