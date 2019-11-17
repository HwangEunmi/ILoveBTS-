package com.love.bts.ilovebts.adapter.presenter;

import android.widget.AdapterView;

/**
 * Adapter Presenter의 공통 인터페이스
 */
public interface BaseAdapterContract {
    // View에서 사용되는 메소드
    interface View {
        void notifyData(); // 데이터를 갱신한다.
    }

    // Model에서 사용되는 메소드
    interface Model {
    }
}
