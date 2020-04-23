package com.first.bachlekim.experiment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationData implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer; //speed
    private Sensor magnometer; //direction

    private float[] accelOutput;  //store data given from the meters
    private float[] magOutput;

    private float[] orientation = new float[3];

    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null; //to see how the players have moved from previous spot

    public float[] getStartOrientation(){
        return startOrientation;
    }

    //reset the reference points for orientation when new game starts
    public void newGame(){
        startOrientation = null;
    }

    public OrientationData(){
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    public void register(){ //register the sensors to this class
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME); //delay how often the game registers the sensor
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);

    }

    public void pause(){
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //in this event, what changed?
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){//if it is a change in accelerometer then
            accelOutput = event.values;//put those values in accel output
        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){//if it is a change in direction then
            magOutput = event.values;//put those values in mag output
        }
        if(accelOutput != null && magOutput != null){ //if we have sth in both output then we want to calculate our orientation
            float[] R = new float[9]; //Rotation matrix - rotate the phone's vector corresponding to the earths coordinate system
            float[] I = new float[9]; //Inclination matrix - not relevant
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput); //put the output data to R and I, boolean because it tells us if it succeeds or not
            if(success){
                SensorManager.getOrientation(R, orientation); //take values from matrix, calculates the orientation and out put to OUR orientation
                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0,orientation.length); //copy orientation into startOrientation

                }
            }

        }
    }

}
