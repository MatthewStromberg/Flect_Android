package com.goforit.go_for_it_flect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class lv2_won_lost extends AppCompatActivity {

    TextView win_lose_tv;
    Button replay_next;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv2_won_lost);

        // Get the win/lose label
        win_lose_tv = (TextView)findViewById(R.id.win_lose_status);

        // Get buttons
        replay_next = (Button)findViewById(R.id.replay_next_button);
        home = (Button)findViewById(R.id.home_button);

        home.setText("Main Menu");
        //home.setOnClickListener(home_clicked);

        Bundle b = getIntent().getExtras();
        int val = b.getInt("key");

        // 0 means we lost
        if ( val == 0 )
        {
            lv_2_lost();
        }
        else
        {
            lv_2_won();
        }

    }

    public void lv_2_lost()
    {
        win_lose_tv.setText("YOU LOST!");
        replay_next.setText("Replay Level 2");
        replay_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lv2_won_lost.this, LevelTwo.class));
                finish();
            }
        });
    }

    public void lv_2_won()
    {
        win_lose_tv.setText("YOU WON!");
        replay_next.setText("Go To Level 3");
        replay_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lv2_won_lost.this, StartScreen.class));
                finish();
            }
        });
    }

    public void home_clicked(View v)
    {
        startActivity(new Intent(lv2_won_lost.this, StartScreen.class));
        finish();
    }
}
