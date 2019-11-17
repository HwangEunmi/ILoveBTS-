package com.love.bts.ilovebts.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.love.bts.ilovebts.R;
import com.twitter.sdk.android.core.models.Tweet;

/**
 * 비디오뷰를 담는 레이아웃
 */
public class BaseVideoLayout extends RelativeLayout implements View.OnClickListener, CustomVideoView.OnReadyListener {

    private Context mContext;

    /**
     * 비디오 뷰
     */
    private CustomVideoView mVideoView;

    /**
     * 총 재생시간
     */
    private TextView mTvTotalTime;


    public BaseVideoLayout(final Context context) {
        this(context, null);
    }

    public BaseVideoLayout(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoLayout(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 부모뷰의 크기에 따라 뷰 크기를 정해준다.
        // 비디오뷰를 감싸는 레이아웃 크기 설정
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

        }
    }

    /**
     * 재생 준비 완료 되었을때 호출되는 Listener
     */
    @Override
    public void onVideoReady() {
        // 비디오 관련 셋팅, 재생하기
        renderVideo(null);
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_list_video_view, this, true);
        mTvTotalTime = findViewById(R.id.tv_total_time);
        mVideoView = findViewById(R.id.vv_view);
        // mThumbnailView = findViewById(R.id.iv_thumbnail);
    }


    /**
     * 비디오뷰에 동영상 랜더링 시작
     */
    public void renderVideo(final Tweet tweet) {
        mVideoView.setmOnReadyListener(this);

        mVideoView.setOnPreparedListener(new CustomVideoView.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                Log.d("THEEND", "동영상 준비 완료");
                // 동영상 준비가 완료되었을때
                // 동영상 반복하기
                mp.setLooping(true);
                mp.start();
            }
        });

        mVideoView.setOnInfoListener(new CustomVideoView.OnInfoListener() {
            @Override
            public void onInfo(final MediaPlayer mp, final int what, final int extra) {
                // 버퍼링 상태를 가져오는 리스너
                switch (what) {
                    // 버퍼링 시작
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        break;
                    // 버퍼링 끝
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        // mVideoView.setBackground(null);
                        break;
                    default:
                        break;
                }
            }
        });

        mVideoView.setVideoURI("http://www.shp.care/shpcontents/S34/S34.mp4");
        mVideoView.openMediaPlayer();

        // 비디오뷰에 동영상 썸네일 이미지 셋팅하기
        Glide.with(mContext)
                .asBitmap()
                .load("http://www.shp.care/shpcontents/S34/S34.mp4")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, final Transition<? super Bitmap> transition) {
                        // TODO : Picasso로 ImageView에 사진처럼 셋팅하고, 준비되면 ImageView Gone, VideoView Visible 시키기
                        // recycleBitmap(mThumbnailView.getDrawable());
                        // mThumbnailView.setImageBitmap(resource);
                    }
                });
    }

    /**
     * 동영상 재생하기
     */
    public void startVideo() {
        mVideoView.startVideo();
    }

    /**
     * 동영상 일시정지하기
     */
    public void pauseVideo() {
        mVideoView.pauseVideo();
    }

    /**
     * 동영상 종료하기
     */
    public void stopVideo() {
        mVideoView.stopVideo();
    }


    /**
     * 이미지뷰에 셋팅되어있는 비트맵이 있으면 recycle()
     *
     * @param drawable : Drawable 객체
     * @return
     */
    private void recycleBitmap(final Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap.recycle();
            bitmap = null;
        }
    }

    // TODO : android getfirstvisibleposition 로 구하기 (현재 item이 뭔지)
    // TODO : https://stackoverflow.com/questions/31937896/play-video-in-recyclerview-play-pause-video-when-scrolled-off-screen 참고

}
