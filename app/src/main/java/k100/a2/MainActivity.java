package k100.a2;

// Used Libraries
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;


// Unused Libraries
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import java.io.InputStream;
import java.io.BufferedInputStream;
import android.provider.MediaStore;
import android.content.Intent;



public class MainActivity extends AppCompatActivity implements SensorEventListener{ //Register to listen to sensor events. When sensor avail, you get a call back

    //Declarations
    private SensorManager sensorManager;
    private Sensor acceleration;
    private TextView textX;
    private TextView Event;
    public TextView outputView;
    public static Button get_button;
    public static Button post_button;
    public long LastUpdate = 1;
    public float last_x = 0;
    public float last_y = 0;
    public float last_z = 0;
    private static final int SHAKE_THRESHOLD = 200;
    private static final int delay = 450; //mili-seconds

    public String Request_Type;
    public String Field_1 = "F1";
    public String Field_2 = "F2";

    // PLACE TOMCAT SERVER OR TUNNLED ADDRESS HERE
    public String Server_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define Variables
        textX = (TextView) findViewById(R.id.textView);
        Event = (TextView) findViewById(R.id.textView2);
        outputView = (TextView)findViewById(R.id.textView4);
        get_button = (Button) findViewById(R.id.button_get);
        post_button = (Button) findViewById(R.id.button_post);

        // Run methods
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Which sensor to read: Accelerometer
        sensorManager.registerListener(this, acceleration, SensorManager.SENSOR_STATUS_ACCURACY_HIGH); // Register the Listener  + Sampling Rate

    }


    // Manual OnClick handles (See buttons in layout xml)

    public void sendGetRequest(View v) {
        Request_Type = "GET";
        new HTTP_request().execute();
    }

    public void sendPostRequest(View v) {
        Field_2 = "F1 Test";
        Field_2 = "F2 Test";
        Request_Type = "POST";
        new HTTP_request().execute();
    }


    // HTTP Request Class (Handles both GET & POST all in one wowieee)
    public class HTTP_request extends AsyncTask<String, Void, Void> {
        public Void doInBackground(String... params) {
            try {
                // DEFINE POST or GET components:
                //  - Http POST format: Server_URL ? first_fieldname = "string value 1" + second_filedname = "string value 2"
                //  - Http GET format: Server_URL ? first_fieldname = "string value 1" + second_filedname = "string value 2"
                URL url = new URL(Server_URL + "?time=" + Field_1 + "&event=" + Field_2);

                // Open HTTP Connection
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod(Request_Type);

                // Variables for storing output response
                String response = "";
                BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream())); ;
                final TextView outputView = (TextView) findViewById(R.id.textView4); // Reference variable for output textview

                // Get output response
                String str = "";
                while ((str = buff.readLine()) != null) {
                    response = response + str;
                }
                buff.close();

                final String finalResponse = response;
                // Display output response on separate thread (UI Thread)
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() { outputView.setText(finalResponse); }
                }
                );

            }
            catch (IOException e) {
            }
            return null;
        }
    } // End of HTTP Request Class



    // --------------------------------------- GESTURE METHODS ---------------------------------------



    // ON SENSOR CHANGE / EVENT HANDLE
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // CHECK TO MAKE SURE IT IS ACCELEROMETER
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        String timeStamp = new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(new Date());

        // Collect data from sensor
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        long WaitTime = System.currentTimeMillis();


        // Wait delay time before processing
        if ((WaitTime - LastUpdate) > delay) {
            long diffTime = (WaitTime - LastUpdate);
            LastUpdate = WaitTime;

            // Round off Numbers
            DecimalFormat dF = new DecimalFormat("#.#");
            x = Float.parseFloat(dF.format(x));
            y = Float.parseFloat(dF.format(y));
            z = Float.parseFloat(dF.format(z));
            textX.setText(String.valueOf(x) + " m/s2");

            float shake = Math.abs((z) - (last_z)) / diffTime * 10000;

            // SHAKE return to home activity
            if (shake > SHAKE_THRESHOLD) {
                Field_1 = timeStamp;
                Field_2 = "EARTHQUAKE";
                Request_Type = "POST";
                new HTTP_request().execute();
                Event.setText(Field_2);

            }
            else if (shake < SHAKE_THRESHOLD) {
                // TILT right
                if (x < -3) {
                    Field_1 = timeStamp;
                    Field_2 = "Tilt Right";
                    Request_Type = "POST";
                    new HTTP_request().execute();
                    Event.setText(Field_2);
                }
                // TILT left
                else if (x > 3) {
                    Field_1 = timeStamp;
                    Field_2 = "Tilt LEFT";
                    Request_Type = "POST";
                    new HTTP_request().execute();
                    Event.setText(Field_2);
                }
            }

            // Update Old Values
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume() {
        super.onResume(); // register this class as a listener for the
        sensorManager.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() { // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
