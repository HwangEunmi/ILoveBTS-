package com.love.bts.ilovebts.view.activity;

import com.google.firebase.auth.FirebaseUser;
import com.love.bts.ilovebts.BTSApplication;
import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.adapter.GroundViewPagerAdapter;
import com.love.bts.ilovebts.databinding.ActivityMainBinding;
import com.love.bts.ilovebts.listener.ITwitterCheckLoginStateListener;
import com.love.bts.ilovebts.listener.ITwitterLoginApiListener;
import com.love.bts.ilovebts.view.presenter.GroundContract;
import com.love.bts.ilovebts.view.presenter.GroundPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// 1순위 : MVP
// 2순위 : Bitmap Cache
// 3순위 : DataBinding
public class GroundActivity extends BaseActivity implements ITwitterCheckLoginStateListener, GroundContract.View {

    private ActivityMainBinding mBinding;

    private GroundPresenter mPresenter;

    /**
     * Twitter Login API 결과값 리스너
     */
    private ITwitterLoginApiListener mTwitterLoginApiListener;

    /**
     * Twitter/유투브/다음카페 뷰 Adapter
     */
    private GroundViewPagerAdapter mAdapter;

    /**
     * Twitter 로그인버튼
     */
    private MenuItem mBtnLoginTwitter;

    /**
     * 검색버튼
     */
    private SearchView mBtnSearchView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setMain(this);

        mPresenter = new GroundPresenter();
        mPresenter.attachView(this, this);
        mPresenter.initLoginTwitter();
        initToolbar();

        // ViewPager 셋팅
        mAdapter = new GroundViewPagerAdapter(getSupportFragmentManager());
        mBinding.vpView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Twitter Login API Callback
        mBinding.tBtnTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mBtnLoginTwitter = menu.findItem(R.id.login_twitter);
        initViewMenuOption(menu);
        // 자동 로그인 상태 체크하기
        getLoginState(BTSApplication.getFirebaseAuthInstance().getCurrentUser());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // Twitter Login 버튼
            case R.id.login_twitter:
                mBinding.tBtnTwitterLogin.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * (Twitter) 사용자가 현재 자동 로그인이 되어있는지 체크하기
     * 
     * @param t_IsLoginUser : 로그인 상태
     */
    @Override
    public void getLoginState(final FirebaseUser t_IsLoginUser) {
        if (t_IsLoginUser == null) { // 로그인 X
            mBtnLoginTwitter.setVisible(true);
            mBtnSearchView.setVisibility(View.GONE);
            callTwitterLoginApi();
        } else { // 로그인 O
            mBtnLoginTwitter.setVisible(false);
            mBtnSearchView.setVisibility(View.VISIBLE);
            // TODO : New 트위터 갯수 표시하는 아이콘으로 변경
        }
    }

    /**
     * 검색버튼 셋팅 초기화
     */
    private void initViewMenuOption(final Menu menu) {
        mBtnSearchView = (SearchView)menu.findItem(R.id.btn_search).getActionView();
        mBtnSearchView.setMaxWidth(Integer.MAX_VALUE); // 검색창 길이 MAX
        mBtnSearchView.setQueryHint(mContext.getString(R.string.input_search_word));
        mBtnSearchView.setVisibility(View.GONE);
        mBtnSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                // 검색어 입력후 엔터시 이벤트 제어
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                // 검색어 입력중 이벤트 제어
                return false;
            }
        });
    }

    /**
     * Twitter Login API Listener 셋팅하기
     *
     * @param listener : Twitter Login API 호출 Listener
     */
    @Override
    public void setITwitterLoginApiListener(final ITwitterLoginApiListener listener) {
        this.mTwitterLoginApiListener = listener;
    }

    protected void callTwitterLoginApi() {
        mBinding.tBtnTwitterLogin.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(final Result<TwitterSession> t_result) {
                mTwitterLoginApiListener.onLoginApiSuccess(t_result); // TODO : 굳이 이 메소드를 Presenter용으로 하나 빼서 거기서 처리 안해도 되지?? (번거롭게)
            }

            @Override
            public void failure(final TwitterException t_exception) {
                mTwitterLoginApiListener.onLoginApiFail(t_exception);
            }
        });
    }

}

// TODO : GroundActivity와 TwitterFragment 끼리 통신할때 MVP에서도 Interface로 통신하는게 맞음? (이것처럼 GroundActivity에서 TwitterPresenter로 Interface 이용해서 직접 호출)
