package com.bcappslab.nonsense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bcappslab.nonsense.sensorclasses.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Barry on 2014-07-09.
 */
public class MainActivity2 extends Activity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private List<String> sensorNames;
    private Map<String, Sensor> sensorMap;

    private ListView listView;

    public static final String SENSOR_NAME = "SENSOR_NAME";
    public static final String SENSOR_TYPE = "SENSOR_TYPE";

    // Will be used to launch the correct class
    private Intent genericSensorIntent;

    // Recyclable toast
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanSensors();
        /*
        Log.d("MainActivity2.onCreate", "sensorList: " + sensorList.toString());
        Log.d("MainActivity2.onCreate", "sensorNames: " + sensorNames.toString());
        Log.d("MainActivity2.onCreate", "Sensor names and types found: ");
        for (Sensor s : sensorList){
            Log.d("MainActivity2.onCreate", "Name: " + s.getName() + ", Type: " + s.getType());
        }

        */

        sensorMap = new TreeMap<String, Sensor>();

        for (int i = 0; i < sensorNames.size(); i++) {
            sensorMap.put(sensorNames.get(i), sensorList.get(i));
        }
        //Log.d("MainActivity2.onCreate", "sensorList.size(): " + sensorList.size());
        //Log.d("MainActivity2.onCreate", "sensorNames.size(): " + sensorNames.size());

        sensorList.clear();
        sensorNames.clear();

        for (String s : sensorMap.keySet()) {
            sensorNames.add(s);
            sensorList.add(sensorMap.get(s));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorNames);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        toast = Toast.makeText(getApplicationContext(), sensorNames.size() + " sensors found.", Toast.LENGTH_LONG);
        toast.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity2.onCreate.setOnItemClickListener", "Name: " + sensorNames.get(position));
                Log.d("MainActivity2.onCreate.setOnItemClickListener", "Type: " + sensorList.get(position).getType());

                /*
                Intent intent = new Intent(getApplicationContext(), GenericSensorClassActivity.class);
                intent.putExtra(SENSOR_NAME, sensorNames.get(position));
                intent.putExtra(SENSOR_TYPE, sensorList.get(position).getType());

                startActivity(intent);
                */

                /*
                Intent intent = new Intent(getApplicationContext(), GravitySensor.class);
                startActivity(intent);
                */

                launchSensorClass(sensorList.get(position).getType());
            }
        });

    }

    void scanSensors() {
        // Scan the sensors and populate lists.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorList = new ArrayList<Sensor>(sensorManager.getSensorList(Sensor.TYPE_ALL));
        sensorNames = new ArrayList<String>(sensorList.size());

        for (Sensor x : sensorList) {
            Log.d("MainActivity2", x.toString());

            switch (x.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    sensorNames.add(getString(R.string.sensor_accelerometer));
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sensorNames.add(getString(R.string.sensor_ambient_temperature));
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    sensorNames.add(getString(R.string.sensor_game_rotation_vector));
                    break;
                case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                    sensorNames.add(getString(R.string.sensor_geomagnetic_rotation_vector));
                    break;
                case Sensor.TYPE_GRAVITY:
                    sensorNames.add(getString(R.string.sensor_gravity));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sensorNames.add(getString(R.string.sensor_gyroscope));
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    sensorNames.add(getString(R.string.sensor_gyroscope_uncalibrated));
                    break;
                case Sensor.TYPE_LIGHT:
                    sensorNames.add(getString(R.string.sensor_light));
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    sensorNames.add(getString(R.string.sensor_linear_acceleration));
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sensorNames.add(getString(R.string.sensor_magnetic_field));
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    sensorNames.add(getString(R.string.sensor_magnetic_field_uncalibrated));
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sensorNames.add(getString(R.string.sensor_orientation));
                    break;
                case Sensor.TYPE_PRESSURE:
                    sensorNames.add(getString(R.string.sensor_pressure));
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sensorNames.add(getString(R.string.sensor_proximity));
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    sensorNames.add(getString(R.string.sensor_relative_humidity));
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    sensorNames.add(getString(R.string.sensor_rotation_vector));
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    sensorNames.add(getString(R.string.sensor_significant_motion));
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    sensorNames.add(getString(R.string.sensor_step_counter));
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    sensorNames.add(getString(R.string.sensor_step_detector));
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sensorNames.add(getString(R.string.sensor_temperature));
                    break;

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear_logs) {
            clearLogs();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearLogs() {
        int deleted = 0;
        //Log.d("MainActivity2", "Clear logs.");
        //File filesDir = getApplicationContext().getFilesDir();
        File filesDir = getApplicationContext().getFilesDir();
        if (filesDir.isDirectory()) {
            for (File item : filesDir.listFiles()) {
                item.delete();
                deleted++;
            }
        }

        if (deleted == 1) {
            toast = Toast.makeText(getApplicationContext(), deleted + " file deleted.", Toast.LENGTH_LONG);
        } else {
            toast = Toast.makeText(getApplicationContext(), deleted + " files deleted.", Toast.LENGTH_LONG);
        }
        toast.show();
    }

    private void launchSensorClass(int sensorType) {

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                genericSensorIntent = new Intent(getApplicationContext(), AccelerometerSensor.class);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                genericSensorIntent = new Intent(getApplicationContext(), AmbientTemperatureSensor.class);
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                genericSensorIntent = new Intent(getApplicationContext(), GameRotationVectorSensor.class);
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                genericSensorIntent = new Intent(getApplicationContext(), GeomagneticRotationVectorSensor.class);
                break;
            case Sensor.TYPE_GRAVITY:
                genericSensorIntent = new Intent(getApplicationContext(), GravitySensor.class);
                break;
            case Sensor.TYPE_GYROSCOPE:
                genericSensorIntent = new Intent(getApplicationContext(), GyroscopeSensor.class);
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                genericSensorIntent = new Intent(getApplicationContext(), GyroscopeUncalibratedSensor.class);
                break;
            case Sensor.TYPE_LIGHT:
                genericSensorIntent = new Intent(getApplicationContext(), LightSensor.class);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                genericSensorIntent = new Intent(getApplicationContext(), LinearAccelerationSensor.class);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                genericSensorIntent = new Intent(getApplicationContext(), MagneticFieldSensor.class);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                genericSensorIntent = new Intent(getApplicationContext(), MagneticFieldUncalibratedSensor.class);
                break;
            case Sensor.TYPE_ORIENTATION:
                genericSensorIntent = new Intent(getApplicationContext(), OrientationSensor.class);
                break;
            case Sensor.TYPE_PRESSURE:
                genericSensorIntent = new Intent(getApplicationContext(), PressureSensor.class);
                break;
            case Sensor.TYPE_PROXIMITY:
                genericSensorIntent = new Intent(getApplicationContext(), ProximitySensor.class);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                genericSensorIntent = new Intent(getApplicationContext(), RelativeHumiditySensor.class);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                genericSensorIntent = new Intent(getApplicationContext(), RotationVectorSensor.class);
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                genericSensorIntent = new Intent(getApplicationContext(), SignificantMotionSensor.class);
                break;
            case Sensor.TYPE_STEP_COUNTER:
                genericSensorIntent = new Intent(getApplicationContext(), StepCounterSensor.class);
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                genericSensorIntent = new Intent(getApplicationContext(), StepDetectorSensor.class);
                break;
            case Sensor.TYPE_TEMPERATURE:
                genericSensorIntent = new Intent(getApplicationContext(), TemperatureSensor.class);
                break;
        }

        if (genericSensorIntent != null) {
            startActivity(genericSensorIntent);
        }
    }

    public Context getMainApplicationContext() {
        return getApplicationContext();
    }


}
