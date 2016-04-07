package com.example.victoria.pearlhacks_1_2_3_4cast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Temperature extends AppCompatActivity implements WeatherInterface {
    private TextView temp, feels, hum, wind, precip, city;
    private Weather weather;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        String c = "Chapel Hill, US";

        city = (TextView) findViewById(R.id.city);
        temp = (TextView) findViewById(R.id.temp);
        feels = (TextView) findViewById(R.id.feels_temp);
        hum = (TextView) findViewById(R.id.hum);
        wind = (TextView) findViewById(R.id.wind);
        precip = (TextView) findViewById(R.id.precip);

        JSONWeatherTask task = new JSONWeatherTask(this);
        task.execute(new String[]{c});

        Button outfits = (Button) findViewById(R.id.button3);
        if(outfits!=null){
            outfits.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Intent intent = new Intent(Temperature.this, MainActivity_Outfits.class);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temperature, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void receivedWeather(Weather weather) {
        city.setText(Html.fromHtml(String.format("City: Chapel Hill")));
        temp.setText(Html.fromHtml(String.format("Temperature: <b>%.2fºF</b>", weather.getTemp())));
        feels.setText(Html.fromHtml("Feels like: <b>" + weather.getFeels() + "ºF</b>"));
        hum.setText(Html.fromHtml("Humidity: <b>" + weather.getHumidity() + "</b>"));
        wind.setText(Html.fromHtml(String.format("Wind speed: <b>%.2fmph</b>", weather.getWind())));
        precip.setText(Html.fromHtml("Precipitation: <b>" + weather.getPrecip() + "in</b>"));
    }
}

class JSONWeatherParser {
    protected Weather weather;
    JSONObject jObj;

    public JSONWeatherParser(String data) throws JSONException {
        try {
            jObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject info = getObject("current_observation", jObj);
        double temp = getFloat("temp_f", info);
        double feels_temp = Double.parseDouble(getString("feelslike_f", info));
        String humidity = getString("relative_humidity", info);
        double wind_speed = getFloat("wind_mph", info);
        double precip = Double.parseDouble(getString("precip_today_in", info));

        weather = new Weather(temp, humidity, wind_speed, feels_temp, precip);
    }

    public Weather getWeather() {
        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = null;
        try {
            subObj = jObj.getJSONObject(tagName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        String output = "";
        try {
            output = jObj.getString(tagName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        float output = 0;
        try {
            output = (float) jObj.getDouble(tagName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}

class Weather {
    protected double temp;
    protected String humidity;
    protected double wind_speed;
    protected double feels_temp;
    protected double precip;

    public Weather(double t, String h, double w, double ft, double p) {
        temp = t;
        humidity = h;
        wind_speed = w;
        feels_temp = ft;
        precip = p;
    }

    public double getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public double getWind() {
        return wind_speed;
    }

    public double getFeels() {
        return feels_temp;
    }

    public double getPrecip() {
        return precip;
    }

}

class WeatherHttpClient {
    private static String BASE_URL = "http://api.wunderground.com/api/be3f019f86280ce2/conditions/q/NC/Chapel_Hill.json";

    public WeatherHttpClient() {
    }

    public String getWeatherData() {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(BASE_URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

// Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }
}

class JSONWeatherTask extends AsyncTask {

    private static final String TAG = "JSONWeatherTask";
    WeatherInterface weatherInterface;

    public JSONWeatherTask(WeatherInterface weatherInter) {
        weatherInterface = weatherInter;
    }

    protected Object doInBackground(Object[] params) {
        Weather weather = new Weather(0.0, "", 0.0, 0.0, 0.0);
        String data = ((new WeatherHttpClient()).getWeatherData());

        try {
            Log.d(TAG, data);
            weather = new JSONWeatherParser(data).getWeather();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }

    protected void onPostExecute(Object o) {
        Weather weather = (Weather) o;
        weatherInterface.receivedWeather(weather);
    }
}