package com.example.simpleweatherapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

/**
 * MainActivity class is the entry point of the application and extends AppCompatActivity.
 * It displays weather information for a specific city using the OpenWeatherMap API.
 */
class MainActivity : AppCompatActivity() {
    var cityId: String = "745044" // Default city ID
    val apiKey: String = "6f3fa896055ace0cfc805afe0a50095a" // API key for OpenWeatherMap

    /**
     * Called when the activity is starting. Initializes the UI and retrieves weather data.
     *
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city = intent.getStringExtra("Id")
        if (city != null) {
            cityId = city
        }

        weatherTask().execute()

        val buttonClick = findViewById<ImageButton>(R.id.searchButtonNotSelected)
        buttonClick.setOnClickListener{
            /**
             * Click event handler for the search button.
             * Starts the SearchPage activity with the selected city ID.
             */
            val intent = Intent(this, SearchPage::class.java)
            intent.putExtra("Id", cityId)
            startActivity(intent)
        }

        val buttonClickHome = findViewById<ImageButton>(R.id.homeButtonSelected)
        buttonClickHome.setOnClickListener{
            /**
             * Click event handler for the home button.
             * Restarts the MainActivity activity with the current city ID.
             */
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Id", cityId)
            startActivity(intent)
        }

    }

    /**
     * AsyncTask subclass that performs network request to fetch weather data.
     */
    inner class weatherTask() : AsyncTask<String, Void, String>() {

        /**
         * Performs the network request in the background and returns the response.
         *
         * @param params The optional parameters for the request.
         * @return The weather data response as a String.
         */
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?APPID=$apiKey&units=metric&id=$cityId")
                    .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        /**
         * Called after the background task is complete. Parses the weather data response and updates the UI.
         *
         * @param result The weather data response as a String.
         */
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val name = jsonObj.getString("name")
                val current = jsonObj.getJSONObject("main").getString("temp")
                val feels = jsonObj.getJSONObject("main").getString("feels_like")
                val minTemp = jsonObj.getJSONObject("main").getString("temp_min")
                val maxTemp = jsonObj.getJSONObject("main").getString("temp_max")
                val humidity = jsonObj.getJSONObject("main").getString("humidity")
                val speed = jsonObj.getJSONObject("wind").getString("speed")
                val windDegree = jsonObj.getJSONObject("wind").getString("deg")
                val seaLevel = jsonObj.getJSONObject("main").getString("pressure")
                val latitude = jsonObj.getJSONObject("coord").getString("lat")
                val longtitude = jsonObj.getJSONObject("coord").getString("lon")
                val desc = jsonObj.getJSONArray("weather").getJSONObject(0).getString("description")

                findViewById<TextView>(R.id.cityName).text = name
                findViewById<TextView>(R.id.current).text = "Current Temperature: $current °C"
                findViewById<TextView>(R.id.feel).text = "Feeling Temperature: $feels °C"
                findViewById<TextView>(R.id.temp_max).text = "Highest Temp in Day: $maxTemp °C"
                findViewById<TextView>(R.id.temp_min).text = "Lowest Temp in Day: $minTemp °C"
                findViewById<TextView>(R.id.humidityInfo).text = "%$humidity"
                findViewById<TextView>(R.id.speedInfo).text = "Speed: $speed km/h"
                findViewById<TextView>(R.id.degreeInfo).text = "Degree: $windDegree"
                findViewById<TextView>(R.id.seaLevelInfo).text = "$seaLevel"
                findViewById<TextView>(R.id.latitudeInfo).text = "Latitude: $latitude"
                findViewById<TextView>(R.id.longtitudeInfo).text = "Longtitude: $longtitude"
                findViewById<TextView>(R.id.description).text = "Today is $desc"
            } catch (e: Exception) {
                findViewById<TextView>(R.id.cityName).text = "Error"
            }
        }
    }
}
