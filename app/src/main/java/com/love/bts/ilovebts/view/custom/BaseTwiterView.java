package com.love.bts.ilovebts.view.custom;

import java.util.List;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.databinding.ViewTwiterBaseBinding;
import com.love.bts.ilovebts.listener.ITimeLineClickListener;
import com.twitter.sdk.android.core.models.Tweet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 전체뷰
 */
public class BaseTwiterView extends RelativeLayout implements  View.OnClickListener {

    private ViewTwiterBaseBinding mBinding;

    /**
     * TimeLine Click Listener
     */
    private ITimeLineClickListener mClickListener;

    /**
     * 프로필 사진 뷰
     */
    private ImageView mIvProfile;

    /**
     * 유저명 뷰
     */
    private TextView mTvUserName;

    /**
     * 유저ID 뷰
     */
    private TextView mTvUserId;

    /**
     * 올린날짜 뷰
     */
    private TextView mTvDate;

    /**
     * 트윗내용 뷰
     */
    private TextView mTvContent;

    /**
     * 이미지뷰 레이아웃
     */
    private BaseImageLayout mImageLayout;

    /**
     * 비디오뷰 레이아웃
     */
    private BaseVideoLayout mVideoLayout;

    /**
     * URL 리스트
     */
    private List<String> mList;

    public BaseTwiterView(final @NonNull Context context) {
        this(context, null);
    }

    public BaseTwiterView(final @NonNull Context context, final @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTwiterView(final @NonNull Context context, final @Nullable AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    // TODO : View가 attach 될 때 호출되는지 확인하기
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    // TODO : View가 detatch 될 때 호출되는지 확인하기
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            // ImageView
            case R.id.b_image_layout:
                if (mClickListener != null) {
                    mClickListener.loadBigScreen(true);
                }
                break;
            // VideoView
            // case R.id.b_video_layout:
            //     break;
            default:
                break;
        }
    }

    /**
     * 뷰 초기화
     *
     * @param context : Context 객체
     */
    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_twiter_base, this, true);
        // mBinding = ViewTwiterBaseBinding.inflate(in)
        mIvProfile = findViewById(R.id.iv_profile);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvUserId = findViewById(R.id.tv_user_id);
        mTvDate = findViewById(R.id.tv_date);
        mTvContent = findViewById(R.id.tv_content);
        mImageLayout = findViewById(R.id.b_image_layout);
        // mVideoLayout = findViewById(R.id.b_video_layout);

        mImageLayout.setOnClickListener(this);
    }

    /**
     * 해당 타임라인글 포맷에 따라 뷰에 셋팅 (비디오/이미지)
     *
     * @param tweet : 타임라인 데이터
     */
    public void setTimelineData(final Tweet tweet) {
        if (tweet == null) {
            return;
        }

        // 데이터를 받아 타입을 구분한 후 (이미지 또는 비디오) 타입에 맞게 로딩해야 한다.
        // TODO : 현재 이미지 로딩 테스트를 진행중이므로 비디오 로딩로직은 주석처리를 한다.
        // if (MediaFilterUtil.hasPoto(tweet)) { // 해당 타임라인글이 사진을 포함한 경우
        mImageLayout.renderImage(tweet);
        //  } else if (MediaFilterUtil.hasSupportedVideo(tweet)) { // 해당 타임라인글이 비디오를 포함한 경우
        //    mVideoLayout.renderVideo(tweet);
        //  }
    }

    /**
     * Click Listener 셋팅하기
     *
     * @param listener : Click Listener
     */
    public void setOnItemClickListener(final ITimeLineClickListener listener) {
        this.mClickListener = listener;
    }

}
