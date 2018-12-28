package br.edu.unidavi.listadesensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements SensorEventListener {

    private RecyclerView sensorList;
    private TextView sensorName;
    private TextView sensorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorList = findViewById(R.id.sensor_list);
        sensorName = findViewById(R.id.sensor_name);
        sensorValues = findViewById(R.id.sensor_values);

        sensorName.setText(R.string.placeholder);
        fetchSensorList();
    }

    private void fetchSensorList() {
        SensorManager manager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = manager
                .getSensorList(Sensor.TYPE_ALL);

        SensorAdapter adapter = new SensorAdapter(sensors,
                new SensorAdapter.OnClickListener() {
            @Override
            public void onClick(Sensor sensor) {
                sensorName.setText(sensor.getName());
                listenToSensor(sensor);
            }
        });

        sensorList.setLayoutManager(
                new LinearLayoutManager(this));
        sensorList.setAdapter(adapter);
    }

    private void listenToSensor(Sensor sensor) {
        SensorManager manager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        manager.unregisterListener(this);

        manager.registerListener(this,
                sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder builder = new StringBuilder();
        for (float value : event.values) {
            builder.append(value).append(" ");
        }
        sensorValues.setText(builder.toString().trim());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,
                                  int accuracy) {
        // Nada a fazer
    }
}
