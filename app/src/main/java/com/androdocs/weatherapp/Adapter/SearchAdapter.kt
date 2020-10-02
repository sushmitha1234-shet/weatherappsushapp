package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.R
import com.androdocs.weatherapp.ViewHolders.SearchHolder

class SearchAdapter (private val sun2: ArrayList<Int>,private val place2 : ArrayList<String>, private val temp2: ArrayList<String>, private val tempinfo2:ArrayList<String>,private val fav: ArrayList<Int>,private val mContext: Context):
RecyclerView.Adapter<SearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.searchlist,parent,false)
        return SearchHolder(v, mContext)
    }

    override fun getItemCount(): Int {
        return sun2.size
    }


    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder?.index(sun2[position],place2[position],temp2[position],tempinfo2[position],fav[position])
    }

}