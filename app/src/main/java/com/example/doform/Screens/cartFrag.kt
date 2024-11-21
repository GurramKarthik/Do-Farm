package com.example.doform.Screens

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doform.DataBase.CropViewModel
import com.example.doform.R
import com.example.doform.Recycler.myCropsList
import com.example.doform.dataClass.myPlantsList
import kotlinx.coroutines.launch


class cartFrag : Fragment() {
    lateinit var mCropViewModel: CropViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =inflater.inflate(R.layout.fragment_cart, container, false)
        var soldCropsRecyclerView = layout.findViewById<RecyclerView>(R.id.soldCropsRecyclerView)
        soldCropsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mCropViewModel= ViewModelProvider(this).get(CropViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE)
        var UID = sharedPreferences.getString("userUID", null);
        var userId = UID?.toInt()!!
        var userType = sharedPreferences.getString("userType", null);
        var userName = sharedPreferences.getString("userName", null)
        var userPhone = sharedPreferences.getString("userPhone", null)
        var userLocation = sharedPreferences.getString("userLocation", null);

        var adaptor = myCropsList(requireContext(), mCropViewModel,viewLifecycleOwner, userId, userName.toString(), userPhone.toString(), userType.toString() ,userLocation.toString() )
        soldCropsRecyclerView.adapter = adaptor
        lifecycleScope.launch { mCropViewModel.getCropsByUser(userId).observe(viewLifecycleOwner, Observer { crop ->
                   if (userType == "Farmer") {
                       val soldCrops = crop.filter { it.status == "Sold" }
                       adaptor.setList(soldCrops)
                   }else{
                       adaptor.setList(crop)
                   } }) }
        return layout; } }