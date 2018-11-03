package shishkin.cleanarchitecture.mvi.common;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import shishkin.cleanarchitecture.mvi.R;


public class RippleTextView extends AppCompatTextView {

    private float radius;
    private Paint paint;
    private Paint rectPaint;
    private Coord coord;

    private class Coord {
        private float x;
        private float y;

        private Coord(float xValue, float yValue) {
            this.x = xValue;
            this.y = yValue;
        }

        @Keep
        private void setX(float value) {
            this.x = value;
        }

        private float getX() {
            return x;
        }

        @Keep
        private void setY(float value) {
            this.y = value;
        }

        private float getY() {
            return y;
        }
    }

    public RippleTextView(final Context context) {
        super(context);

        init(context, null, 0);
    }

    public RippleTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public RippleTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleTextView, defStyleAttr, 0);
        int color = Color.argb(200, 80, 80, 80);
        int colorRect = Color.argb(0, 160, 160, 160);

        try {
            color = a.getColor(R.styleable.RippleTextView_rippleColor, Color.argb(200, 80, 80, 80));
            colorRect = a.getColor(R.styleable.RippleTextView_rippleRectColor, Color.argb(0, 160, 160, 160));
        } finally {
            a.recycle();
        }

        paint = new Paint();
        paint.setColor(color);

        rectPaint = new Paint();
        rectPaint.setColor(colorRect);

        coord = new Coord(0, 0);
    }

    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        if (this.isEnabled()) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                float mCenterX = (getTranslationX() + getWidth()) / 2.0f;
                float mCenterY = (getTranslationY() + getHeight()) / 2.0f;

                // reliable? http://stackoverflow.com/q/1410885
                coord.setX(event.getX());
                coord.setY(event.getY());

                final Interpolator interpolator = new LinearInterpolator();
                long duration = 500;

                final ObjectAnimator animRadius = ObjectAnimator.ofFloat(this, "radius", 10f, getWidth() / 3f);
                animRadius.setInterpolator(interpolator);
                animRadius.setDuration(duration);

                final ObjectAnimator animAlpha = ObjectAnimator.ofInt(paint, "alpha", 200, 0);
                animAlpha.setInterpolator(interpolator);
                animAlpha.setDuration(duration);

                final ObjectAnimator animX = ObjectAnimator.ofFloat(coord, "x", coord.getX(), mCenterX);
                animX.setInterpolator(interpolator);
                animX.setDuration(duration);

                final ObjectAnimator animY = ObjectAnimator.ofFloat(coord, "y", coord.getY(), mCenterY);
                animY.setInterpolator(interpolator);
                animY.setDuration(duration);

                final ObjectAnimator animRectAlpha = ObjectAnimator.ofInt(rectPaint, "alpha", 0, 100, 0);
                animRectAlpha.setInterpolator(interpolator);
                animRectAlpha.setDuration(duration);

                final AnimatorSet animSetAlphaRadius = new AnimatorSet();
                animSetAlphaRadius.playTogether(animRadius, animAlpha, animX, animY, animRectAlpha);
                animSetAlphaRadius.start();

            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Keep
    public void setRadius(final float radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        // before super.onDraw so it goes under the text
        canvas.drawCircle(coord.x, coord.y, radius, paint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), rectPaint);
        super.onDraw(canvas);
    }
}