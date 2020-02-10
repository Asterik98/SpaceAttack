package com.example.spaceattack;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class GameView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void playClick(View v){
        Intent intent = new Intent(this, OnePlayerAddData.class);
        startActivity(intent);
    }
    public void play2Click(View v){
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }
    public void stopClick(View v){
    }
}
