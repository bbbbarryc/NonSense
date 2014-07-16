package com.bcappslab.nonsense.testing;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.androidplot.xy.*;
import com.bcappslab.nonsense.R;

import java.util.Arrays;

/**
 * Created by Barry on 2014-07-12.
 */


public class TestPlotter extends Activity{

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This will prevent screenshots on ICS+ devices
        // How fun and obnoxious!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.simple_xy_plot_example);

        // find our plot and reference it in java
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

        // Create a couple of arrays
        Number[] series1Vals = {1,2,3,4,5,6,7,8,9,10};
        Number[] series2Vals = {3,6,9,1,4,8,12,16,10,6};

        // Turn above into XYSeries
        // Takes a list.  Y VALS ONLY uses the index as the x value.
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Vals), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Vals), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 2");

        // Create a formatter to use for drawing and a series using LineAndPointRenderer and configure it from xml
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);

        // add a series to the xplot
        plot.addSeries(series1, series1Format);


        // Same as above
        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf2);
        plot.addSeries(series2, series2Format);


        // reduce number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
