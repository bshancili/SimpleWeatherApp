package com.example.simpleweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter class for displaying city data in the RecyclerView.
 *
 * @param items The list of CityData objects to be displayed.
 * @param onItemClick The callback function to handle item click events.
 */
class CityAdapter(private val items: List<CityData>, private val onItemClick: (CityData) -> Unit) :
    RecyclerView.Adapter<CityAdapter.RecyclerViewDataHolder>() {

    /**
     * ViewHolder class for holding the views of each item in the RecyclerView.
     *
     * @param itemView The view representing a city item in the RecyclerView.
     */
    inner class RecyclerViewDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]
                    onItemClick(clickedItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewDataHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.city_view, parent, false)
        return RecyclerViewDataHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewDataHolder, position: Int) {
        val currentCity: CityData = items[position]
        val cityname: TextView = holder.itemView.findViewById(R.id.tvCityName)
        cityname.text = currentCity.city
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
