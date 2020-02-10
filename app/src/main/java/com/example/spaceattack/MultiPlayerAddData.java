package com.example.spaceattack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MultiPlayerAddData extends AppCompatActivity {
    EditText name1;
    Button p1, p2;
    ArrayList<String> dataRoom;
    TextView countdown;
    public static final String EXTRA_PLAYER = "extra_player";
    private String NameP1, NameP2;
    public String nameroom;
    public int count;
    //database
    FirebaseDatabase firedb = FirebaseDatabase.getInstance();
    DatabaseReference roommulti = firedb.getReference("room");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiadddata_activity);
        dataRoom = new ArrayList<>();
        name1 = findViewById(R.id.name1);
        countdown=findViewById(R.id.count);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        dataRoom = getIntent().getStringArrayListExtra(EXTRA_PLAYER);
        nameroom = dataRoom.get(0);
        if(dataRoom.get(1).equals("make")) {
            roommulti.child(nameroom).child("player1").setValue("");
            roommulti.child(nameroom).child("player2").setValue("");
            roommulti.child(nameroom).child("countdown").setValue(10000);
        }
        readFire();
    }
    public void readFire(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NameP1= dataSnapshot.child(nameroom).child("player1").getValue(String.class);
                NameP2= dataSnapshot.child(nameroom).child("player2").getValue(String.class);
                count= dataSnapshot.child(nameroom).child("countdown").getValue(Integer.class);
                if(dataRoom.get(1).equals("join")) {
                    if (!NameP1.equals("")) {
                        p1.setEnabled(false);
                        p1.setBackgroundColor(0000);
                        p1.setText("Player 1 Ready!!");
                        p1.setTextSize(10);
                    }
                    if (!NameP2.equals("")) {
                        p2.setEnabled(false);
                        p2.setBackgroundColor(0000);
                        p2.setText("Player 2 Ready!!");
                        p2.setTextSize(10);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        roommulti.addValueEventListener(postListener);
    }

    public void play1(View v) {
        if (name1.getText().length() != 0) {
            roommulti.child(nameroom).child("player1").setValue(name1.getText().toString());
            dataRoom.set(1,"join");
            p2.setEnabled(false);
            readFire();
            new CountDownTimer(count, 1000) {
                public void onTick(long millisUntilFinished) {
                    readFire();
                    countdown.setText("seconds remaining: " + millisUntilFinished/1000);
                    roommulti.child(nameroom).child("countdown").setValue(millisUntilFinished);
                }

                public void onFinish() {
                    if(!NameP2.equals("")) {
                        ArrayList<String> name = new ArrayList<>();
                        name.add(name1.getText().toString());
                        name.add(NameP2);
                        name.add("Im P1");
                        name.add(nameroom);
                        Intent intent = new Intent(getApplicationContext(), play2.class);
                        intent.putExtra(play2.EXTRA_PLAYER, name);
                        startActivity(intent);
                    }
                }
            }.start();
        } else if (name1.getText().length() == 0) {
            name1.setError("Isi nama terlebih dahulu");
        } else if (name1.getText().length() == 0) {
            name1.setError("Isi nama terlebih dahulu");
        }
    }

    public void play2(View v) {
        if (name1.getText().length() != 0) {
            roommulti.child(nameroom).child("player2").setValue(name1.getText().toString());
            dataRoom.set(1,"join");
            p1.setEnabled(false);
            readFire();
            new CountDownTimer(count, 1000) {

                public void onTick(long millisUntilFinished) {
                    readFire();
                    countdown.setText("seconds remaining: " + millisUntilFinished / 1000);
                    roommulti.child(nameroom).child("countdown").setValue(millisUntilFinished);
                }

                public void onFinish() {
                    if(!NameP1.equals("")) {
                        ArrayList<String> name = new ArrayList<>();
                        name.add(NameP1);
                        name.add(name1.getText().toString());
                        name.add("Im P2");
                        name.add(nameroom);
                        Intent intent = new Intent(getApplicationContext(), play2.class);
                        intent.putExtra(play2.EXTRA_PLAYER, name);
                        startActivity(intent);
                    }
                }
            }.start();
        } else if (name1.getText().length() == 0) {
            name1.setError("Isi nama terlebih dahulu");
        } else if (name1.getText().length() == 0) {
            name1.setError("Isi nama terlebih dahulu");
        }
    }

}
