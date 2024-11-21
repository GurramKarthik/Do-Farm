package com.example.doform.Recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.doform.DataBase.CropViewModel
import com.example.doform.DataBase.Crops
import com.example.doform.R
import com.example.doform.Screens.EditCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class myCropsList(
    private val cont: Context,
    private val mCropViewModel: CropViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val userId: Int,
    private val userName: String,
    private val userPhone: String,
    private val userType :String,
    private val userLocation :String

) : RecyclerView.Adapter<myCropsList.viewHolder>() {
    private var items = mutableListOf<Crops>()
    inner class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cropImg: ImageView = view.findViewById(R.id.cropImage)
        val name: TextView = view.findViewById(R.id.cropName)
        val quantity: TextView = view.findViewById(R.id.quantity)
        val price: TextView = view.findViewById(R.id.price)
        val status: TextView = view.findViewById(R.id.status)
        val card: CardView = view.findViewById(R.id.cropDetails)
        val badge: CardView = view.findViewById(R.id.badge)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(cont).inflate(R.layout.myplant_recycler, parent, false)
        return viewHolder(view)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val crop = items[position]
        holder.name.text = crop.cropName ?: "No Data"
        holder.quantity.text = crop.quantity.toString()
        holder.price.text = crop.price.toString()
        holder.status.text = crop.status ?: "No Data"
        // Update badge color based on crop status
        if (crop.status == "Available") {
            holder.badge.setCardBackgroundColor(Color.parseColor("#33B5E5"))
        } else {
            holder.badge.setCardBackgroundColor(Color.parseColor("#FF7043"))
        }
        // Set crop image from byte array
        holder.cropImg.setImageBitmap(byteArrayToBitmap(crop.cropImg))
        // Handle card click
        holder.card.setOnClickListener {
            if (crop.status == "Available") {
                if (userType == "Farmer") {
                    // Navigate to EditCrop activity with cropId
                    val intent = Intent(cont, EditCrop::class.java)
                    intent.putExtra("cropId", crop.cropId)
                    cont.startActivity(intent)
                } else {
                    showBuyDialog(crop)
                }
            } else {
                showDetails(crop)
            }
        }

        // Handle card long-click for deletion
        holder.card.setOnLongClickListener {
            showDeleteDialog(crop, position)
            true
        }

    }

    private fun showDeleteDialog(crop: Crops, position: Int) {
        val dialogBuilder = AlertDialog.Builder(cont)
        dialogBuilder.setTitle("Delete Crop")
            .setMessage("Are you sure you want to delete this crop?")
            .setPositiveButton("Delete") { dialog, _ ->
                // Delete crop from database and update UI
                lifecycleOwner.lifecycleScope.launch {
                    mCropViewModel.deleteCropById(crop.cropId)
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    Toast.makeText(cont, "Crop deleted successfully", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showBuyDialog(crop: Crops) {
        val dialogBuilder = AlertDialog.Builder(cont)
        var buiedCrop = userId?.let {
            Crops(
                0, crop.cropImg, crop.cropName, crop.quantity, crop.unit, crop.price,
                "Purchased", it.toInt(), crop.oppositeName, crop.oppositePhone, System.currentTimeMillis(),
                userLocation
            )
        }
        dialogBuilder.setTitle("Buy Crop")
            .setMessage("Are you sure you want to buy crops")
            .setPositiveButton("Buy") { dialog, _ ->
                // Delete crop from database and update UI
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    if (buiedCrop != null) {
                        mCropViewModel.addCrop(buiedCrop)
                    }else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(cont, "Something Went Wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    mCropViewModel.updateCropStatus(crop.cropId, "Sold", userName, userPhone)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(cont, "Thank you for buying the Crop", Toast.LENGTH_SHORT)
                            .show() } }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showDetails(crop: Crops) {
        val dialogView = LayoutInflater.from(cont).inflate(R.layout.dialogbox_sold, null)
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(cont)
            .setView(dialogView)
        val dialog = dialogBuilder.create()
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val edDate = dialogView.findViewById<TextView>(R.id.date)
        val buyerName = dialogView.findViewById<TextView>(R.id.buyerName)
        val buyerPhone = dialogView.findViewById<TextView>(R.id.buyerPhone)
        val action = dialogView.findViewById<TextView>(R.id.action)
        val message = dialogView.findViewById<TextView>(R.id.message)
        val closeButton = dialogView.findViewById<Button>(R.id.closeButton)
        val date = Date(crop.createdAt)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(date)
        dialogTitle.text = crop.cropName
        edDate.text = formattedDate
        buyerName.text = crop.oppositeName
        buyerPhone.text = crop.oppositePhone
        if (userType == "Client") {
            action.text = "Brought from:"
            message.text = "This crop was purchased on: "
        }
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    fun setList(list: List<Crops>) {
        this.items = list.toMutableList()
        notifyDataSetChanged()
    }
}