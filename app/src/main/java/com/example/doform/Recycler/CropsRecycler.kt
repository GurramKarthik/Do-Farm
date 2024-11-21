package com.example.doform.Recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doform.R
import com.example.doform.dataClass.Crop
import com.example.doform.dataClass.cropsGrid

class CropsRecycler(var cont : Context, var items : List<Crop>?)
    : RecyclerView.Adapter<CropsRecycler.viewHolder>(){

    inner class viewHolder(var view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(cont).inflate(R.layout.crops_recycler, parent, false)
        return viewHolder(view);
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0;
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            var cropImg = findViewById<ImageView>(R.id.cropImg)
            var name = findViewById<TextView>(R.id.CropName)

            name.text = items?.get(position)?.name ?: "No Data"

        }
    }


}