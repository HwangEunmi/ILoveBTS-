package com.love.bts.ilovebts.view.custom;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

/**
 * 리스트에서 사용될 비디오뷰
 */
public class CustomVideoView extends TextureView implements TextureView.SurfaceTextureListener {

    /**
     * MediaPlayer 준비 Listener
     */
    private OnPreparedListener mOnPreparedListener;

    /**
     * MediaPlayer 버퍼링 정보 불러오는 Listener
     */
    private OnInfoListener mOnInfoListener;

    /**
     * 재생 준비 완료 되었을때 호출되는 Listener
     */
    private OnReadyListener mOnReadyListener;


    /**
     * Context 객체
     */
    private Context mContext;

    /**
     * 미디어를 재생할 수 있는 객체
     */
    private MediaPlayer mMpPlayer;

    private Surface mSurface;

    /**
     * 비디오뷰 높이
     */
    private int mVideoHeight;

    /**
     * 비디오뷰 넓이
     */
    private int mVideoWidth;

    /**
     * 테스트용 URL
     */
    private String mUrl;


    public CustomVideoView(final Context context) {
        this(context, null);
    }

    public CustomVideoView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    // TextureView 상태 Listener
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
        Log.d("THEEND", "onSurfaceTextureAvailable");
        mSurface = new Surface(surface);
        if (mOnReadyListener != null) {
            mOnReadyListener.onVideoReady();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
        Log.d("THEEND", "onSurfaceTextureDestroyed");
        mSurface = null;
        mOnReadyListener = null;
        releaseMediaPlayer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surface) {
    }

    /**
     * 비디오뷰 초기화
     *
     * @param context : Context 객체
     */
    private void initView(final Context context) {
        this.mContext = context;

        mVideoHeight = 0;
        mVideoWidth = 0;
        setFocusable(false);
        setSurfaceTextureListener(this);
    }

    /**
     * MediaPlayer 시작하기
     */
    public void openMediaPlayer() {
        if (mSurface == null) {
            return;
        }

        try {
            mMpPlayer = new MediaPlayer();
            mMpPlayer.setSurface(mSurface);
            mMpPlayer.setScreenOnWhilePlaying(true);

            // 동영상 상태별 리스너 셋팅
            mMpPlayer.setOnPreparedListener(onPreparedListener);
            mMpPlayer.setOnInfoListener(onInfoListener);

            mMpPlayer.setDataSource(mUrl);
            mMpPlayer.prepareAsync();
        } catch (final IllegalStateException | IOException e) {
            Log.d("THEEND", e.getMessage() != null ? e.getMessage() : "");
        }
    }

    /**
     * MediaPlayer 정지하기
     */
    public void closeMediaPlayer() {
        if (mMpPlayer != null) {
            mMpPlayer.stop();
            mMpPlayer.release();
            mMpPlayer = null;
        }
    }

    /**
     * MediaPlayer 초기화하기
     */
    public void releaseMediaPlayer() {
        if (mMpPlayer != null) {
            mMpPlayer.reset();
            mMpPlayer.release();
            mMpPlayer = null;
        }
    }

    /**
     * 동영상 URL 셋팅하기
     */
    public void setVideoURI(final String url) {
        mUrl = url;
    }


    /**
     * 동영상 재생하기
     */
    public void startVideo() {
        if (!mMpPlayer.isPlaying()) {
            mMpPlayer.start();
        }
    }

    /**
     * 동영상 일시정지하기
     */
    public void pauseVideo() {
        if (mMpPlayer.isPlaying()) {
            mMpPlayer.pause();
        }
    }

    /**
     * 동영상 종료하기
     */
    public void stopVideo() {
        mMpPlayer.stop();
        mMpPlayer.release();
        mMpPlayer = null;
    }

    /**
     * 해당 시작위치(초) 로 이동하기
     *
     * @param msec : 초단위
     */
    public void seekTo(final int msec) {
        mMpPlayer.seekTo(msec);
    }


    /**
     * MediaPlayer 준비 Listener 셋팅
     */
    public void setOnPreparedListener(final OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    /**
     * MediaPlayer 준비 Listener
     */
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(final MediaPlayer mp) {
            mOnPreparedListener.onPrepared(mp);
        }
    };

    public interface OnPreparedListener {
        void onPrepared(final MediaPlayer mp);
    }


    /**
     * MediaPlayer 버퍼링 정보 Listener 셋팅
     *
     * @param listener
     */
    public void setOnInfoListener(final OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    /**
     * MediaPlayer 버퍼링 정보 불러오는 Listener
     */
    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
            mOnInfoListener.onInfo(mp, what, extra);
            return false;
        }
    };

    public interface OnInfoListener {
        void onInfo(final MediaPlayer mp, final int what, final int extra);
    }

    /**
     * 재생 준비 완료 되었을때 호출되는 Listener 셋팅
     *
     * @param listener
     */
    public void setmOnReadyListener(final OnReadyListener listener) {
        this.mOnReadyListener = listener;
    }

    /**
     * 재생 준비 완료 되었을때 호출되는 Listener
     */
    public interface OnReadyListener {
        void onVideoReady();
    }

// TextureView로 만든 VideoView가 SurfaceView로 만들어진 VideoView보다
// 리스트에서의 성능이 더 뛰어나다고 함, 반대로 ListView가 아닌 전체화면일 경우 SurfaceView가 더 성능이 뛰어나다고 함 (again)

// https://github.com/danylovolokh/VideoPlayerManager/tree/master/app/src/main/java/com/volokh/danylo/videolist/utils
// https://medium.com/@v.danylo/implementing-video-playback-in-a-scrolled-list-listview-recyclerview-d04bc2148429
// https://ggeutzzang.tistory.com/12
// https://bigstark.tistory.com/13

}
