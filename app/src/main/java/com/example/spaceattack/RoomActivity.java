package com.example.spaceattack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity {
    EditText room;
    ArrayList<String> dataRoom=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_activity);
        room=findViewById(R.id.roomName);
    }
    public void makeRoom(View v){
        if(room.getText().length()!=0) {
            dataRoom.add(room.getText().toString());
            dataRoom.add("make");
            Intent intent = new Intent(this, MultiPlayerAddData.class);
            intent.putExtra(MultiPlayerAddData.EXTRA_PLAYER, dataRoom);
            startActivity(intent);
        }else{
            room.setError("Isi nama room terlebih dahulu");
        }
    }
    public void joinClick(View v){
        if(room.getText().length()!=0) {
            dataRoom.add(room.getText().toString());
            dataRoom.add("join");
            Intent intent = new Intent(this, MultiPlayerAddData.class);
            intent.putExtra(MultiPlayerAddData.EXTRA_PLAYER, dataRoom);
            startActivity(intent);
        }else{
            room.setError("Isi nama room terlebih dahulu");
        }
    }
}
