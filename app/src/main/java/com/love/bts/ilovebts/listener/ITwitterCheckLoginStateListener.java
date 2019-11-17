package com.love.bts.ilovebts.listener;

import com.google.firebase.auth.FirebaseUser;

// 트위터 자동 로그인 상태 리스너
public interface ITwitterCheckLoginStateListener {
    void getLoginState(final FirebaseUser isLoginUser);
}
