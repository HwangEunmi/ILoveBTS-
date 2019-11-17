package com.love.bts.ilovebts.view.presenter;

import android.content.Context;

import com.love.bts.ilovebts.adapter.presenter.BaseAdapterContract;
import com.love.bts.ilovebts.view.activity.GroundActivity;

/**
 * Presenter의 공통 인터페이스
 */
public interface BaseContract {
    // View에서 사용되는 메소드
    interface View {
    }

    // Presenter에서 사용되는 메소드
    interface Presenter {
        void attachView(final View view, final GroundActivity activity); // Presenter를 사용할 View를 가져온다.

        void detatchView(); // View를 제거한다.

        void setAdapterView(final BaseAdapterContract.View adapterView); // Adapter에서 사용될 View 인터페이스를 셋팅한다.

        void setAdapterModel(final BaseAdapterContract.Model adapterModel); // Adapter에서 사용될 Model 인터페이스를 셋팅한다.

    }
}
