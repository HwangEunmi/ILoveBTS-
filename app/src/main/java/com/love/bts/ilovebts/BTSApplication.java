package com.love.bts.ilovebts;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class BTSApplication extends Application {

    // TODO : static으로 빼도 되는지 확인
    /**
     * Firebase 인증 객체
     */
    private static FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Firebase 인증 객체 리턴
     *
     * @return
     */
    public static FirebaseAuth getFirebaseAuthInstance() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }
}
