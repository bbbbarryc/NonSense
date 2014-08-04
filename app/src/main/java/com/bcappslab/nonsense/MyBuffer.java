package com.bcappslab.nonsense;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.androidplot.util.PlotStatistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2014-08-02.
 */
public class MyBuffer {

    // Need a context because it will interact with the activity
    private Context context;
    private String fileName;

    private int bufferSize;
    private int plotSize;
    private int sensorValueLength;
    private int iterator = 0; // holds the current index of the buffer
    private int plotIterator = 0; // plot iterator

    private long[] timeValues;
    private float[][] sensorValues;

    private long[] timeValuesForPlot;
    private float[][] sensorValuesForPlot;


    public MyBuffer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.bufferSize = 100;
        this.plotSize = 20;
    }

    public MyBuffer(Context context, String fileName, int sensorValueLength, int bufferSize, int plotSize) {
        this.context = context;
        this.fileName = fileName;
        this.sensorValueLength = sensorValueLength;
        this.bufferSize = bufferSize;
        this.plotSize = plotSize;

        this.timeValues = new long[bufferSize];
        this.sensorValues = new float[sensorValueLength][bufferSize];

        this.timeValuesForPlot = new long[plotSize];
        this.sensorValuesForPlot = new float[sensorValueLength][plotSize];
    }


    public void add(long timeStamp, float... values) {

        if (iterator > bufferSize - 1) {
            log(timeValues.clone(), sensorValues.clone());
            iterator = 0;
        }
        if (plotIterator > plotSize - 1) {
            plot(timeValuesForPlot.clone(), sensorValuesForPlot.clone());
            plotIterator = 0;
        }

        timeValues[iterator] = timeStamp;
        for (int i = 0; i < values.length; i++) {
            sensorValues[i][iterator] = values[i];
        }

        timeValuesForPlot[plotIterator] = timeStamp;
        for (int i = 0; i < values.length; i++) {
            sensorValuesForPlot[i][plotIterator] = values[i];
        }

//        Log.d("MyBuffer", "timestamp: " + timeStamp);
//        Log.d("MyBuffer", "sensorValues: " + sensorValues[0][iterator] + ", " + sensorValues[1][iterator] + ", " + sensorValues[2][iterator]);

        iterator++;
        plotIterator++;

    }

    private void log(long[] timeValues, float[][] dataValues) {

        File file;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;

        try {
            file = new File(context.getFilesDir(), fileName);
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            // Data is structured as float[3][1000]
            for (int i = 0; i < timeValues.length; i++) {
                bufferedWriter.write(timeValues[i] + ",");
                for (int j = 0; j < dataValues.length; j++) {
                    bufferedWriter.write(dataValues[j][i] + ",");
                }
                bufferedWriter.write("\n");
            }

            bufferedWriter.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


    public void plot(long[] timeValuesForPlot, float[][] sensorValuesForPlot) {
        this.timeValuesForPlot = timeValuesForPlot;
        this.sensorValuesForPlot = sensorValuesForPlot;
    }

    public long[] getTimeValuesForPlot() {
        return this.timeValuesForPlot.clone();
    }

    public float[][] getSensorValuesForPlot() {
        return this.sensorValuesForPlot.clone();
    }

    public long[] getTimeValues() {
        return this.timeValues.clone();
    }

    public float[][] getSensorValues() {
        return this.sensorValues.clone();
    }

    public int getIterator() {
        return this.iterator;
    }

}
