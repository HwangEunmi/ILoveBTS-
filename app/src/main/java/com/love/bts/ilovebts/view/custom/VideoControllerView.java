package com.love.bts.ilovebts.view.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.common.util.VideoTimeUtil;

import java.lang.ref.WeakReference;

/**
 * 비디오 컨트롤러뷰
 */
public class VideoControllerView extends MediaController implements View.OnClickListener {

    /**
     * 재생시간 퍼센트 기준값 (1/1000)
     */
    private static final long PROGRESS_BAR_PERCENT_BASE = 1000L;

    /**
     * 컨트롤러 UI Handler Message 상태값
     */
    private static final int VIDEO_CONTROLLER_UI_UPDATE = 100;

    /**
     * Context 객체
     */
    private Context mContext;

    /**
     * 비디오 컨트롤러 관련 리스너
     */
    private OnVideoPlayListener mListener;

    /**
     * 해당 클래스(Handler에서 사용)
     */
    private VideoControllerView mAct;

    /**
     * 컨트롤러 UI 업데이트하는 Handler
     */
    private final UpdateViewHandler mHandler = new UpdateViewHandler(this);

    /**
     * SeekBar 뷰
     */
    private SeekBar mSbBar;

    /**
     * 재생/일시정지 버튼
     */
    private ImageButton btnPlay;

    /**
     * 현재 재생시간
     */
    private TextView tvCurrentTime;

    /**
     * 총 재생시간
     */
    private TextView tvTotalTime;

    public VideoControllerView(final Context context) {
        this(context, null);
    }

    public VideoControllerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public VideoControllerView(final Context context, final boolean useFastForward) {
        super(context, useFastForward);
    }

    /**
     * 뷰가 다 inflate 되었을때 호출됨
     */
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initControllerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 재생/일시정지버튼
            case R.id.btn_play:
                if (mListener.isPlaying()) {
                    mListener.pause();
                } else {
                    mListener.start();
                }
                showControllerView();
                break;
            default:
                break;
        }
    }

    /**
     * 비디오 컨트롤러뷰를 위해 초기화하기
     */
    private void initControllerView() {
        initView();
        setCurrentTime(0);
        setTotalTime(0);
        setProgressForSeekBar(0, 0, 0);

        mSbBar.setMax((int) PROGRESS_BAR_PERCENT_BASE);
        mSbBar.setOnSeekBarChangeListener(createSeekBarChangeListener());
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_video_controller, this);

        mSbBar = findViewById(R.id.sb_bar);
        btnPlay = findViewById(R.id.btn_play);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        btnPlay.setOnClickListener(this);
    }

    /**
     * 비디오 컨트롤러뷰 UI 갱신
     * <p>
     * (재생시간, SeekBar)
     */
    private void updateVideoUI() {
        final int totalTime = mListener.getTotalTime();
        final int currentTime = mListener.getCurrentPosition();
        final int bufferPercent = mListener.getBufferPercent();

        setTotalTime(totalTime);
        setCurrentTime(currentTime);
        setProgressForSeekBar(currentTime, totalTime, bufferPercent);
    }


    /**
     * 현재 재생시간 셋팅
     *
     * @param timeMillis : 재생시간 (초단위)
     */
    private void setCurrentTime(final int timeMillis) {
        tvCurrentTime.setText(VideoTimeUtil.getPlayTimeFormat(timeMillis));
    }

    /**
     * 총 재생시간 셋팅
     *
     * @param timeMillis : 재생시간 (초단위)
     */
    private void setTotalTime(final int timeMillis) {
        tvTotalTime.setText(VideoTimeUtil.getPlayTimeFormat(timeMillis));
    }

    /**
     * 총 재생시간 대비 현재 재생시간의 Progress 값 셋팅 (SeekBar의)
     *
     * @param currentTimeMillis : 현재 재생시간
     * @param totalTimeMillis   : 총 재생시간
     * @param bufferPercentage  : 버퍼링 퍼센트
     */
    private void setProgressForSeekBar(final int currentTimeMillis, final int totalTimeMillis, final int bufferPercentage) {
        final long percent = totalTimeMillis > 0 ? PROGRESS_BAR_PERCENT_BASE * currentTimeMillis / totalTimeMillis : 0;
        mSbBar.setProgress((int) percent);
        mSbBar.setSecondaryProgress(bufferPercentage * 10);
    }

    /**
     * 비디오 컨트롤러뷰 버튼 상태 갱신
     */
    private void updateVideoStateControl() {
        if (mListener.isPlaying()) {
            setPauseDrawable();
        } else if (mListener.getCurrentPosition() > Math.max(mListener.getTotalTime() - 500, 0)) {
            setReplayDrawable();
        } else {
            setPlayDrawable();
        }
    }


    /**
     * 시작버튼 이미지 셋팅
     */
    private void setPlayDrawable() {
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
        btnPlay.setContentDescription(mContext.getString(R.string.button_description_play));
    }

    /**
     * 일시정지버튼 이미지 셋팅
     */
    private void setPauseDrawable() {
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        btnPlay.setContentDescription(mContext.getString(R.string.button_description_pause));
    }

    /**
     * 리플레이버튼 이미지 셋팅
     */
    private void setReplayDrawable() {
        btnPlay.setImageResource(android.R.drawable.ic_media_rew);
        btnPlay.setContentDescription(mContext.getString(R.string.button_description_replay));
    }


    /**
     * 비디오 컨트롤러 보이기
     * (컨트롤러 UI 갱신 O)
     */
    public void showControllerView() {
        mHandler.sendEmptyMessage(VIDEO_CONTROLLER_UI_UPDATE);
        // TODO : 애니메이션 추가
    }


    /**
     * 비디오 컨트롤러 숨기기
     * (컨트롤러 UI 갱신 X)
     */
    public void hideControllerView() {
        mHandler.removeMessages(VIDEO_CONTROLLER_UI_UPDATE);
        // TODO : 애니메이션 추가
    }


    /**
     * 비디오 컨트롤러가 현재 보이는지에 대한 여부
     */
    private boolean isShowingControllerView() {
        return getVisibility() == View.VISIBLE;
    }


    /**
     * 비디오 컨트롤러 관련 리스너 셋팅
     *
     * @param listener : 비디오 컨트롤러 관련 리스너
     */
    public void setVideoPlayListener(final OnVideoPlayListener listener) {
        this.mListener = listener;
    }

    /**
     * SeekBar뷰 상태변경 리스너
     *
     * @return
     */
    private SeekBar.OnSeekBarChangeListener createSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 프로그래스 상태 변경되었을 경우
                if (!fromUser) {
                    // 사용자가 움직인것이 아니면
                    return;
                }

                final int totalTime = mListener.getTotalTime();
                final long position = (totalTime * progress) / PROGRESS_BAR_PERCENT_BASE;

                mListener.setSeekPosition((int) position);
                setCurrentTime((int) position);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // SeekBar뷰에 막 손댔을때
                mHandler.removeMessages(VIDEO_CONTROLLER_UI_UPDATE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // SeekBar뷰에 막 손뗐을때
                mHandler.sendEmptyMessage(VIDEO_CONTROLLER_UI_UPDATE);

            }
        };
    }

    /**
     * 비디오 컨트롤러 관련 Listener
     */
    public interface OnVideoPlayListener {
        void start(); // 시작

        void pause(); // 일시정지

        int getTotalTime(); // 총 재생시간

        int getCurrentPosition(); // 현재 재생지점

        void setSeekPosition(final int position); // 재생지점 셋팅

        boolean isPlaying(); // 현재 재생중인지 여부

        int getBufferPercent(); // 버퍼링 퍼센트

    }


    /**
     * 컨트롤러 UI 업데이트하는 Handler
     */
    private class UpdateViewHandler extends Handler {

        private final WeakReference<VideoControllerView> ref;

        public UpdateViewHandler(final VideoControllerView act) {
            super();
            ref = new WeakReference<VideoControllerView>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAct = ref.get();

            if (mAct != null) {
                if (VIDEO_CONTROLLER_UI_UPDATE == msg.what) {

                    if (mListener == null) {
                        return;
                    }

                    updateVideoUI();
                    updateVideoStateControl();

                    if (isShowingControllerView() && mListener.isPlaying()) {
                        msg = obtainMessage(VIDEO_CONTROLLER_UI_UPDATE);
                        sendMessageDelayed(msg, 500);
                    }
                }
            }
        }
    }

}
