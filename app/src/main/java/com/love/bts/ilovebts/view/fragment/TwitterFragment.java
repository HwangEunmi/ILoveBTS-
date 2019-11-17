package com.love.bts.ilovebts.view.fragment;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.adapter.TwitterTimeLineAdapter;
import com.love.bts.ilovebts.databinding.FragmentTwitterBinding;
import com.love.bts.ilovebts.presenter.UserTimeline;
import com.love.bts.ilovebts.view.activity.GroundActivity;
import com.love.bts.ilovebts.view.activity.TwitterImageActivity;
import com.love.bts.ilovebts.view.activity.TwitterVideoActivity;
import com.love.bts.ilovebts.view.presenter.TwitterContract;
import com.love.bts.ilovebts.view.presenter.TwitterPresenter;
import com.twitter.sdk.android.core.TwitterException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

/**
 * 트위터 화면
 */
public class TwitterFragment extends Fragment implements TwitterContract.View, AbsListView.OnScrollListener {

    /**
     * Context 객체
     */
    private GroundActivity mGroundActivity;

    private FragmentTwitterBinding mBinding;

    private TwitterPresenter mPresenter;

    private TwitterTimeLineAdapter mAdapter;

    public static TwitterFragment newInstance() {
        final TwitterFragment fragment = new TwitterFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mGroundActivity = (GroundActivity)context;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mGroundActivity = (GroundActivity)activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_twitter, container, false);
        mBinding.setTwitter(this);
        final View view = mBinding.getRoot();
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new TwitterPresenter();
        mPresenter.attachView(this, mGroundActivity);
        mPresenter.setAdapterView(mAdapter);
    }

    @Override
    public void onDestroy() {
        mPresenter.detatchView();
        super.onDestroy();
    }

    // ListView 스크롤 리스너
    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // 스크롤이 정지상태이거나 스크롤을 터치한 상태인 경우
        switch (scrollState) {
            // 스크롤이 정지되어있는 상태
            case SCROLL_STATE_IDLE:
                // 스크롤을 터치하고 있는 상태
            case SCROLL_STATE_TOUCH_SCROLL:
                break;
            // 스크롤이 움직이고 있는 상태
            case SCROLL_STATE_FLING:
                break;
        }
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    /**
     * View 초기화하기
     */
    public void initView() {
        // TODO : 나중에 RecyclerView로 변경
        // BTS의 타임라인 가져옴 (@id값 입력) BTS_twt
        final UserTimeline timeline = new UserTimeline.Builder().setScreenName("seoul_dessert").build();
        //        final UserTimeline timeline = new UserTimeline.Builder().screenName("BTS_twt").build();
        mAdapter = new TwitterTimeLineAdapter(mGroundActivity, timeline);
        //        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(mGroundActivity).setTimeline(timeline).build();
        mBinding.lvView.setAdapter(mAdapter);
        mBinding.lvView.setOnScrollListener(this);
    }

    /**
     * Twitter Login에 성공한다.
     */
    @Override
    public void onLoginSuccess() {
        Toast.makeText(mGroundActivity, mGroundActivity.getString(R.string.success_login_twitter), Toast.LENGTH_SHORT)
             .show();
    }

    /**
     * Twitter Login에 실패한다.
     * 
     * @param t_exception : Exception 객체
     */
    @Override
    public void onLoginFail(final TwitterException t_exception) {
        Log.d("THEEND", "Fail Twitter Login : " + t_exception.getMessage());
        Toast.makeText(mGroundActivity, mGroundActivity.getString(R.string.fail_login_twitter), Toast.LENGTH_SHORT)
             .show();

        // 앱이 설치되어있는지 확인 -> 미설치시 구글 플레이로 이동
        // CommonUtil.checkInstallTwitter(mGroundActivity, "com.twitter.android");
    }

    /**
     * 해당 화면으로 이동하기
     * 
     * @param isImageView : True -> Full ImageView 화면, False -> Full VideoView 화면
     */
    @Override
    public void startFullActivity(final boolean isImageView) {
        Intent intent = null;
        if (isImageView) { // Full ImageView 
            intent = new Intent(mGroundActivity, TwitterImageActivity.class);
        } else { // Full VideoView
            intent = new Intent(mGroundActivity, TwitterVideoActivity.class);
        }
        startActivity(intent);
    }

}
