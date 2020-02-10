package com.example.spaceattack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class gameOver extends AppCompatActivity {
    public static final String EXTRA_DATA="a";
    public ArrayList<String>DataAkhir=new ArrayList<>();
    TextView nama;
    TextView score;
    TextView judul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        nama=findViewById(R.id.player);
        score=findViewById(R.id.nilai);
        judul=findViewById(R.id.judul);
        DataAkhir = getIntent().getStringArrayListExtra(EXTRA_DATA);
        nama.setText(DataAkhir.get(0));
        score.setText(DataAkhir.get(1));
        if(DataAkhir.size()==3) {
            judul.setText(DataAkhir.get(2));
        }
    }
    public void BackToMenu(View v){
        Intent intent = new Intent(this, GameView.class);
        startActivity(intent);
    }

}
