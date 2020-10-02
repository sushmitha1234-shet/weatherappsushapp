package com.androdocs.weatherapp.Model

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SharedPreferences (context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()
    //private var favouritesList = ArrayList<IndividualCityWeatherDetails>()
    private var favouritesList = ArrayList<CityDetails>()
    private var recentSearchList = ArrayList<CityDetails>()
    val context = context



    fun saveToFavouritesList(location: String) {
        var isPresent = false
        favouritesList = loadFavouriteList()
        for (i in favouritesList){
            Log.d("Message : ", "i.cityName:"+ i.cityName + ", "+ location)
            if (i.cityName == location){
                isPresent = true
                break
            }
        }
        if(!isPresent){
            favouritesList.add(
                CityDetails(
                    location
                )
            )
            saveToFavouriteArrayList(favouritesList)
            Log.d("adding","Sharepref")
        }

    }

    fun saveToFavouriteArrayList(newList: ArrayList<CityDetails>) {
        val gson = Gson()
        //convert to json
        val json: String = gson.toJson(newList)
        editor.putString("favourite_list", json)
        editor.apply()

    }

    fun loadFavouriteList(): ArrayList<CityDetails> {
        val gson = Gson()
        val json = prefs.getString("favourite_list",null)
        val type: Type = object : TypeToken<ArrayList<CityDetails>?>() {}.type
        return if (json !=null){
            (gson.fromJson(json, type))
        } else {
            favouritesList
        }
    }


    fun removeFavourites() {
        editor.remove("favourite_list")
        editor.apply()
        favouritesList.clear()
    }

    fun removeSingleItem(cityName: String) {
        favouritesList = loadFavouriteList()
        Log.d("Message:", favouritesList.size.toString())
        for (i in 0 until  favouritesList.size)
            if (favouritesList[i].cityName == cityName){
                favouritesList.removeAt(i)
                break
            }
        saveToFavouriteArrayList(favouritesList)
    }

    fun saveToRecentSearch(location: String){
        var isPresent = false
        recentSearchList = loadRecentSearchList()
        for (i in recentSearchList){
            if (i.cityName == location){
                isPresent = true
                break
            }
        }
        if(!isPresent){
            recentSearchList.add(0,
                CityDetails(location)
            )
            saveToRecentSearchArrayList(recentSearchList)
        }
    }

    fun saveToRecentSearchArrayList(newList: ArrayList<CityDetails>) {
        val gson = Gson()
        //convert to json
        val json: String = gson.toJson(newList)
        editor.putString("recent_search_list", json)
        editor.apply()

    }

    fun loadRecentSearchList(): ArrayList<CityDetails> {
        val gson = Gson()
        val json = prefs.getString("recent_search_list",null)
        val type: Type = object : TypeToken<ArrayList<CityDetails>?>() {}.type
        return if (json !=null){
            (gson.fromJson(json, type))
        } else {
            recentSearchList
        }
    }

    fun removeSingleRecentItem(cityName: String) {
        recentSearchList = loadRecentSearchList()
        for (i in 0 until  recentSearchList.size)
            if (recentSearchList[i].cityName == cityName){
                recentSearchList.removeAt(i)
                break
            }
        saveToRecentSearchArrayList(recentSearchList)
    }

    fun removeAllRecentSearches() {
        editor.remove("recent_search_list")
        editor.apply()
    }

    fun remove(pos: Int){
        var favouritesList1 = ArrayList<CityDetails>()
        favouritesList1=loadFavouriteList()
        favouritesList1.removeAt(pos)
        saveToFavouriteArrayList(favouritesList1)

//        Log.d("size",favouritesList.size.toString())
    }

}
