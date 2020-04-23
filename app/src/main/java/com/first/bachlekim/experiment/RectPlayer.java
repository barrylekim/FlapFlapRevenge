package com.first.bachlekim.experiment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {
    private Rect rectangle;
    private int color;

    private Animation idle;
    private Animation idle2;
    private Animation flapRight;
    private Animation flapLeft;
    private AnimationManager animManager;

    private int state = 0; //idle bird by default, facing right.

    public Rect getRectangle(){
        return rectangle;
    }

    public RectPlayer (Rect rectangle, int color){
        this. rectangle = rectangle;
        this.color = color;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.redbird_midflap);
        Bitmap flapup = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.redbird_upflap);
        Bitmap flapdown = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.redbird_downflap);


        idle = new Animation(new Bitmap[]{idleImg}, 2);
        flapRight = new Animation(new Bitmap[]{flapup, idleImg, flapdown}, 0.1f);

        //flipping the shapes over the vertical axis, code from stack overflow
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        //reassigning walk1 and walk2 (reuse so that don't have to create another var)
        idleImg = Bitmap.createBitmap(idleImg, 0, 0, idleImg.getWidth(), idleImg.getHeight(), m, false);
        flapup = Bitmap.createBitmap(flapup, 0, 0, flapup.getWidth(), flapup.getHeight(), m, false);
        flapdown = Bitmap.createBitmap(flapdown, 0, 0, flapdown.getWidth(), flapdown.getHeight(), m, false);

        idle2 = new Animation(new Bitmap[]{idleImg}, 2);
        flapLeft = new Animation(new Bitmap[]{flapup, idleImg, flapdown}, 0.1f);


        animManager = new AnimationManager(new Animation[]{idle, idle2, flapRight, flapLeft});
    }

    @Override
    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(color);
//        canvas.drawRect(rectangle, paint);
        animManager.draw(canvas, rectangle);
    }

    @Override
    public void update() { //make this update do nothing
        animManager.update();
    }


    //need to make a new update because this one takes in parameters
    public void update(Point point){    //point is center of the player rectangle
        float oldLeft = rectangle.left; //left x coordinate of player before modification to compare to after modification to see if it moved to right/left

        //l,t,r,b
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);


        if(state == 3){
            state = 1;
        }

        if(state == 2){
            state = 0;
        }

        if(rectangle.left - oldLeft > 5){ //move to the right at least 5 pixels to recognize (too much movement if put 0)
            state = 2; //move right
        } else if(rectangle.left - oldLeft < -5){
            state = 3; //move left
        }

        animManager.playAnim(state);

        animManager.update();


    }
}
