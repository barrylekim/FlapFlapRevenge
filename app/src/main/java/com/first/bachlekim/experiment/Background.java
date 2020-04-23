package com.first.bachlekim.experiment;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap image;
    int x, y;

    public Background(Bitmap res){
        image = res;
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    public int getWidth(){
        return image.getWidth();
    }

    public  int getHeight(){
        return image.getHeight();
    }

}
