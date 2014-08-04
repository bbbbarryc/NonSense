package com.bcappslab.nonsense;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2014-08-02.
 */
public class MyBuffer {

    private int bufferSize;
    private int sensorValueLength;
    private int iterator; // holds the current index of the buffer

    private long[] timeValues;
    private float[][] sensorValues;


    public MyBuffer() {
        this.bufferSize = 1000;
    }

    public MyBuffer(int bufferSize, int sensorValueLength) {
        this.bufferSize = bufferSize;
        this.sensorValueLength = sensorValueLength;

        this.timeValues = new long[bufferSize];
        this.sensorValues = new float[sensorValueLength][bufferSize];
    }


    public void add(long timeStamp, float... values) {


        timeValues[iterator] = timeStamp;
        for (int i = 0; i < values.length; i++) {
            sensorValues[i][iterator] = values[i];
        }

        Log.d("MyBuffer", "timestamp: " + timeStamp);
        Log.d("MyBuffer", "sensorValues: " + sensorValues[0][iterator] + ", " + sensorValues[1][iterator] + ", " + sensorValues[2][iterator]);

        if (iterator % 10 == 0) {
            plot();
        }
        if (iterator > bufferSize) {
            log();
            iterator = 0;
        }
        iterator += 1;
    }

    private void log() {


    }

    private void plot() {

    }
}
