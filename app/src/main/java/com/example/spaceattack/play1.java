package com.example.spaceattack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class play1 extends AppCompatActivity {
    public static final String EXTRA_PLAYER= "extra_player";
    public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_view);
        name=getIntent().getStringExtra(EXTRA_PLAYER);
    }
    public static String getName(){
        return name;
    }
}
