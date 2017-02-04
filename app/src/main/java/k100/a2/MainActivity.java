package k100.a2;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainActivity extends AppCompatActivity implements SensorEventListener{ //Register to listen to sensor events. When sensor avail, you get a call back

    //Declarations
    TextView textView;
    private SensorManager sensorManager;
    private Sensor acceleration;
    private TextView textX;
    private TextView textY;
    private TextView textZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Start onCreate Methods
        textX = (TextView) findViewById(R.id.textView);
        textY = (TextView) findViewById(R.id.textView2);
        textZ = (TextView) findViewById(R.id.textView3);

        textView = (TextView)findViewById(R.id.textView);
        HttpTask task = new HttpTask();
        task.execute(new String[] { "http://d39a0b5e.ngrok.io/midp/hits" });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Which sensor to read: Accelerometer
        sensorManager.registerListener(this, acceleration, SensorManager.SENSOR_STATUS_ACCURACY_HIGH); // Register the Listener  + Sampling Rate

    }


    //Declare Classes
    private class HttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            BufferedReader br = null;

            try {
                URL url = new URL(urls[0]); // Specifiy URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // Open Connection
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // Get Data
                String str = "";
                while ((str = br.readLine()) != null) {
                    response = response + str;
                }
                br.close();
            }
            catch (Exception e) {
                e.printStackTrace();


            }


            return response;
        } // End of Declared Class


        @Override
        protected void onPostExecute(String result) {
            // HANDLE GET RESULTS HERE
            //textView.setText(result);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    // ON SENSOR CHANGE
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // CHECK TO MAKE SURE IT IS ACCELEROMETER
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        DecimalFormat dF = new DecimalFormat("#.#");
        x = Float.parseFloat(dF.format(x));
        y = Float.parseFloat(dF.format(y));
        z = Float.parseFloat(dF.format(z));

        textX.setText(String.valueOf(x) + " m/s2");
        textY.setText(String.valueOf(y) + " m/s2");
        textZ.setText(String.valueOf(z) + " m/s2");
    }

    @Override
    protected void onResume() {
        super.onResume(); // register this class as a listener for the
        // orientation and
        // accelerometer sensors
        sensorManager.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() { // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}
