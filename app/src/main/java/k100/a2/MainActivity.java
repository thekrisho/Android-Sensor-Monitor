package k100.a2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    //Declarations
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Start onCreate Methods
        textView = (TextView)findViewById(R.id.textView);
        HttpTask task = new HttpTask();
        task.execute(new String[] { "http://d39a0b5e.ngrok.io/midp/hits" });


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
            textView.setText(result);
        }
    }

}
