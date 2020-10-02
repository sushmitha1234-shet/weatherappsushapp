package com.androdocs.weatherapp.ViewHolders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.R
import com.bumptech.glide.Glide

class SearchHolder
    (itemView: View, private val mContext: Context): RecyclerView.ViewHolder(itemView){
        private val tvsearchplace: TextView
        private val ivSun2: ImageView
        private val ittemp2 : TextView
        private val itvmostlysunny2 : TextView
        private val fav : ImageView

    //private val imagefav:ImageView


        init{
            tvsearchplace=itemView.findViewById<View>(R.id.tvsearchplace) as TextView
            ivSun2 = itemView.findViewById<View>(R.id.ivSun2) as ImageView
            fav = itemView.findViewById<View>(R.id.searchfav) as ImageView
            ittemp2 = itemView.findViewById<View>(R.id.ittemp2) as TextView
            itvmostlysunny2=itemView.findViewById<View>(R.id.itvmostlysunny2) as TextView
            //imagefav=itemView.findViewById<View>(R.id.imagefav)as ImageView

        }
        fun index(item: Int, s: String, s1: String,s2:String,item1: Int){
            Glide.with(mContext).load(item).into(ivSun2)
            tvsearchplace.text = s
            ittemp2.text=s1
            itvmostlysunny2.text=s2
            Glide.with(mContext).load(item1).into(fav)
            // Glide.with((mContext).load(item).into(imagefav)

        }

    }