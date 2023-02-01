package com.example.projektwaluty;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView summary;
    Button button;
    Spinner firstCurr, secondCurr;
    String firstCurrSelected = "USD - U.S. Dollar", secondCurrSelected = "PLN - Polish Złoty";
    SensorManager sensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    String[] myString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        summary = findViewById(R.id.TextView);
        firstCurr = findViewById(R.id.firstCurrency);
        secondCurr = findViewById(R.id.secondCurrency);
        String[] currencies = getResources().getStringArray(R.array.currencies);
        Fragment fragment = new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cardView,fragment).commit();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(sensorManager).registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstCurr.setAdapter(adapter);
        secondCurr.setAdapter(adapter);
        firstCurr.setOnItemSelectedListener(this);
        secondCurr.setOnItemSelectedListener(this);
        secondCurr.setSelection(10);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "https://open.er-api.com/v6/latest/";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+firstCurrSelected.substring(0,3), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject rates = new JSONObject();
                            String valueConverted = "";
                            try {
                                rates = response.getJSONObject("rates");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                valueConverted = rates.getString(secondCurrSelected.substring(0,3));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            float exchange = 1/ Float.valueOf(valueConverted);
                            exchange = Math.round(exchange*10000)/10000F;
                            summary.setText("1 " + firstCurrSelected.substring(0,3)+ " equals " + valueConverted.substring(0,6) + " " + secondCurrSelected.substring(0,3)+"\n"+
                                    "1 " + secondCurrSelected.substring(0,3)+ " equals " + exchange + " " + firstCurrSelected.substring(0,3) );
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                }
                else {
                    Toast.makeText(MainActivity.this, "Brak Internetu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 13) {
                Resources res = getResources();
                myString = res.getStringArray(R.array.currencies);
                int len = myString.length;
                int index = (int)(Math.random() * 30) +1;
                String q = myString[index];
                firstCurrSelected = q;
                firstCurr.setSelection(index);
                index = (int)(Math.random() * 30) +1;
                q = myString[index];
                secondCurr.setSelection(index);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorListener);
        super.onPause();
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.firstCurrency){
            firstCurrSelected = adapterView.getItemAtPosition(i).toString();
        }
        if(adapterView.getId() == R.id.secondCurrency){
            secondCurrSelected = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}