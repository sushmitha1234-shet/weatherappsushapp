package com.androdocs.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import com.androdocs.weatherapp.SharedPreferencex

class HomeActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var preferenceModel: SharedPreferencex
    var context = this


    var CITY: String = "Udupi,India"
    val API: String = "5086ee31682cb35c05cb1f0d9bf0c676"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceModel = SharedPreferencex(context)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
         drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setSupportActionBar(toolbar)
        supportActionBar?.setLogo(R.drawable.logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation(tv_location)
        }
        else {
            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

            Log.d("Loc2",CITY)

        }
        Log.d("Loc",CITY)
//        weatherTask().execute()


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
               R.id.mitem1 ->startActivity(Intent(this,HomeActivity::class.java))
                R.id.mitem2 -> startActivity(Intent(this,FavouriteActivity::class.java))
                R.id.mitem3 -> startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.homesearchbar-> {
                val intent = PlaceAutocomplete.IntentBuilder()
                    .accessToken("pk.eyJ1Ijoic3VzaG1pdGhhc2hldDEyMyIsImEiOiJja2ZjYjNndHkxZTZjMnpxc2k4bndnaTJ2In0.YtNvTh7-VtuL_0lJ1X-5Kg")
                    .placeOptions(PlaceOptions.builder().build(PlaceOptions.MODE_CARDS))
                    .build(this)

               startActivityForResult(intent,1)
            }
        }
        return true
         return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 1) {
            var feature = PlaceAutocomplete.getPlace(data)

            var searchValue =  feature.text()!!
            CITY = searchValue
           preferenceModel.saveToRecentSearch(CITY)
            weatherTask().execute()
            Log.d("LogReport", "" + searchValue)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
   }


    @SuppressLint("StaticFieldLeak")
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            // findViewById<RelativeLayout>(R.id.layout).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY &units=default&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }



        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)


                val updatedAt: Long = jsonObj.getLong("dt")
                val textView: TextView = findViewById(R.id.tv_datetime)
                val simpleDateFormat = SimpleDateFormat("E, dd MMM yyy hh:mm a")
                val currentDateAndTime: String = simpleDateFormat.format(Date())
                textView.text = currentDateAndTime


                var temp = main.getString("temp") + "°"
                var tempMinMax =
                    "" + main.getString("temp_min") + "°" + "-" + main.getString("temp_max") + "°"

                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity") + "km/h"
                val visiblility = jsonObj.getString("visibility") + "km"




                val windSpeed = wind.getString("speed") + "%"
                val weatherDescription = weather.getString("description")


                // var visibility:Long=visibility.getString("visible")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")
                val id = weather.getString("id").toInt()
                if (id < 300) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_thunderstorm_big)


                } else if (id < 500) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_rain_big)


                } else if (id < 600) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_rain_big)


                } else if (id < 700) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_partially_cloudy_big)


                } else if (id < 800) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_sunny)


                } else if (id == 800) {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_sunny)


                } else {
                    iv_weathericon.setBackgroundResource(R.drawable.icon_mostly_cloudy_big)

                }



                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.tv_location).text = address

                findViewById<TextView>(R.id.tv_status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.tv_temp).text = temp
                findViewById<TextView>(R.id.tv_minmax).text = tempMinMax

                findViewById<TextView>(R.id.tv_wind).text = windSpeed

                findViewById<TextView>(R.id.tv_humidity).text = humidity

                findViewById<TextView>(R.id.tv_visibility).text = visiblility
                for(i in preferenceModel.loadFavouriteList()){
                    if(i.cityName==CITY){
                        findViewById<CheckBox>(R.id.checkBox).isChecked=true
//                        findViewById<CheckBox>(R.id.checkBox).isChecked=true
                    }
                    else{

                        findViewById<CheckBox>(R.id.checkBox).isChecked=false
//                        findViewById<CheckBox>(R.id.checkBox).isChecked=false
                    }
                }

                          radioCelsius.setOnClickListener() {
                                radioCelsius.let {
                                    it.setBackgroundResource(R.color.white)
                                    it.setTextColor(Color.parseColor("#E32843"))

                                }
                                radioFahrenheit.let {
                                    it.setBackgroundResource(R.color.transparent)
                                    it.setTextColor(Color.parseColor("#FFFFFF"))
                                }

                                 var temp = (main.getString("temp").toDouble())-273.15
                                 findViewById<TextView>(R.id.tv_temp).text = temp.toString()+"°"
                                  var tempmin = ((main.getString("temp_min").toDouble())-273.15).toString()+"°"
                                  var tempmax = ((main.getString("temp_max").toDouble())-273.15).toString()+"°"
                                  var tempMinMax= tempmin +"-" +tempmax
                                  findViewById<TextView>(R.id.tv_minmax).text=tempMinMax

                          }


                            radioFahrenheit.setOnClickListener() {
                               radioFahrenheit.let {
                               it.setBackgroundResource(R.color.white)
                               it.setTextColor(Color.parseColor("#E32843"))
                    }
                              radioCelsius.let {
                               it.setBackgroundResource(R.color.transparent)
                               it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                            var temp = (((main.getString("temp").toDouble())-273.15)*9/5)+32
                            findViewById<TextView>(R.id.tv_temp).text = temp.toString()+"°"
                                var tempmin = ((((main.getString("temp_min").toDouble())-273.15)*9/5)+32).toString()+"°"
                                var tempmax = ((((main.getString("temp_max").toDouble())-273.15)*9/5)+32).toString()+"°"
                                var tempMinMax= tempmin +"-"+ tempmax
                                findViewById<TextView>(R.id.tv_minmax).text=tempMinMax


                }
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->0
                    if (isChecked) {
                        checkBox.setBackgroundResource(R.color.yello)
                        preferenceModel.saveToFavouritesList(CITY)
                        Log.d("check",isChecked.toString())
                    }
                    else{
                        checkBox.setBackgroundResource(R.color.transparent)
                        preferenceModel.removeSingleItem(CITY)
                    }
                }



            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE

            }

        }

    }
    fun getCurrentLocation(tv_location: TextView) {
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location = it.getResult()
            val geocoder: Geocoder = Geocoder(this)
            val address: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            tv_location.text = "${address.get(0).subAdminArea}"
            CITY=tv_location.text.toString()
            weatherTask().execute()
            Log.d("loc1",address.get(0).subAdminArea)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation(tv_location)
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


//    fun checkOrGetPermissions(context: Context) {
//        val string: String = android.Manifest.permission.ACCESS_FINE_LOCATION
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocation(context, true)
//        }
//        else {
//            Log.d("Message:" ,"No permission")
//            ActivityCompat.requestPermissions(context as Activity, arrayOf(string), 100)
//            Log.d("Message:", "Got permission")
//            getCurrentLocation(context,true)
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    fun getCurrentLocation(context: Context, hasPermission: Boolean) {
//        //imageResourceId = context.getDrawable(R.drawable.icon_wind_info)!!
//        Log.d("Message:","Control is in getCurrentLoc()" )
//        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        if (hasPermission) {
//            Log.d("Message:1", isLocationEnabled(context).toString())
//            fusedLocationProviderClient.lastLocation.addOnSuccessListener{
//                var location:Location? = it
//                if(location == null){
//                    Log.d("Message:","Null")
//                }
//                else{
//
////                    latitude.value = location.latitude
////                    longitude.value = location.longitude
//                    var geoCoder = Geocoder(context, Locale.getDefault())
//                    var Address = geoCoder.getFromLocation(location.latitude,location.longitude,10)
//                    CITY = Address[0].subAdminArea
//                    Log.d("Message:" ,"Your Location:"+Address[0].subAdminArea )
////
////                    currentLocation. value = Address[0].locality  + ", " + Address[0].subAdminArea + ", " + Address[0].adminArea
////                    isCurrentLocation.value = true
//                }
//            }
//
//        }
//
//    }
//
//    fun getLocationDeatils(cityName: String, latitude: Double, longitude: Double, context: Context) {
//        var geoCoder = Geocoder(context, Locale.getDefault())
//        var Address = geoCoder.getFromLocation(latitude, longitude, 10)
//        CITY=  cityName + ", "  + Address[0].countryName //Address[0].premises
////        isChecked.value = false
////        preferenceModel = PreferenceModel(context)
////        for (i in preferenceModel.loadFavouriteList())
////            if (i.cityName == currentLocation.value.toString())
////                isChecked.value = true
//
//    }
//
//
//
//    fun isLocationEnabled(context: Context):Boolean {
//        var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//
//    }


}



























