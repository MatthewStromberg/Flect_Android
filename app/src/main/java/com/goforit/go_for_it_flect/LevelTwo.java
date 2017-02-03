package com.goforit.go_for_it_flect;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LevelTwo extends AppCompatActivity {


    BallView mBallView = null;
    SlingshotView sView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight, mBallRadius, mScrCtrX, mScrCtrY, numWalls;
    android.graphics.PointF mBallPos, mBallSpd;
    RelativeLayout mainView;

    ArrayList<WallView> walls = new ArrayList<>();

    BallView mBallView2 = null;
    TextView l2_label;

    Boolean ball_moving = true, game_over = false;

    //Speed slow downs for X and Y
    public static final double x_slow = 30.0;
    public static final double y_slow = 30.0;

    // Stops the ball from moving more than once
    public static int num_moves = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Reset how many times we can shoot the ball
        this.num_moves = 1;

        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen2);
        //create pointer to main screen
        mainView = (RelativeLayout) findViewById(R.id.gameScreen2);

        // Get the ids we need
        l2_label = (TextView) findViewById(R.id.lv_two_label);

        // Hide level label after 2 seconds
        l2_label.setVisibility(View.VISIBLE);
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                hide_level_label();
            }
        };
        h.postDelayed(r, 2000);

        //get screen dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        Point size = new Point();
        display.getSize(size);
        mScrWidth = size.x;
        mScrHeight = size.y;
        mScrCtrX = size.x/2;
        mScrCtrY = size.y/2;
        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

        //create variables for ball position and speed
        mBallPos.x = mScrWidth/2;
        mBallPos.y = (mScrHeight/4)*3;
        mBallRadius = 50;
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        //create initial ball
        mBallView = new BallView(this,mBallPos.x,mBallPos.y,mBallRadius);
        sView = new SlingshotView(this, 1, mBallPos.x, mBallPos.y, mScrWidth);

        //listener for touch event
        mainView.setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(android.view.View v, android.view.MotionEvent e) {

                // android.util.Log.d("action: ", "" + e.toString());

                if ( ( e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE )
                        && LevelTwo.num_moves > 0 )
                {
                    //set ball position based on screen touch
                    mBallPos.x = e.getX();
                    mBallPos.y = e.getY();


                    // Can't go above starting position and cant go below screen
                    if (mBallPos.y < mBallView.origY)
                    {
//                        mBallPos.y = mBallView.origY;
                    }
                    if ( mBallPos.y > (mScrHeight-20) )
                    {
                        mBallPos.y = (mScrHeight-20);
                    }

                    // Adjust slingshot
                    sView.b_curX = mBallPos.x;
                    sView.b_curY = mBallPos.y;

                    //mBallSpd.x = (float) ( Math.sqrt(Math.abs(mScrCtrX - mBallPos.x))/4 );
                    //if(mBallPos.x > mScrCtrX) mBallSpd.x *= -1;
                    //mBallSpd.y = (float) ( Math.sqrt(Math.abs(mScrCtrY - mBallPos.y))/4 );
                    //if(mBallPos.y > mScrCtrY) mBallSpd.y *= -1;
                    android.util.Log.d("Is touching?", " Yes!" + mBallPos);
                    //timer event will redraw bal
                }
                else if ( e.getAction() == MotionEvent.ACTION_UP)
                {
                    // Make it so we can't move again, right away at least
                    num_moves--;

                    // Calculate speed of ball
                    float starting_x = mBallView.origX;
                    float starting_y = mBallView.origY;

                    float chg_x = (mBallPos.x - starting_x);
                    float chg_y = (mBallPos.y - starting_y);

                    float speed_x = (float) (chg_x / LevelTwo.x_slow);
                    float speed_y = (float) (chg_y / LevelTwo.y_slow);

                    mBallSpd.x = speed_x;
                    mBallSpd.y = speed_y;

                    // Adjust for sides of map
                    if(mBallPos.y > mScrCtrY) mBallSpd.y *= -1;
                    if(mBallPos.x > mScrCtrX) mBallSpd.x *= -1;

                    android.util.Log.d("LIFTED UP!", "" + mTmr.toString());
                    sView.setVisibility(View.INVISIBLE);
                    android.util.Log.d("Ball speed: ", "" + mBallSpd.x + ", " + mBallSpd.y);
                }
                return true;

            }});

        //while ( true )
        //{
        //if ( !ball_moving )
        //{
        //mTmr.cancel();
        //level_over();
        // }
        //}


    } //OnCreate



    //listener for menu button on phone
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit"); //only one menu item
        return super.onCreateOptionsMenu(menu);
    }

    //listener for menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getTitle() == "Exit") //user clicked Exit
            finish(); //will call onPause
        return super.onOptionsItemSelected(item);
    }

    //For state flow see http://developer.android.com/reference/android/app/Activity.html
    @Override
    public void onPause() //app moved to background, stop background threads
    {
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        super.onPause();
    }

    //@Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {
        //Boolean finished = false;
        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {

/*

                if (bricks[i].getVisibility()){
                 if(RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                bricks[i].setInvisible();
                ball.reverseYVelocity();
                score = score + 10;
                soundPool.play(explodeID, 1, 1, 0, 0, 1);

 */


                mBallPos.x += mBallSpd.x;
                mBallPos.y += mBallSpd.y;
                if (mBallPos.x + mBallRadius > mScrWidth)
                {
                    mBallPos.x = mScrWidth - mBallRadius;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    ball_moving = false;
                }
                if (mBallPos.y + mBallRadius > mScrHeight)
                {
                    mBallPos.y = mScrHeight - mBallRadius;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    ball_moving = false;
                }
                if (mBallPos.x - mBallRadius < 0)
                {
                    mBallPos.x = mBallRadius;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    ball_moving = false;
                }
                if (mBallPos.y - mBallRadius < 0)
                {
                    mBallPos.y = mBallRadius;
                    mBallSpd.x = 0;
                    mBallSpd.y = 0;
                    ball_moving = false;
                }
                for ( int i = 0; i < walls.size(); ++i )
                {
                    RectF this_r = walls.get(i).rect;
//                    RectF ball = new RectF(mBallPos.x,mBallPos.y,mBallRadius,mBallRadius);
                    if(walls.get(i).isPowerup &&(( this_r.contains((int)mBallPos.x+mBallRadius, (int)mBallPos.y) || this_r.contains((int)mBallPos.x-mBallRadius, (int)mBallPos.y)) ||
                            ( this_r.contains((int)mBallPos.x, (int)mBallPos.y+mBallRadius) || this_r.contains((int)mBallPos.x, (int)mBallPos.y-mBallRadius)))){
                            //mBallView2 = new BallView(this,mBallPos.x+20,mBallPos.y+20,mBallRadius/2);

                    }

                    else if ( this_r.contains((int)mBallPos.x+mBallRadius, (int)mBallPos.y) || this_r.contains((int)mBallPos.x-mBallRadius, (int)mBallPos.y))
                    {
                        android.util.Log.d("Hit rect", "" + i);
                        // Remove the wall we hit if its hits are gone
                        walls.get(i).num_hits--;
                        walls.get(i).update_color();
                        if ( walls.get(i).num_hits <= 0 )
                        {
                            walls.get(i).rect = new RectF(0, 0, 0, 0);
                            numWalls--;
  //                          walls.remove(walls.get(i));
                        }
                        mBallSpd.x *= -1;
                        if(numWalls<1) {
                            ball_moving = false;
                            level_over();
                        }
                    }
                    else if ( this_r.contains((int)mBallPos.x, (int)mBallPos.y+mBallRadius) || this_r.contains((int)mBallPos.x, (int)mBallPos.y-mBallRadius))
                    {
                        android.util.Log.d("Hit rect", "" + i);
                        // Remove the wall we hit if its hits are gone
                        walls.get(i).num_hits--;
                        walls.get(i).update_color();
                        if ( walls.get(i).num_hits <= 0 )
                        {
                            walls.get(i).rect = new RectF(0, 0, 0, 0);
                            numWalls--;
//                            walls.remove(walls.get(i));
                        }
                        mBallSpd.y *= -1;
                        if(numWalls<1){
                            ball_moving=false;
                            level_over();
                        }

                    }

                }
                //update ball class instance
                mBallView.mX = mBallPos.x;
                mBallView.mY = mBallPos.y;
                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mBallView.invalidate();
                        sView.invalidate();
                        for (int i = 0; i < walls.size(); ++i) {
                            walls.get(i).invalidate();
                        }
                        if (!ball_moving ) {
                            level_over();
                        }
                    }
                });
            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();
    } // onResume

    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
        android.os.Process.killProcess(android.os.Process.myPid());  //remove app from memory
    }

    //listener for config change.
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    // function to hide over level label
    public void hide_level_label()
    {
        l2_label.setVisibility(View.INVISIBLE);

        // Add slingshot and ball to main screen
        mainView.addView(sView);
        mainView.addView(mBallView);

        // Draw ball and slingshot
        sView.invalidate();
        mBallView.invalidate();

        // Call this levels draw obstacles function
        this.draw_obs();
    }

    public void draw_obs()
    {
        // ****wall params: (1)context, (2)hits, (3)x, (4)y, (5)w, (6)l
        //x, y are bottom left corner coordinates

        // First Wall
//        WallView w1 = new WallView(this, 1, 0, 350, 120, 500);
        WallView w1 = new WallView(this, 1, 0,250,120,500,false);
        walls.add(w1);

        WallView w2 = new WallView(this, 1, 0,mScrHeight-620,120,500,false);
        walls.add(w2);

        WallView w3 = new WallView(this, 1, 120,mScrHeight-120,500,120,false);
        walls.add(w3);

        WallView w4 = new WallView(this, 1, mScrWidth-120, 250, 120, 1000, false);
        walls.add(w4);
/*
        WallView w4 = new WallView(this, 1, mScrCtrX-250,0,500,120,false);
        walls.add(w4);

        // Second Wall
        WallView w5 = new WallView(this, 1, mScrWidth-120, 900, 120, 500,false);
        walls.add(w5);
*/



        // Add all walls to view
        for ( int i = 0; i < walls.size(); ++i )
        {
            mainView.addView(walls.get(i));
        }

        // Redraw all walls
        for ( int i = 0; i < walls.size(); ++i )
        {
            walls.get(i).invalidate();
        }
        numWalls = walls.size();
    }

    // Method to determine level over logic
    public void level_over()
    {

        android.util.Log.d("Check levover", "");

        Boolean all_gone = true;
        for ( int x = 0; x< walls.size(); ++x )
        {
            if ( walls.get(x).num_hits > 0 )
            {
                all_gone = false;
            }
        }


        Intent i = new Intent(LevelTwo.this, lv2_won_lost.class);
        Bundle b = new Bundle();

        if ( all_gone )
        {
            b.putInt("key", 1);
        }
        else
        {
            b.putInt("key", 0);
        }
        i.putExtras(b);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}