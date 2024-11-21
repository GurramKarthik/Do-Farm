package com.example.doform.Screens

import android.content.Intent
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
import com.example.doform.DataBase.UserViewModel
import com.example.doform.R
import com.example.doform.Recycler.myCropsList
import com.example.doform.dataClass.myPlantsList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class myPlantsFrag1 : Fragment() {
    lateinit var mCropViewModel: CropViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout =  inflater.inflate(R.layout.fragment_my_plants_frag1, container, false)
        var addCropBtn = layout.findViewById<FloatingActionButton>(R.id.addCropBtn)
        var recyclerView = layout.findViewById<RecyclerView>(R.id.myPlantsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mCropViewModel= ViewModelProvider(this).get(CropViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE)
        var UID = sharedPreferences.getString("userUID", null);
        var userId = UID?.toInt()!!
        var userName = sharedPreferences.getString("userName", null)
        var userPhone = sharedPreferences.getString("userPhone", null)
        var userType = sharedPreferences.getString("userType", null);
        var userLocation = sharedPreferences.getString("userLocation", null);

        var adapter = myCropsList(requireContext(), mCropViewModel, viewLifecycleOwner, userId , userName.toString(), userPhone.toString(), userType.toString(), userLocation.toString() )
        recyclerView.adapter = adapter
        addCropBtn.setOnClickListener { var intent = Intent(requireContext(), addCrop::class.java)
            requireContext().startActivity(intent)
        }
        lifecycleScope.launch {
            mCropViewModel.getCropsByUser(userId).observe(viewLifecycleOwner, Observer { crop ->
                val availableCrops = crop.filter { it.status == "Available" }
                adapter.setList(availableCrops) }) }
        return layout;
    }
}