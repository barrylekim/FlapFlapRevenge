package com.first.bachlekim.experiment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {
    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private long gameOverTime; //time that will display the game over screen

    private boolean gameOver = false;

    private OrientationData orientationData;
    private long frameTime; //used to calculate time elapsed between frames

    private Background background;



    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,1*Constants.SCREEN_HEIGHT/4); //center of the player
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);

        BitmapFactory bf = new BitmapFactory();
        Bitmap bg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        background = new Background(bg);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

    }


    public void reset(){   // reset all the obstacles and put player to a safe position
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,1*Constants.SCREEN_HEIGHT/4); //center of the player
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;


    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: //when you press your finger down
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY())) {   //if game is not over && pass int the point that we tap; see if that point is within Rectplayer
                    movingPlayer = true;
                }
                if(gameOver && (System.currentTimeMillis() - gameOverTime >= 500)){    //500 = 0.5 seconds
                    reset();
                    gameOver = false; //these two lines will reset the game
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer){ // if game is not over and you tapped inside the Rectplayer, it means that you can move the player
                    playerPoint.set((int)event.getX(), (int)event.getY());
                }
                break;
            case MotionEvent.ACTION_UP: //when you lift your finger up
                movingPlayer = false; //this sets the default
        }
    }


    @Override
    public void draw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);

        final float  scaleFactorX = (float)Constants.SCREEN_WIDTH/(float)background.getWidth();
        final float scaleFactorY = (float)Constants.SCREEN_HEIGHT/(float)background.getHeight();
        final int savedState = canvas.save();
        canvas.scale(scaleFactorX, scaleFactorY);
        background.draw(canvas);
        canvas.restoreToCount(savedState);

        player.draw(canvas);

        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(145);
            paint.setColor(Color.WHITE);
            drawCenterText(canvas,paint, "Game Over");
        }
    }


    @Override
    public void update() {
        if(!gameOver) {  // only update if game is not over
            if(frameTime < Constants.INIT_TIME){
                frameTime = Constants.INIT_TIME;
            }
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1]; //up down direction
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2]; //left right direction

                float xSpeed = 2*roll*Constants.SCREEN_WIDTH/950f;
                float ySpeed = pitch*Constants.SCREEN_HEIGHT/900f; //if player tilt the phone all the way down then player will go across the screen in 0.9 sec

                playerPoint.x += Math.abs(xSpeed*elapsedTime) >= 5 ? xSpeed*elapsedTime : 0; //if xpeed*elapsed time (the number of pixels that moved) is larger than 5 then move the player
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) >= 5 ? ySpeed*elapsedTime : 0;
            }

            //if it goes off the screen
            if(playerPoint.x < 0){
                playerPoint.x = 0;
            }

            if(playerPoint.x > Constants.SCREEN_WIDTH){
                playerPoint.x = Constants.SCREEN_WIDTH;
            }

            if(playerPoint.y < 0){
                playerPoint.y = 0;
            }

            if(playerPoint.x > Constants.SCREEN_HEIGHT){
                playerPoint.x = Constants.SCREEN_HEIGHT;
            }

            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    //this method is taken from stack overflow on how set Text at center of Canvas
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
