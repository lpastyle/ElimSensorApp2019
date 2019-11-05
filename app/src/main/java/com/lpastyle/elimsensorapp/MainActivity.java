package com.lpastyle.elimsensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemClickListener {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final int SAMPLING_INTERVAL_US = 1000000; // one second

    private SensorManager mSensorManager;
    private Sensor mCurrentSensor;

    private List<Sensor> mSensors;

    // Views
    private ListView mSensorLv;
    private CustomSensorView mCustomSensorView;
    private TextView mArrayValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // init views
        mCustomSensorView = findViewById(R.id.customSensorView);
        mArrayValues = findViewById(R.id.arrayValuesTv);
        mSensorLv = findViewById(R.id.sensorLv);
        if (mSensorLv != null) {
            mSensorLv.setAdapter(new SensorListAdapter(this, mSensors));
            mSensorLv.setOnItemClickListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(LOG_TAG, "Sensor item clicked=" + position);
        unRegisterSensorListener();
        mCurrentSensor = mSensors.get(position);
        switch (mCurrentSensor.getType()) {
            // only sensor handled by Android emulator:
            //   case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_PROXIMITY:
            case Sensor.TYPE_LIGHT:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                Log.i(LOG_TAG, "Handle " + mCurrentSensor.getStringType());
                registerSensorListener();
                break;
            default:
                Toast.makeText(this, R.string.unsupported_sensor_type, Toast.LENGTH_SHORT).show();
                mCurrentSensor = null;
                mCustomSensorView.setAngle(0);
                mArrayValues.setText(R.string.no_values);

        }
    }

    private void registerSensorListener() {
        if (mSensorManager != null && mCurrentSensor != null)
            mSensorManager.registerListener(this, mCurrentSensor, SAMPLING_INTERVAL_US);
    }

    private void unRegisterSensorListener() {
        if (mSensorManager != null && mCurrentSensor != null)
            mSensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(LOG_TAG, "onAccuracyChanged(" + accuracy + ")");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(LOG_TAG, "onSensorChanged()");
        if (event != null) {
            String values = "";
            int n = event.values.length;
            for (int i = 0; i < n; i++) {
                values += "v[" + i + "]=" + event.values[i] + (((n - i) == 1) ? '.' : ", ");
            }

            mCustomSensorView.setAngle((event.values[0] * 360) / event.sensor.getMaximumRange());
            mArrayValues.setText(values);
        }

    }


    // activity life cycle management:
    // unregister sensors listener to save power

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterSensorListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterSensorListener();
    }


}
