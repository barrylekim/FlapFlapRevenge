package com.first.bachlekim.experiment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private int color;
    private Rect rectangle2;

    private Animation left;
    private Animation right;
    private AnimationManager animManager1;
    private AnimationManager animManager2;


    public Rect getRectangle(){
        return rectangle;
    }

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public void decrementY(float y){
        rectangle.top -= y;
        rectangle.bottom -= y;
        rectangle2.top -= y;
        rectangle2.bottom -= y;
    }

    //startX = the length of left rectangle (vertical line)
    //startY = the horizontal start line of the top of rectangle
    public  Obstacle(int rectHeight, int color, int startX, int startY, int playerGap){

        this.color = color;

        rectangle = new Rect(0, startY, startX, startY + rectHeight); //lines of left, top, right, bottom
        rectangle2 = new Rect(startX+playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);

        BitmapFactory bf = new BitmapFactory();
        Bitmap lt = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.oleft);
        Bitmap rt = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.oright);

        left = new Animation(new Bitmap[]{lt}, 2);
        right = new Animation(new Bitmap[]{rt}, 2);

        animManager1 = new AnimationManager(new Animation[]{left});
        animManager2 = new AnimationManager(new Animation[]{right});

    }

    public boolean playerCollide(RectPlayer player){
//        if(rectangle.contains(player.getRectangle().left, player.getRectangle().top)
//                || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
//                || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
//                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom)){
//            return true;
//        }
//        return false;

        // Can also do this

        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangle2,paint);

//        animManager1.draw(canvas, rectangle);
//        animManager2.draw(canvas, rectangle2);

    }

    @Override
    public void update() {
//        animManager1.playAnim(0);
//        animManager2.playAnim(0);
//        animManager1.update();
//        animManager2.update();

    }

}
