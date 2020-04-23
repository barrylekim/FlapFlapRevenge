package com.first.bachlekim.experiment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    //higher index = lower on screen = higher Y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap; //width for the player to fit through
    private int obstacleGap; //gap between the obstacles (the layers between obstacles)
    private int obstacleHeight; //height of the obstacle
    private int color;


    private long startTime;
    private long initTime; //initial time and is used to speed up as time goes on

    private int score = 0;


    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();

    }


    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob : obstacles){
            if(ob.playerCollide(player)){
                return true;
            }
        }
        return false;
    }

    //pre creates a set of obstacles to roll ot down the screen. Only use at the beginning, this method is used before the game begins.
    private void populateObstacles(){
        int currY = 10*Constants.SCREEN_HEIGHT/4; //generates the obstacles above the screen and lets it roll down
        while(currY > Constants.SCREEN_HEIGHT){ //haven't gone/appeared into the screen yet
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap)); // generate a number between 0 - 1, so we need to scale it to screen width (subtract player gap so the actual gap doesnt go off screen)
            // xStart is the length of the first rectangle
            // use the line currY as the top of the obstacle rectangle
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY =  currY - (obstacleHeight + obstacleGap);


        }
    }

    public void update(){
        if(startTime < Constants.INIT_TIME){
            startTime = Constants.INIT_TIME; //stops the game and resets the start time when we press home and exits the screen
        }

        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f); //speeds up as time goes on
        for(Obstacle ob : obstacles){
            ob.decrementY(speed * elapsedTime);
        }

        //
        if(obstacles.get(obstacles.size()-1).getRectangle().top <= 0){ //obstacle go off the screen
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap));
            obstacles.remove(obstacles.size() - 1);
            obstacles.add(0, new Obstacle(obstacleHeight,color, xStart, obstacles.get(0).getRectangle().bottom + obstacleGap, playerGap));
            //obstacles.remove(obstacles.size() - 1);


        }


        score++;

    }

    public void draw(Canvas canvas){
        for(Obstacle ob : obstacles){
            ob.draw(canvas);
            ob.update();
        }
        Paint paint = new Paint();
        paint.setTextSize(110);
        paint.setColor(Color.WHITE);
        canvas.drawText("" + score/30, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
