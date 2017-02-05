package com.goforit.go_for_it_flect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PlayStyleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_style_screen);
    }

    public void game_start_pressed(View v)
    {
        startActivity(new Intent(PlayStyleScreen.this, LevelHolder.class));
        finish();
    }

    public void begin_lev_editor(View v)
    {
        startActivity(new Intent(PlayStyleScreen.this, LevelEditorScreen.class));
        finish();
    }
}
