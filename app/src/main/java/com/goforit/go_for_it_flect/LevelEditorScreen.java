package com.goforit.go_for_it_flect;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LevelEditorScreen extends AppCompatActivity {

    // Timer stuff
    Timer mTmr = null;
    TimerTask mTsk = null;

    // Ball stuff
    android.graphics.PointF mBallPos, mBallSpd;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    int mScrCtrX, mScrCtrY;

    //Speed slow downs for X and Y
    public static final double x_slow = 30.0;
    public static final double y_slow = 30.0;

    // Instruction label to keep updating
    static TextView inst;
    // Layout of relative layout
    RelativeLayout edView;

    // Step we are on
    int step;

    // Lloyd
    public BallView l;
    public int lr = 100;
    //Slingshot
    public SlingshotView s;

    // Walls in this screen
    ArrayList<WallView> walls = new ArrayList<>();

    // Dimensions of screen
    float sh, sw;

    // Number of times we can shoot
    int num_moves = 1;

    // Possible colors
    public final String[] cols = {"RED", "YELLOW", "GREEN"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.activity_level_editor_screen);

        //get screen dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        Point size = new Point();
        display.getSize(size);
        sw = size.x;
        sh = size.y;
        mScrCtrX = size.x/2;
        mScrCtrY = size.y/2;
        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

        mBallSpd.x = 0;
        mBallSpd.y = 0;

        // Start with step '0'
        step = 0;

        // Get the layout
        edView = (RelativeLayout)findViewById(R.id.ed_layout);

        inst = (TextView)findViewById(R.id.inst_label);
        inst.setText("Tap To Place Lloyd");


    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
            ++this.step;

            switch (this.step) {
                case 1:
                    place_ball(e.getX(), e.getY());
                    break;
                case 2:
                    select_slingshot();
                    break;
                //temp
                case 3:
                    //start_movement();
                    break;
            }
        }
            if ( e.getAction() == MotionEvent.ACTION_UP ) {
                //++this.step;

                switch (this.step) {
                    //case 1:
                    //place_ball(e.getX(), e.getY());
                    //break;
                    //case 2:
                    //select_slingshot();
                    //break;
                    //temp
                    case 3:
                        ++this.step;
                        start_movement();
                        break;
                }
            }

        return true;
    }

    public void place_ball(float x, float y)
    {
        l = new BallView(this, x, y, lr);
        edView.addView(l);
        l.invalidate();

        inst.setText("Select Sling Shot");
    }

    public void select_slingshot()
    {
        edView.removeView(l);

        s = new SlingshotView(this, 1, l.mX, l.mY, sw);
        edView.addView(s);

        // Re add lloyd so he is on top
        edView.addView(l);

        s.invalidate();
        l.invalidate();

        inst.setText("Drag and Drop Walls");
    }

    public void start_movement()
    {
        edView.removeView(inst);

        //listener for touch event
        edView.setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(android.view.View v, android.view.MotionEvent e) {

                // android.util.Log.d("action: ", "" + e.toString());

                if ((e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE)
                        && LevelOne.num_moves > 0) {
                    //set ball position based on screen touch
                    mBallPos.x = e.getX();
                    mBallPos.y = e.getY();


                    // Can't go above starting position and cant go below screen
                    if (mBallPos.y < l.origY) {
                        mBallPos.y = l.origY;
                    }
                    if (mBallPos.y > (sh - 20)) {
                        mBallPos.y = (sh - 20);
                    }

                    // Adjust slingshot
                    s.b_curX = mBallPos.x;
                    s.b_curY = mBallPos.y;

                    //mBallSpd.x = (float) ( Math.sqrt(Math.abs(mScrCtrX - mBallPos.x))/4 );
                    //if(mBallPos.x > mScrCtrX) mBallSpd.x *= -1;
                    //mBallSpd.y = (float) ( Math.sqrt(Math.abs(mScrCtrY - mBallPos.y))/4 );
                    //if(mBallPos.y > mScrCtrY) mBallSpd.y *= -1;
                    android.util.Log.d("Is touching?", " Yes!" + mBallPos);
                    //timer event will redraw bal
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    // Make it so we can't move again, right away at least
                    num_moves--;

                    // Calculate speed of ball
                    float starting_x = l.origX;
                    float starting_y = l.origY;

                    float chg_x = Math.abs(mBallPos.x - starting_x);
                    float chg_y = Math.abs(mBallPos.y - starting_y);

                    float speed_x = (float) (chg_x / LevelOne.x_slow);
                    float speed_y = (float) (chg_y / LevelOne.y_slow);

                    mBallSpd.x = speed_x;
                    mBallSpd.y = speed_y;

                    // Adjust for sides of map
                    if (mBallPos.y > mScrCtrY) mBallSpd.y *= -1;
                    if (mBallPos.x > mScrCtrX) mBallSpd.x *= -1;

                    android.util.Log.d("LIFTED UP!", "");
                    android.util.Log.d("Ball speed: ", "" + mBallSpd.x + ", " + mBallSpd.y);
                }
                return true;

            }
        });

        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {

                //if ( !ball_moving )
                //{
                //level_over();
                //this.cancel();
                //}

                //if debugging with external device,
                //  a cat log viewer will be needed on the device
//                android.util.Log.d("TiltBall","Timer Hit - " + mBallPos.x + ":" + mBallPos.y);
                /*
                Have ball speed be distance between 2 points * some math
                NOT DONE
                If user lets go for 2 seconds, then start the impulse
                 */
                //move ball based on current speed
                mBallPos.x += mBallSpd.x;
                mBallPos.y += mBallSpd.y;
                //if ball goes off screen, reposition to opposite side of screen
                //if (mBallPos.x + mBallRadius > mScrWidth) mBallPos.x = mScrWidth - mBallRadius;
                //if (mBallPos.y + mBallRadius > mScrHeight) mBallPos.y = mScrHeight - mBallRadius;
                //if (mBallPos.x - mBallRadius < 0) mBallPos.x = mBallRadius;
                //if (mBallPos.y - mBallRadius < 0) mBallPos.y = mBallRadius;

                // Stop ball from moving any more if we hit the edge of the screen
                if (mBallPos.x + lr > sw)
                {
                    mBallPos.x = sw - lr;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    //level_over();
                }
                if (mBallPos.y + lr > sh)
                {
                    mBallPos.y = sh - lr;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    //level_over();
                }
                if (mBallPos.x - lr < 0)
                {
                    mBallPos.x = lr;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    //level_over();
                }
                if (mBallPos.y - lr < 0)
                {
                    mBallPos.y = lr;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    //level_over();
                }


                for ( int i = 0; i < walls.size(); ++i )
                {
                    RectF this_r = walls.get(i).rect;

                    if ( this_r.contains((int)mBallPos.x, (int)mBallPos.y) )
                    {
                        android.util.Log.d("Hit rect", "" + i);

                        // Remove the wall we hit if its hits are gone
                        walls.get(i).num_hits--;
                        walls.get(i).update_color();
                        if ( walls.get(i).num_hits <= 0 )
                        {
                            walls.get(i).rect = new RectF(0, 0, 0, 0);
                        }
                        //walls.get(i).invalidate();

                        mBallSpd.x *= -1;


                    }
                }


                //update ball class instance
                l.mX = mBallPos.x;
                l.mY = mBallPos.y;


                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        l.invalidate();
                        s.invalidate();

                        for (int i = 0; i < walls.size(); ++i) {
                            walls.get(i).invalidate();
                        }

                        /*if (!ball_moving ) {
                            //game_over = true;
                            level_over();
                            //mTmr = null;

                        }*/

                    }
                });
            }}; // TimerTask

        mTmr.schedule(mTsk, 10, 10); //start timer
    }
}
