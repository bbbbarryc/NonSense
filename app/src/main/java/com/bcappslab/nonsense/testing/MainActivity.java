package com.bcappslab.nonsense.testing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bcappslab.nonsense.R;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by Barry on 2014-07-06.
 */
public class MainActivity extends Activity {

    private SensorManager sensorManager;
    private ListView listView;
    List<Sensor> deviceSensorList;
    List<String> deviceSensorNames;
    SortedMap<String, Sensor> myMap;
    ArrayAdapter arrayAdapter;
    int sensorType;

    public static final String SENSOR_NAME = "SENSOR_NAME";
    public static final String SENSOR_TYPE = "SENSOR_TYPE";
    public static final String SENSOR_MAXIMUM_RANGE = "SENSOR_MAXIMUM_RANGE";
    public static final String SENSOR_MIN_DELAY = "SENSOR_MIN_DELAY";
    public static final String SENSOR_POWER = "SENSOR_POWER";
    public static final String SENSOR_RESOLUTION = "SENSOR_RESOLUTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanSensors();

        /*
        myMap = new TreeMap<String, Sensor>();
        for (int i=0; i<deviceSensorList.size(); i++) {
            myMap.put(deviceSensorNames.get(i), deviceSensorList.get(i));
        }
        //Log.d("scanSensors",myMap.toString());

        //Log.d("scanSensors", "Clearing sensor names.");
        deviceSensorNames = new ArrayList<String>();
        //Log.d("scanSensors", "Clearing sensor list.");
        deviceSensorList = new ArrayList<Sensor>();
        //Log.d("scanSensors", "Redoing names and list in order.");
        for (String s : myMap.keySet()){
            deviceSensorNames.add(s);
            deviceSensorList.add(myMap.get(s));
        }


        */

        Toast toast = Toast.makeText(getApplicationContext(), "Found " + deviceSensorNames.size() + " sensors.", Toast.LENGTH_LONG);
        toast.show();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceSensorNames);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClicked position", ": " + position);

                Intent intent = new Intent(getApplicationContext(), TestSensorClasser.class);

                intent.putExtra(SENSOR_NAME, deviceSensorNames.get(position));
                intent.putExtra(SENSOR_TYPE, deviceSensorList.get(position).getType());
                intent.putExtra(SENSOR_MAXIMUM_RANGE, deviceSensorList.get(position).getMaximumRange());
                intent.putExtra(SENSOR_MIN_DELAY, deviceSensorList.get(position).getMinDelay());
                intent.putExtra(SENSOR_POWER, deviceSensorList.get(position).getPower());
                intent.putExtra(SENSOR_RESOLUTION, deviceSensorList.get(position).getResolution());

                startActivity(intent);
            }
        });
    }


    private void scanSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        deviceSensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        deviceSensorNames = new ArrayList<String>();

        for (Sensor x : deviceSensorList) {
            //Log.d("scanSensors", "Name: " + x.getName());
            //Log.d("scanSensors", "  Details: " + x.toString());
            //deviceSensorNames.add(x.getName());
            sensorType = x.getType();

            switch (sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    deviceSensorNames.add(getString(R.string.sensor_accelerometer));
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    deviceSensorNames.add(getString(R.string.sensor_ambient_temperature));
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    deviceSensorNames.add(getString(R.string.sensor_game_rotation_vector));
                    break;
                case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                    deviceSensorNames.add(getString(R.string.sensor_geomagnetic_rotation_vector));
                    break;
                case Sensor.TYPE_GRAVITY:
                    deviceSensorNames.add(getString(R.string.sensor_gravity));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    deviceSensorNames.add(getString(R.string.sensor_gyroscope));
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    deviceSensorNames.add(getString(R.string.sensor_gyroscope_uncalibrated));
                    break;
                case Sensor.TYPE_LIGHT:
                    deviceSensorNames.add(getString(R.string.sensor_light));
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    deviceSensorNames.add(getString(R.string.sensor_linear_acceleration));
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    deviceSensorNames.add(getString(R.string.sensor_magnetic_field));
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    deviceSensorNames.add(getString(R.string.sensor_magnetic_field_uncalibrated));
                    break;
                case Sensor.TYPE_ORIENTATION:
                    deviceSensorNames.add(getString(R.string.sensor_orientation));
                    break;
                case Sensor.TYPE_PRESSURE:
                    deviceSensorNames.add(getString(R.string.sensor_pressure));
                    break;
                case Sensor.TYPE_PROXIMITY:
                    deviceSensorNames.add(getString(R.string.sensor_proximity));
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    deviceSensorNames.add(getString(R.string.sensor_relative_humidity));
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    deviceSensorNames.add(getString(R.string.sensor_rotation_vector));
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    deviceSensorNames.add(getString(R.string.sensor_significant_motion));
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    deviceSensorNames.add(getString(R.string.sensor_step_counter));
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    deviceSensorNames.add(getString(R.string.sensor_step_detector));
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    deviceSensorNames.add(getString(R.string.sensor_temperature));
                    break;
                default:
                    deviceSensorNames.add("Unidentified");
                    break;
            }
        }
    }
}
