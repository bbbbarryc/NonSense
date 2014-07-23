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
import android.widget.TextView;

import com.androidplot.xy.XYPlot;
import com.bcappslab.nonsense.R;

import org.w3c.dom.Text;

/**
 * Created by Barry on 2014-07-22.
 */
public class GravitySensor extends GenericSensor {
    /* what you want to do is have a plotting class or method
    that takes in all of the required information in the first line
    */

    private int bufferSize = 10;
    private int eventValueSize = 3; // 3 is the number of event values this sensor has
    private float[][] eventValues = new float[bufferSize][eventValueSize];

    private TextView magValView;
    private TextView xValView;
    private TextView yValView;
    private TextView zValView;

    private float magVal;
    private float xVal;
    private float yVal;
    private float zVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_sensor);

        // Inherited from GenericSensor
        sensorName = getString(R.string.sensor_gravity);
        sensorType = Sensor.TYPE_GRAVITY;

        // Inherited from GenericSensor
        // Start the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        // Inherited from GenericSensor
        linearLayout = (LinearLayout) findViewById(R.id.GravityLinearLayout);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout.addView(layoutInflater.inflate(R.layout.xy_plot, linearLayout, false));

        // Inherited from GenericSensor
        xyPlot = (XYPlot) findViewById(R.id.GenericXYPlot);


        // This own class instantiations
        xValView = (TextView) findViewById(R.id.gravityX);
        yValView = (TextView) findViewById(R.id.gravityY);
        zValView = (TextView) findViewById(R.id.gravityZ);
        magValView = (TextView) findViewById(R.id.gravityMagnitude);


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        xVal = event.values[0];
        yVal = event.values[1];
        zVal = event.values[2];
        magVal = (float) Math.sqrt(xVal * xVal + yVal * yVal + zVal * zVal);

        magValView.setText("Magnitude: " + magVal);
        xValView.setText("x: " + xVal);
        yValView.setText("y: " + yVal);
        zValView.setText("z: " + zVal);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
