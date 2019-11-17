package com.love.bts.ilovebts.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 이미지뷰
 */
public class CustomImageView extends AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 이미지뷰 위에 재생버튼 용 이미지를 그린다.
     */
    private static class Overlay {
        final Drawable drawable;

        private Overlay(final Drawable drawable) {
            this.drawable = drawable;
        }

        /**
         * 이미지를 어느 위치에 그릴 것인지 (draw()호출시 그릴 위치 지정)
         *
         * @param width  : 넓이
         * @param height : 높이
         */
        protected void setDrawableBounds(final int width, final int height) {
            if(drawable != null) {
                drawable.setBounds(0, 0, width, height);
            }
        }

        /**
         * 이미지를 그린다.
         */
        protected void setDrawableDraw(final Canvas canvas) {
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }

        /**
         * 이미지의 상태를 셋팅한다. (ex. state_focused ..)
         * @param state
         */
        protected void setDrawableState(final int[] state) {
            if(drawable != null) {
                drawable.setState(state);
            }
        }

    }
}
