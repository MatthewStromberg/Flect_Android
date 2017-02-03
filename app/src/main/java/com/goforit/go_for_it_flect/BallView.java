package com.goforit.go_for_it_flect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.TimerTask;

import static android.graphics.Color.BLUE;

/**
 * Created by Stromz on 4/24/2016.
 */
public class BallView extends View {

    public float origX;
    public float origY;
    public float mX;
    public float mY;
    public final int mR;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //construct new ball object
    public BallView(Context context, float x, float y, int r) {
        super(context);
        mPaint.setColor(Color.RED);
        this.mX = x;
        this.mY = y;
        this.origX = x;
        this.origY = y;
        this.mR = r; //radius

    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, mR, mPaint);
    }
}
