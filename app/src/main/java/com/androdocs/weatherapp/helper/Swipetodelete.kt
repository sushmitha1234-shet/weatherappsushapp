package com.androdocs.weatherapp.helper

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.FavouriteAdapter



abstract class Swipetodelete(context: Context, dragDir:Int, swipeDir:Int):ItemTouchHelper.SimpleCallback(dragDir,swipeDir)
{
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }
}