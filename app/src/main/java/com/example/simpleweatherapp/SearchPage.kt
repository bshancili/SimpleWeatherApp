package com.example.simpleweatherapp

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream
import java.io.InputStreamReader

/**
 * SearchPage class is responsible for displaying a search page for selecting a city.
 * It extends AppCompatActivity and provides functionality for searching and selecting cities.
 */
class SearchPage : AppCompatActivity() {

    var lastCityId: String = "745044" // ID of the city that is displaying on the home page currently

    private lateinit var cityAdapter: CityAdapter
    private lateinit var recyclerViewData: MutableList<CityData>

    /**
     * Called when the activity is starting. Initializes the UI and sets up the search functionality.
     *
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        val recyclerView: RecyclerView = findViewById(R.id.rvItemsList)

        val originalCityDataList = mutableListOf<CityData>()

        // Reading city data from the "city.list.json" file
        val inputStream: InputStream = assets.open("city.list.json")
        val reader = JsonReader(InputStreamReader(inputStream))

        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()

            var id: String? = null
            var name: String? = null

            while (reader.hasNext()) {
                val propertyName = reader.nextName()
                when (propertyName) {
                    "id" -> id = reader.nextInt().toString()
                    "name" -> name = reader.nextString()
                    else -> reader.skipValue()
                }
            }

            originalCityDataList.add(CityData(name ?: "", id ?: ""))
            reader.endObject()
        }

        reader.endArray()
        reader.close()

        recyclerViewData = mutableListOf()
        recyclerViewData.addAll(originalCityDataList)

        cityAdapter = CityAdapter(recyclerViewData) { clickedCity ->
            // Click event handler for the city item in the RecyclerView
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Id", clickedCity.id)
            startActivity(intent)
        }

        recyclerView.adapter = cityAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**
             * Called when the user submits the search query.
             *
             * @param query The search query submitted by the user.
             * @return True if the query has been handled, false otherwise.
             */
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            /**
             * Called when the search query text changes.
             *
             * @param newText The new text entered in the search query.
             * @return True to indicate the query has been handled, false otherwise.
             */
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    recyclerViewData.clear()

                    if (query.isEmpty()) {
                        recyclerViewData.addAll(originalCityDataList)
                    } else {
                        val filteredList = originalCityDataList.filter { cityData ->
                            cityData.city.contains(query, ignoreCase = true)
                        }
                        recyclerViewData.addAll(filteredList)
                    }

                    cityAdapter.notifyDataSetChanged()
                }

                return true
            }
        })

        val city = intent.getStringExtra("Id")
        if (city != null) {
            lastCityId = city
        }

        val buttonClick = findViewById<ImageButton>(R.id.homeButtonNotSelected)
        buttonClick.setOnClickListener{
            /**
             * Click event handler for the home button.
             * Restarts the MainActivity activity with the last selected city ID.
             */
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Id", lastCityId)
            startActivity(intent)
        }

        val buttonClickHome = findViewById<ImageButton>(R.id.searchButtonSelected)
        buttonClickHome.setOnClickListener{
            /**
             * Click event handler for the search button.
             * Restarts the SearchPage activity.
             */
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }
    }
}
