package com.example.spaceattack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import stanford.androidlib.graphics.*;
import stanford.androidlib.util.RandomGenerator;

public class MultiPlayerGamePlay extends GCanvas {
    public GSprite rocket;
    public GSprite rocket2;
    private GSprite bg;
    private GSprite moonSurface;
    private GSprite asteroid;
    private GSprite asteroid2;
//    private GSprite asteroid3;
    private GLabel txt;
    private GLabel scoreText,scorePoint,name1;
    private GLabel scoreText2,scorePoint2,name2;
    private int score,score2,frames=0;
    private Bitmap crashed;
    private Bitmap crashed2;

    int x=30;
    private ArrayList<GSprite> asteroids=new ArrayList<>();
    private ArrayList<Bitmap> rocketPics = new ArrayList<>();
    private ArrayList<Bitmap> rocketPics2 = new ArrayList<>();
//    private ArrayList<String> dataRoom = new ArrayList<>();
//    public static final String EXTRA_PLAYER = "extra_player";
    public String nameroom;
    public String status;

    //private static final String[] Arah = {"kiri", "kanan"};
//    private ArrayList<String> list;
//    private String keyRoom;

    private String p1Name, p2Name;
    private Integer scorep1db, scorep2db, xRocket1, xRocket2;
    private Float xAster1, xAster2;

    //bagian firebase
    FirebaseDatabase firedb = FirebaseDatabase.getInstance();
    DatabaseReference roommulti = firedb.getReference("room");

    public MultiPlayerGamePlay(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    View MultiView=(View)findViewById(R.id.view);

    public void init(){
//        keyRoom = roommulti.push().getKey();
        nameroom = play2.getRoom();
        status = play2.getStatus();
        Log.d("status",status);

        for(GSprite asteroid:asteroids){
            remove(asteroid);
        }

        Bitmap back = loadScaledBitmap(R.drawable.background,2);
        bg = new GSprite(back);
        add(bg);

        Bitmap moonImage = loadScaledBitmap(R.drawable.earthrise2,1);
        float newWidth=getWidth();
        float newHeight=moonImage.getHeight()/(moonImage.getWidth() /getWidth());
        moonImage=moonImage.createScaledBitmap(moonImage,(int)newWidth,(int)newHeight,true);
        moonSurface = new GSprite(moonImage);
        moonSurface.setBottomY(getHeight());
        add(moonSurface);

        txt=new GLabel("",200,getHeight()/2);
        add(txt);

        scoreText=new GLabel("Score ",getWidth()-250,10);
        scoreText.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        scoreText.setColor(GColor.WHITE);
        add(scoreText);

        scorePoint=new GLabel("0",getWidth()-80,10);
        scorePoint.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        scorePoint.setColor(GColor.WHITE);
        add(scorePoint);

        name1= new GLabel(play2.getName1(),getWidth()-250,70);
        name1.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        name1.setColor(GColor.WHITE);
        add(name1);
        roommulti.child(nameroom).child("player1").setValue(play2.getName1());
        roommulti.child(nameroom).child("scoreP1").setValue(0);


        scoreText2=new GLabel("Score ",10,10);
        scoreText2.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        scoreText2.setColor(GColor.WHITE);
        add(scoreText2);

        scorePoint2=new GLabel("0",180,10);
        scorePoint2.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        scorePoint2.setColor(GColor.WHITE);
        add(scorePoint2);

        name2=new GLabel(play2.getName2(),10,70);
        name2.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        name2.setColor(GColor.WHITE);
        add(name2);
        roommulti.child(nameroom).child("scoreP2").setValue(0);

        rocketPics.add(loadScaledBitmap(R.drawable.lander_plain,1));
        rocketPics.add(loadScaledBitmap(R.drawable.lander_firing,1));
        rocket= new GSprite(rocketPics,getWidth()/4,getHeight()-300);
        rocket.setFramesPerBitmap(2);
        rocket.setCollisionMargin(50);
        add(rocket);

        roommulti.child(nameroom).child("xP1").setValue(rocket.getX());
        roommulti.child(nameroom).child("yP1").setValue(rocket.getY());

        rocketPics2.add(loadScaledBitmap(R.drawable.lander_plainplayer2,1));
        rocketPics2.add(loadScaledBitmap(R.drawable.lander_firingplayer2,1));
        rocket2= new GSprite(rocketPics2,getWidth()/3,getHeight()-300);
        rocket2.setFramesPerBitmap(2);
        rocket2.setCollisionMargin(50);
        add(rocket2);

        roommulti.child(nameroom).child("xP2").setValue(rocket2.getX());
        roommulti.child(nameroom).child("yP2").setValue(rocket2.getY());

        MultiView.setOnTouchListener(handleTouch);
        crashed = BitmapFactory.decodeResource(getResources(), R.drawable.lander_crashed);
        crashed2 = BitmapFactory.decodeResource(getResources(), R.drawable.lander_crashedplayer2);
        animate(20);
    }

    private Bitmap loadScaledBitmap(int id, int factor){
        Bitmap image = BitmapFactory.decodeResource(getResources(),id);
        image= Bitmap.createScaledBitmap(image,image.getWidth()/factor, image.getHeight()/factor,true);
        return image;
    }

    @Override
    public void onAnimateTick(){
        super.onAnimateTick();

        //membaca fb
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                xRocket1 = dataSnapshot.child(nameroom).child("xP1").getValue(Integer.class);
                Log.d("xrocket1",xRocket1.toString());
                xRocket2 = dataSnapshot.child(nameroom).child("xP2").getValue(Integer.class);
                Log.d("xrocket2",xRocket2.toString());
                xAster1= dataSnapshot.child(nameroom).child("aster1X").getValue(Float.class);
//                Log.d("xaster1",xAster1.toString());
                xAster2 = dataSnapshot.child(nameroom).child("aster2X").getValue(Float.class);
//                Log.d("xaster2",xAster2.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        roommulti.addValueEventListener(postListener);


        frames++;
//
        if(status.equals("Im P1")){
            if(frames%70==0){
                asteroid = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
                asteroid.setRightX(getHeight());
                float y= RandomGenerator.getInstance().nextFloat(getWidth()-20);
                asteroid.setX(y);
                roommulti.child(nameroom).child("aster1X").setValue(asteroid.getX());
                asteroid.setVelocityY(x);
                asteroid.setCollisionMargin(10);
                add(asteroid);
                asteroids.add(asteroid);
                x=x+2;


            }else if(frames%110==0){
                asteroid2 = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
                asteroid2.setRightX(getHeight());
                float y2= RandomGenerator.getInstance().nextFloat(getWidth()-20);
                asteroid2.setX(y2);
                roommulti.child(nameroom).child("aster2X").setValue(asteroid2.getX());
                asteroid2.setVelocityY(x);
                asteroid2.setCollisionMargin(10);
                add(asteroid2);
                asteroids.add(asteroid2);
            }
        }

        if (status.equals("Im P2")){
            if(frames%70==0){
                if (xAster1!=null){
                    asteroid = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
                    asteroid.setRightX(getHeight());
                    float y= xAster1;
                    asteroid.setX(y);
                    asteroid.setVelocityY(x);
                    asteroid.setCollisionMargin(10);
                    add(asteroid);
                    asteroids.add(asteroid);
                    x=x+2;
                }
            }else if(frames%110==0){
                if (xAster2 != null){
                    asteroid2 = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
                    asteroid2.setRightX(getHeight());
                    float y2= xAster2;
                    asteroid2.setX(y2);
                    asteroid2.setVelocityY(x);
                    asteroid2.setCollisionMargin(10);
                    add(asteroid2);
                    asteroids.add(asteroid2);
                }
            }
        }


//        membatasi move rocket
        if(rocket.getX()>=getWidth()-200){
            rocket.setVelocityX(-1);
        }else if(rocket.getX()<=50){
            rocket.setVelocityX(1);
        }

        if(rocket2.getX()>=getWidth()-200){
            rocket2.setVelocityX(-1);
        }else if(rocket2.getX()<=50){
            rocket2.setVelocityX(1);
        }

//        rocket.update();
        if(status.equals("Im P1")){
            roommulti.child(nameroom).child("xP1").setValue(rocket.getX());
        } else if(status.equals("Im P2")){
            roommulti.child(nameroom).child("xP2").setValue(rocket2.getX());
        }

        if(asteroid!=null){
            asteroid.update();
            if(asteroid.getY()>=getHeight()){
                if(asteroid.getVelocityY()!=0) {
                    asteroid.setVelocityY(0);
                    score++;
                    scorePoint.setLabel(String.valueOf(score));
                    asteroid.remove();
                }
            }
        }

        if(asteroid2!=null){
            asteroid2.update();
            if(asteroid2.getY()>=getHeight()){
                if(asteroid2.getVelocityY()!=0) {
                    asteroid2.setVelocityY(0);
                    score++;
                    scorePoint.setLabel(String.valueOf(score));
                    asteroid.remove();
                }
            }
        }

        roommulti.child("scoreP1").setValue(score);

        if (status.equals("Im P2")){
            if (xRocket1 != null){
                rocket.setX(xRocket1);
            }
        }

        if (status.equals("Im P1")){
            if (xRocket2 != null){
                rocket2.setX(xRocket2);
            }
        }

        rocket.update();
        rocket2.update();
        doCollisions();
    }

    private void doCollisions(){
        for (GSprite asteroid : asteroids) {
            if (rocket.collidesWith(asteroid)) {
                rocket.stop();
                animationStop();
                rocket.setBitmap(crashed);
                txt.setLabel("YOU CRASHED");
                txt.setFont(Typeface.DEFAULT, Typeface.NORMAL, 100f);
                txt.setColor(GColor.WHITE);
                gameOver();
            }

        }
    }

    private void gameOver(){
        ArrayList<String>dataOver=new ArrayList<>();
        if(score<score2){
            dataOver.add(String.valueOf(play2.getName2()));
            dataOver.add(String.valueOf(score2));
            dataOver.add("Player 2 Winn!!");
        }else if(score>score2){
            dataOver.add(String.valueOf(play2.getName1()));
            dataOver.add(String.valueOf(score));
            dataOver.add("Player 1 Winn!!");
        } else if(score == score2){
            dataOver.add(String.valueOf(play2.getName1()));
            dataOver.add(String.valueOf(score));
            dataOver.add("Draw!!");
        }
        Intent intent=new Intent(getContext(),gameOver.class);
        intent.putStringArrayListExtra(gameOver.EXTRA_DATA,dataOver);
        getContext().startActivity(intent);
    }

    private OnePlayerGamePlay.OnTouchListener handleTouch = new OnSwipeTouchListener() {
        public void onSwipeRight() {
            // swipe layar ke kanan
            if (status.equals("Im P1")){
                rocket.setVelocityX(10f);
            } else if (status.equals("Im P2")){
                rocket2.setVelocityX(10f);
            }
        }
        public void onSwipeLeft() {
            // swipe layar ke kiri
            if (status.equals("Im P1")){
                rocket.setVelocityX(-10f);
            } else if (status.equals("Im P2")){
                rocket2.setVelocityX(-10f);
            }

        }
    };
}