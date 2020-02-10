package com.example.spaceattack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class play2 extends AppCompatActivity {
    public static final String EXTRA_PLAYER= "extra_player";
    public static ArrayList<String> name=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_view);

        name=getIntent().getStringArrayListExtra(EXTRA_PLAYER);

    }
    public static String getName1(){
        String name1=name.get(0);
        return name1;
    }
    public static String getName2(){
        String name2=name.get(1);
        return name2;
    }

    public static String getStatus(){
        String status = name.get(2);
        return status;
    }

    public static String getRoom(){
        String room = name.get(3);
        return  room;
    }

}
