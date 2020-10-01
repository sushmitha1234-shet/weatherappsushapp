package com.example.myapplication

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.R
import com.bumptech.glide.Glide

class FavouriteHolder
    (itemView: View, private val mContext: Context): RecyclerView.ViewHolder(itemView){
    private val tvplace: TextView
    private val ivSun : ImageView
    private val ittemp : TextView
    private val itvmostlysunny : TextView
    //private val imagefav:ImageView


    init{
        tvplace=itemView.findViewById<View>(R.id.tvplace) as TextView
        ivSun = itemView.findViewById<View>(R.id.ivSun) as ImageView
        ittemp = itemView.findViewById<View>(R.id.ittemp) as TextView
        itvmostlysunny=itemView.findViewById<View>(R.id.itvmostlysunny) as TextView
        //imagefav=itemView.findViewById<View>(R.id.imagefav)as ImageView

    }
    fun index(item: Int, s: String, s1: String,s2:String){
        Glide.with(mContext).load(item).into(ivSun)
        tvplace.text = s
        ittemp.text=s1
        itvmostlysunny.text=s2
       // Glide.with((mContext).load(item).into(imagefav)

    }

}
