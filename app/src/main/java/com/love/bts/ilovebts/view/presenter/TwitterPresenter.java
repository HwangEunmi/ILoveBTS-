package com.love.bts.ilovebts.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.love.bts.ilovebts.BTSApplication;
import com.love.bts.ilovebts.adapter.presenter.BaseAdapterContract;
import com.love.bts.ilovebts.adapter.presenter.TwitterAdapterContract;
import com.love.bts.ilovebts.listener.ITimeLineClickListener;
import com.love.bts.ilovebts.listener.ITwitterCheckLoginStateListener;
import com.love.bts.ilovebts.listener.ITwitterLoginApiListener;
import com.love.bts.ilovebts.view.activity.GroundActivity;
import com.love.bts.ilovebts.view.activity.TwitterImageActivity;
import com.love.bts.ilovebts.view.activity.TwitterVideoActivity;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * TwitterFragment의 Presenter
 */
public class TwitterPresenter implements TwitterContract.Presenter, ITwitterLoginApiListener {

    /**
     * Context 객체
     */
    private GroundActivity mGroundActivity;

    /**
     * Presenter를 사용할 뷰
     */
    private TwitterContract.View mView;

    /**
     * Adapter View 에 접근하기 위한 객체
     */
    private TwitterAdapterContract.View mAdapterView;

    /**
     * Adapter Model 에 접근하기 위한 객체
     */
    private TwitterAdapterContract.Model mAdapterModel;

    /**
     * (Twitter) 자동 로그인 상태 리스너
     */
    private ITwitterCheckLoginStateListener mTwitterCheckLoginStateListener;

    /**
     * (Twitter)Firebase 인증 객체
     */
    private FirebaseAuth mAuth;

    /**
     * Presenter를 사용할 View를 가져오기
     * 
     * @param view : View 객체
     * @param activity : Context 객체
     */
    @Override
    public void attachView(BaseContract.View view, GroundActivity activity) {
        this.mView = (TwitterContract.View)view;
        this.mGroundActivity = activity;
        if (activity != null) {
            mTwitterCheckLoginStateListener = activity;
            activity.setITwitterLoginApiListener(this);
        }

        mAuth = BTSApplication.getFirebaseAuthInstance();
    }

    // TODO : TwitterPresenter에서 GroundActivity에 접근하는게 맞음? 아니면 GroundPresenter에 접근하는게 맞음?

    /**
     * View를 제거하기
     */
    @Override
    public void detatchView() {
        mAuth.signOut();
        mView = null;
    }

    @Override
    public void setAdapterView(final BaseAdapterContract.View adapterView) {
        this.mAdapterView = (TwitterAdapterContract.View)adapterView;
        mAdapterView.setOnItemClickListener(timeLineClickListener);
    }

    @Override
    public void setAdapterModel(final BaseAdapterContract.Model adapterModel) {
        this.mAdapterModel = (TwitterAdapterContract.Model)adapterModel;
    }

    @Override
    public void loadRemoteData(final Context context, final Long minPosition, final Long maxPosition) {

    }

    /**
     * Twitter 로그인 API 호출 결과값 - 성공
     * 
     * @param t_result : 결과값
     */
    @Override
    public void onLoginApiSuccess(final Result<TwitterSession> t_result) {
        handleLoginAuthTwitter(t_result.data);
    }

    /**
     * Twitter 로그인 API 호출 결과값 - 실패
     *
     * @param t_exception : Exception
     */
    @Override
    public void onLoginApiFail(final TwitterException t_exception) {
        mView.onLoginFail(t_exception);
    }

    /**
     * Firebase를 이용하여 Twitter 로그인 인증 진행
     */
    private void handleLoginAuthTwitter(final TwitterSession t_session) {
        final AuthCredential t_credential = TwitterAuthProvider.getCredential(t_session.getAuthToken().token,
                                                                              t_session.getAuthToken().secret);
        mAuth.signInWithCredential(t_credential).addOnCompleteListener(mGroundActivity, t_authListener);
    }

    /**
     * Firebase Twitter Login Listener
     */
    final OnCompleteListener<AuthResult> t_authListener = new OnCompleteListener<AuthResult>() {

        @Override
        public void onComplete(@NonNull Task<AuthResult> t_task) {
            if (t_task.isSuccessful()) { // 인증 성공
                mView.onLoginSuccess();
                mTwitterCheckLoginStateListener.getLoginState(mAuth.getCurrentUser());
            }
        }
    };

    /**
     * 해당 TimeLine을 큰 화면으로 보기
     */
    final ITimeLineClickListener timeLineClickListener = new ITimeLineClickListener() {

        @Override
        public void loadBigScreen(final boolean isImageView) {
            mView.startFullActivity(isImageView);
        }
    };

}
