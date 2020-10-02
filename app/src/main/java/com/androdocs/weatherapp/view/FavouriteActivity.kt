package com.androdocs.weatherapp.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.wifi.rtt.CivicLocationKeys.CITY
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.Model.SharedPreferences
import com.androdocs.weatherapp.helper.Swipetodelete
import com.example.myapplication.FavouriteAdapter
import kotlinx.android.synthetic.main.activity_favourite.*
import org.json.JSONObject
import java.net.URL
import com.androdocs.weatherapp.R as R1

class FavouriteActivity : AppCompatActivity() {

    lateinit var preferenceModel: SharedPreferences
    var context = this
   lateinit var adapter:FavouriteAdapter
    val API: String = "5086ee31682cb35c05cb1f0d9bf0c676"

    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode
            .setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

        super.onCreate(savedInstanceState)
        setContentView(R1.layout.activity_favourite)

        preferenceModel =
            SharedPreferences(context)

        val toolbar = findViewById(R1.id.favtoolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val recyclerfavView = findViewById<View>(R1.id.recyclerfavView) as RecyclerView
        val sun = ArrayList<Int>()
        val place = ArrayList<String>()
        val temp = ArrayList<String>()
        val tempinfo = ArrayList<String>()

        val lManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
        recyclerfavView.layoutManager = lManager


        var arr = preferenceModel.loadFavouriteList()


        if (arr.size > 0) {
            for (i in preferenceModel.loadFavouriteList()) {

                var response: String?
                try {
                    response =
                        URL("https://api.openweathermap.org/data/2.5/weather?q=${i.cityName} &units=default&appid=$API").readText(
                            Charsets.UTF_8
                        )
                    Log.d("exp", response.toString())
                } catch (e: Exception) {
                    Log.d("exp", e.toString())
                    response = null
                }
                Log.d("resp", response.toString())
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                var tempr = main.getString("temp") + "°"
                val weatherDescription = weather.getString("description")
                val id = weather.getString("id").toInt()
                if (id < 300) {
                    sun.add(R1.drawable.icon_thunderstorm_big)


                } else if (id < 500) {
                    sun.add(R1.drawable.icon_rain_big)


                } else if (id < 600) {
                    sun.add(R1.drawable.icon_rain_big)


                } else if (id < 700) {
                    sun.add(R1.drawable.icon_partially_cloudy_big)


                } else if (id < 800) {
                    sun.add(R1.drawable.icon_mostly_sunny)


                } else if (id == 800) {
                    sun.add(R1.drawable.icon_mostly_sunny)


                } else {
                    sun.add(R1.drawable.icon_mostly_cloudy_big)

                }
                place.add(i.cityName)
                temp.add(tempr)
                tempinfo.add((weatherDescription).capitalize())
            }
            recyclerfavView.adapter = FavouriteAdapter(sun, place, temp, tempinfo, this)

            val item=object:
                Swipetodelete(this,0,ItemTouchHelper.LEFT){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                    val preferenceModel =
                        SharedPreferences(context)
                    preferenceModel.removeSingleItem(CITY.toString())
                   (recyclerfavView.adapter as FavouriteAdapter).removeAt(viewHolder.adapterPosition)
                    val refresh = intent
                    finish()
                    startActivity(refresh)

                }
            }
            val itemTouchHelper=ItemTouchHelper(item)
            itemTouchHelper.attachToRecyclerView(recyclerfavView)


            tvCityadded.text = arr.size.toString() + " Cities added as favourites"


            if (arr.size == 1) {
                tvCityadded.text = arr.size.toString() + " City added as favourite"
            }

        }

        else {
            startActivity(Intent(this,
                NoFavouriteActivity::class.java))
            finish()
        }


        val remove = findViewById<TextView>(R1.id.tvRemoveAll)

        remove.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            //set message for alert dialog
            builder.setMessage(R1.string.dialogMessage)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                Log.d("Remove","here")
                preferenceModel.removeFavourites()
                startActivity(Intent(this,
                    NoFavouriteActivity::class.java))
                finish()
            }

            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R1.menu.search_fav,menu)

        val manager = getSystemService(Context.SEARCH_SERVICE)as SearchManager
        val searchItem = menu?.findItem(R1.id.favsearchbar)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true

    }

}