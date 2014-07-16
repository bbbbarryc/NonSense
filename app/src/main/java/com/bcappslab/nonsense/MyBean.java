package com.bcappslab.nonsense;

/**
 * Created by Barry on 2014-07-13.
 */
public class MyBean {
    private float values[];
    private long timeStamps[];
    private float plotValues[][];

    public MyBean(int size) {
        values = new float[size];
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public long[] getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(long[] timeStamps) {
        this.timeStamps = timeStamps;
    }

    public float[][] getPlotValues() {
        return plotValues;
    }

    public void setPlotValues(float[][] plotValues) {
        this.plotValues = plotValues;
    }
}

