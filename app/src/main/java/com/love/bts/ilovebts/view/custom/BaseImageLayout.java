package com.love.bts.ilovebts.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.love.bts.ilovebts.R;
import com.love.bts.ilovebts.common.constants.BTSConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.internal.TweetMediaUtils;

import java.util.Collections;
import java.util.List;

public class BaseImageLayout extends LinearLayout {

    private static final int IMAGE_MAX_COUNT = 4;

    private static final String SIZED_IMAGE_SMALL = ":small";

    private Context mContext;

    /**
     * Image 배열 (최대 4개)
     */
    private CustomImageView[] mImageArray = new CustomImageView[4];

    /**
     * Response 값
     */
    private Tweet tweet;

    private List<String> mUrlList;

    private List<MediaEntity> mediaEntities = Collections.emptyList();

    /**
     * 이미지 갯수
     */
    private int mImageCount;

    public BaseImageLayout(Context context) {
        this(context, null);
    }

    public BaseImageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseImageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    // 백그라운드 뷰의 위치를 지정 (onMeasure() 이후 호출)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mImageCount > 0) {
            loadLayout();
        }
    }

    // 백그라운드 뷰의 사이즈 결정
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Pair<Integer, Integer> pair;
        if (mImageCount > 0) {
            pair = loadMeasureSize(widthMeasureSpec, heightMeasureSpec);
        } else {
            pair = new Pair<>(0, 0);
        }
        setMeasuredDimension(pair.first, pair.second); // 이미지뷰를 포함할 뷰의 사이즈를 지정해준다.
    }

    /**
     * 이미지뷰에 이미지 셋팅
     */
    private void setImageViewToView() {
        for (int i = 0; i < mImageCount; i++) {
            final ImageView view = getImageView(i);
            view.setVisibility(View.VISIBLE);
            setImageView(mContext, view, getSizedImagePath(mediaEntities.get(i)));
        }
    }

    String getSizedImagePath(MediaEntity mediaEntity) {
        if (mImageCount > 1) {
            return mediaEntity.mediaUrlHttps + SIZED_IMAGE_SMALL;
        }
        return mediaEntity.mediaUrlHttps; // defaults to :medium
    }

    /**
     * 이미지 갯수에 따른 이미지뷰 위치 지정
     */
    private void loadLayout() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int halfWidth = width / 2;
        final int halfHeight = height / 2;
        final int middle = halfWidth;

        switch (mImageCount) {
            case 1:
                setLayout(0, 0, 0, width, height);
                break;
            case 2:
                setLayout(0, 0, 0, halfWidth, height);
                setLayout(1, halfWidth, 0, width, height);
                break;
            case 3:
                setLayout(0, 0, 0, halfWidth, height);
                setLayout(1, halfWidth, 0, width, halfHeight);
                setLayout(2, halfWidth, halfHeight, width, height);
                break;
            case 4:
                setLayout(0, 0, 0, halfWidth, halfHeight);
                setLayout(1, halfWidth, 0, width, halfHeight);
                setLayout(2, 0, halfHeight, halfWidth, height);
                setLayout(3, halfWidth, halfHeight, width, height);
                break;
            default:
                break;
        }
    }

    /**
     * 백그라운드 뷰에 이미지뷰 위치 셋팅
     *
     * @param index : 인덱스
     * @param left : 좌측 위치
     * @param top : 위쪽 위치
     * @param right : 우측 위치
     * @param bottom : 아래쪽 위치
     */
    private void setLayout(final int index, final int left, final int top, final int right, final int bottom) {
        final CustomImageView imageView = mImageArray[index];
        if (imageView.getLeft() == left && imageView.getTop() == top
            && imageView.getRight() == right
            && imageView.getBottom() == bottom) {
            return;
        }
        imageView.layout(left, top, right, bottom);
    }

    /**
     * 이미지 갯수에 따른 이미지뷰 크기 지정
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * @return
     */
    private Pair<Integer, Integer> loadMeasureSize(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int halfWidth = width / 2;
        final int halfHeight = height / 2;
        final int middle = halfWidth;

        switch (mImageCount) {
            case 1:
                setMeasureSize(0, width, height);
                break;
            case 2:
                setMeasureSize(0, halfWidth, height);
                setMeasureSize(1, halfWidth, height);
                break;
            case 3:
                setMeasureSize(0, halfWidth, height);
                setMeasureSize(1, halfWidth, halfHeight);
                setMeasureSize(2, halfWidth, halfHeight);
                break;
            case 4:
                setMeasureSize(0, halfWidth, halfHeight);
                setMeasureSize(1, halfWidth, halfHeight);
                setMeasureSize(2, halfWidth, halfHeight);
                setMeasureSize(3, halfWidth, halfHeight);
                break;
            default:
                break;
        }
        return new Pair<>(width, height);
    }

    /**
     * 백그라운드 뷰에 이미지뷰 크기 셋팅
     *
     * @param index : 인덱스
     * @param width : 넓이
     * @param height : 높이
     */
    private void setMeasureSize(final int index, final int width, final int height) {
        mImageArray[index].measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                                   MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)); // 뷰의 크기를 계산한다.
    }

    /**
     * 이미지뷰에 이미지 랜더링 시작
     *
     * @param tweet : Response 값
     */
    public void renderImage(final Tweet tweet) {
        final List<MediaEntity> mediaEntities = TweetMediaUtils.getPhotoEntities(tweet);
        if (tweet == null || mediaEntities == null
            || mediaEntities.isEmpty()
            || this.mediaEntities.equals(mediaEntities)) {
            return;
        }

        initImageView();
        this.mediaEntities = mediaEntities;
        mImageCount = Math.min(IMAGE_MAX_COUNT, mediaEntities.size());
        setImageViewToView();
        requestLayout();
        // onLayout은 뷰가 불릴때 onMeasure 후 호출, 그래서 약간 호출되는데 시간이 걸림
        // 일반적으로 renderImage -> onLayout 순으로 호출되지만 onLayout이 먼저 호출될 수도 있으므로
        // requestLayout로 onLayout 한번 더 호출 (정확한 mImageCount가 필요하므로)
    }

    /**
     * index에 따른 이미지뷰 리턴
     *
     * @param index : 인덱스
     * @return
     */
    private ImageView getImageView(final int index) {
        CustomImageView imageView = mImageArray[index];
        if (imageView == null) {
            imageView = new CustomImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
            // imageView.setOnClickListener(this);
            mImageArray[index] = imageView;
            addView(imageView, index); // 내부에 requestLayout(), invalidate()포함
        }

        return imageView;
    }

    /**
     * 이미지뷰에 이미지 셋팅
     *
     * @param context : Context 객체
     * @param view : 이미지뷰
     * @param url : 이미지 URL
     */
    private void setImageView(final Context context, final ImageView view, String url) {
        // url = "http://pbs.twimg.com/ext_tw_video_thumb/1134534440322052097/pu/img/H4CY2xZgTjUe1Qmr.jpg";
        final Picasso picasso = Picasso.with(context);
        if (picasso == null) {
            return;
        }
        picasso.load(url).fit().centerCrop().noFade().error(R.mipmap.ic_launcher).into(view);
        // 이런식으로 Callback을 달아서 로딩성공후/실패후의 처리도 할 수 있음
        // .into(view, new Callback() {
        //          @Override
        //          public void onLoginApiSuccess() {
        //            // 이미지를 성공적으로 로딩한 경우
        //          }
        //         @Override
        //         public void onError() {
        //           // 이미지를 로딩하는데 실패한 경우
        //         }
        //      });
    }

    /**
     * 이미지뷰 초기 셋팅
     */
    public void initImageView() {
        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CustomImageView imageView = mImageArray[i];
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }
        mImageCount = 0;
    }

    /**
     * 이미지 URL 리스트 셋팅
     *
     * @param list : 이미지 URL 리스트
     */
    public void setImageUrlList(final List<String> list) {
        this.mUrlList = list;
        mImageCount = tweet.extendedEntities.urls.size();
    }

}
