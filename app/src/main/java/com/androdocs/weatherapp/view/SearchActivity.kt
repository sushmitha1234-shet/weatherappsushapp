package com.androdocs.weatherapp.view


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androdocs.weatherapp.R
import com.androdocs.weatherapp.Model.SharedPreferences
import com.androdocs.weatherapp.replaceAndCapitalize
import com.example.myapplication.SearchAdapter
import org.json.JSONObject
import java.net.URL

class SearchActivity : AppCompatActivity() {
    lateinit var preferenceModel: SharedPreferences
    var context = this
    val API: String = "5086ee31682cb35c05cb1f0d9bf0c676"
    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode
            .setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById(R.id.searchtoolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val recyclersearchView = findViewById<View>(R.id.recyclersearchView) as RecyclerView
        val sun = ArrayList<Int>()
        val fav = ArrayList<Int>()
        val place= ArrayList<String>()
        val temp =ArrayList<String>()
        val tempinfo = ArrayList<String>()

        val lManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
        recyclersearchView.layoutManager = lManager
        preferenceModel =
            SharedPreferences(context)
        var arr=preferenceModel.loadRecentSearchList()
        if(arr.size>0) {
            for (i in preferenceModel.loadRecentSearchList()) {

                var response: String?
                try {
                    response =
                        URL("https://api.openweathermap.org/data/2.5/weather?q=${i.cityName} &units=metric&appid=$API").readText(
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
                var tempr = main.getString("temp").toDouble().toInt().toString()+ "°C"
                val weatherDescription = weather.getString("description").replaceAndCapitalize()
                val id = weather.getString("id").toInt()
                if (id < 300) {
                    sun.add(R.drawable.icon_thunderstorm_big)


                } else if (id < 500) {
                    sun.add(R.drawable.icon_rain_big)


                } else if (id < 600) {
                    sun.add(R.drawable.icon_rain_big)


                } else if (id < 700) {
                    sun.add(R.drawable.icon_partially_cloudy_big)


                } else if (id < 800) {
                    sun.add(R.drawable.icon_mostly_sunny)


                } else if (id == 800) {
                    sun.add(R.drawable.icon_mostly_sunny)


                } else {
                    sun.add(R.drawable.icon_mostly_cloudy_big)

                }
                var flag=0
                for(j in preferenceModel.loadFavouriteList()) {
                    Log.d("Ciyt", i.cityName)
                    if (i.cityName == j.cityName) {
                        flag = 1
                        break
//
                    }
                }
                if(flag==1){
                    fav.add(R.drawable.icon_favourite_active1)
                }
                else{
                    fav.add(R.drawable.icon_favourite)
                }
                place.add(i.cityName)
                temp.add(tempr)
                tempinfo.add(weatherDescription)
            }



            recyclersearchView.adapter = SearchAdapter(sun, place, temp, tempinfo,fav,this)
        }
        else{
            startActivity(Intent(this,
                NoSearchActivity::class.java))
            finish()
        }
        val remove = findViewById<TextView>(R.id.tvClearAll)

        remove.setOnClickListener {

                Log.d("Remove","here")
                preferenceModel.removeAllRecentSearches()
                startActivity(Intent(this,
                    NoFavouriteActivity::class.java))
                finish()

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_recent,menu)

        val manager = getSystemService(Context.SEARCH_SERVICE)as SearchManager
        val searchItem = menu?.findItem(R.id.recentsearchbar)
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

