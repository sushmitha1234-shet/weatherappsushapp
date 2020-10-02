package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.R
import com.androdocs.weatherapp.Model.SharedPreferences


class FavouriteAdapter   (private var sun: ArrayList<Int>, private var place : ArrayList<String>, private var temp: ArrayList<String>, private var tempinfo:ArrayList<String>, private var mContext: Context):
    RecyclerView.Adapter<FavouriteHolder>() {

    lateinit var preferenceModel: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.favouritelist,parent,false)
        return FavouriteHolder(v,mContext)
    }

    fun removeAt(pos: Int){
        preferenceModel=
            SharedPreferences(mContext)
        preferenceModel.remove(pos)
//        Log.d("ud",id.toString())
        notifyDataSetChanged()
        Log.d("here",pos.toString())
    }


    override fun getItemCount(): Int {
        return sun.size
    }

    override fun onBindViewHolder(holder: FavouriteHolder, position: Int) {
        holder?.index(sun[position],place[position],temp[position],tempinfo[position])
    }

}
