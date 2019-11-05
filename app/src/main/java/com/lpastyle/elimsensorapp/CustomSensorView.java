package com.lpastyle.elimsensorapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view example
 */
public class CustomSensorView extends View {
    private String mCaption;
    private int mColor = Color.BLUE; // default color
    private float mRadius = 0;

    private Paint paint;
    private float angle = 0;


    public CustomSensorView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomSensorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSensorView, defStyle, 0);
        mCaption = a.getString(R.styleable.CustomSensorView_caption);
        mColor = a.getColor(R.styleable.CustomSensorView_color, mColor);
        mRadius = a.getDimension(R.styleable.CustomSensorView_radius, mRadius);
        a.recycle();

        // set up a default Paint object
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get circle center coordinates
        int xPoint = getMeasuredWidth() / 2;
        int yPoint = getMeasuredHeight() / 2;

        // If radius given via xml attributes is null, cirle will fit view dimensions
        float radius = mRadius == 0 ? (float) (Math.max(xPoint, yPoint) * 0.6) : mRadius;
        canvas.drawCircle(xPoint, yPoint, radius, paint);

        // draw the circle
        canvas.drawLine(
                xPoint,
                yPoint,
                (float) (xPoint + radius * Math.sin((double) (angle) / 180 * Math.PI)),
                (float) (yPoint - radius * Math.cos((double) (angle) / 180 * Math.PI)), paint);

        // draw current relative value
        canvas.drawText(mCaption + (int) (100 * angle / 360) + '%', 0, yPoint / 4, paint);


    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }
}
