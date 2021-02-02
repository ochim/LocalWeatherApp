package com.example.localweatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localweatherapp.R
import com.example.localweatherapp.model.City

class CityAdapter(private val context: Context, private val dataset: List<City>, private val onClick: (City) -> Unit)
    : RecyclerView.Adapter<CityAdapter.ItemViewHolder>()  {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val city = dataset[position]
        holder.textView.text = city.name
        holder.view.setOnClickListener{ onClick(city) }
    }

    override fun getItemCount() = dataset.size
}