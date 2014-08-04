package com.bcappslab.nonsense.sensorclasses;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;
import com.bcappslab.nonsense.MyBuffer;
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

    private MyBuffer myBuffer;

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

        setContentView(R.layout.activity_gravity_sensor);

        // Inherited from GenericSensor
        sensorName = getString(R.string.sensor_gravity);
        sensorType = Sensor.TYPE_GRAVITY;
        bufferSize = 50;
        sensorValueLength = 4; // x, y, z, magnitude
        fileName = getString(R.string.log_gravity);

        myBuffer = new MyBuffer(this, fileName, bufferSize, sensorValueLength);

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


        // Calling this superclass AFTER the name and type have been established??
        // The superclass starts the sensor manager with this information
        // Kind of confusing.  Hard to trace.  Look into a change.
        super.onCreate(savedInstanceState);

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


        myBuffer.add(event.timestamp, xVal, yVal, zVal, magVal);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
