package com.example.harsh.plexusmdassignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HARSH_APP";

    Button submitButton;
    EditText ipInput, cityInput;
    TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitButton = (Button) findViewById(R.id.button);
        cityInput = (EditText) findViewById(R.id.editText);
        ipInput = (EditText) findViewById(R.id.editText2);
        output = (TextView) findViewById(R.id.textView);

        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            output.setText("Please wait...");

            String ip = ipInput.getText().toString();
            String cityName = cityInput.getText().toString();

            Log.i(TAG, "Using host : " + ip);
            Log.i(TAG, "City Name : " + cityName);

            Fetcher fetcher = new Fetcher();
            fetcher.execute(ip, cityName);
        }
    }


    class Fetcher extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String ip = params[0];
                String cityName = params[1];

                Log.i(TAG, "Using host : " + ip);
                Log.i(TAG, "City Name : " + cityName);


                String baseURL = "http://" + ip + "/location/getCityInfo?cityName=" + cityName;
                URL base = new URL(baseURL);
                URLConnection urlConnection = base.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder urlString = new StringBuilder();
                String current;

                while ((current = in.readLine()) != null) {
                    urlString.append(current);
                }

                Log.i(TAG, urlString.toString());
                String receivedData = urlString.toString();

                JSONObject json = new JSONObject(receivedData);
                String time = json.getString("time");

                String lat = ((JSONObject)json.get("location")).getString("lat");
                String lng = ((JSONObject)json.get("location")).getString("lng");

                final String outputToRender = "Time: "+time+"\n\n"+
                        "Latitude: "+lat+"\n"+
                        "Longitude: "+lng;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText(outputToRender);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getLocalizedMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return "Okay!";
        }
    }

}

