package com.goforit.go_for_it_flect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void rules_button_pressed(View v)
    {
        startActivity(new Intent(StartScreen.this, RulesScreen.class));
        finish();
    }

    public void game_button_pressed(View v)
    {
        startActivity(new Intent(StartScreen.this, PlayStyleScreen.class));
        finish();
    }
}
