package com.first.bachlekim.experiment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;

    private boolean isPlaying = false;   //is the animation playing
    public boolean isPlaying(){
        return isPlaying;
    }

    public void play(){
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop(){
        isPlaying = false;

    }

    private float frameTime; //time spent on each frame in the animation

    private long lastFrame;


    public Animation(Bitmap[] frames, float animTime){
        this.frames = frames;
        frameIndex = 0;

        frameTime = animTime/frames.length;  //this makes each frame in the animation have equal amount of time

        lastFrame = System.currentTimeMillis();
    }

    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying){
            return;
        }

        scaleRect(destination);

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }

    private void scaleRect(Rect rect){
        float whRatio = (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();

        if(rect.width()>rect.height()){
            rect.left = rect.right - (int)(rect.height()*whRatio);
        } else {
            rect.top = rect.bottom - (int)(rect.width()*(1/whRatio));
        }
    }

    public void update(){
        if(!isPlaying) {//we're not playing
            return;
        }

        if(System.currentTimeMillis() - lastFrame > frameTime*1000){  //the amount of time has passed for us to change the frame - if its time to move on to next frame
            frameIndex++; //go to the next frame
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            // same as
//            if(frameIndex >= frames.length){
//                frameIndex = 0;
//            } else {
//                frameIndex = frameIndex;
//            }

            lastFrame = System.currentTimeMillis();
        }
    }
}
