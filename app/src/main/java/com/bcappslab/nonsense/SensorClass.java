package com.bcappslab.nonsense;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Barry on 2014-07-12.
 */
public class SensorClass {

    private Context activityContext;

    private String name;
    private int type;
    private int eventValueLength;
    private float[][] eventValues;
    private String[] eventValueLabels;
    private long[] timeStamps;
    private int bufferSize = 10;
    private int bufferIndex = 0;

    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    // Bean to pass information back and forth
    MyBean myBean;

    public SensorClass(String name, int type, Context activityContext) {
        this.name = name;
        this.type = type;
        this.activityContext = activityContext;
        initializeBuffers();
        file = new File(this.activityContext.getFilesDir(), name + " Log.bclog");
    }

    public void plot(long[] timeStamps, float[][] eventValues) {
        myBean.setTimeStamps(timeStamps);
        myBean.setPlotValues(eventValues);
    }

    public void log(long timeStamp, float[] values) {
        Log.d("SensorClass", "bufferIndex: " + bufferIndex);
        if (bufferIndex > bufferSize - 1) {
            logFile();
            plot(timeStamps.clone(), eventValues.clone());
            bufferIndex = 0;
        }

        timeStamps[bufferIndex] = timeStamp;
        eventValues[bufferIndex] = values.clone();

        // Probably not the best spot to do this.... but it will work for now.
        myBean.setValues(values.clone());

        /*
        Log.d("SensorClass", "eventValues[bufferIndex]: " + eventValues[bufferIndex]);
        Log.d("SensorClass", "eventValues[bufferIndex][0]: " + eventValues[bufferIndex][0]);
        Log.d("SensorClass", "eventValues[bufferIndex][1]: " + eventValues[bufferIndex][1]);
        Log.d("SensorClass", "eventValues[bufferIndex][2]: " + eventValues[bufferIndex][2]);
        */
        bufferIndex += 1;
    }

    private void logFile() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true)); // true/false --> append

            // Format: timestamp, val1, val2, val3, [val4, val5, ...]
            for (int x = 0; x < bufferSize; x++) {
                bufferedWriter.write(timeStamps[x] + ",");
                for (int y = 0; y < eventValueLength; y++) {
                    bufferedWriter.write(eventValues[x][y] + ",");
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ioe) {
            Log.e("SensorClass.logFile", "Error logging file");
        }
    }

    private void initializeBuffers() {

        switch (this.type) {
            case Sensor.TYPE_ACCELEROMETER:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                this.eventValueLength = 4;
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_GRAVITY:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_GYROSCOPE:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                this.eventValueLength = 6;
                break;
            case Sensor.TYPE_LIGHT:
                this.eventValueLength = 1; //3;
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                this.eventValueLength = 6;
                break;
            case Sensor.TYPE_ORIENTATION:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_PRESSURE:
                this.eventValueLength = 3;
                break;
            case Sensor.TYPE_PROXIMITY:
                this.eventValueLength = 1;
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                this.eventValueLength = 5;
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_STEP_COUNTER:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                this.eventValueLength = 0;
                break;
            case Sensor.TYPE_TEMPERATURE:
                this.eventValueLength = 0;
                break;
            default:
                this.eventValueLength = 0;
                break;
        }

        eventValues = new float[bufferSize][eventValueLength];
        eventValueLabels = new String[eventValueLength];
        timeStamps = new long[bufferSize];

        // Create a "bean" to pass information back and forth
        myBean = new MyBean(this.eventValueLength);

    }

    public MyBean updateBean() {
        return myBean;
    }


}
