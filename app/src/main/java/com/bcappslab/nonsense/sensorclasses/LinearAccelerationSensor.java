package com.bcappslab.nonsense.sensorclasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.bcappslab.nonsense.MyBuffer;
import com.bcappslab.nonsense.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2014-07-24.
 */
public class LinearAccelerationSensor extends GenericSensor{

    private TextView magValView;
    private TextView xValView;
    private TextView yValView;
    private TextView zValView;

    private float magVal;
    private float xVal;
    private float yVal;
    private float zVal;

    private long offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_linear_acceleration_sensor);

        offset = System.currentTimeMillis() * 1000000L;

        // Inherited from GenericSensor
        sensorName = getString(R.string.sensor_linear_acceleration);
        sensorType = Sensor.TYPE_LINEAR_ACCELERATION;
        bufferSize = 100;
        plotSize = 20;
        sensorValueLength = 4; // x, y, z, magnitude
        fileName = getString(R.string.log_linear_acceleration);
        myBuffer = new MyBuffer(this, fileName, sensorValueLength, bufferSize, plotSize);

        // Inherited from GenericSensor
        linearLayout = (LinearLayout) findViewById(R.id.linearAccelerationLinearLayout);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout.addView(layoutInflater.inflate(R.layout.xy_plot, linearLayout, false));

        // Inherited from GenericSensor
        xyPlot = (XYPlot) findViewById(R.id.GenericXYPlot);

        // This own class instantiations
        xValView = (TextView) findViewById(R.id.linearAccelerationX);
        yValView = (TextView) findViewById(R.id.linearAccelerationY);
        zValView = (TextView) findViewById(R.id.linearAccelerationZ);
        magValView = (TextView) findViewById(R.id.linearAccelerationMagnitude);

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


        long offsetTimestamp = event.timestamp - offset;
        //Log.d("LinearAccelerationSensor","time: " + event.timestamp + ", offset: " + offset + ", offsetTimestamp: " + offsetTimestamp + ", currentMillis: " + System.currentTimeMillis());

        myBuffer.add(offsetTimestamp, xVal, yVal, zVal, magVal);

        if (myBuffer.getIterator() % plotSize == 0){
            new MyAsynchronousUpdater().execute();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class MyAsynchronousUpdater extends AsyncTask<Void, Void, XYPlot> {

        @Override
        protected XYPlot doInBackground(Void... params) {
            xyPlot.clear();

            long[] timeValues = myBuffer.getTimeValuesForPlot();
            float[][] sensorValues = myBuffer.getSensorValuesForPlot();

            // So we can start the time at 0
            long offset = timeValues[0];

            List timeValueList = new ArrayList();
            List xValueList = new ArrayList();
            List yValueList = new ArrayList();
            List zValueList = new ArrayList();
            List magValueList = new ArrayList();

            for (int i = 0; i < plotSize; i++) {
                timeValueList.add((timeValues[i]-offset)/1000000);
                //Log.d("GravitySensor", "timevalue: " + ((timeValues[i]-offset)/1000000));
                for (int j = 0; j < sensorValues.length; j++) {
                    if (j==0)
                        xValueList.add(sensorValues[j][i]);
                    else if (j==1)
                        yValueList.add(sensorValues[j][i]);
                    else if (j==2)
                        zValueList.add(sensorValues[j][i]);
                    else if (j==3)
                        magValueList.add(sensorValues[j][i]);
                }
            }


            XYSeries xSeries = new SimpleXYSeries(timeValueList, xValueList, "x");
            XYSeries ySeries = new SimpleXYSeries(timeValueList, yValueList, "y");
            XYSeries zSeries = new SimpleXYSeries(timeValueList, zValueList, "z");
            XYSeries magSeries = new SimpleXYSeries(timeValueList, magValueList, "mag");

            // Create a formatter to use for drawing and a series using LineAndPointRenderer and configure it from xml
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
            seriesFormat.setPointLabelFormatter(new PointLabelFormatter());
            seriesFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);

//            Paint paint = new Paint();
//            paint.setStrokeWidth(1);
//            paint.setColor(Color.RED);
//            seriesFormat.setLinePaint(paint);

            // add a series to the xplot
            xyPlot.addSeries(xSeries, seriesFormat);
            xyPlot.addSeries(ySeries, seriesFormat);
            xyPlot.addSeries(zSeries, seriesFormat);
            xyPlot.addSeries(magSeries, seriesFormat);

            // reduce number of range labels
            xyPlot.setRangeTopMax(10);
            xyPlot.setRangeTopMin(10);
            xyPlot.setTicksPerRangeLabel(2);
            xyPlot.getGraphWidget().setDomainLabelOrientation(-45);

            return xyPlot;
        }

        @Override
        protected void onPostExecute(XYPlot xyPlot) {
            super.onPostExecute(xyPlot);

            xyPlot.redraw();

        }
    }
}
