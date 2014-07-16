package com.bcappslab.nonsense.testing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bcappslab.nonsense.R;
import com.bcappslab.nonsense.testing.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Barry on 2014-07-06.
 */
public class TestSensorClasser extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    TextView tvSensorName;
    TextView tvSensorValue0;
    TextView tvSensorValue1;
    TextView tvSensorValue2;
    TextView tvSensorMagnitude;

    // Sensor values passed from MainActivity
    String sensorName;
    int sensorType;
    int sensorMaximumRange;
    int sensorMinDelay;
    int sensorPower;
    int sensorResolution;

    // Event values recycled for different sensors
    // used in plotting
    float v, w, x, y, z, lux, pressure, magnitude;
    String units;
    float[] gravity = new float[3];

    // Used to write to files
    File file;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sensor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Receive the intent
        Intent intent = getIntent();
        sensorName = intent.getStringExtra(MainActivity.SENSOR_NAME);
        sensorType = intent.getIntExtra(MainActivity.SENSOR_TYPE, -1);
        sensorMaximumRange = intent.getIntExtra(MainActivity.SENSOR_MAXIMUM_RANGE, -1);
        sensorMinDelay = intent.getIntExtra(MainActivity.SENSOR_MIN_DELAY, -1);
        sensorPower = intent.getIntExtra(MainActivity.SENSOR_POWER, -1);
        sensorResolution = intent.getIntExtra(MainActivity.SENSOR_RESOLUTION, -1);

        // Instead of instantiating each one just access directly.  Kinda funky but saves many lines of code.
        //((TextView) findViewById(R.id.sensorName)).setText(sensorName);
        tvSensorName = (TextView) findViewById(R.id.sensorName);
        tvSensorName.setText(sensorName); // Set this up here so the name appears before the activity loads.  Looks nicer.
        tvSensorValue0 = (TextView) findViewById(R.id.sensorValue0);
        tvSensorValue1 = (TextView) findViewById(R.id.sensorValue1);
        tvSensorValue2 = (TextView) findViewById(R.id.sensorValue2);
        tvSensorMagnitude = (TextView) findViewById(R.id.sensorMagnitude);


        // Start the fun stuff
        // This starts the monitoring of the sensor type passed to this class
        // You don't really get to see it but it works
        sensor = sensorManager.getDefaultSensor(sensorType);

        // For logging ... this must change eventually.  Putting it here to see what happens
        // because it didn't work in the plot() function.
        file = new File(this.getFilesDir(), "log.txt");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                tvSensorName.setText(R.string.sensor_accelerometer);

                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time constant
                // and dT, the event delivery rate

                final float alpha = (float) 0.8;

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                x = event.values[0] - gravity[0];
                y = event.values[1] - gravity[1];
                z = event.values[2] - gravity[2];

                /*
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                */

                magnitude = (float) Math.pow((x * x + y * y + z * z), 1 / 2.0);

                plot(getString(R.string.sensor_accelerometer), "m/s^2", x, y, z, magnitude);

                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                tvSensorName.setText(R.string.sensor_ambient_temperature);

                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                tvSensorName.setText(R.string.sensor_game_rotation_vector);

                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                tvSensorName.setText(R.string.sensor_geomagnetic_rotation_vector);

                break;
            case Sensor.TYPE_GRAVITY:
                tvSensorName.setText(R.string.sensor_gravity);
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                magnitude = (float) Math.pow((x * x + y * y + z * z), 1 / 2.0);
                plot(getString(R.string.sensor_gravity), "m/s^2", x, y, z, magnitude);

                break;
            case Sensor.TYPE_GYROSCOPE:
                tvSensorName.setText(R.string.sensor_gyroscope);
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                magnitude = (float) Math.pow((x * x + y * y + z * z), 1 / 2.0);
                plot(getString(R.string.sensor_gyroscope), "rad/s", x, y, z, magnitude);

                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                tvSensorName.setText(R.string.sensor_gyroscope_uncalibrated);

                break;
            case Sensor.TYPE_LIGHT:
                tvSensorName.setText(R.string.sensor_light);
                lux = event.values[0];
                plot(getString(R.string.sensor_light), "lux", lux);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                tvSensorName.setText(R.string.sensor_linear_acceleration);

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                magnitude = (float) Math.pow((x * x + y * y + z * z), 1 / 2.0);
                plot(getString(R.string.sensor_linear_acceleration), "m/s^2", x, y, z, magnitude);

                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                tvSensorName.setText(R.string.sensor_magnetic_field);
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                magnitude = (float) Math.pow((x * x + y * y + z * z), 1 / 2.0);
                plot(getString(R.string.sensor_magnetic_field), "uT", x, y, z, magnitude);

                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                tvSensorName.setText(R.string.sensor_magnetic_field_uncalibrated);

                break;
            case Sensor.TYPE_ORIENTATION:
                tvSensorName.setText(R.string.sensor_orientation);

                //SensorManager.getRotationMatrix(null, null, )

                break;
            case Sensor.TYPE_PRESSURE:
                tvSensorName.setText(R.string.sensor_pressure);
                pressure = event.values[0] / 10;
                plot(getString(R.string.sensor_pressure), "kPa", pressure);


                break;
            case Sensor.TYPE_PROXIMITY:
                tvSensorName.setText(R.string.sensor_proximity);
                x = event.values[0];
                plot(getString(R.string.sensor_proximity), "cm", x);

                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                tvSensorName.setText(R.string.sensor_relative_humidity);

                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                tvSensorName.setText(R.string.sensor_rotation_vector);
                v = event.values[0];
                w = event.values[1];
                x = event.values[2];
                y = event.values[3];
                z = event.values[4];
                plot(getString(R.string.sensor_rotation_vector), "", v, w, x, y, z);


                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                tvSensorName.setText(R.string.sensor_significant_motion);

                break;
            case Sensor.TYPE_STEP_COUNTER:
                tvSensorName.setText(R.string.sensor_step_counter);

                break;
            case Sensor.TYPE_STEP_DETECTOR:
                tvSensorName.setText(R.string.sensor_step_detector);

                break;
            case Sensor.TYPE_TEMPERATURE:
                tvSensorName.setText(R.string.sensor_temperature);

                break;

            default:
                break;


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something if it changes ??

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            Log.d("sensorLogging", "Error closing file");
        }

    }

    protected void plot(String sensorName, String units, float x) {
        tvSensorValue0.setText("Value: " + x + " " + units);

        //Log.d("sensorLogging",file.getAbsolutePath());
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true)); // append
            bufferedWriter.write(sensorName + ": " + x + "\r\n");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void plot(String fileName, String units, float v, float w, float x, float y, float z) {
        tvSensorValue0.setText("x*sin(theta/2): " + v + " " + units);
        tvSensorValue1.setText("y*sin(theta/2): " + w + " " + units);
        tvSensorValue2.setText("z*sin(theta/2): " + x + " " + units);
    }

    protected void plot(String fileName, String units, float x, float y, float z, float magnitude) {
        tvSensorValue0.setText("x: " + x + " " + units);
        tvSensorValue1.setText("y: " + y + " " + units);
        tvSensorValue2.setText("z: " + z + " " + units);
        tvSensorMagnitude.setText("Magnitude: " + magnitude + " " + units);
    }
}
