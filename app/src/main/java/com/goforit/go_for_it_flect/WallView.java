package com.goforit.go_for_it_flect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by cmont on 4/25/2016.
 */

public class WallView extends View {

    public float w;
    public float l;

    public int num_hits;

    public float x, y;

    private final Paint wPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RectF rect;

    boolean isPowerup;

    public Canvas canvas;


    public WallView(Context context, int hits, float x, float y, float w, float l, boolean powerup) {
        super(context);

        // Get dimensions
        this.x = x;
        this.y = y;
        this.w = w+x;
        this.l = l+y;
        isPowerup = powerup;

        rect = new RectF((int)this.x, (int)this.y, (int)this.w, (int)this.l);

        //Number of hits, assign color based on this
        this.num_hits = hits;
        if ( this.num_hits == 1 )
        {
            wPaint.setColor(Color.RED);
        }
        else if ( this.num_hits == 2 )
        {
            wPaint.setColor(Color.YELLOW);
        }
        else
        {
            wPaint.setColor(Color.GREEN);
        }
        if(isPowerup){
            wPaint.setColor(Color.BLACK);
        }
    }

    public void update_color()
    {
        if ( this.num_hits <= 1 )
        {
            wPaint.setColor(Color.RED);
        }
        else if ( this.num_hits == 2 )
        {
            wPaint.setColor(Color.YELLOW);
        }
        else
        {
            wPaint.setColor(Color.GREEN);
        }
    }

    public boolean isPowerup(){
        return isPowerup;
    }
    public RectF getRect(){
        return this.rect;
    }
    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // rect is left, top, right, bottom
        //canvas.drawRect(x, y+l, x+w, y, wPaint);
        canvas.drawRect(rect, wPaint);
    }
}
