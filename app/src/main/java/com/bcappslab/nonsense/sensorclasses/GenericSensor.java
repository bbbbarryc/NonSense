package com.bcappslab.nonsense.sensorclasses;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.androidplot.xy.XYPlot;
import com.bcappslab.nonsense.MyBean;
import com.bcappslab.nonsense.MyBuffer;
import com.bcappslab.nonsense.SensorClass;

/**
 * Created by Barry on 2014-07-22.
 */
public abstract class GenericSensor extends Activity implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor sensor;
    protected SensorClass mySensor;

    protected String sensorName;
    protected int sensorType;
    protected int sensorValueLength;

    // To update the UI thread
    // Also holds plotting buffer
    protected MyBean myBean;
    protected MyBuffer myBuffer;

    // To get the linear layout and inflater
    protected LinearLayout linearLayout;
    protected LayoutInflater layoutInflater;

    // Plot that we need to add programatically
    protected XYPlot xyPlot;
    protected float[][] plotValues;
    protected long[] timeStamps;

    // the length of the bean is defined by the number of values the sensor throws
    protected int beanLength;
    protected float values[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the sensor manager
        // sensorType is set in the child class
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
