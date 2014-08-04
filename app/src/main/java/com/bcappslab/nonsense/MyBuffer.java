package com.bcappslab.nonsense;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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
    private int sensorValueLength;
    private int iterator; // holds the current index of the buffer

    private long[] timeValues;
    private float[][] sensorValues;


    public MyBuffer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.bufferSize = 1000;
    }

    public MyBuffer(Context context, String fileName, int bufferSize, int sensorValueLength) {
        this.context = context;
        this.fileName = fileName;
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

//        Log.d("MyBuffer", "timestamp: " + timeStamp);
//        Log.d("MyBuffer", "sensorValues: " + sensorValues[0][iterator] + ", " + sensorValues[1][iterator] + ", " + sensorValues[2][iterator]);
        if (iterator % 10 == 0) {
            plot();
        }
        if (iterator >= bufferSize-1) {
            log(timeValues.clone(), sensorValues.clone());
            iterator = 0;
        }
        iterator += 1;

    }

    private void log(long[] timeValues, float[][] dataValues) {
        if (isExternalStorageWriteable()) {
            File file;
            FileWriter fileWriter;
            BufferedWriter bufferedWriter;

            try {
                file = new File(context.getFilesDir(), fileName);
                fileWriter = new FileWriter(file,true);
                bufferedWriter = new BufferedWriter(fileWriter);

                // Data is structured as float[3][1000]
                for (int i=0; i<timeValues.length; i++){
                    bufferedWriter.write(timeValues[i] + ",");
                    for (int j=0; j<dataValues.length; j++){
                        bufferedWriter.write(dataValues[j][i] + ",");
                    }
                    bufferedWriter.write("\n");
                }


                bufferedWriter.close();

            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            Toast toast = Toast.makeText(context, "Logging to: " + context.getFilesDir().toString(), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, "External storage not available.", Toast.LENGTH_LONG);
            toast.show();
        }



    }

    private void plot() {

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
