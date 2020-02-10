package com.example.spaceattack;
import android.app.RemoteInput;
import android.content.Intent;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import stanford.androidlib.graphics.*;
import stanford.androidlib.util.RandomGenerator;

public class OnePlayerGamePlay extends GCanvas {
    public GSprite rocket;
    private GSprite moonSurface;
    private GSprite bg;
    private GSprite asteroid;
    private GSprite asteroid2;
    private GSprite asteroid3;
    private GLabel txt,scoreAkhir;
    private GLabel scoreText,scorePoint,name;
    private GSprite window;
    private int score,frames=0;
    private Bitmap crashed ;
    int x=30;
    private ArrayList<GSprite> asteroids=new ArrayList<>();
    private ArrayList<Bitmap> rocketPics = new ArrayList<>();
    public OnePlayerGamePlay(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    View LanderView=(View)findViewById(R.id.view);

    public void init(){
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

        name=new GLabel(play1.getName(),getWidth()-250,70);
        name.setFont(Typeface.DEFAULT, Typeface.NORMAL, 50f);
        name.setColor(GColor.WHITE);
        add(name);

        rocketPics.add(loadScaledBitmap(R.drawable.lander_plain,1));
        rocketPics.add(loadScaledBitmap(R.drawable.lander_firing,1));
        rocket= new GSprite(rocketPics,getWidth()/2,getHeight()-300);
        rocket.setFramesPerBitmap(2);
        rocket.setCollisionMargin(50);
        add(rocket);
        LanderView.setOnTouchListener(handleTouch);
        crashed = BitmapFactory.decodeResource(getResources(), R.drawable.lander_crashed);
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
        frames++;
        if(frames%70==0){
            asteroid = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
            asteroid.setRightX(getHeight());
            float y= RandomGenerator.getInstance().nextFloat(getWidth()-20);
            asteroid.setX(y);
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
            asteroid2.setVelocityY(x);
            asteroid2.setCollisionMargin(10);
            add(asteroid2);
            asteroids.add(asteroid2);
        }else if(frames%140==0){
            asteroid3 = new GSprite(loadScaledBitmap(R.drawable.asteroid,8));
            asteroid3.setRightX(getHeight());
            float y3= RandomGenerator.getInstance().nextFloat(getWidth()-20);
            asteroid3.setX(y3);
            asteroid3.setVelocityY(x);
            asteroid3.setCollisionMargin(10);
            add(asteroid3);
            asteroids.add(asteroid3);
        }
        if(rocket.getX()>=getWidth()-200){
            rocket.setVelocityX(-1);
        }else if(rocket.getX()<=50){
            rocket.setVelocityX(1);
        }
        rocket.update();
        float a = rocket.getX();
        Log.d("rocket pos",Float.toString(a));
        Log.d("tinggi",String.valueOf(getHeight()));

        if(asteroid!=null){
            asteroid.update();
            if(asteroid.getY()>=getHeight()){
                if(asteroid.getVelocityY()!=0) {
                    asteroid.setVelocityY(0);
                    score++;
                    scorePoint.setLabel(String.valueOf(score));
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
                }
            }
        }
        if(asteroid3!=null){
            asteroid3.update();
            if(asteroid3.getY()>=getHeight()){
                if(asteroid3.getVelocityY()!=0) {
                    asteroid3.setVelocityY(0);
                    score++;
                    scorePoint.setLabel(String.valueOf(score));
                }
            }
        }
        doCollisions();
    }

    private void doCollisions(){
        for (GSprite asteroid : asteroids) {
                if (rocket.collidesWith(asteroid)) {
                    rocket.stop();
                    animationStop();
                    rocket.setBitmap(crashed);
//                    txt.setLabel("YOU CRASHED");
//                    txt.setFont(Typeface.DEFAULT, Typeface.NORMAL, 100f);
//                    txt.setColor(GColor.WHITE);
                    gameOver();
                }
        }
    }

    private void gameOver(){
        ArrayList<String>dataOver=new ArrayList<>();
        dataOver.add(String.valueOf(play1.getName()));
        dataOver.add(String.valueOf(score));


        Intent intent=new Intent(getContext(),gameOver.class);
        intent.putStringArrayListExtra(gameOver.EXTRA_DATA,dataOver);
        getActivity().startActivity(intent);
    }

    private OnePlayerGamePlay.OnTouchListener handleTouch = new OnSwipeTouchListener() {
        public void onSwipeRight() {
            // swipe layar ke kanan
            rocket.setVelocityX(10f);
        }
        public void onSwipeLeft() {
            // swipe layar ke kiri
            rocket.setVelocityX(-10f);

        }

    };
}
