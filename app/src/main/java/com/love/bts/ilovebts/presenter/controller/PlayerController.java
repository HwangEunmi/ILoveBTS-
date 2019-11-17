package com.love.bts.ilovebts.presenter.controller;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.view.custom.VideoControllerView;


/**
 * (비디오) 전반적인 컨트롤러
 */
public class PlayerController implements VideoControllerView.OnVideoPlayListener, View.OnClickListener {

    /**
     * 비디오뷰
     */
    private VideoView mVideoView;

    /**
     * 비디오 컨트롤러 (하단)
     */
    private VideoControllerView mVideoController;

    /**
     * 버퍼링 상태일 경우 보여지는 프로그래스바
     */
    private ProgressBar mPbBar;

    /**
     * 전체 레이아웃
     */
    private View rootView;

    /**
     * 재생 포지션
     */
    private int seekPosition;

    /**
     * 현재 재생 상태
     */
    private boolean isPlaying = true;

    public PlayerController(final View rootView) {
        this.rootView = rootView;
        mVideoView = this.rootView.findViewById(R.id.vv_view);
        mPbBar = this.rootView.findViewById(R.id.pb_bar);
        mVideoController = this.rootView.findViewById(R.id.cv_controller);

        mVideoView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            // 비디오뷰
            case R.id.vv_view:
                break;
            default:
                break;
        }
    }

    /**
     * 비디오 컨트롤러 관련 Listener
     */
    @Override
    public void start() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }

    @Override
    public int getTotalTime() {
        return mVideoView.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void setSeekPosition(int position) {
        mVideoView.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public int getBufferPercent() {
        return mVideoView.getBufferPercentage();
    }

    /**
     * 비디오뷰 호출을 위해 초기화하기
     */
    public void initController() {
        mVideoController.setVideoPlayListener(this);

        // 비디오뷰 상태별 리스너 셋팅
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(final MediaPlayer mp) {
                // 동영상 준비가 완료되었을때
                mPbBar.setVisibility(View.GONE);

                // 동영상 반복하기
                mp.setLooping(true);
            }
        });

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
                // 버퍼링 상태를 가져오는 리스너
                switch (what) {
                    // 버퍼링 시작
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mPbBar.setVisibility(View.VISIBLE);
                        break;

                    // 버퍼링 끝
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mPbBar.setVisibility(View.GONE);
                        mVideoController.showControllerView();
                        break;
                }

                return false;
            }
        });

        mVideoView.setVideoURI(Uri.parse("http://www.shp.care/shpcontents/P11/P11.mp4"));
        mVideoView.requestFocus();
    }

    /**
     * 비디오뷰 onResume 상태
     */
    public void onResume() {
        // 이전 시청했던 상태가 있는 경우
        if (seekPosition != 0) {
            mVideoView.seekTo(seekPosition);
        }

        // 현재 재생상태였을 경우 재생
        if (isPlaying) {
            mVideoView.start();
        }
    }

    /**
     * 비디오뷰 onPause 상태
     */
    public void onPause() {
        isPlaying = mVideoView.isPlaying();
        seekPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    /**
     * 비디오뷰 onDestroy 상태
     */
    public void onDestroy() {
        mVideoView.stopPlayback();
    }


}


