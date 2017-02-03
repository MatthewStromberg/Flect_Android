package com.goforit.go_for_it_flect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by cmont on 4/25/2016.
 */
public class SlingshotView extends View {

    public int number_attempts;

    public float b_origX;
    public float b_origY;
    public float b_curX;
    public float b_curY;

    public float screenEdge;


    private final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SlingshotView(Context context, int tries, float bX, float bY, float edgeX) {

        super(context);

        //number of attempts is how many times lloyd can be reset
        this.number_attempts = tries;

        //the starting position of the ball
        this.b_origX = this.b_curX = bX;
        this.b_origY = this.b_curY = bY;
        this.screenEdge = edgeX;

        if ( this.number_attempts == 1 )
        {
            sPaint.setColor(Color.RED);
            sPaint2.setColor(Color.RED);
        }
        else if ( this.number_attempts == 2 )
        {
            sPaint.setColor(Color.YELLOW);
            sPaint2.setColor(Color.YELLOW);
        }
        else
        {
            sPaint.setColor(Color.GREEN);
            sPaint2.setColor(Color.GREEN);
        }

        sPaint.setStrokeWidth(5);
        sPaint2.setStrokeWidth(5);

    }

    public void updateView(){
        if (this.number_attempts<1){
            android.util.Log.d("Updating the view","The thing");
            sPaint.setStrokeWidth(0);
            sPaint2.setStrokeWidth(0);

        }
    }
    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawCircle(mX, mY, mR, mPaint);
        canvas.drawLine(0,b_origY, b_curX, b_curY, sPaint);
        canvas.drawLine(screenEdge, b_origY, b_curX, b_curY, sPaint2);
    }
}
