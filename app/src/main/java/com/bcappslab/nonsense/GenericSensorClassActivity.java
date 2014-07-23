package com.bcappslab.nonsense;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Barry on 2014-07-12.
 */
public class GenericSensorClassActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorClass mySensor;

    private String sensorName;
    private int sensorType;

    // To update the UI thread
    private MyBean myBean;

    // To get the linear layout
    private LinearLayout linearLayout;

    // Plot that we need to add programatically
    private XYPlot xyPlot;
    private float[][] plotValues;
    private long[] timeStamps;

    // the length of th ebean is defined by the number of values the sensor throws
    private int beanLength;
    private float values[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_sensor);

        // Receive the intent
        Intent intent = getIntent();
        sensorName = intent.getStringExtra(MainActivity2.SENSOR_NAME);
        sensorType = intent.getIntExtra(MainActivity2.SENSOR_TYPE, -1);

        // Start the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        // Create your sensor class
        mySensor = new SensorClass(sensorName, sensorType, this);

        // Populate the bean ... what does this do?
        myBean = mySensor.updateBean();

        // Get the linear layout so you can populate it with the appropriate amount of textviews
        linearLayout = (LinearLayout) findViewById(R.id.sensor_linear_layout);


        beanLength = mySensor.updateBean().getValues().length;
        values = new float[beanLength];
        for (int i = 0; i < beanLength; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setId(i);
            textView.setText("Value" + i);
            textView.setTextSize(15);
            textView.setTextColor(Color.BLACK);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView);
        }

        // Sweet!  Don't have to do this programatically.
        // Maybe yuo can include the other views as well?  Or at least just the textviews.
        // Have a textview set up in xml so you don't have to change it through Java.
        // Neat!
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout.addView(layoutInflater.inflate(R.layout.simple_xy_plot_example, linearLayout, false));

        // Gotta reference the xyplot... of course!
        xyPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("GenericSensorClassActivity.onSensorChanged", "Name: " + sensorName + ", valuelength: " + event.values.length);

        mySensor.log(event.timestamp, event.values);
        updateUI();
        updatePlot();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Context getContext() {
        return getApplicationContext();
    }

    private void updateUI() {
        values = myBean.getValues();

        for (int i = 0; i < beanLength; i++) {
            TextView textView = (TextView) findViewById(i);
            textView.setText("Value" + i + ": " + values[i]);
        }

    }

    private void updatePlot() {
        xyPlot.clear();
        timeStamps = myBean.getTimeStamps();
        plotValues = myBean.getPlotValues();

        if (timeStamps != null) {

            // So we can start our timer at 0.
            long offset = timeStamps[0];

            List timeStampList = new ArrayList();
            List plotValue1 = new ArrayList();
            List plotValue2 = new ArrayList();
            List plotValue3 = new ArrayList();
            List magnitudes = new ArrayList();

            for (int i = 0; i < timeStamps.length; i++) {

                timeStampList.add(timeStamps[i]-offset);
                plotValue1.add(plotValues[i][0]);
                plotValue2.add(plotValues[i][1]);
                plotValue3.add(plotValues[i][2]);
                magnitudes.add(Math.pow((plotValues[i][0] * plotValues[i][0] + plotValues[i][1] * plotValues[i][1] + plotValues[i][2] * plotValues[i][2]), (1 / 2.0)));
            }


            XYSeries series1 = new SimpleXYSeries(timeStampList, plotValue1, "x values");
            XYSeries series2 = new SimpleXYSeries(timeStampList, plotValue2, "y values");
            XYSeries series3 = new SimpleXYSeries(timeStampList, plotValue3, "z values");
            XYSeries magnitude = new SimpleXYSeries(timeStampList, magnitudes, "magnitude");

            // Create a formatter to use for drawing and a series using LineAndPointRenderer and configure it from xml
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
            seriesFormat.setPointLabelFormatter(new PointLabelFormatter());
            seriesFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);

            /*
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(1);
            */

            // add a series to the xplot
            //seriesFormat.setLinePaint(paint);
            xyPlot.addSeries(series1, seriesFormat);
            xyPlot.addSeries(series2, seriesFormat);
            xyPlot.addSeries(series3, seriesFormat);
            xyPlot.addSeries(magnitude, seriesFormat);

            // reduce number of range labels
            xyPlot.setTicksPerRangeLabel(3);
            xyPlot.getGraphWidget().setDomainLabelOrientation(-45);

            xyPlot.redraw();
        }

    }
}
